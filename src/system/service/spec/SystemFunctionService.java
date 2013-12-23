package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.System;
import system.po.SystemFunction;

public interface SystemFunctionService {

	public void save(SystemFunction t) throws Exception;

	public void save(List<SystemFunction> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(SystemFunction t) throws Exception;

	public SystemFunction get(String id) throws Exception;

	public List<SystemFunction> findAllByCondition(
			Map<String, Object> sqlWhereMap) throws Exception;

	public abstract List<SystemFunction> get(String[] idList) throws Exception;

	public abstract List<SystemFunction> findFunctionBySystem(String systemName)
			throws Exception;

	public abstract List<SystemFunction> findFunctionBySystems(
			List<System> systems) throws Exception;

	public abstract List<SystemFunction> findFunctionByTarget(String id)
			throws Exception;

	public abstract List<SystemFunction> findFunctionByUser(String userName)
			throws Exception;

	public abstract List<SystemFunction> findFunctionByRole(String roleName)
			throws Exception;

}