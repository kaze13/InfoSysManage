package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.SysAdmin;

public interface SysAdminService {

	public void save(SysAdmin t) throws Exception;

	public void save(List<SysAdmin> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(SysAdmin t) throws Exception;

	public SysAdmin get(String id) throws Exception;

	public List<SysAdmin> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;

	public abstract List<SysAdmin> findAdminRelationBySystem(String systemName)
			throws Exception;

}