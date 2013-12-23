package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.DependentSystem;

public interface DependentSystemService {

	public void save(DependentSystem t) throws Exception;

	public void save(List<DependentSystem> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(DependentSystem t) throws Exception;

	public DependentSystem get(String id) throws Exception;

	public List<DependentSystem> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;

	public abstract void refresh(List<DependentSystem> list, String missionid)
			throws Exception;

}