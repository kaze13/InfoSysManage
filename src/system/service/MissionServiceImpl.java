package system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.MissionPartitionInject;
import system.dao.impl.MissionPartitionDaoImpl;
import system.interceptor.TimerInterceptor;
import system.po.Mission;
import system.po.MissionPartition;
import system.po.Task;
import system.service.spec.MissionService;

@Interceptors(TimerInterceptor.class)
public class MissionServiceImpl extends AbstractDataAccessService<Mission>
		implements MissionService,Serializable {



	@MissionPartitionInject @Inject
	private MissionPartitionDaoImpl missionPartitionDao;
	public MissionServiceImpl() {
		super(Mission.class);
	}

	public List<Mission> findMissionByLeader(String leaderName) throws Exception
	{
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("leader_name", leaderName);
		return this.findAllByCondition(sqlWhereMap);
	}
	public void startMission(Mission mission) throws Exception {
		mission.setProgress(0);
		update(mission);

	}

	public void acceptMission(Mission mission) throws Exception {
		mission.setProgress(100);
		update(mission);
	}

	public void rejectCompleteMission(Mission mission, String reason)
			throws Exception {
		mission.setProgress(-6);
		update(mission);

	}

	public void rejectMission(Mission mission, String reason) throws Exception {
		mission.setProgress(-3);
		update(mission);
	}

	public void submitMission(String reciever, Mission mission, String report, String fileid) throws Exception
	{
		mission.setProgress(-4);
		update(mission);
	}
	public Task saveTemporaryMission(Mission mission, long endTime, String fileid) throws Exception
	{
//		this.save(mission);
//		return taskService.startNewMissionTask(mission, endTime, fileid, false);
		return null;
	}
	public void verifyMission(Mission mission) throws Exception {
		mission.setProgress(-2);
		update(mission);
	}

	public int calculateProgress(Mission mission) throws Exception {
		List<MissionPartition> missionPartitionList = missionPartitionDao
				.findPartitionWithMission(MissionPartition.class, mission.getId(),transaction);

		int completeProgress = 0;
		int totalProgress = 0;
		for (MissionPartition m : missionPartitionList) {
			totalProgress += 100;
			completeProgress += m.getProgress();
		}

		return completeProgress / totalProgress;
	}

//	public Mission findMissionWithPartition(String partitionid)
//			throws Exception {
//		try {
//			transaction.begin();
//			Mission result = missionDao.findMissionWithPartition(partitionid,
//					transaction);
//			transaction.commit();
//			return result;
//		} catch (IOException e) {
//			transaction.rollback();
//			throw e;
//		}
//	}
}
