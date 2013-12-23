package system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.MissionUnitInject;
import system.dao.impl.MissionUnitDaoImpl;
import system.interceptor.TimerInterceptor;
import system.po.MissionUnit;
import system.service.spec.MissionUnitService;

@Interceptors(TimerInterceptor.class)
public class MissionUnitServiceImpl extends
		AbstractDataAccessService<MissionUnit> implements MissionUnitService,
		Serializable {

	@Inject
	@MissionUnitInject
	private MissionUnitDaoImpl missionUnitDao;

	public MissionUnitServiceImpl() {
		super(MissionUnit.class);
	}

	public void startMissionUnit(MissionUnit missionUnit) throws Exception {
		missionUnit.setProgress(0);
		update(missionUnit);
	}

	public void acceptUnit(MissionUnit missionUnit) throws Exception {
		missionUnit.setProgress(100);
		update(missionUnit);
	}

	public void rejectUnit(MissionUnit missionUnit, String reason)
			throws Exception {
		missionUnit.setProgress(-3);
		update(missionUnit);

	}

	public void rejectCompleteUnit(MissionUnit missionUnit, String reason)
			throws Exception {
		missionUnit.setProgress(-6);
		update(missionUnit);

	}

	public void submitUnit(String reciever, MissionUnit missionUnit,
			String report, String fileid) throws Exception {
		missionUnit.setProgress(-4);
		update(missionUnit);
	}

	public void transferUnit(String target, MissionUnit missionUnit)
			throws Exception {
		missionUnit.setLeaderName(target);
		update(missionUnit);
	}

	public void deliverUnit(MissionUnit missionUnit, long endTime, String fileid)
			throws Exception {
		missionUnit.setProgress(-2);
		update(missionUnit);
	}

	public void abandonUnit(MissionUnit missionUnit) throws Exception {
		missionUnit.setProgress(-2);
		update(missionUnit);
	}

	public List<MissionUnit> findUnitWithPartition(String partitionid)
			throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("partition_id", partitionid);
		List<MissionUnit> results = this.findAllByCondition(sqlWhereMap);
		return results;
	}

	public List<MissionUnit> findDependentUnit(String unitid) throws Exception {
		List<MissionUnit> results = missionUnitDao.findDependentUnit(MissionUnit.class, unitid,
				transaction);

		return results;

	}
}
