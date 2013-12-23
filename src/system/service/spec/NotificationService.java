package system.service.spec;

import java.util.List;
import java.util.Map;

import javax.interceptor.Interceptors;

import system.interceptor.NotificationInterceptor;
import system.po.Application;
import system.po.BugReport;
import system.po.Mission;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.Notification;
import system.po.Task;
import system.po.TemporaryAuthority;
import system.po.User;

public interface NotificationService {

	public void save(Notification t) throws Exception;

	public void save(List<Notification> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(Notification t) throws Exception;

	public Notification get(String id) throws Exception;

	public List<Notification> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;

	public abstract List<Notification> getUnreadNotifications()
			throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendNotification(String reciever,
			String title, String body, String fileid) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendStartMissionNotification(String reciever,
			Mission mission, String fileid) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendRejectMissionNotification(Mission mission,
			String reason, String fileid) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendRejectPartitionNotification(
			MissionPartition partition, String reason, String fileid)
			throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendRejectUnitNotification(MissionUnit unit,
			String reason, String fileid) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendAcceptTaskNotification(Task task)
			throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendStartPartitionNotification(
			MissionPartition missionPartition, String fileid) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendStartUnitNotification(
			MissionUnit missionUnit, String fileid) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendSubmitTaskNotification(String reciever,
			MissionUnit task, String body, String fileid) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendAbandonTaskAcceptedNotification(Task task)
			throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendRejectAbandonTaskNotification(Task task,
			String report) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendMissionAcceptedNotification(Mission mission)
			throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendRejectRejectTaskNotification(Task task,
			String reason) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendAcceptSubmittedUnitNotification(
			MissionUnit unit) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendRejectSubmittedUnitNotification(
			MissionUnit unit, String reason, String fileid) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendAuthorityPromotedNotification(
			String recieverName, List<TemporaryAuthority> temporaryAuthorityList)
			throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendStartFixBugNotification(BugReport bugReport)
			throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendBugFixedNotification(BugReport bugReport)
			throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public Notification sendRejectFixBugNotification(BugReport bugReport,
			String report) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification[] sendTaskTransferAcceptedNotification(
			Task task, String origin, String target) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendRejectTaskTransferNotification(Task task)
			throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendChangeScheduleAcceptedNotification(
			Application application) throws Exception;

	@Interceptors(NotificationInterceptor.class)
	public abstract Notification sendShutdownSystemAcceptedNotification(
			Application application) throws Exception;

	public abstract Notification sendGuestAccountCreatedNotification(
			User guest, String guestDescription, String businessDescription,
			String[] dependentFunctions, String expireDate, String reciever)
			throws Exception;

}