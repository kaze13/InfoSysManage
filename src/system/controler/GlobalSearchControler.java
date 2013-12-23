package system.controler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;


import system.manager.spec.SessionManager;
import system.po.Application;
import system.po.Message;
import system.po.Notification;
import system.po.Task;
import system.service.ApplicationServiceImpl;
import system.service.MessageServiceImpl;
import system.service.NotificationServiceImpl;
import system.service.TaskServiceImpl;
import system.service.UserServiceImpl;
import system.vo.UserWrap;

@Named
@SessionScoped
public class GlobalSearchControler implements Serializable{


	@Inject
	private MessageServiceImpl messageService;
	@Inject
	private NotificationServiceImpl notificationService;
	@Inject
	private ApplicationServiceImpl applicationService;
	@Inject
	private TaskServiceImpl taskService;
	@Inject
	private UserServiceImpl userService;
	@Inject
	private SessionManager sessionManager;

	private List<Message> allMyMessages;
	private List<Notification> allMyNotifications;
	private List<Application> allMyApplications;
	private List<Task> allMyTasks;
	private List<UserWrap> allMyUsers;
	private List<Message> resultMessages = new ArrayList<Message>();
	private List<Notification> resultNotifications = new ArrayList<Notification>();
	private List<Application> resultApplications = new ArrayList<Application>();
	private List<Task> resultTasks = new ArrayList<Task>();
	private List<UserWrap> resultUsers = new ArrayList<UserWrap>();

	private boolean searchMessage;
	private boolean searchNotification;
	private boolean searchApplication;
	private boolean searchTask;
	private boolean searchPerson;

	private UserWrap selectedUser;
	private String keyword;
	public void init() throws Exception
	{
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("reciever_name", sessionManager.getLoginUser().getName());
		allMyMessages = messageService.findAllByCondition(sqlWhereMap);
		allMyNotifications = notificationService.findAllByCondition(sqlWhereMap);
		allMyApplications = applicationService.findAllByCondition(sqlWhereMap);
		allMyUsers = UserWrap.convert(userService.findAllByCondition(null));
		sqlWhereMap.clear();
		sqlWhereMap.put("owner_name", sessionManager.getLoginUser().getName());
		allMyTasks = taskService.findAllByCondition(sqlWhereMap);
	}
	public void search() throws Exception
	{
		init();
		resultMessages.clear();
		resultNotifications.clear();
		resultApplications.clear();
		resultUsers.clear();
		resultTasks.clear();
		for(Message m:allMyMessages)
		{
			if(m.toSignatureString().contains(keyword))
				resultMessages.add(m);
		}
		for(Notification m:allMyNotifications)
		{
			if(m.toSignatureString().contains(keyword))
				resultNotifications.add(m);
		}
		for(Application m:allMyApplications)
		{
			if(m.toSignatureString().contains(keyword))
				resultApplications.add(m);
		}
		for(UserWrap u:allMyUsers)
		{
			if(u.getUser().toSignatureString().contains(keyword))
				resultUsers.add(u);
		}
		for(Task t:allMyTasks)
		{
			if(t.toSignatureString().contains(keyword))
				resultTasks.add(t);
		}

	}

	public String redirect(Object obj)
	{
		if(obj instanceof Message)
			return "./commonMessage.jsf?faces-redirect=true&id=" + ((Message)obj).getId();
		if(obj instanceof Notification)
			return "./notification.jsf?faces-redirect=true&id=" + ((Notification)obj).getId();
		if(obj instanceof Application)
			return "./application.jsf?faces-redirect=true&id=" + ((Application)obj).getId();
		if(obj instanceof Task)
			return "./task.jsf?faces-redirect=true&id=" + ((Task)obj).getId();
		return null;
	}



	public List<Message> getResultMessages() {
		return resultMessages;
	}
	public void setResultMessages(List<Message> resultMessages) {
		this.resultMessages = resultMessages;
	}
	public List<Notification> getResultNotifications() {
		return resultNotifications;
	}
	public void setResultNotifications(List<Notification> resultNotifications) {
		this.resultNotifications = resultNotifications;
	}
	public List<Application> getResultApplications() {
		return resultApplications;
	}
	public void setResultApplications(List<Application> resultApplications) {
		this.resultApplications = resultApplications;
	}
	public List<Task> getResultTasks() {
		return resultTasks;
	}
	public void setResultTasks(List<Task> resultTasks) {
		this.resultTasks = resultTasks;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public boolean isSearchMessage() {
		return searchMessage;
	}
	public void setSearchMessage(boolean searchMessage) {
		this.searchMessage = searchMessage;
	}
	public boolean isSearchNotification() {
		return searchNotification;
	}
	public void setSearchNotification(boolean searchNotification) {
		this.searchNotification = searchNotification;
	}
	public boolean isSearchApplication() {
		return searchApplication;
	}
	public void setSearchApplication(boolean searchApplication) {
		this.searchApplication = searchApplication;
	}
	public boolean isSearchTask() {
		return searchTask;
	}
	public void setSearchTask(boolean searchTask) {
		this.searchTask = searchTask;
	}
	public boolean isSearchPerson() {
		return searchPerson;
	}
	public void setSearchPerson(boolean searchPerson) {
		this.searchPerson = searchPerson;
	}
	public UserWrap getSelectedUser() {
		return selectedUser;
	}
	public void setSelectedUser(UserWrap selectedUser) {
		this.selectedUser = selectedUser;
	}
	public List<UserWrap> getResultUsers() {
		return resultUsers;
	}
	public void setResultUsers(List<UserWrap> resultUsers) {
		this.resultUsers = resultUsers;
	}





}
