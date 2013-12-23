package system.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.GenericDaoInject;
import system.annotation.MissionPartitionInject;
import system.annotation.UnitDependencyInject;
import system.dao.spec.Transaction;
import system.interceptor.GetCacheInterceptor;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.PartitionDependency;
import system.po.UnitDependency;

@MissionPartitionInject
public class MissionPartitionDaoImpl extends GenericDaoImpl<MissionPartition> {

	@GenericDaoInject
	@Inject
	private GenericDaoImpl<PartitionDependency> partitionDependencyDao;

	// @Interceptors(GetCacheInterceptor.class)
	public List<MissionPartition> findPartitionWithMission(
			Class<MissionPartition> useless, String id, Transaction transaction)
			throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("mission_id", id);
		List<MissionPartition> results = this.findAllByConditions(
				MissionPartition.class, sqlWhereMap, "and", true, transaction);
		return results;
	}

	public List<MissionPartition> findDependentPartition(
			Class<MissionPartition> useless, String partitionid,
			Transaction transaction) throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(partitionid);
		String sql = "select a.id,a.mission_id as missionid,a.leader_name as leaderName,"
				+ " a.title,a.description,a.progress,a.file_id as fileId from rd4_mission_partition a , rd4_partition_dependency b"
				+ " where a.id=b.pre_partition_id and b.post_partition_id=?";

		// TODO cache
		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (!cachePool.containsKey(MissionPartition.class)) {
			List<MissionPartition> partitionData = this.findAllByConditions(
					MissionPartition.class, null, "and", false, transaction);
			List<MissionPartition> copy = new ArrayList<MissionPartition>(
					partitionData);
			cachePool
					.put(MissionPartition.class, (List<MissionPartition>) copy);
		}
		List<MissionPartition> partitionCache = (List<MissionPartition>) cachePool
				.get(MissionPartition.class);

		if (!cachePool.containsKey(PartitionDependency.class)) {
			List<PartitionDependency> partitionDependencyData = partitionDependencyDao
					.findAllByConditions(PartitionDependency.class, null,
							"and", false, transaction);
			List<PartitionDependency> copy = new ArrayList<PartitionDependency>(
					partitionDependencyData);
			cachePool.put(PartitionDependency.class,
					(List<PartitionDependency>) copy);
		}
		List<PartitionDependency> partitionDependencyCache = (List<PartitionDependency>) cachePool
				.get(PartitionDependency.class);

		List<MissionPartition> result = new ArrayList<MissionPartition>();
		for (MissionPartition partition : partitionCache) {
			for (PartitionDependency dependency : partitionDependencyCache) {
				if (partition.getId().equals(dependency.getPrePartitionId())
						&& dependency.getPostPartitionId().equals(partitionid)) {
					result.add(partition.clone());
				}
			}
		}
		return result;
		// return generalExecute(MissionPartition.class, params, sql,
		// transaction);
	}
}
