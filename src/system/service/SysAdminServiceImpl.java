package system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.SysAdmin;
import system.service.spec.SysAdminService;

@Interceptors(TimerInterceptor.class)
public class SysAdminServiceImpl extends AbstractDataAccessService<SysAdmin>
		implements Serializable, SysAdminService {



	public SysAdminServiceImpl() {
		super(SysAdmin.class);
	}

	/* (non-Javadoc)
	 * @see system.service.SysAdminService#findAdminRelationBySystem(java.lang.String)
	 */
	@Override
	public List<SysAdmin> findAdminRelationBySystem(String systemName) throws Exception {
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("system_name", systemName);
		return this.findAllByCondition(sqlMap);
	}
}
