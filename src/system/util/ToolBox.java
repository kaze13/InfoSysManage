package system.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import system.interceptor.TimerInterceptor;
import system.interceptor.TransactionInterceptor;
import system.manager.spec.SessionManager;
import system.po.MainAuthority;
import system.po.Task;
import system.po.TemporaryAuthority;
import system.po.UploadFile;
import system.po.User;
import system.po.Task.Type;
import system.po.spec.Downloadable;
import system.po.spec.Printable;
import system.service.ApplicationServiceImpl;
import system.service.AuthorityServiceImpl;
import system.service.DependentFunctionServiceImpl;
import system.service.DependentSystemServiceImpl;
import system.service.MainAuthorityServiceImpl;
import system.service.MissionPartitionServiceImpl;
import system.service.MissionServiceImpl;
import system.service.MissionUnitServiceImpl;
import system.service.NotificationServiceImpl;
import system.service.SystemFunctionServiceImpl;
import system.service.SystemServiceImpl;
import system.service.TaskReportServiceImpl;
import system.service.TaskServiceImpl;
import system.service.TemporaryAuthorityServiceImpl;
import system.service.UploadFileServiceImpl;
import system.service.UserServiceImpl;
import system.vo.ApplicationStageWrap;
import system.vo.MissionWrap;
import system.vo.PartitionWrap;
import system.vo.TaskWrap;
import system.vo.TemporaryAuthorityWrap;
import system.vo.UnitWrap;
import system.vo.UserWrap;

@Named
@SessionScoped
@Interceptors(TimerInterceptor.class)
public class ToolBox implements Serializable {

	@Inject
	private MainAuthorityServiceImpl mainAuthorityService;
	@Inject
	private TemporaryAuthorityServiceImpl temporaryAuthorityService;
	@Inject
	private SystemFunctionServiceImpl systemFunctionService;
	@Inject
	private SystemServiceImpl systemService;
	@Inject
	private MissionServiceImpl missionService;
	@Inject
	private MissionPartitionServiceImpl missionPartitionService;
	@Inject
	private MissionUnitServiceImpl missionUnitService;

	@Inject
	private UploadFileServiceImpl uploadFileService;
	@Inject
	private TaskServiceImpl taskService;
	@Inject
	private TaskReportServiceImpl taskReportService;
	@Inject
	private UserServiceImpl userService;
	private StreamedContent downloadFile;

	// private UserWrap user;

	@Interceptors(TransactionInterceptor.class)
	public void initUserWrap(UserWrap user) throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("user_name", user.getName());
		List<MainAuthority> authorities = mainAuthorityService
				.findAllByCondition(sqlWhereMap);
		List<String> authorityStr = new ArrayList<String>();
		for (MainAuthority authority : authorities) {
			authorityStr.add(authority.getAuthorityName());
		}
		user.setAuthority(authorityStr);
		user.setTemporaryAuthority(TemporaryAuthorityWrap
				.convert(temporaryAuthorityService
						.findAllByCondition(sqlWhereMap)));
		for (TemporaryAuthorityWrap t : user.getTemporaryAuthority()) {
			if (t.getFunction() == null)
				t.setFunction(systemFunctionService.get(t.getSystemFunctionId()));
		}
		user.setAuthorisedFunctions(systemFunctionService
				.findFunctionByRole(user.getRoleName()));
	}

	@Interceptors(TransactionInterceptor.class)
	public void initApplicationStageWrap(ApplicationStageWrap wrap)
			throws Exception {
		wrap.setDealerWrap(new UserWrap(userService.get(wrap.getDealerName())));
	}

	// @Interceptors(TransactionInterceptor.class)
	// public UserWrap getUserWrap(String userName) throws Exception
	// {
	// if(userName == null || userName.equals(""))
	// return null;
	// User user = userService.get(userName);
	// UserWrap wrap = new UserWrap(user);
	// initUserWrap(wrap);
	// return wrap;
	// }

	@Interceptors(TransactionInterceptor.class)
	public void initMissionTarget(Object obj) throws Exception {
		if (obj instanceof MissionWrap) {
			MissionWrap target = (MissionWrap) obj;
			if (target.getLeader() == null)
				target.setLeader(new UserWrap(userService.get(target
						.getLeaderName())));
			if (target.getTask() == null)
				target.setTask(taskService.findTaskByDataid(target.getId()));
			if (target.getTaskReport() == null)
				target.setTaskReport(taskReportService
						.findReportWithTarget(target.getId()));
			if (target.getDependentSystems() == null)
				target.setDependentSystems(systemService
						.findDependentSystems(target.getId()));
			if (target.getDependentFunctions() == null)
				target.setDependentFunctions(systemFunctionService
						.findFunctionByTarget(target.getId()));
		}

		if (obj instanceof PartitionWrap) {
			PartitionWrap target = (PartitionWrap) obj;
			if (target.getLeader() == null)
				target.setLeader(new UserWrap(userService.get(target
						.getLeaderName())));
			if (target.getTask() == null)
				target.setTask(taskService.findTaskByDataid(target.getId()));
			if (target.getTaskReport() == null)
				target.setTaskReport(taskReportService
						.findReportWithTarget(target.getId()));
			if (target.getDependentSystems() == null)
				target.setDependentSystems(systemService
						.findDependentSystems(target.getId()));
			if (target.getDependentFunctions() == null)
				target.setDependentFunctions(systemFunctionService
						.findFunctionByTarget(target.getId()));
			if (target.getDependentObj() == null)
				target.setDependentObj(PartitionWrap.convert(missionPartitionService
						.findDependentPartition(target.getId())));
		}

		if (obj instanceof UnitWrap) {
			UnitWrap target = (UnitWrap) obj;
			if (target.getLeader() == null)
				target.setLeader(new UserWrap(userService.get(target
						.getLeaderName())));
			if (target.getTask() == null)
				target.setTask(taskService.findTaskByDataid(target.getId()));
			if (target.getTaskReport() == null)
				target.setTaskReport(taskReportService
						.findReportWithTarget(target.getId()));
			if (target.getDependentSystems() == null)
				target.setDependentSystems(systemService
						.findDependentSystems(target.getId()));
			if (target.getDependentFunctions() == null)
				target.setDependentFunctions(systemFunctionService
						.findFunctionByTarget(target.getId()));
			if (target.getDependentObj() == null)
				target.setDependentObj(UnitWrap.convert(missionUnitService
						.findDependentUnit(target.getId())));
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void initTask(TaskWrap task) throws Exception {
		if (task.getTaskObject() == null) {
			if (task.getType() == Type.MISSION) {
				MissionWrap mission = new MissionWrap(missionService.get(task
						.getData()));
				initMissionTarget(mission);
				task.setTaskObject(mission);
			}
			if (task.getType() == Type.MISSION_PARTITION) {
				PartitionWrap partition = new PartitionWrap(
						missionPartitionService.get(task.getData()));
				initMissionTarget(partition);
				task.setTaskObject(partition);
			}
			if (task.getType() == Type.MISSION_UNIT) {
				UnitWrap unit = new UnitWrap(missionUnitService.get(task
						.getData()));
				initMissionTarget(unit);
				task.setTaskObject(unit);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static String convertTime(Long second) {
		return new Date(second).toGMTString();
	}

	public static String printFormatString(Printable obj) {
		if (obj == null)
			return null;
		return obj.toFormatString();
	}

	public static String abstraction(String text, int length) {
		if (text.length() <= length)
			return text;
		else {
			return text.substring(0, length) + "....";
		}
	}

	public boolean userExist(String userName) throws Exception
	{
		User user = userService.get(userName);
		if(user == null)
			return false;
		else
			return true;
	}

	public boolean systemExist(String systemName) throws Exception
	{
		system.po.System system = systemService.get(systemName);
		if(system == null)
			return false;
		else
			return true;
	}
	public static String progressStr(int num) {
		if (num >= 0 && num <= 99)
			return String.valueOf(num);
		if (num == -1)
			return "Not delivered";
		if (num == -2)
			return "Waiting";
		if (num == -3)
			return "Rejected";
		if (num == -4)
			return "Submitted";
		if (num == 100)
			return "Completed";
		if (num == -6)
			return "Submission rejected";

		return "Unkown status";
	}

	public static String padRight(String s, int n, String str) {
		while (s.length() < n)
			s += " ";
		s += str;
		s += "\n";
		return s;
	}

	@Interceptors(TransactionInterceptor.class)
	public void processDownload(Downloadable obj) throws Exception {
		if (!obj.getFileId().equals("none")) {
			UploadFile file = uploadFileService.get(obj.getFileId());
			InputStream stream = new FileInputStream(file.getFilePath()
					+ file.getFileName());
			downloadFile = new DefaultStreamedContent(stream, "test",
					file.getFileName());
		}
	}

	public StreamedContent getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(StreamedContent downloadFile) {
		this.downloadFile = downloadFile;
	}

	@Override
	protected void finalize() throws Throwable {
		System.out.println("tool box finalize");
	}

}
