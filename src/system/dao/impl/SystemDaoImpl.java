package system.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.GenericDaoInject;
import system.annotation.SystemInject;
import system.dao.spec.Transaction;
import system.interceptor.GetCacheInterceptor;
import system.po.System;
import system.po.SysAdmin;
import system.po.System;
import system.po.SysAdmin;

@SystemInject
public class SystemDaoImpl extends GenericDaoImpl<System> {

	@GenericDaoInject @Inject
	private GenericDaoImpl<SysAdmin> sysAdminDao;

	// @Interceptors(GetCacheInterceptor.class)
	public List<System> findSystemByAdmin(Class<System> useless,
			String adminName, Transaction transaction) throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(adminName);
		String sql = "select a.system_name as name,a.url,a.description,"
				+ "a.war_path as warPath, a.duration, a.status from rd4_system a ,"
				+ " rd4_system_admin b where a.system_name=b.system_name and b.user_name=?";

		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (!cachePool.containsKey(System.class)) {
			List<System> data = this.findAllByConditions(System.class, null,
					"and", false, transaction);
			List<System> copy = new ArrayList<System>(data);
			cachePool.put(System.class, (List<System>) copy);
		}
		List<System> cacheA = (List<System>) cachePool.get(System.class);

		if (!cachePool.containsKey(SysAdmin.class)) {
			List<SysAdmin> data = sysAdminDao.findAllByConditions(
					SysAdmin.class, null, "and", false, transaction);
			List<SysAdmin> copy = new ArrayList<SysAdmin>(data);
			cachePool.put(SysAdmin.class, (List<SysAdmin>) copy);
		}
		List<SysAdmin> cacheB = (List<SysAdmin>) cachePool.get(SysAdmin.class);

		List<System> result = new ArrayList<System>();
		for (System a : cacheA) {
			for (SysAdmin b : cacheB) {
				if (a.getName().equals(b.getSystemName())
						&& b.getUserName().equals(adminName)) {
					result.add(a.clone());
				}
			}
		}
		return result;
		// return generalExecute(System.class, params, sql, transaction);
	}
}
