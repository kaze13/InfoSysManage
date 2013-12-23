package system.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import system.interceptor.TransactionInterceptor;
import system.manager.impl.DicManager;
import system.manager.impl.GlobalCacheManagerImpl;
import system.manager.spec.SessionManager;
import system.po.Application;
import system.po.Authority;
import system.po.MainAuthority;
import system.po.Message;
import system.po.Notification;
import system.po.Task;
import system.po.TemporaryAuthority;
import system.po.User;
import system.service.ApplicationServiceImpl;
import system.service.AuthorityServiceImpl;
import system.service.MainAuthorityServiceImpl;
import system.service.NotificationServiceImpl;
import system.service.SystemFunctionServiceImpl;
import system.service.TemporaryAuthorityServiceImpl;
import system.service.spec.MessageService;
import system.service.spec.NotificationService;
import system.service.spec.TaskService;
import system.service.spec.TemporaryAuthorityService;
import system.vo.TemporaryAuthorityWrap;
import system.vo.UserWrap;

@Named
@SessionScoped
public class MainPageControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3919644600313660087L;

	@Inject
	private MessageService messageService;
	@Inject
	private NotificationServiceImpl notificationService;
	@Inject
	private ApplicationServiceImpl applicationService;
	@Inject
	private TaskService taskService;
	@Inject
	private SessionManager sessionManager;
	@Inject
	private GlobalCacheManagerImpl cacheManager;
	@Inject
	private SystemFunctionServiceImpl systemFunctionService;
	@Inject
	private TemporaryAuthorityServiceImpl temporaryAuthorityService;
	@Inject
	private DicManager dicManager;
	@Inject
	private MainAuthorityServiceImpl mainAuthorityService;
	@Inject
	private AuthorityServiceImpl authorityService;
	private List<Message> unreadMessages;
	private List<Notification> unreadNotifications;
	private List<Application> undealedApplications;
	private List<Task> unacceptedTasks;
	private List<String> lackedAuthorityList = new ArrayList<String>();
	private UserWrap currentUser;
	private int newMessagesNumber;
	private int newNotificationsNumber;
	private int newApplicationsNumber;
	private int newTasksNumber;
	private DashboardModel dashModel;

	private String promoteAuthority;
	private String comment;
	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {

		currentUser = new UserWrap(sessionManager.getLoginUser());
		// currentUser.setAuthorisedFunctions(systemFunctionService
		// .findFunctionByRole(currentUser.getRealName()));
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("user_name", currentUser.getName());
		List<TemporaryAuthorityWrap> tmpAuthority = TemporaryAuthorityWrap
				.convert(temporaryAuthorityService
						.findAllByCondition(sqlWhereMap));
		for (TemporaryAuthorityWrap t : tmpAuthority) {
			t.setFunction(systemFunctionService.get(t.getSystemFunctionId()));
		}
		List<TemporaryAuthorityWrap> expiredAuthority = new ArrayList<TemporaryAuthorityWrap>();
		for(int i = tmpAuthority.size() - 1; i >= 0; --i)
		{
			if(tmpAuthority.get(i).getExpireTime() < System.currentTimeMillis())
			{
				expiredAuthority.add(tmpAuthority.get(i));
				tmpAuthority.remove(i);
			}
		}
		if(expiredAuthority.size() > 0)
		{
			String[] ids = new String[expiredAuthority.size()];
			for(int i = 0; i < expiredAuthority.size(); ++i)
			{
				ids[i] = expiredAuthority.get(i).getId();
			}
			temporaryAuthorityService.delete(ids);
			notificationService.sendAuthorityExpiredNotification(expiredAuthority);
		}
		currentUser.setTemporaryAuthority(tmpAuthority);

		unreadMessages = messageService.getUnreadMessages();
		unreadNotifications = notificationService.getUnreadNotifications();
		undealedApplications = applicationService.getUndealedApplications();
		unacceptedTasks = taskService.getUnacceptedTasks();

		sqlWhereMap.clear();
		sqlWhereMap.put("user_name", sessionManager.getLoginUser().getName());
		List<MainAuthority> ownedAuthority = mainAuthorityService.findAllByCondition(sqlWhereMap);
		List<Authority> allAuthority = authorityService.findAllByCondition(null);
		lackedAuthorityList = new ArrayList<String>();
		for(Authority a:allAuthority)
		{
			for(int i = 0; i < ownedAuthority.size(); ++i)
			{
				if(a.getAuthorityName().equals(ownedAuthority.get(i).getAuthorityName()))
					break;
				if(i == ownedAuthority.size() - 1)
					lackedAuthorityList.add(a.getAuthorityName());
			}
		}
	}

	public MainPageControler() {
		dashModel = new DefaultDashboardModel();
		DashboardColumn column1 = new DefaultDashboardColumn();
		DashboardColumn column2 = new DefaultDashboardColumn();
		DashboardColumn column3 = new DefaultDashboardColumn();
		DashboardColumn column4 = new DefaultDashboardColumn();
		column1.addWidget("messageDash");
		column1.addWidget("applicationDash");
		column1.addWidget("notificationDash");
		column2.addWidget("taskDash");
		column3.addWidget("authorityDash");
		dashModel.addColumn(column1);
		dashModel.addColumn(column2);
		dashModel.addColumn(column3);
		dashModel.addColumn(column4);

	}

	public void promoteMainAuthority() throws Exception
	{
		applicationService.sendPromoteMainAuthorityApplication(promoteAuthority, comment);
		promoteAuthority = null;
		comment = null;
	}

	public void clearCache() {
		cacheManager.clear();
	}

	public void reloadDictionary() throws Exception
	{
		dicManager.init();
	}
	public void checkNewEvent() throws Exception {
		onRefresh();
		newMessagesNumber = unreadMessages.size();
		newNotificationsNumber = unreadNotifications.size();
		newApplicationsNumber = undealedApplications.size();
		newTasksNumber = unacceptedTasks.size();
		if (newMessagesNumber + newNotificationsNumber > 0)
			RequestContext.getCurrentInstance().execute("reminderDlg_w.show()");
	}

	public String redirectToMessage() {

		return "./commonMessage.jsf?faces-redirect=true";
	}

	public String redirectToNotification() {
		return "./notification.jsf?faces-redirect=true";
	}

	public String redirectToTask() {
		return "./task.jsf?faces-redirect=true";
	}

	public String redirectToApplication() {
		return "./application.jsf?faces-redirect=true";
	}

	public void logoff() throws IOException {
		sessionManager.logoff();
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect("/rd34/index.jsf");
	}

	public List<Message> getUnreadMessages() {
		return unreadMessages;
	}

	public void setUnreadMessages(List<Message> unreadMessages) {
		this.unreadMessages = unreadMessages;
	}

	public List<Notification> getUnreadNotifications() {
		return unreadNotifications;
	}

	public void setUnreadNotifications(List<Notification> unreadNotifications) {
		this.unreadNotifications = unreadNotifications;
	}

	public List<Application> getUndealedApplications() {
		return undealedApplications;
	}

	public void setUndealedApplications(List<Application> undealedApplications) {
		this.undealedApplications = undealedApplications;
	}

	public List<Task> getUnacceptedTasks() {
		return unacceptedTasks;
	}

	public void setUnacceptedTasks(List<Task> unacceptedTasks) {
		this.unacceptedTasks = unacceptedTasks;
	}

	//

	public int getNewTasksNumber() {
		return newTasksNumber;
	}

	@Interceptors(TransactionInterceptor.class)
	public UserWrap getCurrentUser() throws Exception {
		if (currentUser == null)
			currentUser = new UserWrap(sessionManager.getLoginUser());

		return currentUser;

	}

	public void setCurrentUser(UserWrap currentUser) {
		this.currentUser = currentUser;
	}

	public int getNewNotificationsNumber() {
		return newNotificationsNumber;
	}

	public void setNewNotificationsNumber(int newNotificationsNumber) {
		this.newNotificationsNumber = newNotificationsNumber;
	}

	public void setNewTasksNumber(int newTasksNumber) {
		this.newTasksNumber = newTasksNumber;
	}

	public int getNewMessagesNumber() {
		return newMessagesNumber;
	}

	public void setNewMessagesNumber(int newMessagesNumber) {
		this.newMessagesNumber = newMessagesNumber;
	}

	public int getNewApplicationsNumber() {
		return newApplicationsNumber;
	}

	public void setNewApplicationsNumber(int newApplicationsNumber) {
		this.newApplicationsNumber = newApplicationsNumber;
	}

	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void increment() {
		count++;
	}

	public DashboardModel getDashModel() {
		return dashModel;
	}

	public void setDashModel(DashboardModel dashModel) {
		this.dashModel = dashModel;
	}

	public List<String> getLackedAuthorityList() {
		return lackedAuthorityList;
	}

	public void setLackedAuthorityList(List<String> lackedAuthorityList) {
		this.lackedAuthorityList = lackedAuthorityList;
	}

	public String getPromoteAuthority() {
		return promoteAuthority;
	}

	public void setPromoteAuthority(String promoteAuthority) {
		this.promoteAuthority = promoteAuthority;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
