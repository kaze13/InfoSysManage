package system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.TaskReportInject;
import system.dao.impl.TaskReportDaoImpl;
import system.interceptor.TimerInterceptor;
import system.po.TaskReport;
import system.service.spec.TaskReportService;

@Interceptors(TimerInterceptor.class)
public class TaskReportServiceImpl extends
		AbstractDataAccessService<TaskReport> implements Serializable,
		TaskReportService {

	@Inject
	@TaskReportInject
	private TaskReportDaoImpl taskReportDao;

	public TaskReportServiceImpl() {
		super(TaskReport.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.TaskReportService#findReportWithUnit(java.lang.String)
	 */
	@Override
	public TaskReport findReportWithTarget(String id) throws Exception {

		TaskReport result = taskReportDao.findTaskReportWithTarget(TaskReport.class, id,
				transaction);

		return result;

	}



	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.TaskReportService#findReportWithTask(java.lang.String)
	 */
	@Override
	public TaskReport findReportWithTask(String taskid) throws Exception {
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("task_id", taskid);
		List<TaskReport> result = this.findAllByCondition(sqlMap);
		if (result.size() > 1)
			throw new Exception("more than one report");
		if (result.size() == 0)
			return null;
		else
			return result.get(0);
	}

}
