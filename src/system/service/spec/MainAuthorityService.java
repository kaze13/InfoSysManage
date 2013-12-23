package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.MainAuthority;

public interface MainAuthorityService {

	public void save(MainAuthority t) throws Exception;

	public void save(List<MainAuthority> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(MainAuthority t) throws Exception;

	public MainAuthority get(String id) throws Exception;

	public List<MainAuthority> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;

}