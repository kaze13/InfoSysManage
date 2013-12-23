package system.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.MainAuthority;
import system.po.User;
import system.vo.UserWrap;
import system.service.spec.UserService;


@Interceptors(TimerInterceptor.class)
public class UserServiceImpl extends AbstractDataAccessService<User> implements
		Serializable, UserService {


	@Inject
	private AbstractDataAccessService<MainAuthority> mainAuthorityDataAccessService;
	public UserServiceImpl() {
		super(User.class);
	}

	/* (non-Javadoc)
	 * @see system.service.UserService#findUserByRole(java.lang.String)
	 */
	@Override
	public List<User> findUserByRole(String roleName) throws Exception
	{
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("role_name", roleName);
		return this.findAllByCondition(sqlWhereMap);
	}

	/* (non-Javadoc)
	 * @see system.service.UserService#generateUserVo(system.po.User)
	 */
	@Override
	public UserWrap generateUserVo(User user) throws Exception {
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("user_name", user.getName());
		List<MainAuthority> mainAuthorityList = mainAuthorityDataAccessService
				.findAllByCondition(sqlMap);
		List<String> authorityList = new ArrayList<String>();
		for (MainAuthority m : mainAuthorityList) {
			authorityList.add(m.getAuthorityName());
		}
		return new UserWrap(user, authorityList);
	}

//	public void promoteUserAuthority(User user, System system, int level, long expireTime) throws Exception {
//		TemporaryAuthority newAuthority = new TemporaryAuthority(
//				user.getName(), system.getName(), level, expireTime);
//		temporaryAuthorityService.save(newAuthority);
//	}

//	public void createGuest(long expireTime) throws Exception
//	{
//		String guestName = UUID.randomUUID().toString().substring(0,7);
//		String guestPassword = UUID.randomUUID().toString().substring(0,7);
//		User guest = new User(guestName, guestPassword, "guest", expireTime );
//		this.save(guest);
//	}

}
