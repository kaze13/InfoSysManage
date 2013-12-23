package system.service;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.TaskInject;
import system.dao.impl.TaskDaoImpl;
import system.interceptor.TimerInterceptor;
import system.manager.spec.SessionManager;
import system.po.Application;
import system.po.BugReport;
import system.po.Mission;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.Task;
import system.po.Task.StatueType;
import system.po.Task.Type;
import system.service.spec.TaskService;
import system.vo.TaskWrap;

@Interceptors(TimerInterceptor.class)
public class TaskServiceImpl extends AbstractDataAccessService<Task> implements
		Serializable, TaskService {

	@Inject
	@TaskInject
	private TaskDaoImpl taskDao;
	@Inject
	private SessionManager sessionManager;
	@Inject
	private AbstractDataAccessService<MissionPartition> missionPartitionDataAccessService;
	@Inject
	private AbstractDataAccessService<MissionUnit> missionUnitDataAccessService;
	@Inject
	private AbstractDataAccessService<Mission> missionDataAccessService;

	public TaskServiceImpl() {
		super(Task.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#findShutdownSystemTask(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<Task> findShutdownSystemTask(String adminName, String systemName)
			throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("owner_name", adminName);
		sqlWhereMap.put("data", systemName);
		sqlWhereMap.put("type", Task.Type.SHUTDOWN_SYSTEM.ordinal());
		sqlWhereMap.put("statue", Task.StatueType.NOT_COMPLETE);
		return this.findAllByCondition(sqlWhereMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#acceptTask(system.po.Task)
	 */
	@Override
	public void acceptTask(Task task) throws Exception {

		String id = task.getData();
		if (task.getType() == Task.Type.MISSION) {
			Mission mission = missionDataAccessService.get(id);
			mission.setProgress(0);
			missionDataAccessService.update(mission);
		}
		if (task.getType() == Task.Type.MISSION_PARTITION) {
			MissionPartition partition = missionPartitionDataAccessService
					.get(id);
			partition.setProgress(0);
			missionPartitionDataAccessService.update(partition);
		}
		if (task.getType() == Task.Type.MISSION_UNIT) {
			MissionUnit unit = missionUnitDataAccessService.get(id);
			unit.setProgress(0);
			missionUnitDataAccessService.update(unit);
		}
		task.setStatue(StatueType.NOT_COMPLETE);
		update(task);

		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Task accepted", task.getTitle()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#completeTask(system.po.Task)
	 */
	@Override
	public void completeTask(Task task) throws Exception {
		task.setStatue(Task.StatueType.COMPLETED);
		update(task);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Task completed", task.getTitle()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#rejectTask(system.po.Task,
	 * java.lang.String)
	 */
	@Override
	public void rejectTask(Task task, String reason) throws Exception {
		task.setStatue(StatueType.WAITING);
		update(task);

		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Task rejected", task.getTitle()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#retoreTask(system.po.Task)
	 */
	@Override
	public void retoreTask(Task task) throws Exception {
		task.setStatue(StatueType.UNACCEPTED);
		update(task);
	}

	public void retoreToUncomplete(Task task) throws Exception {
		task.setStatue(StatueType.NOT_COMPLETE);
		update(task);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#abandonTask(system.po.Task)
	 */
	@Override
	public void abandonTask(Task task) throws Exception {
		// task.setStatue(Task.StatueType.ABANDONED);
		// update(task);
		delete(task.getId());
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Task abandoned", task.getTitle()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#transferTask(system.po.Task,
	 * java.lang.String)
	 */
	@Override
	public void transferTask(Task task, String target) throws Exception {
		task.setOwnerName(target);
		task.setStatue(StatueType.NOT_COMPLETE);
		update(task);

		if (task.getType() == Task.Type.MISSION_UNIT) {
			MissionUnit unit = missionUnitDataAccessService.get(task.getData());
			unit.setLeaderName(target);
			missionUnitDataAccessService.update(unit);
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Task transfered", task.getTitle()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#startNewMissionTask(system.po.Mission,
	 * long, java.lang.String, boolean)
	 */
	@Override
	public Task startNewMissionTask(Mission mission, long endTime,
			String fileid, boolean deliverNow) throws Exception {
		Task task = new Task(mission.getCreatorName(), mission.getLeaderName(),
				Type.MISSION, "Separate and deliver the mission",
				"Here is the mission detial:", System.currentTimeMillis(),
				endTime, mission.getId(), mission.getFileId());
		if (!deliverNow)
			task.setStatue(Task.StatueType.NOT_DELIVERED);

		save(task);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Task started", task.getTitle()));
		return task;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * system.service.TaskService#startNewPartitionTask(system.po.MissionPartition
	 * , long, java.lang.String, boolean)
	 */
	@Override
	public Task startNewPartitionTask(MissionPartition missionPartition,
			long endTime, String fileid, boolean deliverNow) throws Exception {
		Task task = new Task(sessionManager.getLoginUser().getName(),
				missionPartition.getLeaderName(), Type.MISSION_PARTITION,
				"Separate and deliver the partition",
				"Here is the partition detial:", System.currentTimeMillis(),
				endTime, missionPartition.getId(), fileid);
		if (!deliverNow)
			task.setStatue(Task.StatueType.NOT_DELIVERED);
		save(task);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Task started", task.getTitle()));
		return task;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#startNewUnitTask(system.po.MissionUnit,
	 * long, java.lang.String, boolean)
	 */
	@Override
	public Task startNewUnitTask(MissionUnit missionUnit, long endTime,
			String fileid, boolean deliverNow) throws Exception {
		Task task = new Task(sessionManager.getLoginUser().getName(),
				missionUnit.getLeaderName(), Type.MISSION_UNIT, "Do the unit",
				"Here is the unit detial:", System.currentTimeMillis(),
				endTime, missionUnit.getId(), fileid);
		if (!deliverNow)
			task.setStatue(Task.StatueType.NOT_DELIVERED);
		save(task);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Task started", task.getTitle()));
		return task;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#startNewFixBugTask(system.po.BugReport,
	 * long)
	 */
	@Override
	public Task startNewFixBugTask(BugReport bugReport, long endTime)
			throws Exception {
		Task task = new Task(sessionManager.getLoginUser().getName(),
				sessionManager.getLoginUser().getName(), Type.FIX_BUG,
				"Fix bug", "Here is the bug report: \n"
						+ bugReport.toFormatString(),
				System.currentTimeMillis(), endTime, bugReport.getId(),
				bugReport.getFileId());
		task.setStatue(Task.StatueType.NOT_COMPLETE);
		save(task);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Task started", task.getTitle()));
		return task;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * system.service.TaskService#startNewShutdownSystemTask(system.po.Application
	 * , java.lang.String)
	 */
	@Override
	public Task startNewShutdownSystemTask(Application application,
			String systemName) throws Exception {
		String[] data = application.getData().split("\\;");
		String duration = data[2];
		String endTimeStr = duration.split("\\-")[1];
		DateFormat format = new SimpleDateFormat("MM_dd hh:mm");
		Date endTime = format.parse(endTimeStr);
		Task task = new Task(application.getSenderName(),
				application.getSenderName(), Type.SHUTDOWN_SYSTEM,
				"Shutdown system", application.getBody(),
				System.currentTimeMillis(), endTime.getTime(), systemName,
				"none");

		task.setStatue(Task.StatueType.NOT_COMPLETE);
		save(task);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Task started", task.getTitle()));
		return task;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#findTaskByDataid(java.lang.String)
	 */
	@Override
	public Task findTaskByDataid(String unitid) throws Exception {
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("data", unitid);
		List<Task> results = this.findAllByCondition(sqlMap);
		for (int i = results.size() - 1; i >= 0; --i) {
			if (results.get(i).getStatue() == Task.StatueType.ABANDONED
					|| results.get(i).getStatue() == Task.StatueType.REJECTED)
				results.remove(i);
		}
		if (results.size() > 1)
			throw new Exception("more than one result");
		if (results.size() == 0)
			return null;
		return results.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * system.service.TaskService#findPartitionTasksByMission(java.lang.String)
	 */
	@Override
	public List<Task> findPartitionTasksByMission(String missionid)
			throws Exception {

		List<Task> results = taskDao.findPartitionTasksByMission(Task.class,
				missionid, transaction);
		return results;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * system.service.TaskService#findUnitTasksByPartition(java.lang.String)
	 */
	@Override
	public List<Task> findUnitTasksByPartition(String partitionid)
			throws Exception {

		List<Task> results = taskDao.findUnitTasksByPartition(Task.class,
				partitionid, transaction);
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#getUnacceptedTasks()
	 */
	@Override
	public List<Task> getUnacceptedTasks() throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("statue", 2);
		sqlWhereMap.put("owner_name", sessionManager.getLoginUser().getName());

		return this.findAllByCondition(sqlWhereMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see system.service.TaskService#toVo(system.po.Task)
	 */
	// @Override
	// public TaskWrap toVo(Task task) throws Exception {
	// // TODO taskVo
	// TaskWrap vo = new TaskWrap();
	// Object taskData = null;
	// if (task.getType() == Type.MISSION) {
	// taskData = missionDataAccessService.get(task.getData());
	// }
	// if (task.getType() == Type.MISSION_PARTITION) {
	// taskData = missionPartitionDataAccessService.get(task.getData());
	// }
	// if (task.getType() == Type.MISSION_UNIT) {
	// taskData = missionUnitDataAccessService.get(task.getData());
	// }
	// vo.setTask(task);
	// vo.setTaskObject(taskData);
	//
	// return vo;
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see system.service.TaskService#toVoList(java.util.List)
	// */
	// @Override
	// public List<TaskWrap> toVoList(List<Task> taskList) throws Exception {
	// List<TaskWrap> result = new ArrayList<TaskWrap>();
	// for (Task task : taskList) {
	// result.add(this.toVo(task));
	// }
	// return result;
	// }

}
