package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.TaskReport;

public interface TaskReportService {

	public void save(TaskReport t) throws Exception;

	public void save(List<TaskReport> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(TaskReport t) throws Exception;

	public TaskReport get(String id) throws Exception;

	public List<TaskReport> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;

	public abstract TaskReport findReportWithTarget(String id)
			throws Exception;

	public abstract TaskReport findReportWithTask(String taskid)
			throws Exception;

}