package system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.MissionPartitionInject;
import system.annotation.MissionUnitInject;
import system.dao.impl.MissionPartitionDaoImpl;
import system.dao.impl.MissionUnitDaoImpl;
import system.interceptor.TimerInterceptor;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.Task;
import system.service.spec.MissionPartitionService;

@Interceptors(TimerInterceptor.class)
public class MissionPartitionServiceImpl extends
		AbstractDataAccessService<MissionPartition> implements Serializable, MissionPartitionService {



	@MissionUnitInject @Inject
	private MissionUnitDaoImpl missionUnitDao;
	@MissionPartitionInject @Inject
	private MissionPartitionDaoImpl missionPartitionDao;
	public MissionPartitionServiceImpl() {
		super(MissionPartition.class);
	}

	/* (non-Javadoc)
	 * @see system.service.MissionPartitionService#startMissionPartition(system.po.MissionPartition)
	 */
	@Override
	public void startMissionPartition(MissionPartition missionPartition)
			throws Exception {
		missionPartition.setProgress(0);
		update(missionPartition);
	}

	public void acceptPartition(MissionPartition missionPartition) throws Exception {
		missionPartition.setProgress(100);
		update(missionPartition);
	}

	/* (non-Javadoc)
	 * @see system.service.MissionPartitionService#rejectPartition(system.po.MissionPartition, java.lang.String)
	 */
	@Override
	public void rejectPartition(MissionPartition missionPartition, String reason)
			throws Exception {
		missionPartition.setProgress(-3);
		update(missionPartition);

	}

	public void rejectCompletePartition(MissionPartition missionPartition, String reason)
			throws Exception {
		missionPartition.setProgress(-6);
		update(missionPartition);

	}

	public void submitPartition(String reciever, MissionPartition missionPartition, String report, String fileid) throws Exception
	{
		missionPartition.setProgress(-4);
		update(missionPartition);
	}

	/* (non-Javadoc)
	 * @see system.service.MissionPartitionService#saveTemporaryPartition(system.po.MissionPartition, long, java.lang.String)
	 */
	@Override
	public Task saveTemporaryPartition(MissionPartition missionPartition,
			long endTime, String fileid) throws Exception {
//		this.save(missionPartition);
//		return taskService.startNewPartitionTask(missionPartition, endTime,
//				fileid, false);
		return null;
	}

	/* (non-Javadoc)
	 * @see system.service.MissionPartitionService#findPartitionWithMission(java.lang.String)
	 */
	@Override
	public List<MissionPartition> findPartitionWithMission(String missionid)
			throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("mission_id", missionid);
		List<MissionPartition> results = this.findAllByCondition(sqlWhereMap);
		return results;

	}

	public List<MissionPartition> findDependentPartition(String partition) throws Exception {
		List<MissionPartition> results = missionPartitionDao.findDependentPartition(MissionPartition.class, partition,
				transaction);

		return results;

	}
	/* (non-Javadoc)
	 * @see system.service.MissionPartitionService#findMissionPartitionByLeader(java.lang.String)
	 */
	@Override
	public List<MissionPartition> findMissionPartitionByLeader(String leaderName) throws Exception
	{
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("leader_name", leaderName);
		List<MissionPartition> results = this.findAllByCondition(sqlWhereMap);
		return results;
	}
	//
	// public MissionPartition findPartitionWithUnit(String unitid) throws
	// Exception {
	// try {
	// transaction.begin();
	// MissionPartition result = missionPartitionDao
	// .findPartitionWithUnit(unitid, transaction);
	// transaction.commit();
	// return result;
	// } catch (IOException e) {
	// transaction.rollback();
	// throw e;
	// }
	// }

	/* (non-Javadoc)
	 * @see system.service.MissionPartitionService#deliverPartition(system.po.MissionPartition, long, java.lang.String)
	 */
	@Override
	public void deliverPartition(MissionPartition missionPartition,
			long endTime, String fileid) throws Exception {
		missionPartition.setProgress(-2);
		update(missionPartition);



	}

	/* (non-Javadoc)
	 * @see system.service.MissionPartitionService#calculateProgress(system.po.MissionPartition)
	 */
	@Override
	public int calculateProgress(MissionPartition missionPartition)
			throws Exception {
		List<MissionUnit> missionUnitList = missionUnitDao
				.findUnitWithPartition(MissionUnit.class, missionPartition.getId(),transaction);

		int completeProgress = 0;
		int totalProgress = 0;
		for (MissionUnit m : missionUnitList) {
			totalProgress += 100;
			completeProgress += m.getProgress();
		}

		return completeProgress / totalProgress;
	}

}
