package system.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.MissionUnitInject;
import system.annotation.UnitDependencyInject;
import system.dao.spec.Transaction;
import system.interceptor.GetCacheInterceptor;
import system.po.MissionUnit;
import system.po.UnitDependency;

@MissionUnitInject
public class MissionUnitDaoImpl extends GenericDaoImpl<MissionUnit> {

	@UnitDependencyInject
	@Inject
	private UnitDependencyDaoImpl unitDependencyDao;

	// @Interceptors(GetCacheInterceptor.class)
	public List<MissionUnit> findDependentUnit(Class<MissionUnit> useless,
			String unitid, Transaction transaction) throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(unitid);
		String sql = "select a.id,a.partition_id as partitionid,a.leader_name as leaderName,"
				+ " a.title,a.description,a.progress,a.file_id as fileId from rd4_mission_unit a , rd4_unit_dependency b"
				+ " where a.id=b.pre_unit_id and b.post_unit_id=?";

		// TODO cache
		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (!cachePool.containsKey(MissionUnit.class)) {
			List<MissionUnit> unitData = this.findAllByConditions(
					MissionUnit.class, null, "and", false, transaction);
			List<MissionUnit> copy = new ArrayList<MissionUnit>(unitData);
			cachePool.put(MissionUnit.class, (List<MissionUnit>) copy);
		}
		List<MissionUnit> unitCache = (List<MissionUnit>) cachePool
				.get(MissionUnit.class);

		if (!cachePool.containsKey(UnitDependency.class)) {
			List<UnitDependency> unitDependencyData = unitDependencyDao
					.findAllByConditions(UnitDependency.class, null, "and",
							false, transaction);
			List<UnitDependency> copy = new ArrayList<UnitDependency>(
					unitDependencyData);
			cachePool.put(UnitDependency.class, (List<UnitDependency>) copy);
		}
		List<UnitDependency> unitDependencyCache = (List<UnitDependency>) cachePool
				.get(UnitDependency.class);

		List<MissionUnit> result = new ArrayList<MissionUnit>();
		for (MissionUnit unit : unitCache) {
			for (UnitDependency dependency : unitDependencyCache) {
				if (unit.getId().equals(dependency.getPreUnitId())
						&& dependency.getPostUnitId().equals(
								unitid)) {
					result.add(unit.clone());
				}
			}
		}
		return result;
		// return generalExecute(MissionUnit.class, params, sql, transaction);
	}

	// @Interceptors(GetCacheInterceptor.class)
	public List<MissionUnit> findUnitWithPartition(Class<MissionUnit> useless,
			String id, Transaction transaction) throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("partition_id", id);
		List<MissionUnit> results = this.findAllByConditions(MissionUnit.class,
				sqlWhereMap, "and", true, transaction);
		return results;
	}
}