package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.SystemAuthority;

public interface SystemAuthorityService {

	public void save(SystemAuthority t) throws Exception;

	public void save(List<SystemAuthority> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(SystemAuthority t) throws Exception;

	public SystemAuthority get(String id) throws Exception;

	public List<SystemAuthority> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;
}