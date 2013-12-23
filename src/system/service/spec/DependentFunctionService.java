package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.DependentFunction;

public interface DependentFunctionService {

	public void save(DependentFunction t) throws Exception;

	public void save(List<DependentFunction> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(DependentFunction t) throws Exception;

	public DependentFunction get(String id) throws Exception;

	public List<DependentFunction> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;


	public abstract void refresh(List<DependentFunction> list, String id)
			throws Exception;

	public abstract List<DependentFunction> findFunctionByTarget(String id)
			throws Exception;

}