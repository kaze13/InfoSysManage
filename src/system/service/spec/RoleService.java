package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.Role;

public interface RoleService {

	public void save(Role t) throws Exception;

	public void save(List<Role> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(Role t) throws Exception;

	public Role get(String id) throws Exception;

	public List<Role> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;
}