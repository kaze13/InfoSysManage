package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.MissionUnit;

public interface MissionUnitService {

	public void save(MissionUnit t) throws Exception;

	public void save(List<MissionUnit> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(MissionUnit t) throws Exception;

	public MissionUnit get(String id) throws Exception;

	public List<MissionUnit> findAllByCondition(
			Map<String, Object> sqlWhereMap) throws Exception;

	public void startMissionUnit(MissionUnit missionUnit) throws Exception;
	public void acceptUnit(MissionUnit missionUnit) throws Exception;
	public void rejectUnit(MissionUnit missionUnit, String reason)
			throws Exception;
	public void rejectCompleteUnit(MissionUnit missionUnit, String reason)
			throws Exception ;
	public void submitUnit(String reciever, MissionUnit missionUnit,
			String report, String fileid) throws Exception;
	public void transferUnit(String target, MissionUnit missionUnit) throws Exception ;
	public void deliverUnit(MissionUnit missionUnit, long endTime, String fileid)
			throws Exception;
	public void abandonUnit(MissionUnit missionUnit) throws Exception;
	public List<MissionUnit> findUnitWithPartition(String partitionid)
			throws Exception ;
	public List<MissionUnit> findDependentUnit(String unitid) throws Exception ;
}
