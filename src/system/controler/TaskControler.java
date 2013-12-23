package system.controler;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.component.layout.LayoutUnit;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.TaskVoListDataModel;
import system.manager.spec.SessionManager;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.Task;
import system.po.Task.Type;
import system.po.User;
import system.service.ApplicationServiceImpl;
import system.service.MissionPartitionServiceImpl;
import system.service.MissionServiceImpl;
import system.service.MissionUnitServiceImpl;
import system.service.NotificationServiceImpl;
import system.service.TaskServiceImpl;
import system.vo.MissionWrap;
import system.vo.PartitionWrap;
import system.vo.TaskWrap;
import system.vo.UnitWrap;

@Named
@SessionScoped
public class TaskControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3238009136930869369L;
	@Inject
	private TaskServiceImpl taskService;
	@Inject NotificationServiceImpl notificationService;
	@Inject ApplicationServiceImpl applicationService;
	@Inject MissionServiceImpl missionService;
	@Inject MissionPartitionServiceImpl missionPartitionService;
	@Inject MissionUnitServiceImpl missionUnitService;
	private TaskVoListDataModel taskListModel;
	private TaskWrap newTask = new TaskWrap();
	private Date newStartTime;
	private Date newEndTime;
	private TaskWrap selectedTask;
	private String comment;
	private User currentUser;
	private LayoutUnit dataUnit;
	private LayoutUnit detialUnit;


	@Inject
	private SessionManager sessionManager;


	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		currentUser = sessionManager.getLoginUser();
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("owner_name", currentUser.getName());
		List<TaskWrap> baseTaskList = TaskWrap.convert(taskService
				.findAllByCondition(sqlMap));
		Collections.sort(baseTaskList);
		taskListModel = new TaskVoListDataModel(baseTaskList);

		Map<String, String> varMap = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String id = varMap.get("id");
		for(TaskWrap task:baseTaskList)
		{
			if(task.getId().equals(id))
			{
				selectedTask = task;
				break;
			}
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void acceptTask() throws Exception {
		taskService.acceptTask(selectedTask.getTask());
		notificationService.sendAcceptTaskNotification(selectedTask.getTask());
	}

	@Interceptors(TransactionInterceptor.class)
	public void rejectTask() throws Exception {
		taskService.rejectTask(selectedTask.getTask(), comment);
		applicationService.sendRejectTaskApplication(selectedTask.getTask(), comment);
		comment = null;
	}


	@Interceptors(TransactionInterceptor.class)
	public void doSaveTODOTask() throws Exception
	{
		Task task = newTask.getTask();
		task.setCreatorName(sessionManager.getLoginUser().getName());
		task.setData("none");
		task.setEndTime(newEndTime.getTime());
		task.setStartTime(newStartTime.getTime());
		task.setOwnerName(sessionManager.getLoginUser().getName());
		task.setStatue(Task.StatueType.NOT_COMPLETE);
		task.setType(Task.Type.TODO);
		taskService.save(newTask.getTask());
		((List<TaskWrap>)taskListModel.getWrappedData()).add(newTask);
		newTask = new TaskWrap();
		newStartTime = null;
		newEndTime = null;
	}

	public void completeTODOTask() throws Exception
	{
		Task task = selectedTask.getTask();
		task.setStatue(Task.StatueType.COMPLETED);
		taskService.update(task);
	}

	public void abortTODOTask() throws Exception
	{
		Task task = selectedTask.getTask();
		task.setStatue(Task.StatueType.ABANDONED);
		taskService.update(task);
	}
	public String redirect() {
		if (selectedTask.getType() == Type.MISSION)
			return "./missionwizard.jsf?faces-redirect=true&missionid="
					+ selectedTask.getData();
		if (selectedTask.getType() == Type.MISSION_PARTITION)
			return "./partitionwizard.jsf?faces-redirect=true&partitionid="
					+ selectedTask.getData();
		if (selectedTask.getType() == Type.MISSION_UNIT)
			return "./unitdetial.jsf?faces-redirect=true&unitid="
					+ selectedTask.getData();
		if (selectedTask.getType() == Type.FIX_BUG)
			return "./fixbug.jsf?faces-redirect=true&reportid="
					+ selectedTask.getData();
		if (selectedTask.getType() == Type.SHUTDOWN_SYSTEM)
			return "./shutdownsystem.jsf.jsf?faces-redirect=true&id="
					+ selectedTask.getData();
		return null;
	}
	public void changeLayoutHorizontal() {
		dataUnit.setPosition("west");
		detialUnit.setPosition("center");
	}

	public void changeLayoutVertical() {
		dataUnit.setPosition("center");
		detialUnit.setPosition("south");
	}
	// getter/setter

	public String getComment() {
		return comment;
	}

	public TaskVoListDataModel getTaskListModel() {
		return taskListModel;
	}

	public void setTaskListModel(TaskVoListDataModel taskListModel) {
		this.taskListModel = taskListModel;
	}

	public TaskWrap getSelectedTask() {
		return selectedTask;
	}

	public void setSelectedTask(TaskWrap selectedTask) {
		this.selectedTask = selectedTask;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public TaskWrap getNewTask() {
		return newTask;
	}

	public void setNewTask(TaskWrap newTask) {
		this.newTask = newTask;
	}

	public Date getNewStartTime() {
		return newStartTime;
	}

	public void setNewStartTime(Date newStartTime) {
		this.newStartTime = newStartTime;
	}

	public Date getNewEndTime() {
		return newEndTime;
	}

	public void setNewEndTime(Date newEndTime) {
		this.newEndTime = newEndTime;
	}

	// ////////////////////
	// TODO SCHEDULE
	private ScheduleModel eventModel = new DefaultScheduleModel();

	private ScheduleEvent event = new DefaultScheduleEvent();

	private String theme;

	public void refreshSchedule() {
		// List<Task> taskList = (List<Task>) taskListModel.getWrappedData();
		for (TaskWrap task : taskListModel) {
			ScheduleEvent newEvent = new DefaultScheduleEvent(task.getTitle(),
					new Date(task.getStartTime()), new Date(task.getEndTime()),
					task);

			eventModel.addEvent(newEvent);
		}
	}

	public void addEvent(ActionEvent actionEvent) {
		if (event.getId() == null)
			eventModel.addEvent(event);
		else
			eventModel.updateEvent(event);

		event = new DefaultScheduleEvent();
	}

	public void onEventSelect(ScheduleEntrySelectEvent selectEvent) {
		event = selectEvent.getScheduleEvent();
		selectedTask = (TaskWrap) event.getData();
	}

	public void onDateSelect(DateSelectEvent selectEvent) {
		event = new DefaultScheduleEvent(Math.random() + "",
				selectEvent.getDate(), selectEvent.getDate());
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Event moved", "Day delta:" + event.getDayDelta()
						+ ", Minute delta:" + event.getMinuteDelta());

		addMessage(message);
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Event resized", "Day delta:" + event.getDayDelta()
						+ ", Minute delta:" + event.getMinuteDelta());

		addMessage(message);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public LayoutUnit getDataUnit() {
		return dataUnit;
	}

	public void setDataUnit(LayoutUnit dataUnit) {
		this.dataUnit = dataUnit;
	}

	public LayoutUnit getDetialUnit() {
		return detialUnit;
	}

	public void setDetialUnit(LayoutUnit detialUnit) {
		this.detialUnit = detialUnit;
	}

}
