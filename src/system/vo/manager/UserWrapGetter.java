package system.vo.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import system.po.MainAuthority;
import system.service.MainAuthorityServiceImpl;
import system.service.SystemFunctionServiceImpl;
import system.service.TemporaryAuthorityServiceImpl;
import system.service.UserServiceImpl;
import system.vo.TemporaryAuthorityWrap;
import system.vo.UserWrap;

@Singleton
public class UserWrapGetter implements Serializable{

	@Inject
	private UserServiceImpl userService;
	@Inject
	private  MainAuthorityServiceImpl mainAuthorityService;
	@Inject
	private  TemporaryAuthorityServiceImpl temporaryAuthorityService;
	@Inject
	private  SystemFunctionServiceImpl systemFunctionService;
	private Map<String, UserWrap> cachePool = new HashMap<String, UserWrap>();

	public UserWrap get(String id) throws Exception
	{
		if(id == null || id.equals("none"))
			return null;
		if(cachePool.containsKey(id))
			return cachePool.get(id);

		UserWrap user = new UserWrap(userService.get(id));
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("user_name", user.getName());
		List<MainAuthority> authorities = mainAuthorityService
				.findAllByCondition(sqlWhereMap);
		List<String> authorityStr = new ArrayList<String>();
		for (MainAuthority authority : authorities) {
			authorityStr.add(authority.getAuthorityName());
		}
		user.setAuthority(authorityStr);
		user.setTemporaryAuthority(TemporaryAuthorityWrap
				.convert(temporaryAuthorityService
						.findAllByCondition(sqlWhereMap)));

		for(TemporaryAuthorityWrap t: user.getTemporaryAuthority())
		{
			t.setFunction(systemFunctionService.get(t.getSystemFunctionId()));
		}
		user.setAuthorisedFunctions(systemFunctionService
				.findFunctionByRole(user.getRoleName()));

		cachePool.put(id, user);
		return user;
	}
}
