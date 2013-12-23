package system.controler;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.SystemListDataModel;
import system.manager.spec.SessionManager;
import system.po.Application;
import system.po.SysAdmin;
import system.po.System;
import system.po.Task;
import system.po.User;
import system.service.ApplicationServiceImpl;
import system.service.SysAdminServiceImpl;
import system.service.SystemServiceImpl;
import system.service.UserServiceImpl;
import system.service.spec.TaskService;
import system.service.spec.UserService;

@Named
@SessionScoped
public class SystemAdminControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7365268634962663026L;
	@Inject
	private SystemServiceImpl systemService;
	@Inject
	private ApplicationServiceImpl applicationService;
	@Inject
	private SysAdminServiceImpl sysAdminService;
	@Inject
	private UserServiceImpl userService;
	@Inject
	private TaskService taskService;
	@Inject
	private SessionManager sessionManager;
	private List<System> baseSystemList;
	private SystemListDataModel systemListModel;
	private System selectedSystem;
	private String comment;
	private Date startFrom;
	private Date endWith;
	private User currentUser;

	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		baseSystemList = systemService.findSystemByAdmin(sessionManager
				.getLoginUser().getName());
		systemListModel = new SystemListDataModel(baseSystemList);
		currentUser = sessionManager.getLoginUser();
	}

	@Interceptors(TransactionInterceptor.class)
	public void applyForLeave() throws Exception {
		FacesContext context = FacesContext.getCurrentInstance();
		if (startFrom.getTime() > endWith.getTime()) {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"start date larger than end data", null));
			startFrom = null;
			endWith = null;
			comment = null;
			return;
		}
		applicationService.sendAskForLeaveApplication(startFrom, endWith,
				comment);
		startFrom = null;
		endWith = null;
		comment = null;
	}

	@Interceptors(TransactionInterceptor.class)
	public void backToWork() throws Exception {
		currentUser.setCondition("normal");
		userService.update(currentUser);
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("origin_admin", currentUser.getName());
		List<SysAdmin> tempoList = sysAdminService
				.findAllByCondition(sqlWhereMap);
		String[] ids = new String[tempoList.size()];
		for (int i = 0; i < ids.length; ++i) {
			ids[i] = tempoList.get(i).getId();
		}
		sysAdminService.delete(ids);

	}

	@Interceptors(TransactionInterceptor.class)
	public void applyScheduleChange() throws Exception {
		DateFormat format = new SimpleDateFormat("hh:mm");
		String duration = format.format(startFrom);
		duration += " - ";
		duration += format.format(endWith);
		applicationService.sendChangeScheduleApplication(selectedSystem,
				duration, comment);
		startFrom = null;
		endWith = null;
		comment = null;
	}

	@Interceptors(TransactionInterceptor.class)
	public void applyShutDownSystem() throws Exception {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("sender_name", sessionManager.getLoginUser().getName());
		sqlWhereMap.put("type", Application.Type.SHUTDOWN_SYSTEM.ordinal());
		sqlWhereMap.put("statue", Application.StatueType.NOT_DEALED);
		List<Application> applications = applicationService
				.findAllByCondition(sqlWhereMap);
		if (applications.size() >= 1) {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Operation fail",
					"You have unfinished shutdown system applications."));
			return;
		}
		DateFormat format = new SimpleDateFormat("MM_dd hh:mm");
		String duration = format.format(startFrom);
		duration += " - ";
		duration += format.format(endWith);
		applicationService.sendShutdownSystemApplication(selectedSystem,
				duration, comment);
		startFrom = null;
		endWith = null;
		duration = null;
		comment = null;
	}

	@Interceptors(TransactionInterceptor.class)
	public void shutDownSystem() throws Exception {
		FacesContext context = FacesContext.getCurrentInstance();
		List<Task> task = taskService.findShutdownSystemTask(sessionManager
				.getLoginUser().getName(), selectedSystem.getName());
		if (task.size() > 1) {
			throw new Exception("more than one shutdown tasks");
		} else if (task.size() == 0) {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Operation fail",
					"You should ask for acception first."));
		} else {
			systemService.shutdownSystem(selectedSystem);
			Task theTask = task.get(0);
			taskService.completeTask(theTask);

		}

	}

	@Interceptors(TransactionInterceptor.class)
	public void startupSystem() throws Exception {
		systemService.startupSystem(selectedSystem);
	}

	public SystemListDataModel getSystemListModel() {
		return systemListModel;
	}

	public void setSystemListModel(SystemListDataModel systemListModel) {
		this.systemListModel = systemListModel;
	}

	public System getSelectedSystem() {
		return selectedSystem;
	}

	public void setSelectedSystem(System selectedSystem) {
		this.selectedSystem = selectedSystem;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getStartFrom() {
		return startFrom;
	}

	public void setStartFrom(Date startFrom) {
		this.startFrom = startFrom;
	}

	public Date getEndWith() {
		return endWith;
	}

	public void setEndWith(Date endWith) {
		this.endWith = endWith;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

}
