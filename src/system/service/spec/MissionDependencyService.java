package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.MissionDependency;

public interface MissionDependencyService {

	public void save(MissionDependency t) throws Exception;

	public void save(List<MissionDependency> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(MissionDependency t) throws Exception;

	public MissionDependency get(String id) throws Exception;

	public List<MissionDependency> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;
}