package system.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.interceptor.ApplicationInterceptor;
import system.interceptor.TimerInterceptor;
import system.manager.spec.SessionManager;
import system.po.Application;
import system.po.Application.StatueType;
import system.po.Application.Type;
import system.po.ApplicationResult;
import system.po.ApplicationStage;
import system.po.BugReport;
import system.po.MainAuthority;
import system.po.Mission;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.SysAdmin;
import system.po.System.Status;
import system.po.SystemFunction;
import system.po.Task;
import system.po.TemporaryAuthority;
import system.po.UploadFile;
import system.po.User;
import system.vo.ApplicationWrap;
import system.vo.UnitWrap;

@Interceptors(TimerInterceptor.class)
public class ApplicationServiceImpl extends
		AbstractDataAccessService<Application> implements Serializable {

	@Inject
	private SessionManager sessionManager;
	@Inject
	private AbstractDataAccessService<SysAdmin> sysAdminDataAccessService;
	@Inject
	private UserServiceImpl userService;
	@Inject
	private NotificationServiceImpl notificationService;
	@Inject
	private MissionServiceImpl missionService;
	@Inject
	private TaskServiceImpl taskService;
	@Inject
	private MissionPartitionServiceImpl missionPartitionService;
	@Inject
	private MissionUnitServiceImpl missionUnitService;
	@Inject
	private AbstractDataAccessService<MainAuthority> mainAuthorityDataAccessService;
	@Inject
	private AbstractDataAccessService<SystemFunction> systemFunctionDataAccessService;
	@Inject
	private AbstractDataAccessService<TemporaryAuthority> temporaryAuthorityDataAccessService;
	@Inject
	private AbstractDataAccessService<BugReport> bugReportDataAccessService;
	@Inject
	private AbstractDataAccessService<system.po.System> systemDataAccessService;
	@Inject
	private AbstractDataAccessService<ApplicationStage> applicationStageDataAccessService;
	@Inject
	private AbstractDataAccessService<UploadFile> uploadFileDataAccessService;
	@Inject
	private AbstractDataAccessService<ApplicationResult> applicationResultDataAccessService;
	// private static Map<Application, Integer> decisionMap = new
	// HashMap<Application, Integer>();

	private static String[] guestDealer = new String[] { "hr", "auditor",
			"admin" };
	private static String[] newEmployeeDealer = new String[] { "admin" };
	private static String[] taskTransfer = new String[] { "?", "?" };
	private static String[] abandonUnit = new String[] { "?" };
	private static String[] promoteAuthority = new String[] { "auditor" };
	private static String[] promoteMainAuthority = new String[] { "hr" };
	private static String[] checkError = new String[] { "?" };
	private static String[] verifyMission = new String[] { "auditor" };
	private static String[] rejectTask = new String[] { "?" };
	private static String[] changeSchedule = new String[] { "admin" };
	private static String[] shutdownSystem = new String[] { "admin" };
	private static String[] askForLeave = new String[] { "hr" };

	public ApplicationServiceImpl() {
		super(Application.class);
	}

	public List<Application> findApplicationByClass(String clazz)
			throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("class", clazz);
		return this.findAllByCondition(sqlWhereMap);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see system.service.ApplicationService#getUndealedApplications()
	 */

	public List<Application> getUndealedApplications() throws Exception {
		List<Application> result = new ArrayList<Application>();
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("statue", 0);
		sqlWhereMap.put("reciever_name", sessionManager.getLoginUser()
				.getName());
		result = this.findAllByCondition(sqlWhereMap);

		sqlWhereMap.clear();
		sqlWhereMap.put("user_name", sessionManager.getLoginUser().getName());
		List<MainAuthority> currentAuthority = mainAuthorityDataAccessService
				.findAllByCondition(sqlWhereMap);
		for (MainAuthority m : currentAuthority) {
			sqlWhereMap.clear();
			sqlWhereMap.put("statue", 0);
			sqlWhereMap.put("reciever_role", m.getAuthorityName());
			List<Application> groupApplication = this
					.findAllByCondition(sqlWhereMap);
			result.addAll(groupApplication);
		}
		return result;
	}

	@Interceptors(ApplicationInterceptor.class)
	public Application sendCreateGuestApplication(String guestName,
			String guestDescription, String businessDescription,
			List<SystemFunction> dependentFunctions, Date expireDate)
			throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Guest name: ");
		body.append(guestName);
		body.append("\nGuest description: ");
		body.append(guestDescription);
		body.append("\nBusiness description: ");
		body.append(businessDescription);
		body.append("\nExpire date: ");
		body.append(expireDate);
		body.append("\n\nDependent systems: \n");

		body.append("\nDependent functions: \n");
		for (SystemFunction f : dependentFunctions) {
			body.append(f.getSystemName());
			body.append("-");
			body.append(f.getFunctionName());
			body.append("\n");
		}
		StringBuilder data = new StringBuilder();
		data.append(guestName);
		data.append(";");
		data.append(guestDescription);
		data.append(";");
		data.append(businessDescription);
		data.append(";");
		for (SystemFunction f : dependentFunctions) {
			data.append(f.getId());
			data.append(":");
		}
		if (dependentFunctions == null || dependentFunctions.size() == 0)
			data.append("none:");
		data.deleteCharAt(data.length() - 1);
		data.append(";");
		data.append(expireDate);

		Application application = new Application(sessionManager.getLoginUser()
				.getName(), "none", Type.CREATE_GUEST,
				"Apply for create guest account", body.toString(),
				data.toString(), System.currentTimeMillis(), "none", 0);
		application.setRecieverRole(guestDealer[0]);
		application.setClazz(application.getId());
		ApplicationStage firstStage = new ApplicationStage(
				"Application delivered", 0, application.getClazz(),
				sessionManager.getLoginUser().getName(), businessDescription,
				"none");
		firstStage.setStatus(ApplicationStage.Status.PASS);
		this.save(application);
		applicationStageDataAccessService.save(firstStage);

		return application;

	}

	public Application sendNewEmployeeSettingApplication(List<User> newUsers,
			String comment, String fileId) throws Exception {
		// String realPath = FacesContext.getCurrentInstance()
		// .getExternalContext().getRealPath("test.txt");
		// BufferedWriter writer = new BufferedWriter(new FileWriter(realPath));
		// for (User user : newUsers) {
		//
		// }

		String realPath = "c:/upload/";
		String fileName = "newEmployeeTmp"
				+ UUID.randomUUID().toString().substring(0, 7);

		FileOutputStream outStream = new FileOutputStream(realPath + fileName);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				outStream);
		UploadFile newFile = new UploadFile(sessionManager.getLoginUser()
				.getName(), realPath, fileName);
		uploadFileDataAccessService.save(newFile);
		objectOutputStream.writeObject(newUsers);
		outStream.close();

		StringBuilder body = new StringBuilder();
		body.append("\nComment: \n");
		body.append(comment);
		Application application = new Application(sessionManager.getLoginUser()
				.getName(), "none", Type.NEW_EMPLOYEE,
				"New incoming employee setting application", body.toString(),
				newFile.getId(), System.currentTimeMillis(), fileId, 0);
		application.setRecieverRole(newEmployeeDealer[0]);
		application.setClazz(application.getId());
		ApplicationStage firstStage = new ApplicationStage(
				"Application delivered", 0, application.getClazz(),
				sessionManager.getLoginUser().getName(), comment, fileId);
		applicationStageDataAccessService.save(firstStage);
		this.save(application);
		return application;
	}

	@Interceptors(ApplicationInterceptor.class)
	public Application sendTaskTransferApplication(String reciever, Task task,
			String report, String fileid) throws Exception {
		String applicationClazz = "transfer_"
				+ UUID.randomUUID().toString().substring(0, 7);
		StringBuilder body = new StringBuilder();
		body.append("Apply to transfer the task: \n");
		body.append(task.toFormatString());
		body.append("Comment: \n");
		body.append(report);
		taskTransfer[0] = reciever;
		taskTransfer[1] = task.getCreatorName();

		Application application = new Application(task.getOwnerName(),
				taskTransfer[0], Type.TRANSFER_TASK_TARGET,
				"Transfer task application", body.toString(), task.getId(),
				System.currentTimeMillis(), fileid, 0);
		application.setClazz(application.getId());
		// Application applicationBoss = new Application(task.getOwnerName(),
		// task.getCreatorName(), Type.TRANSFER_TASK_BOSS,
		// "Transfer task application", body.toString(), task.getId(),
		// System.currentTimeMillis(), fileid, 0);
		// applicationBoss.setClazz(applicationClazz);

		ApplicationStage firstStage = new ApplicationStage(
				"Application delivered", 0, application.getClazz(),
				sessionManager.getLoginUser().getName(), report, fileid);
		applicationStageDataAccessService.save(firstStage);
		this.save(application);

		return application;
	}

	@Interceptors(ApplicationInterceptor.class)
	public Application sendAskForLeaveApplication(Date start, Date end,
			String comment) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Ask for leave: \n");
		body.append("Start from: ");
		body.append(start.toGMTString());
		body.append("\nEnd with: ");
		body.append(end.toGMTString());
		body.append("\nReason: \n");
		body.append(comment);

		Application application = new Application(sessionManager.getLoginUser()
				.getName(), "none", Type.ASK_FOR_LEAVE,
				"Ask for leave application", body.toString(), "none",
				System.currentTimeMillis(), "none", 0);
		application.setRecieverRole(askForLeave[0]);
		application.setClazz(application.getId());
		ApplicationStage newStage = new ApplicationStage(
				"Application delivered", 0, application.getClazz(),
				sessionManager.getLoginUser().getName(), comment, "none");
		applicationStageDataAccessService.save(newStage);

		this.save(application);
		return application;
	}

	@Interceptors(ApplicationInterceptor.class)
	public Application sendAbandonUnitTaskApplication(UnitWrap task,
			String report, String fileid) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Apply to abandon the unit task: \n");
		body.append(task.getTask().toFormatString());
		body.append(task.getUnit().toFormatString());
		body.append("\nComment: \n");
		body.append(report);
		abandonUnit[0] = task.getTask().getCreatorName();
		Application application = new Application(
				task.getTask().getOwnerName(), abandonUnit[0],
				Type.ABANDON_TASK, "Abandon task application", report, task
						.getTask().getId(), System.currentTimeMillis(), fileid,
				0);
		application.setClazz(application.getId());
		ApplicationStage firstStage = new ApplicationStage(
				"Application delivered", 0, application.getClazz(),
				sessionManager.getLoginUser().getName(), report, "none");
		applicationStageDataAccessService.save(firstStage);
		this.save(application);
		return application;
	}

	@Interceptors(ApplicationInterceptor.class)
	public Application sendPromoteAuthorityApplication(
			List<SystemFunction> functions, String userName, String reason,
			String fileid) throws Exception {
		// List<SysAdmin> sysAdminRelations =
		// sysAdminService.findAdminRelationBySystem(systemName)
		List<User> auditors = userService.findUserByRole("auditor");
		List<Application> applications = new ArrayList<Application>();

		String data = "";
		for (SystemFunction function : functions) {
			data += function.getId();
			data += ";";
		}
		// for (User auditor : auditors) {
		// applications.add(new Application(userName, auditor.getName(),
		// Type.PROMOTE_AUTHORITY, "authority promotion", reason,
		// data, System.currentTimeMillis(), fileid, 0));
		// }
		// promoteAuthority[0] = auditors.get(0).getName();
		// promoteAuthority[0] = "auditor";

		Application application = new Application(userName, "none",
				Type.PROMOTE_AUTHORITY, "authority promotion", reason, data,
				System.currentTimeMillis(), fileid, 0);
		application.setClazz(application.getId());
		application.setRecieverRole(promoteAuthority[0]);
		ApplicationStage firstStage = new ApplicationStage(
				"Application delivered", 0, application.getClazz(),
				sessionManager.getLoginUser().getName(), reason, "none");
		applicationStageDataAccessService.save(firstStage);
		this.save(application);
		return application;
	}

	@Interceptors(ApplicationInterceptor.class)
	public Application sendPromoteMainAuthorityApplication(
			String authorityName, String comment) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Apply for authority group promotion: \n");
		body.append("\nAuthority group: " + authorityName);
		body.append("\nReason: " + comment);
		Application application = new Application(sessionManager.getLoginUser()
				.getName(), "none", Type.PROMOTE_MAIN_AUTHORITY,
				"Main authority group promotion", body.toString(), authorityName,
				System.currentTimeMillis(), "none", 0);
		application.setClazz(application.getId());
		application.setRecieverRole(promoteAuthority[0]);
		ApplicationStage firstStage = new ApplicationStage(
				"Application delivered", 0, application.getClazz(),
				sessionManager.getLoginUser().getName(), comment, "none");
		applicationStageDataAccessService.save(firstStage);
		this.save(application);
		return application;
	}

	@Interceptors(ApplicationInterceptor.class)
	public Application sendCheckErrorApplication(BugReport bugReport)
			throws Exception {
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("system_name", bugReport.getSystemName());
		List<SysAdmin> sysAdminList = sysAdminDataAccessService
				.findAllByCondition(sqlMap);
		// String data = systemName + ":" + senderName;
		String admin = "admin";
		Application application = null;
		if (sysAdminList.size() > 0) {
			checkError[0] = sysAdminList.get(0).getUserName();
			application = new Application(sessionManager.getLoginUser()
					.getName(), checkError[0], Type.REPORT_ERROR,
					"Report error", bugReport.getReport(), bugReport.getId(),
					System.currentTimeMillis(), bugReport.getFileId(), 0);
			application.setClazz(application.getId());
		} else {
			checkError[0] = "admin";
			application = new Application(sessionManager.getLoginUser()
					.getName(), "none", Type.REPORT_ERROR, "Report error",
					bugReport.getReport(), bugReport.getId(),
					System.currentTimeMillis(), bugReport.getFileId(), 0);
			application.setClazz(application.getId());
			application.setRecieverRole(checkError[0]);
		}

		ApplicationStage firstStage = new ApplicationStage(
				"Application delivered", 0, application.getClazz(),
				sessionManager.getLoginUser().getName(), bugReport.getReport(),
				"none");
		applicationStageDataAccessService.save(firstStage);
		this.save(application);
		return application;
	}

	@Interceptors(ApplicationInterceptor.class)
	public Application sendVerifyMissionApplication(Mission mission,
			List<SystemFunction> dependentFunctions) throws Exception {
		String body = "Asking for verify mission: \n\n"
				+ mission.toFormatString();
		body += "\nDependent functions: \n";
		if (dependentFunctions.size() == 0)
			body += "none";
		for (SystemFunction sf : dependentFunctions) {
			body += sf.getSystemName();
			body += " - ";
			body += sf.getFunctionName();
			body += "\n";
		}

		// Map<String, Object> sqlMap = new HashMap<String, Object>();
		// sqlMap.put("role_name", "auditor");
		// List<User> auditorList = userService.findAllByCondition(sqlMap);
		// List<Application> applications = new ArrayList<Application>();
		// for (User u : auditorList) {
		// applications.add(new Application(sessionManager.getLoginUser()
		// .getName(), u.getName(), Type.VERIFY_MISSION,
		// "verify mission", body, mission.getId(), System
		// .currentTimeMillis(), "none", 0));
		// }
		// this.save(applications);
		// verifyMission[0] = auditorList.get(0).getName();
		Application application = new Application(sessionManager.getLoginUser()
				.getName(), "none", Type.VERIFY_MISSION,
				"Verify mission application", body, mission.getId(),
				System.currentTimeMillis(), "none", 0);
		application.setClazz(application.getId());
		application.setRecieverRole(verifyMission[0]);
		ApplicationStage firstStage = new ApplicationStage(
				"Application delivered", 0, application.getClazz(),
				sessionManager.getLoginUser().getName(), "none", "none");
		applicationStageDataAccessService.save(firstStage);
		this.save(application);
		return application;

	}

	@Interceptors(ApplicationInterceptor.class)
	public Application sendRejectTaskApplication(Task task, String reason)
			throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(task.toFormatString());
		body.append("\nreason: " + reason);
		rejectTask[0] = task.getCreatorName();
		Application application = new Application(sessionManager.getLoginUser()
				.getName(), rejectTask[0], Type.REJECT_TASK, "reject task",
				body.toString(), task.getId() + ";" + reason,
				System.currentTimeMillis(), "none", 0);
		application.setClazz(application.getId());
		ApplicationStage firstStage = new ApplicationStage(
				"Application delivered", 0, application.getClazz(),
				sessionManager.getLoginUser().getName(), reason, "none");
		applicationStageDataAccessService.save(firstStage);
		this.save(application);
		return application;
	}

	@Interceptors(ApplicationInterceptor.class)
	public Application sendChangeScheduleApplication(system.po.System system,
			String duration, String comment) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(system.toFormatString());
		body.append("\nDuration: ");
		body.append(duration);
		body.append("\nComment: ");
		body.append(comment);
		Application application = new Application(sessionManager.getLoginUser()
				.getName(), "none", Type.CHANGE_SCHEDULE, "Change schedule",
				body.toString(), system.getName() + ";" + comment + ";"
						+ duration, System.currentTimeMillis(), "none", 0);
		application.setClazz(application.getId());
		application.setRecieverRole(changeSchedule[0]);
		ApplicationStage firstStage = new ApplicationStage(
				"Application delivered", 0, application.getClazz(),
				sessionManager.getLoginUser().getName(), comment, "none");
		applicationStageDataAccessService.save(firstStage);
		this.save(application);
		return application;
	}

	@Interceptors(ApplicationInterceptor.class)
	public Application sendShutdownSystemApplication(system.po.System system,
			String duration, String comment) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(system.toFormatString());
		body.append("\nDuration: ");
		body.append(duration);
		body.append("\nComment: ");
		body.append(comment);
		Application application = new Application(sessionManager.getLoginUser()
				.getName(), "none", Type.SHUTDOWN_SYSTEM, "Shutdown system",
				body.toString(), system.getName() + ";" + comment + ";"
						+ duration, System.currentTimeMillis(), "none", 0);
		application.setClazz(application.getId());
		application.setRecieverRole(shutdownSystem[0]);
		ApplicationStage firstStage = new ApplicationStage(
				"Application delivered", 0, application.getClazz(),
				sessionManager.getLoginUser().getName(), comment, "none");
		applicationStageDataAccessService.save(firstStage);
		this.save(application);
		return application;
	}

	public boolean acceptApplication(Application application, Object[] args)
			throws Exception {
		if (application.getType() == Type.PROMOTE_AUTHORITY)
			return acceptPromoteAuthority(application, args);
		if (application.getType() == Type.VERIFY_MISSION)
			return acceptVerifyMission(application, args);
		if (application.getType() == Type.REJECT_TASK)
			return acceptRejectTask(application, args);
		if (application.getType() == Type.REPORT_ERROR)
			return acceptFixErrorApplication(application, args);
		if (application.getType() == Type.ABANDON_TASK)
			return acceptAbandonUnitTaskApplication(application, args);
		if (application.getType() == Type.TRANSFER_TASK_BOSS
				|| application.getType() == Type.TRANSFER_TASK_TARGET)
			return acceptTaskTransferApplication(application, args);
		if (application.getType() == Type.CHANGE_SCHEDULE)
			return acceptChangeScheduleApplication(application, args);
		if (application.getType() == Type.SHUTDOWN_SYSTEM)
			return acceptShutdownSystemApplication(application, args);
		if (application.getType() == Type.CREATE_GUEST)
			return acceptCreateGuestApplication(application, args);
		if (application.getType() == Type.NEW_EMPLOYEE)
			return acceptNewEmployeeApplication(application, args);
		if (application.getType() == Type.ASK_FOR_LEAVE)
			return acceptAskForLeaveApplication(application, args);
		if (application.getType() == Type.PROMOTE_MAIN_AUTHORITY)
			return acceptPromoteMainAuthorityApplication(application, args);
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.ApplicationService#rejectApplication(system.po.Application
	 * , java.lang.Object[])
	 */

	public boolean rejectApplication(Application application, Object[] args)
			throws Exception {
		if (application.getType() == Type.PROMOTE_AUTHORITY)
			return rejectPromoteAuthority(application, args);
		if (application.getType() == Type.VERIFY_MISSION)
			return rejectVerifyMission(application, args);
		if (application.getType() == Type.REJECT_TASK)
			return rejectRejectTask(application, args);
		if (application.getType() == Type.REPORT_ERROR)
			return rejectFixErrorApplication(application, args);
		if (application.getType() == Type.ABANDON_TASK)
			return rejectAbandonUnitTaskApplication(application, args);
		if (application.getType() == Type.TRANSFER_TASK_BOSS
				|| application.getType() == Type.TRANSFER_TASK_TARGET)
			return rejectTaskTransferApplication(application, args);
		if (application.getType() == Type.CHANGE_SCHEDULE)
			return rejectChangeScheduleApplication(application, args);
		if (application.getType() == Type.SHUTDOWN_SYSTEM)
			return rejectShutdownSystemApplication(application, args);
		if (application.getType() == Type.CREATE_GUEST)
			return rejectCreateGuestApplication(application, args);
		if (application.getType() == Type.ASK_FOR_LEAVE)
			return rejectAskForLeaveApplication(application, args);
		if (application.getType() == Type.PROMOTE_MAIN_AUTHORITY)
			return rejectPromoteMainAuthorityApplication(application, args);
		return false;
	}

	private boolean acceptRejectTask(Application application, Object[] args)
			throws Exception {
		try {
			String data = application.getData();
			String taskId = data.split("\\;")[0];
			String reason = data.split("\\;")[1];
			Task task = taskService.get(taskId);
			if (application.getStage() == rejectTask.length - 1) {
				if (task.getType() == Task.Type.MISSION) {
					Mission mission = missionService.get(task.getData());
					missionService.rejectMission(mission, reason);
					task.setStatue(Task.StatueType.REJECTED);
					taskService.update(task);
					notificationService.sendRejectMissionNotification(mission,
							reason, (String) args[1]);
				}
				if (task.getType() == Task.Type.MISSION_PARTITION) {
					MissionPartition missionPartition = missionPartitionService
							.get(task.getData());
					missionPartitionService.rejectPartition(missionPartition,
							reason);
					task.setStatue(Task.StatueType.REJECTED);
					taskService.update(task);
					notificationService.sendRejectPartitionNotification(
							missionPartition, reason, (String) args[1]);
				}
				if (task.getType() == Task.Type.MISSION_UNIT) {
					MissionUnit missionUnit = missionUnitService.get(task
							.getData());
					missionUnitService.rejectUnit(missionUnit, reason);

					// Task task =
					// taskService.findTaskByDataid(missionUnit.getId());
					task.setStatue(Task.StatueType.REJECTED);
					taskService.update(task);

					notificationService.sendRejectUnitNotification(missionUnit,
							reason, (String) args[1]);
				}

				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ sessionManager.getLoginUser().getName(),
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				applicationStageDataAccessService.save(newStage);

				String resultBody = "Comment: \n" + (String) args[0];
				ApplicationResult result = new ApplicationResult(
						application.getClazz(), ApplicationResult.Type.NORMAL,
						"[Success] Task rejection application passed.",
						resultBody, System.currentTimeMillis(),
						(String) args[1], "none", "none", "none");

				applicationResultDataAccessService.save(result);
				notificationService
						.sendApplicationFinishedNotification(application);
			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean rejectRejectTask(Application application, Object[] args) {
		try {
			String data = application.getData();
			// String reason = (String) args[0];
			String taskId = data.split("\\;")[0];
			Task task = taskService.get(taskId);
			taskService.retoreTask(task);
			// notificationService.sendRejectRejectTaskNotification(task,
			// reason);

			ApplicationStage newStage = new ApplicationStage(
					"Application rejected by "
							+ sessionManager.getLoginUser().getName(),
					application.getStage() + 1, application.getClazz(),
					sessionManager.getLoginUser().getName(), (String) args[0],
					(String) args[1]);
			newStage.setStatus(ApplicationStage.Status.FAIL);
			applicationStageDataAccessService.save(newStage);

			String resultBody = "Comment: \n" + (String) args[0];
			ApplicationResult result = new ApplicationResult(
					application.getClazz(), ApplicationResult.Type.NORMAL,
					"[Fail] Task rejection application fail.", resultBody,
					System.currentTimeMillis(), (String) args[1], "none",
					"none", "none");

			applicationResultDataAccessService.save(result);
			notificationService
					.sendApplicationFinishedNotification(application);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean acceptPromoteAuthority(Application application,
			Object[] args) {
		try {
			if (application.getStage() == promoteAuthority.length - 1) {
				String data = application.getData();
				// String[] systemFunctionidList = data.split("\\;");
				// List<SystemFunction> functions =
				// systemFunctionDataAccessService
				// .get(systemFunctionidList);
				Long expireTime = ((Date) args[4]).getTime();
				List<SystemFunction> approvedFunctions = (List<SystemFunction>) args[2];
				List<SystemFunction> rejectedFunctions = (List<SystemFunction>) args[3];
				String arg1 = "";
				String arg2 = "";
				for (SystemFunction f : approvedFunctions) {
					arg1 += f.getId();
					arg1 += ";";
				}
				for (SystemFunction f : rejectedFunctions) {
					arg2 += f.getId();
					arg2 += ";";
				}
				List<TemporaryAuthority> temporaryAuthorityList = new ArrayList<TemporaryAuthority>();

				for (SystemFunction function : approvedFunctions) {
					temporaryAuthorityList.add(new TemporaryAuthority(
							application.getSenderName(), function.getId(),
							"none", expireTime));
				}
				temporaryAuthorityDataAccessService
						.save(temporaryAuthorityList);
				// notificationService.sendAuthorityPromotedNotification(
				// application.getSenderName(), temporaryAuthorityList);
				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ sessionManager.getLoginUser().getName(),
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				applicationStageDataAccessService.save(newStage);

				String resultBody = "Comment: \n" + (String) args[0];
				ApplicationResult result = new ApplicationResult(
						application.getClazz(), ApplicationResult.Type.NORMAL,
						"[Success] Promote authority application passed.",
						resultBody, System.currentTimeMillis(),
						(String) args[1], arg1, arg2, "none");

				applicationResultDataAccessService.save(result);
				notificationService
						.sendApplicationFinishedNotification(application);
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean rejectPromoteAuthority(Application application,
			Object[] args) {
		try {
			String data = application.getData();
			BugReport bugReport = bugReportDataAccessService.get(data);
			notificationService.sendRejectFixBugNotification(bugReport,
					(String) args[0]);

			ApplicationStage newStage = new ApplicationStage(
					"Application rejected by "
							+ sessionManager.getLoginUser().getName(),
					application.getStage() + 1, application.getClazz(),
					sessionManager.getLoginUser().getName(), (String) args[0],
					(String) args[1]);
			newStage.setStatus(ApplicationStage.Status.FAIL);
			applicationStageDataAccessService.save(newStage);

			String resultBody = "Comment: \n" + (String) args[0];
			ApplicationResult result = new ApplicationResult(
					application.getClazz(), ApplicationResult.Type.NORMAL,
					"[Fail] Fix bug application rejected.", resultBody,
					System.currentTimeMillis(), (String) args[1], "none",
					"none", "none");

			applicationResultDataAccessService.save(result);
			notificationService
					.sendApplicationFinishedNotification(application);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean acceptVerifyMission(Application application, Object[] args) {
		try {
			if (application.getStage() == verifyMission.length - 1) {

				Mission mission = missionService.get(application.getData());
				missionService.startMission(mission);

				Task newTask = taskService.startNewMissionTask(mission, 0,
						"none", true);
				notificationService.sendStartMissionNotification(
						mission.getLeaderName(), mission, "none",
						newTask.getId());
				// notificationService.sendMissionAcceptedNotification(mission);
				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ sessionManager.getLoginUser().getName(),
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				applicationStageDataAccessService.save(newStage);

				String resultBody = "Comment: \n" + (String) args[0];
				ApplicationResult result = new ApplicationResult(
						application.getClazz(), ApplicationResult.Type.NORMAL,
						"[Success] Mission accepted.", resultBody,
						System.currentTimeMillis(), (String) args[1], "none",
						"none", "none");

				applicationResultDataAccessService.save(result);
				notificationService
						.sendApplicationFinishedNotification(application);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean rejectVerifyMission(Application application, Object[] args) {
		try {
			String missionid = (String) args[2];
			Mission mission = missionService.get(missionid);
			missionService.rejectMission(mission, (String) args[1]);
			// notificationService.sendRejectMissionNotification(mission,
			// (String) args[0], (String) args[1]);

			ApplicationStage newStage = new ApplicationStage(
					"Application rejected by "
							+ sessionManager.getLoginUser().getName(),
					application.getStage() + 1, application.getClazz(),
					sessionManager.getLoginUser().getName(), (String) args[0],
					(String) args[1]);
			newStage.setStatus(ApplicationStage.Status.FAIL);
			applicationStageDataAccessService.save(newStage);

			String resultBody = "Comment: \n" + (String) args[0];
			ApplicationResult result = new ApplicationResult(
					application.getClazz(), ApplicationResult.Type.NORMAL,
					"[Fail] Mission rejected.", resultBody,
					System.currentTimeMillis(), (String) args[1], "none",
					"none", "none");

			applicationResultDataAccessService.save(result);
			notificationService
					.sendApplicationFinishedNotification(application);
			// TODO duplicate rejection?
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	private boolean acceptFixErrorApplication(Application application,
			Object[] args) {
		try {
			if (application.getStage() == checkError.length - 1) {
				String data = application.getData();
				BugReport bugReport = bugReportDataAccessService.get(data);
				notificationService.sendStartFixBugNotification(bugReport);
				taskService.startNewFixBugTask(bugReport, 0);

				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ sessionManager.getLoginUser().getName(),
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				applicationStageDataAccessService.save(newStage);

				String resultBody = "Comment: \n" + (String) args[0];
				ApplicationResult result = new ApplicationResult(
						application.getClazz(), ApplicationResult.Type.NORMAL,
						"[Success] Fix bug application passed.", resultBody,
						System.currentTimeMillis(), (String) args[1], "none",
						"none", "none");

				applicationResultDataAccessService.save(result);
				notificationService
						.sendApplicationFinishedNotification(application);
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean rejectFixErrorApplication(Application application,
			Object[] args) {
		try {
			String data = application.getData();
			BugReport bugReport = bugReportDataAccessService.get(data);
			// notificationService.sendRejectFixBugNotification(bugReport,
			// (String) args[0]);

			ApplicationStage newStage = new ApplicationStage(
					"Application rejected by "
							+ sessionManager.getLoginUser().getName(),
					application.getStage() + 1, application.getClazz(),
					sessionManager.getLoginUser().getName(), (String) args[0],
					(String) args[1]);
			newStage.setStatus(ApplicationStage.Status.FAIL);
			applicationStageDataAccessService.save(newStage);

			String resultBody = "Comment: \n" + (String) args[0];
			ApplicationResult result = new ApplicationResult(
					application.getClazz(), ApplicationResult.Type.NORMAL,
					"[Fail] Fix bug application rejected.", resultBody,
					System.currentTimeMillis(), (String) args[1], "none",
					"none", "none");

			applicationResultDataAccessService.save(result);
			notificationService
					.sendApplicationFinishedNotification(application);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean acceptAbandonUnitTaskApplication(Application application,
			Object[] args) {
		try {
			if (application.getStage() == abandonUnit.length - 1) {
				String data = application.getData();
				Task task = taskService.get(data);
				taskService.abandonTask(task);
				// notificationService.sendAbandonTaskAcceptedNotification(task);

				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ sessionManager.getLoginUser().getName(),
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				applicationStageDataAccessService.save(newStage);

				String resultBody = "Comment: \n" + (String) args[0];
				ApplicationResult result = new ApplicationResult(
						application.getClazz(), ApplicationResult.Type.NORMAL,
						"[Success] Abandon unit task application passed.",
						resultBody, System.currentTimeMillis(),
						(String) args[1], "none", "none", "none");

				applicationResultDataAccessService.save(result);
				notificationService
						.sendApplicationFinishedNotification(application);
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean rejectAbandonUnitTaskApplication(Application application,
			Object[] args) {
		try {
			String data = application.getData();
			Task task = taskService.get(data);
			taskService.retoreToUncomplete(task);
			// notificationService.sendRejectAbandonTaskNotification(task,
			// (String) args[0]);

			ApplicationStage newStage = new ApplicationStage(
					"Application rejected by "
							+ sessionManager.getLoginUser().getName(),
					application.getStage() + 1, application.getClazz(),
					sessionManager.getLoginUser().getName(), (String) args[0],
					(String) args[1]);
			newStage.setStatus(ApplicationStage.Status.FAIL);
			applicationStageDataAccessService.save(newStage);

			String resultBody = "Comment: \n" + (String) args[0];
			ApplicationResult result = new ApplicationResult(
					application.getClazz(), ApplicationResult.Type.NORMAL,
					"[Fail] Abandon unit task application rejected.",
					resultBody, System.currentTimeMillis(), (String) args[1],
					"none", "none", "none");

			applicationResultDataAccessService.save(result);
			notificationService
					.sendApplicationFinishedNotification(application);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean acceptTaskTransferApplication(Application application,
			Object[] args) {
		try {

			if (application.getStage() == taskTransfer.length - 1) {
				String data = application.getData();
				Task task = taskService.get(data);
				// String target = null;

				// notificationService.sendTaskTransferAcceptedNotification(task,
				// task.getOwnerName(), target);
				taskService.transferTask(task,
						taskTransfer[application.getStage() - 1]);
				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ sessionManager.getLoginUser().getName(),
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				applicationStageDataAccessService.save(newStage);

				String resultBody = "Comment: \n" + (String) args[0];
				ApplicationResult result = new ApplicationResult(
						application.getClazz(), ApplicationResult.Type.NORMAL,
						"[Success] Task transfer application passed.",
						resultBody, System.currentTimeMillis(),
						(String) args[1], "none", "none", "none");

				applicationResultDataAccessService.save(result);
				notificationService
						.sendApplicationFinishedNotification(application);
			} else {
				Application newApplication = new Application(
						application.getSenderName(),
						taskTransfer[application.getStage() + 1],
						application.getType(), application.getTitle(),
						application.getBody(), application.getData(),
						System.currentTimeMillis(), application.getFileId(),
						application.getStage() + 1);
				newApplication.setClazz(application.getClazz());
				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ taskTransfer[application.getStage()],
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				this.save(newApplication);
				applicationStageDataAccessService.save(newStage);
				notificationService.sendApplicationProceedNotification(
						newApplication, newStage);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean rejectTaskTransferApplication(Application application,
			Object[] args) {
		try {
			String data = application.getData();
			Task task = taskService.get(data);
			taskService.retoreToUncomplete(task);
			// notificationService.sendRejectTaskTransferNotification(task);

			ApplicationStage newStage = new ApplicationStage(
					"Application rejected by "
							+ sessionManager.getLoginUser().getName(),
					application.getStage() + 1, application.getClazz(),
					sessionManager.getLoginUser().getName(), (String) args[0],
					(String) args[1]);
			newStage.setStatus(ApplicationStage.Status.FAIL);
			applicationStageDataAccessService.save(newStage);

			String resultBody = "Comment: \n" + (String) args[0];
			ApplicationResult result = new ApplicationResult(
					application.getClazz(), ApplicationResult.Type.NORMAL,
					"[Fail] Task transfer application rejected.", resultBody,
					System.currentTimeMillis(), (String) args[1], "none",
					"none", "none");

			applicationResultDataAccessService.save(result);
			notificationService
					.sendApplicationFinishedNotification(application);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean acceptChangeScheduleApplication(Application application,
			Object[] args) {
		try {
			if (application.getStage() == changeSchedule.length - 1) {
				String[] data = application.getData().split("\\;");
				String systemName = data[0];
				String duration = data[2];
				system.po.System system = systemDataAccessService
						.get(systemName);
				system.setDuration(duration);
				systemDataAccessService.update(system);
				notificationService
						.sendChangeScheduleAcceptedNotification(application);

				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ sessionManager.getLoginUser().getName(),
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				applicationStageDataAccessService.save(newStage);

				String resultBody = "Comment: \n" + (String) args[0];
				ApplicationResult result = new ApplicationResult(
						application.getClazz(), ApplicationResult.Type.NORMAL,
						"[Success] Change system schedule application passed.",
						resultBody, System.currentTimeMillis(),
						(String) args[1], "none", "none", "none");

				applicationResultDataAccessService.save(result);
				notificationService
						.sendApplicationFinishedNotification(application);
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean rejectChangeScheduleApplication(Application application,
			Object[] args) {
		try {

			// notificationService.sendRejectChangeScheduleNotification(
			// application.getSenderName(), system, duration,
			// (String) args[1]);

			ApplicationStage newStage = new ApplicationStage(
					"Application rejected by "
							+ sessionManager.getLoginUser().getName(),
					application.getStage() + 1, application.getClazz(),
					sessionManager.getLoginUser().getName(), (String) args[0],
					(String) args[1]);
			newStage.setStatus(ApplicationStage.Status.FAIL);
			applicationStageDataAccessService.save(newStage);

			String resultBody = "Comment: \n" + (String) args[0];
			ApplicationResult result = new ApplicationResult(
					application.getClazz(), ApplicationResult.Type.NORMAL,
					"[Fail] Change system schedule application rejected.",
					resultBody, System.currentTimeMillis(), (String) args[1],
					"none", "none", "none");

			applicationResultDataAccessService.save(result);
			notificationService
					.sendApplicationFinishedNotification(application);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean acceptShutdownSystemApplication(Application application,
			Object[] args) {
		try {
			if (application.getStage() == shutdownSystem.length - 1) {
				String[] data = application.getData().split("\\;");
				String systemName = data[0];
				String comment = data[1];
				String duration = data[2];
				// system.po.System system =
				// systemDataAccessService.get(systemName);
				// system.setDuration(duration);
				// system.setStatus(Status.OFF);
				// systemDataAccessService.update(system);
				// notificationService
				// .sendShutdownSystemAcceptedNotification(application);
				taskService.startNewShutdownSystemTask(application, systemName);

				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ sessionManager.getLoginUser().getName(),
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				applicationStageDataAccessService.save(newStage);

				String resultBody = "Comment: \n" + (String) args[0];
				ApplicationResult result = new ApplicationResult(
						application.getClazz(), ApplicationResult.Type.NORMAL,
						"[Success] Shutdown system application passed.",
						resultBody, System.currentTimeMillis(),
						(String) args[1], "none", "none", "none");

				applicationResultDataAccessService.save(result);
				notificationService
						.sendApplicationFinishedNotification(application);
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.ApplicationService#rejectShutdownSystemApplication(system
	 * .po.Application, java.lang.Object[])
	 */

	public boolean rejectShutdownSystemApplication(Application application,
			Object[] args) {
		try {
			String[] data = application.getData().split("\\;");
			String systemName = data[0];
			String comment = data[1];
			String duration = data[2];
			system.po.System system = systemDataAccessService.get(systemName);
			// system.setDuration(duration);

			// notificationService.sendRejectShutdownSystemNotification(
			// application.getSenderName(), system, duration,
			// (String) args[1]);

			ApplicationStage newStage = new ApplicationStage(
					"Application rejected by "
							+ sessionManager.getLoginUser().getName(),
					application.getStage() + 1, application.getClazz(),
					sessionManager.getLoginUser().getName(), (String) args[0],
					(String) args[1]);
			newStage.setStatus(ApplicationStage.Status.FAIL);
			applicationStageDataAccessService.save(newStage);

			String resultBody = "Comment: \n" + (String) args[0];
			ApplicationResult result = new ApplicationResult(
					application.getClazz(), ApplicationResult.Type.NORMAL,
					"[Fail] Shutdown system application rejected.", resultBody,
					System.currentTimeMillis(), (String) args[1], "none",
					"none", "none");

			applicationResultDataAccessService.save(result);
			notificationService
					.sendApplicationFinishedNotification(application);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean acceptCreateGuestApplication(Application application,
			Object[] args) {
		try {
			String data[] = application.getData().split("\\;");
			String guestName = data[0]
					+ UUID.randomUUID().toString().substring(0, 7);
			String guestDescription = data[1];
			String businessDescription = data[2];
			String[] dependentFunctions = data[3].split("\\:");
			String expireDate = data[4];
			if (application.getStage() == guestDealer.length - 1) {
				long expireDateMiliSec = Date.parse(expireDate);

				User guest = new User(guestName, guestName, "guest",
						expireDateMiliSec, "none", "none", "none", "none");
				MainAuthority guestAuthority = new MainAuthority(
						guest.getName(), "guest");
				userService.save(guest);
				mainAuthorityDataAccessService.save(guestAuthority);

				List<TemporaryAuthority> temporaryAuthorityList = new ArrayList<TemporaryAuthority>();
				for (String str : dependentFunctions) {
					if (str.equals("none"))
						break;
					temporaryAuthorityList.add(new TemporaryAuthority(
							guestName, str, "TODO", expireDateMiliSec));
				}
				temporaryAuthorityDataAccessService
						.save(temporaryAuthorityList);
				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ sessionManager.getLoginUser().getName()
								+ " ,application conducted.",
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				newStage.setStatus(ApplicationStage.Status.PASS);
				applicationStageDataAccessService.save(newStage);
				ApplicationResult result = new ApplicationResult(
						application.getClazz(),
						ApplicationResult.Type.CREATE_GUEST,
						"[Succeed] Guest account created",
						"Here is the guest account detial.",
						System.currentTimeMillis(), "none", guest.getName(),
						"none", "none");
				applicationResultDataAccessService.save(result);
				notificationService
						.sendApplicationFinishedNotification(application);

			} else {
				Application newApplication = new Application(
						application.getSenderName(), "none", Type.CREATE_GUEST,
						"Apply for create guest account",
						application.getBody(), application.getData(),
						System.currentTimeMillis(), "none",
						application.getStage() + 1);
				newApplication.setRecieverRole(guestDealer[application
						.getStage() + 1]);
				newApplication.setClazz(application.getClazz());
				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ guestDealer[application.getStage()] + " : "
								+ sessionManager.getLoginUser().getName(),
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				newStage.setStatus(ApplicationStage.Status.PASS);
				this.save(newApplication);
				applicationStageDataAccessService.save(newStage);
				notificationService.sendApplicationProceedNotification(
						newApplication, newStage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	public boolean rejectCreateGuestApplication(Application application,
			Object[] args) throws Exception {
		try {
			ApplicationStage newStage = new ApplicationStage(
					"Application rejected by "
							+ sessionManager.getLoginUser().getName(),
					application.getStage() + 1, application.getClazz(),
					sessionManager.getLoginUser().getName(), (String) args[0],
					(String) args[1]);
			newStage.setStatus(ApplicationStage.Status.FAIL);
			applicationStageDataAccessService.save(newStage);

			String resultBody = "Comment: \n" + (String) args[0];
			ApplicationResult result = new ApplicationResult(
					application.getClazz(), ApplicationResult.Type.NORMAL,
					"[Fail] Create guest application rejected.", resultBody,
					System.currentTimeMillis(), (String) args[1], "none",
					"none", "none");

			applicationResultDataAccessService.save(result);
			notificationService
					.sendApplicationFinishedNotification(application);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean acceptAskForLeaveApplication(Application application,
			Object[] args) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			if (application.getStage() == askForLeave.length - 1) {
				List<SysAdmin> tempSysAdmins = (List<SysAdmin>) args[2];
				for (SysAdmin s : tempSysAdmins) {
					User user = userService.get(s.getUserName());
					if (user.getName() == application.getSenderName()) {
						context.addMessage(null, new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Temporary admin can not be the applier", null));
						return false;
					}
					if (user.getCondition().equals("leaving")) {
						context.addMessage(null, new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Target temporary admin are leaving", null));
						return false;
					}

				}
				sysAdminDataAccessService.save(tempSysAdmins);
				User admin = userService.get(application.getSenderName());
				admin.setCondition("leaving");
				userService.update(admin);
				String data1 = "";
				for (SysAdmin s : tempSysAdmins) {
					data1 += s.getId();
					data1 += ";";
				}
				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ sessionManager.getLoginUser().getName(),
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				applicationStageDataAccessService.save(newStage);

				String resultBody = "Comment: \n" + (String) args[0];
				ApplicationResult result = new ApplicationResult(
						application.getClazz(), ApplicationResult.Type.NORMAL,
						"[Success] Ask for leave application passed.",
						resultBody, System.currentTimeMillis(),
						(String) args[1], data1, "none", "none");

				applicationResultDataAccessService.save(result);
				notificationService
						.sendApplicationFinishedNotification(application);
			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean rejectAskForLeaveApplication(Application application,
			Object[] args) {
		try {
			ApplicationStage newStage = new ApplicationStage(
					"Application rejected by "
							+ sessionManager.getLoginUser().getName(),
					application.getStage() + 1, application.getClazz(),
					sessionManager.getLoginUser().getName(), (String) args[0],
					(String) args[1]);
			newStage.setStatus(ApplicationStage.Status.FAIL);
			applicationStageDataAccessService.save(newStage);

			String resultBody = "Comment: \n" + (String) args[0];
			ApplicationResult result = new ApplicationResult(
					application.getClazz(), ApplicationResult.Type.NORMAL,
					"[Fail] Ask for leave application rejected.", resultBody,
					System.currentTimeMillis(), (String) args[1], "none",
					"none", "none");

			applicationResultDataAccessService.save(result);
			notificationService
					.sendApplicationFinishedNotification(application);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean acceptNewEmployeeApplication(Application application,
			Object[] args) {
		try {
			if (application.getStage() == newEmployeeDealer.length - 1) {
				List<User> newUsers = (List<User>) args[2];
				List<User> rejectedUsers = (List<User>) args[3];
				userService.save(newUsers);

				String realPath = "c:/upload/";
				String fileName = "completeEmployeeTmp"
						+ UUID.randomUUID().toString().substring(0, 7);

				FileOutputStream outStream = new FileOutputStream(realPath
						+ fileName);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						outStream);
				UploadFile newFile = new UploadFile(sessionManager
						.getLoginUser().getName(), realPath, fileName);
				uploadFileDataAccessService.save(newFile);
				objectOutputStream.writeObject(newUsers);
				objectOutputStream.writeObject(rejectedUsers);
				outStream.close();

				ApplicationStage newStage = new ApplicationStage(
						"Application dealed with "
								+ newEmployeeDealer[application.getStage()]
								+ " ,some or all of the application conducted.",
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);

				applicationStageDataAccessService.save(newStage);

				String resultBody = "Comment: \n" + (String) args[0];
				ApplicationResult result = new ApplicationResult(
						application.getClazz(),
						ApplicationResult.Type.CREATE_GUEST,
						"[Complete] New incoming employee setting completed.",
						resultBody, System.currentTimeMillis(), "none",
						newFile.getId(), (String) args[0], "none");

				applicationResultDataAccessService.save(result);
				notificationService
						.sendApplicationFinishedNotification(application);
				// notificationService.sendNewEmployeeConductedNotification(
				// (String) args[0], (List<User>) args[2],
				// (List<User>) args[3], application.getSenderName());
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean acceptPromoteMainAuthorityApplication(
			Application application, Object[] args) {
		try {
			if (application.getStage() == promoteMainAuthority.length - 1) {
				String authorityName = application.getData();
				MainAuthority newAuthority = new MainAuthority(
						application.getSenderName(), authorityName);
				mainAuthorityDataAccessService.save(newAuthority);
				ApplicationStage newStage = new ApplicationStage(
						"Application accepted by "
								+ sessionManager.getLoginUser().getName(),
						application.getStage() + 1, application.getClazz(),
						sessionManager.getLoginUser().getName(),
						(String) args[0], (String) args[1]);
				applicationStageDataAccessService.save(newStage);

				String resultBody = "Comment: \n" + (String) args[0];
				ApplicationResult result = new ApplicationResult(
						application.getClazz(), ApplicationResult.Type.NORMAL,
						"[Success] Promote authority application passed.",
						resultBody, System.currentTimeMillis(),
						(String) args[1], "none", "none", "none");

				applicationResultDataAccessService.save(result);
				notificationService
						.sendApplicationFinishedNotification(application);
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean rejectPromoteMainAuthorityApplication(Application application, Object[] args)
	{
		try {
			ApplicationStage newStage = new ApplicationStage(
					"Application rejected by "
							+ sessionManager.getLoginUser().getName(),
					application.getStage() + 1, application.getClazz(),
					sessionManager.getLoginUser().getName(), (String) args[0],
					(String) args[1]);
			newStage.setStatus(ApplicationStage.Status.FAIL);
			applicationStageDataAccessService.save(newStage);

			String resultBody = "Comment: \n" + (String) args[0];
			ApplicationResult result = new ApplicationResult(
					application.getClazz(), ApplicationResult.Type.NORMAL,
					"[Fail] Promote main authority group application rejected.", resultBody,
					System.currentTimeMillis(), (String) args[1], "none",
					"none", "none");

			applicationResultDataAccessService.save(result);
			notificationService
					.sendApplicationFinishedNotification(application);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
