package system.vo.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import system.po.MainAuthority;
import system.service.MainAuthorityServiceImpl;
import system.service.SystemFunctionServiceImpl;
import system.service.SystemServiceImpl;
import system.service.TaskReportServiceImpl;
import system.service.TaskServiceImpl;
import system.service.TemporaryAuthorityServiceImpl;
import system.service.MissionServiceImpl;
import system.service.UserServiceImpl;
import system.vo.TemporaryAuthorityWrap;
import system.vo.MissionWrap;
import system.vo.UserWrap;

@Singleton
public class MissionWrapGetter implements Serializable {

	@Inject
	private MissionServiceImpl missionService;
	@Inject
	private MainAuthorityServiceImpl mainAuthorityService;
	@Inject
	private TemporaryAuthorityServiceImpl temporaryAuthorityService;
	@Inject
	private SystemFunctionServiceImpl systemFunctionService;
	@Inject
	private TaskServiceImpl taskService;
	@Inject
	private UserServiceImpl userService;
	@Inject
	private TaskReportServiceImpl taskReportService;
	@Inject
	private SystemServiceImpl systemService;
	@Inject
	private UserWrapGetter userWrapGetter;
	private Map<String, MissionWrap> cachePool = new HashMap<String, MissionWrap>();

	public MissionWrap get(String id) throws Exception {
		MissionWrap mission = null;
		if (id == null || id.equals("none"))
			return null;
		if (cachePool.containsKey(id))
			mission = cachePool.get(id);
		else {
			mission = new MissionWrap(missionService.get(id));
		}

		if (mission.getLeader() == null)
			mission.setLeader(userWrapGetter.get(mission.getLeaderName()));
		if (mission.getTask() == null)
			mission.setTask(taskService.findTaskByDataid(mission.getId()));
		if (mission.getTaskReport() == null)
			mission.setTaskReport(taskReportService
					.findReportWithTarget(mission.getId()));
		if (mission.getDependentSystems() == null)
			mission.setDependentSystems(systemService
					.findDependentSystems(mission.getId()));
		if (mission.getDependentFunctions() == null)
			mission.setDependentFunctions(systemFunctionService
					.findFunctionByTarget(mission.getId()));
		return mission;
	}
}
