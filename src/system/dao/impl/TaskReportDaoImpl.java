package system.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.GenericDaoInject;
import system.annotation.TaskReportInject;
import system.dao.spec.Transaction;
import system.interceptor.GetCacheInterceptor;
import system.po.MissionUnit;
import system.po.Task;
import system.po.TaskReport;

@TaskReportInject
public class TaskReportDaoImpl extends GenericDaoImpl<TaskReport>{


	@GenericDaoInject @Inject
	private GenericDaoImpl<Task> taskDao;
//	@Interceptors(GetCacheInterceptor.class)
	public TaskReport findTaskReportWithTarget(Class<TaskReport> useless, String unitid, Transaction transaction) throws Exception
	{
		List<Object> params = new ArrayList<Object>();
		params.add(unitid);
		String sql = "select a.id as id,a.task_id as taskId, a.report as report," +
				"a.file_id as fileId from" +
				" RD4_TASK_REPORT a , RD4_TASK b where" +
				" a.task_id=b.id and b.data=?";

		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (!cachePool.containsKey(TaskReport.class)) {
			List<TaskReport> data = this.findAllByConditions(
					TaskReport.class, null, "and", false, transaction);
			List<TaskReport> copy = new ArrayList<TaskReport>(data);
			cachePool.put(TaskReport.class, (List<TaskReport>) copy);
		}
		List<TaskReport> cacheA = (List<TaskReport>) cachePool
				.get(TaskReport.class);

		if (!cachePool.containsKey(Task.class)) {
			List<Task> data = taskDao
					.findAllByConditions(Task.class, null, "and",
							false, transaction);
			List<Task> copy = new ArrayList<Task>(data);
			cachePool.put(Task.class, (List<Task>) copy);
		}
		List<Task> cacheB = (List<Task>) cachePool
				.get(Task.class);

		List<TaskReport> result = new ArrayList<TaskReport>();
		for (TaskReport a : cacheA) {
			for (Task b : cacheB) {
				if (a.getTaskId().equals(b.getId()) && b.getData().equals(unitid)) {
					result.add(a.clone());
				}
			}
		}
//		return result;
//
//		List<TaskReport> result =  generalExecute(TaskReport.class,params, sql,  transaction);
		if(result.size() == 0)
			return null;
		else
			return result.get(0);
	}



}
