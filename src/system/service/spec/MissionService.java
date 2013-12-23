package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.Mission;
import system.po.Task;

public interface MissionService {

	public void save(Mission t) throws Exception;

	public void save(List<Mission> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(Mission t) throws Exception;

	public Mission get(String id) throws Exception;

	public List<Mission> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;

	public List<Mission> findMissionByLeader(String leaderName)
			throws Exception;

	public void startMission(Mission mission) throws Exception;

	public void rejectMission(Mission mission, String reason) throws Exception;

	public Task saveTemporaryMission(Mission mission, long endTime,
			String fileid) throws Exception;

	public void verifyMission(Mission mission) throws Exception;

	public int calculateProgress(Mission mission) throws Exception;
}
