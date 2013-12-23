package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.UnitDependency;
import system.vo.UnitDependencyVo;

public interface UnitDependencyService {

	public void save(UnitDependency t) throws Exception;

	public void save(List<UnitDependency> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(UnitDependency t) throws Exception;

	public UnitDependency get(String id) throws Exception;

	public List<UnitDependency> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;


	public abstract UnitDependencyVo generateVo(UnitDependency dependency)
			throws Exception;

	public abstract boolean check(UnitDependency dependency) throws Exception;

}