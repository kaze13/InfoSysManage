package system.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.SystemFunctionInject;
import system.annotation.UserInject;
import system.dao.impl.SystemFunctionDaoImpl;
import system.dao.impl.UserDaoImpl;
import system.interceptor.TimerInterceptor;
import system.po.System;
import system.po.SystemFunction;
import system.po.TemporaryAuthority;
import system.po.User;
import system.po.spec.Printable;
import system.service.spec.SystemFunctionService;

@Interceptors(TimerInterceptor.class)
public class SystemFunctionServiceImpl extends
		AbstractDataAccessService<SystemFunction> implements Serializable,
		SystemFunctionService {

	@Inject
	@SystemFunctionInject
	private SystemFunctionDaoImpl systemFunctionDao;
	@Inject
	@UserInject
	private UserDaoImpl userDao;

	public SystemFunctionServiceImpl() {
		super(SystemFunction.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see system.service.SystemFunctionService#get(java.lang.String[])
	 */
	@Override
	public List<SystemFunction> get(String[] idList) throws Exception {
		List<SystemFunction> result = new ArrayList<SystemFunction>();
		for (String id : idList) {
			result.add(this.get(id));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.SystemFunctionService#findFunctionBySystem(java.lang.String
	 * )
	 */
	@Override
	public List<SystemFunction> findFunctionBySystem(String systemName)
			throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("system_name", systemName);
		return this.findAllByCondition(sqlWhereMap);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.SystemFunctionService#findFunctionBySystems(java.util.
	 * List)
	 */
	@Override
	public List<SystemFunction> findFunctionBySystems(List<System> systems)
			throws Exception {
		List<SystemFunction> result = new ArrayList<SystemFunction>();
		for (System s : systems) {
			result.addAll(this.findFunctionBySystem(s.getName()));
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.SystemFunctionService#findFunctionByTarget(java.lang.String
	 * )
	 */
	@Override
	public List<SystemFunction> findFunctionByTarget(String id)
			throws Exception {

		List<SystemFunction> results = systemFunctionDao.findFunctionByTarget(
				SystemFunction.class, id, transaction);

		return results;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.SystemFunctionService#findFunctionByUser(java.lang.String)
	 */
	@Override
	public List<SystemFunction> findFunctionByUser(String userName)
			throws Exception {

		User user = userDao.get(User.class, userName, transaction);
		List<SystemFunction> normalFunction = systemFunctionDao
				.findFunctionByRole(SystemFunction.class, user.getRoleName(),
						transaction);
		List<SystemFunction> temporaryFunction = systemFunctionDao
				.findTemporaryAuthorityFunction(new Class<?>[] {
						SystemFunction.class, TemporaryAuthority.class },
						userName, transaction);

		normalFunction.addAll(temporaryFunction);
		return normalFunction;
	}

	@Override
	public List<SystemFunction> findFunctionByRole(String roleName)
			throws Exception {
		return systemFunctionDao.findFunctionByRole(SystemFunction.class,
				roleName, transaction);
	}

	public List<SystemFunction> findTemporaryAuthorityFunctionByUser(
			String userName) throws Exception {
		return systemFunctionDao.findTemporaryAuthorityFunction(new Class<?>[] {
				SystemFunction.class, TemporaryAuthority.class }, userName,
				transaction);
	}

}
