package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.TemporaryAuthority;

public interface TemporaryAuthorityService {

	public void save(TemporaryAuthority t) throws Exception;

	public void save(List<TemporaryAuthority> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(TemporaryAuthority t) throws Exception;

	public TemporaryAuthority get(String id) throws Exception;

	public List<TemporaryAuthority> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;
}