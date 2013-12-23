package system.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.GenericDaoInject;
import system.annotation.TaskInject;
import system.dao.spec.Transaction;
import system.interceptor.GetCacheInterceptor;
import system.po.DependentFunction;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.SystemFunction;
import system.po.Task;

@TaskInject

public class TaskDaoImpl extends GenericDaoImpl<Task> {

	@GenericDaoInject @Inject
	private GenericDaoImpl<MissionPartition> missionPartitionDao;
	@GenericDaoInject @Inject
	private GenericDaoImpl<MissionUnit> missionUnitDao;
//	@Interceptors(GetCacheInterceptor.class)
	public List<Task> findPartitionTasksByMission(Class<Task> useless, String missionid,
			Transaction transaction) throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(missionid);
		String sql = "select a.id,a.creator_name as creatorName,a.owner_name as ownerName," +
				"a.type,a.title,a.description,a.start_time as startTime,a.end_time as " +
				"endTime, a.statue,a.data,a.file_id as fileId from rd4_task a ," +
				" rd4_mission_partition b where a.data=b.id and b.mission_id=?";

		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (!cachePool.containsKey(Task.class)) {
			List<Task> data = this.findAllByConditions(
					Task.class, null, "and", false, transaction);
			List<Task> copy = new ArrayList<Task>(data);
			cachePool.put(Task.class, (List<Task>) copy);
		}
		List<Task> cacheA = (List<Task>) cachePool
				.get(Task.class);

		if (!cachePool.containsKey(MissionPartition.class)) {
			List<MissionPartition> data = missionPartitionDao
					.findAllByConditions(MissionPartition.class, null, "and",
							false, transaction);
			List<MissionPartition> copy = new ArrayList<MissionPartition>(data);
			cachePool.put(MissionPartition.class, (List<MissionPartition>) copy);
		}
		List<MissionPartition> cacheB = (List<MissionPartition>) cachePool
				.get(MissionPartition.class);

		List<Task> result = new ArrayList<Task>();
		for (Task a : cacheA) {
			for (MissionPartition b : cacheB) {
				if (a.getData().equals(b.getId()) && b.getMissionid().equals(missionid)) {
					result.add(a.clone());
				}
			}
		}
		return result;


//		return generalExecute(Task.class,params, sql,  transaction);
	}

	//@Interceptors(GetCacheInterceptor.class)
	public List<Task> findUnitTasksByPartition(Class<Task> useless, String partitionid, Transaction transaction) throws Exception
	{
		List<Object> params = new ArrayList<Object>();
		params.add(partitionid);
		String sql = "select a.id,a.creator_name as creatorName,a.owner_name as ownerName," +
				"a.type,a.title,a.description,a.start_time as startTime,a.end_time as " +
				"endTime, a.statue,a.data,a.file_id as fileId from rd4_task a ," +
				" rd4_mission_unit b where a.data=b.id and b.partition_id=?";

		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (!cachePool.containsKey(Task.class)) {
			List<Task> data = this.findAllByConditions(
					Task.class, null, "and", false, transaction);
			List<Task> copy = new ArrayList<Task>(data);
			cachePool.put(Task.class, (List<Task>) copy);
		}
		List<Task> cacheA = (List<Task>) cachePool
				.get(Task.class);

		if (!cachePool.containsKey(MissionUnit.class)) {
			List<MissionUnit> data = missionUnitDao
					.findAllByConditions(MissionUnit.class, null, "and",
							false, transaction);
			List<MissionUnit> copy = new ArrayList<MissionUnit>(data);
			cachePool.put(MissionUnit.class, (List<MissionUnit>) copy);
		}
		List<MissionUnit> cacheB = (List<MissionUnit>) cachePool
				.get(MissionUnit.class);

		List<Task> result = new ArrayList<Task>();
		for (Task a : cacheA) {
			for (MissionUnit b : cacheB) {
				if (a.getData().equals(b.getId()) && b.getPartitionid().equals(partitionid)) {
					result.add(a.clone());
				}
			}
		}
		return result;
//		return generalExecute(Task.class,params, sql,  transaction);
	}
}
