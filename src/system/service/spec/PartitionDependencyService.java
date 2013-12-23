package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.PartitionDependency;
import system.vo.PartitionDependencyVo;

public interface PartitionDependencyService {

	public void save(PartitionDependency t) throws Exception;

	public void save(List<PartitionDependency> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(PartitionDependency t) throws Exception;

	public PartitionDependency get(String id) throws Exception;

	public List<PartitionDependency> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;

	public abstract PartitionDependencyVo generateVo(
			PartitionDependency dependency) throws Exception;

	public abstract boolean check(PartitionDependency dependency)
			throws Exception;

}