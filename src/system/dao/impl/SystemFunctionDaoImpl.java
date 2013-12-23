package system.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.GenericDaoInject;
import system.annotation.SystemFunctionInject;
import system.dao.spec.Transaction;
import system.interceptor.GetCacheInterceptor;
import system.po.DependentFunction;
import system.po.SysAdmin;
import system.po.System;
import system.po.SystemAuthority;
import system.po.SystemFunction;
import system.po.TemporaryAuthority;
import system.po.spec.Printable;

@SystemFunctionInject
public class SystemFunctionDaoImpl extends GenericDaoImpl<SystemFunction> {

	@GenericDaoInject @Inject
	private GenericDaoImpl<SystemAuthority> systemAuthorityDao;
	@GenericDaoInject @Inject
	private GenericDaoImpl<TemporaryAuthority> temporaryAuthorityDao;
	@GenericDaoInject @Inject
	private GenericDaoImpl<DependentFunction> dependentFunctionDao;
	// @Interceptors(GetCacheInterceptor.class)
	public List<SystemFunction> findFunctionByRole(
			Class<SystemFunction> useless, String roleName,
			Transaction transaction) throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(roleName);
		String sql = "select a.id,a.system_name as systemName, a.function_name as functionName, "
				+ "a.description from rd4_system_function a inner join rd4_system_authority b on"
				+ " a.id=b.system_function_id and b.role_name=?";

		String sqlOrcl = "select a.id,a.system_name as systemName, a.function_name as functionName, "
				+ "a.description from rd4_system_function a, rd4_system_authority b where"
				+ " a.id=b.system_function_id and b.role_name=?";

		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (!cachePool.containsKey(SystemFunction.class)) {
			List<SystemFunction> data = this.findAllByConditions(
					SystemFunction.class, null, "and", false, transaction);
			List<SystemFunction> copy = new ArrayList<SystemFunction>(data);
			cachePool.put(SystemFunction.class, (List<SystemFunction>) copy);
		}
		List<SystemFunction> cacheA = (List<SystemFunction>) cachePool
				.get(SystemFunction.class);

		if (!cachePool.containsKey(SystemAuthority.class)) {
			List<SystemAuthority> data = systemAuthorityDao
					.findAllByConditions(SystemAuthority.class, null, "and",
							false, transaction);
			List<SystemAuthority> copy = new ArrayList<SystemAuthority>(data);
			cachePool.put(SystemAuthority.class, (List<SystemAuthority>) copy);
		}
		List<SystemAuthority> cacheB = (List<SystemAuthority>) cachePool
				.get(SystemAuthority.class);

		List<SystemFunction> result = new ArrayList<SystemFunction>();
		for (SystemFunction a : cacheA) {
			for (SystemAuthority b : cacheB) {
				if (a.getId().equals(b.getSystemFunctionId())
						&& b.getRoleName().equals(roleName)) {
					result.add(a.clone());
				}
			}
		}
		return result;
//		return generalExecute(SystemFunction.class, params, sqlOrcl,
//				transaction);
	}

	// @Interceptors(GetCacheInterceptor.class)
	public List<SystemFunction> findTemporaryAuthorityFunction(
			Class<?>[] useless, String userName, Transaction transaction)
			throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(userName);
		String sql = "select a.id,a.system_name as systemName, a.function_name as functionName, "
				+ "a.description from rd4_system_function a inner join rd4_temporary_authority b on"
				+ " a.id=b.system_function_id and b.user_name=?";

		String sqlOrcl = "select a.id,a.system_name as systemName, a.function_name as functionName, "
				+ "a.description from rd4_system_function a, rd4_temporary_authority b where"
				+ " a.id=b.system_function_id and b.user_name=?";


		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (!cachePool.containsKey(SystemFunction.class)) {
			List<SystemFunction> data = this.findAllByConditions(
					SystemFunction.class, null, "and", false, transaction);
			List<SystemFunction> copy = new ArrayList<SystemFunction>(data);
			cachePool.put(SystemFunction.class, (List<SystemFunction>) copy);
		}
		List<SystemFunction> cacheA = (List<SystemFunction>) cachePool
				.get(SystemFunction.class);

		if (!cachePool.containsKey(TemporaryAuthority.class)) {
			List<TemporaryAuthority> data = temporaryAuthorityDao
					.findAllByConditions(TemporaryAuthority.class, null, "and",
							false, transaction);
			List<TemporaryAuthority> copy = new ArrayList<TemporaryAuthority>(data);
			cachePool.put(TemporaryAuthority.class, (List<TemporaryAuthority>) copy);
		}
		List<TemporaryAuthority> cacheB = (List<TemporaryAuthority>) cachePool
				.get(TemporaryAuthority.class);

		List<SystemFunction> result = new ArrayList<SystemFunction>();
		for (SystemFunction a : cacheA) {
			for (TemporaryAuthority b : cacheB) {
				if (a.getId().equals(b.getSystemFunctionId()) && b.getUserName().equals(userName)) {
					result.add(a.clone());
				}
			}
		}
		return result;



//		return generalExecute(SystemFunction.class, params, sqlOrcl,
//				transaction);
	}

	// @Interceptors(GetCacheInterceptor.class)
	public List<SystemFunction> findFunctionByTarget(
			Class<SystemFunction> useless, String id, Transaction transaction)
			throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		String sql = "select a.id,a.system_name as systemName, a.function_name as functionName, "
				+ "a.description from rd4_system_function a inner join rd4_dependent_function b on"
				+ " a.id=b.function_id and b.target_id=?";

		String sqlOrcl = "select a.id,a.system_name as systemName, a.function_name as functionName, "
				+ "a.description from rd4_system_function a, rd4_dependent_function b where"
				+ " a.id=b.function_id and b.target_id=?";

		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (!cachePool.containsKey(SystemFunction.class)) {
			List<SystemFunction> data = this.findAllByConditions(
					SystemFunction.class, null, "and", false, transaction);
			List<SystemFunction> copy = new ArrayList<SystemFunction>(data);
			cachePool.put(SystemFunction.class, (List<SystemFunction>) copy);
		}
		List<SystemFunction> cacheA = (List<SystemFunction>) cachePool
				.get(SystemFunction.class);

		if (!cachePool.containsKey(DependentFunction.class)) {
			List<DependentFunction> data = dependentFunctionDao
					.findAllByConditions(DependentFunction.class, null, "and",
							false, transaction);
			List<DependentFunction> copy = new ArrayList<DependentFunction>(data);
			cachePool.put(DependentFunction.class, (List<DependentFunction>) copy);
		}
		List<DependentFunction> cacheB = (List<DependentFunction>) cachePool
				.get(DependentFunction.class);

		List<SystemFunction> result = new ArrayList<SystemFunction>();
		for (SystemFunction a : cacheA) {
			for (DependentFunction b : cacheB) {
				if (a.getId().equals(b.getFunctionId()) && b.getTargetId().equals(id)) {
					result.add(a.clone());
				}
			}
		}
		return result;

//		return generalExecute(SystemFunction.class, params, sqlOrcl,
//				transaction);

	}
}
