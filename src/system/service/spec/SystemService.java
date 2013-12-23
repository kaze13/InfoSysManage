package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.System;

public interface SystemService {

	public void save(System t) throws Exception;

	public void save(List<System> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(System t) throws Exception;

	public System get(String id) throws Exception;

	public List<System> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;

	public List<System> findDependentSystems(String id) throws Exception;

	public List<System> findSystemByAdmin(String adminName) throws Exception;

	public void shutdownSystem(System system) throws Exception;
}
