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
import system.service.MissionPartitionServiceImpl;
import system.service.SystemFunctionServiceImpl;
import system.service.SystemServiceImpl;
import system.service.TaskReportServiceImpl;
import system.service.TaskServiceImpl;
import system.service.TemporaryAuthorityServiceImpl;
import system.service.UserServiceImpl;
import system.vo.TemporaryAuthorityWrap;
import system.vo.PartitionWrap;
import system.vo.UserWrap;

@Singleton
public class PartitionWrapGetter implements Serializable {

	@Inject
	private MissionPartitionServiceImpl partitionService;
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
	private Map<String, PartitionWrap> cachePool = new HashMap<String, PartitionWrap>();

	public PartitionWrap get(String id) throws Exception {
		PartitionWrap partition = null;
		if (id == null || id.equals("none"))
			return null;
		if (cachePool.containsKey(id))
			partition = cachePool.get(id);
		else {
			partition = new PartitionWrap(partitionService.get(id));
		}

		if (partition.getLeader() == null)
			partition.setLeader(userWrapGetter.get(partition.getLeaderName()));
		if (partition.getTask() == null)
			partition.setTask(taskService.findTaskByDataid(partition.getId()));
		if (partition.getTaskReport() == null)
			partition.setTaskReport(taskReportService
					.findReportWithTarget(partition.getId()));
		if (partition.getDependentSystems() == null)
			partition.setDependentSystems(systemService
					.findDependentSystems(partition.getId()));
		if (partition.getDependentFunctions() == null)
			partition.setDependentFunctions(systemFunctionService
					.findFunctionByTarget(partition.getId()));
		if (partition.getDependentObj() == null)
			partition.setDependentObj(PartitionWrap.convert(partitionService
					.findDependentPartition(partition.getId())));
		return partition;
	}
}
