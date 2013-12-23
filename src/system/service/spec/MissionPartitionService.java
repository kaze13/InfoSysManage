package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.MissionPartition;
import system.po.Task;

public interface MissionPartitionService {

	public void save(MissionPartition t) throws Exception;

	public void save(List<MissionPartition> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(MissionPartition t) throws Exception;

	public MissionPartition get(String id) throws Exception;

	public List<MissionPartition> findAllByCondition(
			Map<String, Object> sqlWhereMap) throws Exception;

	public void startMissionPartition(MissionPartition missionPartition)
			throws Exception;

	public void rejectPartition(MissionPartition missionPartition, String reason)
			throws Exception;

	public Task saveTemporaryPartition(MissionPartition missionPartition,
			long endTime, String fileid) throws Exception;

	public List<MissionPartition> findPartitionWithMission(String missionid)
			throws Exception;

	public List<MissionPartition> findMissionPartitionByLeader(String leaderName)
			throws Exception;

	public void deliverPartition(MissionPartition missionPartition,
			long endTime, String fileid) throws Exception;

	public int calculateProgress(MissionPartition missionPartition)
			throws Exception;
}
