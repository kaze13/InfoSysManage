package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.Authority;

public interface AuthorityService {

	public void save(Authority t) throws Exception;

	public void save(List<Authority> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(Authority t) throws Exception;

	public Authority get(String id) throws Exception;

	public List<Authority> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;
}