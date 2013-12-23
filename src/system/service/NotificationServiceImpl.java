package system.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.interceptor.NotificationInterceptor;
import system.interceptor.TimerInterceptor;
import system.manager.spec.SessionManager;
import system.po.Application;
import system.po.ApplicationStage;
import system.po.BugReport;
import system.po.Mission;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.Notification;
import system.po.TemporaryAuthority;
import system.po.UploadFile;
import system.po.User;

import system.po.Task;
import system.po.spec.Printable;
import system.service.spec.NotificationService;
import system.vo.TemporaryAuthorityWrap;

@Interceptors(TimerInterceptor.class)
public class NotificationServiceImpl extends
		AbstractDataAccessService<Notification> implements Serializable {

	@Inject
	private SessionManager sessionManager;
	@Inject
	private AbstractDataAccessService<Mission> missionDataAccessService;
	@Inject
	private AbstractDataAccessService<MissionPartition> missionPartitionDataAccessService;
	@Inject
	private AbstractDataAccessService<MissionUnit> missionUnitDataAccessService;
	@Inject
	private AbstractDataAccessService<BugReport> bugReportDataAccessService;
	@Inject
	private AbstractDataAccessService<UploadFile> uploadFileDataAccessService;

	public NotificationServiceImpl() {
		super(Notification.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see system.service.NotificationService#getUnreadNotifications()
	 */

	public List<Notification> getUnreadNotifications() throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("statue", 0);
		sqlWhereMap.put("reciever_name", sessionManager.getLoginUser()
				.getName());

		return this.findAllByCondition(sqlWhereMap);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendNotification(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendNotification(String reciever, String title,
			String body, String fileid) throws Exception {
		Notification notification = new Notification(reciever,
				Notification.Type.NORMAL, title, body,
				System.currentTimeMillis(), fileid);
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendStartMissionNotification(java.
	 * lang.String, system.po.Mission, java.lang.String)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendStartMissionNotification(String reciever,
			Mission mission, String fileid, String newTaskId) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(mission.toFormatString());
		Notification notification = new Notification(reciever,
				Notification.Type.NEW_MISSION, "New mission started.",
				body.toString(), System.currentTimeMillis(), fileid);
		notification.setLink(newTaskId);
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendRejectMissionNotification(system
	 * .po.Mission, java.lang.String, java.lang.String)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectMissionNotification(Mission mission,
			String reason, String fileid) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(mission.toFormatString() + "\n");
		body.append("Reason: " + reason + "\n");
		body.append("Dealer: " + sessionManager.getLoginUser().getName());
		Notification notification = new Notification(mission.getCreatorName(),
				Notification.Type.REJECT_MISSION, "Mission rejected.",
				body.toString(), System.currentTimeMillis(), fileid);
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendRejectPartitionNotification(system
	 * .po.MissionPartition, java.lang.String, java.lang.String)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectPartitionNotification(
			MissionPartition partition, String reason, String fileid)
			throws Exception {
		Mission mission = missionDataAccessService
				.get(partition.getMissionid());

		StringBuilder body = new StringBuilder();
		body.append(partition.toFormatString());
		body.append("\n");
		body.append("reason: " + reason);
		body.append("Dealer: " + sessionManager.getLoginUser().getName());
		Notification notification = new Notification(mission.getLeaderName(),
				Notification.Type.REJECT_PARTITION, "Partition "
						+ partition.getTitle() + " rejected.", body.toString(),
				System.currentTimeMillis(), fileid);
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendRejectUnitNotification(system.
	 * po.MissionUnit, java.lang.String, java.lang.String)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectUnitNotification(MissionUnit unit,
			String reason, String fileid) throws Exception {
		MissionPartition partition = missionPartitionDataAccessService.get(unit
				.getPartitionid());

		StringBuilder body = new StringBuilder();
		body.append(unit.toFormatString());
		body.append("\n");
		body.append("reason: " + reason);
		body.append("Dealer: " + sessionManager.getLoginUser().getName());
		Notification notification = new Notification(partition.getLeaderName(),
				Notification.Type.REJECT_PARTITION, "Unit "
						+ partition.getTitle() + " rejected.", body.toString(),
				System.currentTimeMillis(), fileid);
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendAcceptTaskNotification(system.
	 * po.Task)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendAcceptTaskNotification(Task task) throws Exception {
		Printable object = null;
		if (task.getType() == Task.Type.MISSION)
			object = missionDataAccessService.get(task.getData());
		if (task.getType() == Task.Type.MISSION_PARTITION)
			object = missionPartitionDataAccessService.get(task.getData());
		if (task.getType() == Task.Type.MISSION_UNIT)
			object = missionUnitDataAccessService.get(task.getData());
		if (task.getType() == Task.Type.FIX_BUG)
			object = bugReportDataAccessService.get(task.getData());
		StringBuilder body = new StringBuilder();
		body.append(object.toFormatString());
		Notification notification = new Notification(task.getCreatorName(),
				Notification.Type.ACCEPT_TASK, "Task " + task.getTitle()
						+ " accepted", body.toString(),
				System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendStartPartitionNotification(system
	 * .po.MissionPartition, java.lang.String)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendStartPartitionNotification(
			MissionPartition missionPartition, String fileid) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(missionPartition.toFormatString());
		Notification notification = new Notification(
				missionPartition.getLeaderName(),
				Notification.Type.NEW_PARTITION, "New mission partition started.",
				body.toString(), System.currentTimeMillis(), fileid);
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendStartUnitNotification(system.po
	 * .MissionUnit, java.lang.String)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendStartUnitNotification(MissionUnit missionUnit,
			String fileid) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(missionUnit.toFormatString());
		Notification notification = new Notification(
				missionUnit.getLeaderName(), Notification.Type.NEW_TASK,
				"New task started.", body.toString(),
				System.currentTimeMillis(), fileid);
		this.save(notification);
		return notification;
	}

	// public void sendStartTaskNotification(String reciever, String body,
	// String fileid) throws Exception {
	// Notification notification = new Notification(reciever,
	// Notification.Type.NEW_TASK, "New task started.", body,
	// System.currentTimeMillis(), fileid);
	// this.save(notification);
	// }

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendSubmitTaskNotification(java.lang
	 * .String, system.po.MissionUnit, java.lang.String, java.lang.String)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendSubmitTaskNotification(String reciever,
			MissionUnit task, String body, String fileid) throws Exception {
		Notification notification = new Notification(reciever,
				Notification.Type.SUBMIT_TASK, "Task " + task.getTitle()
						+ " submitted.", body, System.currentTimeMillis(),
				fileid);
		this.save(notification);
		return notification;

	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendSubmitPartitionNotification(String reciever,
			MissionPartition partition, String report, String fileid)
			throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Partition : \n");
		body.append(partition.toFormatString());
		body.append("submitted");
		Notification notification = new Notification(reciever,
				Notification.Type.SUBMIT_PARTITION, "Partition submitted.",
				body.toString(), System.currentTimeMillis(), fileid);
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendSubmitMissionNotification(String reciever,
			Mission mission, String report, String fileid) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Mission : \n");
		body.append(mission.toFormatString());
		body.append("submitted");
		Notification notification = new Notification(reciever,
				Notification.Type.NORMAL, "Mission " + mission.getTitle()
						+ " submitted.", body.toString(),
				System.currentTimeMillis(), fileid);
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendAbandonTaskAcceptedNotification
	 * (system.po.Task)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendAbandonTaskAcceptedNotification(Task task)
			throws Exception {
		Notification notification = new Notification(task.getOwnerName(),
				Notification.Type.ACCEPT_ABANDON_TASK,
				"Abandon task application accepted", task.toFormatString(),
				System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectAbandonTaskNotification(Task task,
			String report) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("\nYour application for abandoning task: \n");
		body.append(task.toFormatString());
		body.append("\n is rejected.\n\nComment:\n");
		body.append(report);

		Notification notification = new Notification(task.getOwnerName(),
				Notification.Type.ACCEPT_ABANDON_TASK,
				"Abandon task application rejected", body.toString(),
				System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendMissionAcceptedNotification(system
	 * .po.Mission)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendMissionAcceptedNotification(Mission mission)
			throws Exception {
		Notification notification = new Notification(mission.getCreatorName(),
				Notification.Type.ACCEPT_MISSION, "Mission accepted",
				mission.toFormatString(), System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendRejectRejectTaskNotification(system
	 * .po.Task, java.lang.String)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectRejectTaskNotification(Task task,
			String reason) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(task.toFormatString());
		body.append("\n");
		body.append("reason: " + reason);
		Notification notification = new Notification(task.getOwnerName(),
				Notification.Type.REJECT_REJECT_TASK,
				"Task rejection application rejected.", body.toString(),
				System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	// public void sendRejectSubmittedUnitNotification(MissionUnit unit, String
	// report) throws Exception
	// {
	// StringBuilder body = new StringBuilder();
	// body.append(unit.toString());
	// body.append("\n");
	// body.append("reason: " + report);
	// Notification message = new Notification(unit.getLeaderName(),
	// Notification.Type.REJECT_SUBMITTED_UNIT,
	// "Submitted unit task rejected.", body.toString(),
	// System.currentTimeMillis(), "none");
	// this.save(message);
	// }

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendAcceptSubmittedUnitNotification
	 * (system.po.MissionUnit)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendAcceptSubmittedUnitNotification(MissionUnit unit)
			throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(unit.toFormatString());

		Notification notification = new Notification(unit.getLeaderName(),
				Notification.Type.ACCEPT_SUBMITTED_UNIT,
				"Submitted unit task accepted.", body.toString(),
				System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendAcceptSubmittedPartitionNotification(
			MissionPartition partition) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Your submitted partition:\n");
		body.append(partition.toFormatString());
		body.append("Is accepted.");
		Notification notification = new Notification(partition.getLeaderName(),
				Notification.Type.NORMAL, "Submitted partition "
						+ partition.getTitle() + " accepted.", body.toString(),
				System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendAcceptSubmittedMissionNotification(Mission mission)
			throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Your submitted mission:\n");
		body.append(mission.toFormatString());
		body.append("Is accepted.");
		Notification notification = new Notification(mission.getLeaderName(),
				Notification.Type.NORMAL, "Submitted mission "
						+ mission.getTitle() + " accepted.", body.toString(),
				System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendRejectSubmittedUnitNotification
	 * (system.po.MissionUnit, java.lang.String, java.lang.String)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectSubmittedUnitNotification(MissionUnit unit,
			String reason, String fileid) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(unit.toFormatString());
		body.append("\n");
		body.append("reason: " + reason);
		Notification notification = new Notification(unit.getLeaderName(),
				Notification.Type.REJECT_UNIT, "Submitted unit task "
						+ unit.getTitle() + " rejected.", body.toString(),
				System.currentTimeMillis(), fileid);
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectSubmittedPartitionNotification(
			MissionPartition partition, String reason, String fileid)
			throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Submission of partition: \n");
		body.append(partition.toFormatString());
		body.append("Is rejected.\n");
		body.append("\nReason: " + reason);
		Notification notification = new Notification(partition.getLeaderName(),
				Notification.Type.REJECT_PARTITION, "Submitted partition "
						+ partition.getTitle() + " rejected.", body.toString(),
				System.currentTimeMillis(), fileid);
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectSubmittedMissionNotification(Mission mission,
			String reason, String fileid) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Submission of mission: \n");
		body.append(mission.toFormatString());
		body.append("Is rejected.\n");
		body.append("\nReason: " + reason);
		Notification notification = new Notification(mission.getLeaderName(),
				Notification.Type.NORMAL, "Submitted mission "
						+ mission.getTitle() + " rejected.", body.toString(),
				System.currentTimeMillis(), fileid);
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendAuthorityPromotedNotification(
	 * java.lang.String, java.util.List)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendAuthorityPromotedNotification(String recieverName,
			List<TemporaryAuthority> temporaryAuthorityList) throws Exception {
		StringBuilder body = new StringBuilder();
		for (TemporaryAuthority authority : temporaryAuthorityList) {
			body.append(authority.toFormatString());
			body.append("\n");
		}
		Notification notification = new Notification(recieverName,
				Notification.Type.ACCEPT_AUTHORITY_PROMOTION,
				"Authority promoted. ", body.toString(),
				System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendStartFixBugNotification(system
	 * .po.BugReport)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendStartFixBugNotification(BugReport bugReport)
			throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(bugReport.toFormatString());
		Notification notification = new Notification(
				bugReport.getFounderName(), Notification.Type.ACCEPT_FIX_BUG,
				"Start to fix bug: " + bugReport.getId().substring(0, 7),
				body.toString(), System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectFixBugNotification(BugReport bugReport,
			String report) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Here is your bug report: \n");
		body.append(bugReport.toFormatString());
		body.append("\nYour report is rejected: \n");
		body.append(report);

		Notification notification = new Notification(
				bugReport.getFounderName(), Notification.Type.REJECT_FIX_BUG,
				"Bug report rejected: " + bugReport.getId().substring(0, 7),
				body.toString(), System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendBugFixedNotification(system.po
	 * .BugReport)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendBugFixedNotification(BugReport bugReport)
			throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(bugReport.toFormatString());
		Notification notification = new Notification(
				bugReport.getFounderName(), Notification.Type.BUG_FIXED,
				"Bug: " + bugReport.getId().substring(0, 7)
						+ " have been fixed.", body.toString(),
				System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendTaskTransferAcceptedNotification
	 * (system.po.Task, java.lang.String, java.lang.String)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification[] sendTaskTransferAcceptedNotification(Task task,
			String origin, String target) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(task.toFormatString());
		body.append("\n");
		body.append("from " + origin + " to " + target);
		Notification notificationOrigin = new Notification(origin,
				Notification.Type.TRANSFER_TASK, "Task " + task.getTitle()
						+ " transfered.", body.toString(),
				System.currentTimeMillis(), "none");
		Notification notificationTarget = new Notification(target,
				Notification.Type.TRANSFER_TASK, "Task " + task.getTitle()
						+ " transfered.", body.toString(),
				System.currentTimeMillis(), "none");
		this.save(notificationOrigin);
		this.save(notificationTarget);
		return new Notification[] { notificationOrigin, notificationTarget };
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectTaskTransferNotification(Task task)
			throws Exception {

		StringBuilder body = new StringBuilder();
		body.append("From :" + sessionManager.getLoginUser().getName() + "\n");
		body.append("Your application for transfering task: \n");
		body.append(task.toFormatString());
		body.append("\nis rejected.");

		Notification notification = new Notification(task.getOwnerName(),
				Notification.Type.REJECT_TRANSFER_TASK, "Task "
						+ task.getTitle() + " transfer application rejected.",
				body.toString(), System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendChangeScheduleAcceptedNotification
	 * (system.po.Application)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendChangeScheduleAcceptedNotification(
			Application application) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(application.getBody());
		Notification notification = new Notification(
				application.getSenderName(),
				Notification.Type.SCHEDULE_CHANGED, "System schedule changed.",
				body.toString(), System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectChangeScheduleNotification(String reciever,
			system.po.System system, String duration, String report)
			throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Your application to change schedule of system: \n");
		body.append(system.toFormatString());
		body.append("\nto duration: \n");
		body.append(duration);
		body.append("\nis rejected\n");
		body.append("\nReport: \n");
		body.append(report);
		Notification notification = new Notification(reciever,
				Notification.Type.REJECT_CHANGE_SCHEDULE,
				"Change system schedule application rejected.",
				body.toString(), System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendShutdownSystemAcceptedNotification
	 * (system.po.Application)
	 */

	@Interceptors(NotificationInterceptor.class)
	public Notification sendShutdownSystemAcceptedNotification(
			Application application) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append(application.getBody());
		Notification notification = new Notification(
				application.getSenderName(), Notification.Type.SHUTDOWN_SYSTEM,
				"Shutdown system accepted.", body.toString(),
				System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectShutdownSystemNotification(String reciever,
			system.po.System system, String duration, String report)
			throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Your application to shutdown the system: \n");
		body.append(system.toFormatString());
		body.append("\nIn duration: \n");
		body.append(duration);
		body.append("\nIs rejected\n");
		Notification notification = new Notification(reciever,
				Notification.Type.NORMAL, "Application to shutdown system "
						+ system.getName() + " rejected.", body.toString(),
				System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.NotificationService#sendGuestAccountCreatedNotification
	 * (system.po.User, java.lang.String, java.lang.String, java.lang.String[],
	 * java.lang.String, java.lang.String)
	 */
	@Interceptors(NotificationInterceptor.class)
	public Notification sendGuestAccountCreatedNotification(User guest,
			String guestDescription, String businessDescription,
			String[] dependentFunctions, String expireDate, String reciever)
			throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Guest name: ");
		body.append(guest.getName());
		body.append("\nGuest password: ");
		body.append(guest.getPassword());
		body.append("\nGuest description: ");
		body.append(guestDescription);
		body.append("\nBusiness description: ");
		body.append(businessDescription);
		body.append("\nExpire date: ");
		body.append(expireDate);
		body.append("\n\nDependent systems: \n");

		body.append("\nDependent functions: \n");
		for (String f : dependentFunctions) {
			body.append(f);
			body.append("\n");
		}
		Notification notification = new Notification(reciever,
				Notification.Type.CREATE_GUEST, "Guest account created.",
				body.toString(), System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendNewEmployeeConductedNotification(String comment,
			List<User> approved, List<User> rejected, String reciever)
			throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("employee body test: \n");

		String realPath = "c:/upload/";
		String fileName = "approvedAndRejectedEmployeeTmp"
				+ UUID.randomUUID().toString().substring(0, 7);

		FileOutputStream outStream = new FileOutputStream(realPath + fileName);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				outStream);
		UploadFile newFile = new UploadFile(sessionManager.getLoginUser()
				.getName(), realPath, fileName);
		uploadFileDataAccessService.save(newFile);
		objectOutputStream.writeObject(approved);
		objectOutputStream.writeObject(rejected);

		Notification notification = new Notification(reciever,
				Notification.Type.NEW_EMPLOYEE,
				"New employee incoming setting proceed", body.toString(),
				System.currentTimeMillis(), "none");
		this.save(notification);
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendApplicationProceedNotification(
			Application application, ApplicationStage stage) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Application: \n");
		body.append(application.toFormatString());
		body.append("\nProceed to new stage: \n");
		body.append(stage.toFormatString());

		// String data = application.getId() + ";" + stage.getId();
		String title = "Your application proceed to new stage.";
		Notification notification = new Notification(
				application.getSenderName(), Notification.Type.NORMAL, title,
				body.toString(), System.currentTimeMillis(), "none");

		notification.setLink(application.getId());
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendApplicationFinishedNotification(
			Application application) throws Exception {
		StringBuilder body = new StringBuilder();
		body.append("Application: \n");
		body.append(application.toFormatString());
		body.append("\nFinished.\n");

		String title = "Your application is finished";
		Notification notification = new Notification(
				application.getSenderName(), Notification.Type.NORMAL, title,
				body.toString(), System.currentTimeMillis(), "none");
		notification.setLink(application.getId());
		this.save(notification);
		return notification;
	}

	@Interceptors(NotificationInterceptor.class)
	public Notification sendAuthorityExpiredNotification(List<TemporaryAuthorityWrap> expiredList) throws Exception
	{
		StringBuilder body = new StringBuilder();
		body.append("Temporary Authority: \n");
		for(TemporaryAuthorityWrap t:expiredList)
		{
			body.append(t.getFunction().getSystemName());
			body.append(" - ");
			body.append(t.getFunction().getFunctionName());
			body.append("\n");
		}
		body.append("\nExpired.\n");

		String title = "Temporary authority expired";
		Notification notification = new Notification(
				expiredList.get(0).getUserName(), Notification.Type.NORMAL, title,
				body.toString(), System.currentTimeMillis(), "none");
		this.save(notification);
		return notification;
	}
}
