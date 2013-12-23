package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.Application;
import system.po.BugReport;
import system.po.Mission;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.Task;
import system.vo.TaskWrap;

public interface TaskService {

	public void save(Task t) throws Exception;

	public void save(List<Task> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(Task t) throws Exception;

	public Task get(String id) throws Exception;

	public List<Task> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;

	public abstract List<Task> findShutdownSystemTask(String adminName,
			String systemName) throws Exception;

	public abstract void acceptTask(Task task) throws Exception;

	public abstract void completeTask(Task task) throws Exception;

	public abstract void rejectTask(Task task, String reason) throws Exception;

	public abstract void retoreTask(Task task) throws Exception;

	public abstract void retoreToUncomplete(Task task) throws Exception;

	public abstract void abandonTask(Task task) throws Exception;

	public abstract void transferTask(Task task, String target)
			throws Exception;

	public abstract Task startNewMissionTask(Mission mission, long endTime,
			String fileid, boolean deliverNow) throws Exception;

	public abstract Task startNewPartitionTask(
			MissionPartition missionPartition, long endTime, String fileid,
			boolean deliverNow) throws Exception;

	public abstract Task startNewUnitTask(MissionUnit missionUnit,
			long endTime, String fileid, boolean deliverNow) throws Exception;

	public abstract Task startNewFixBugTask(BugReport bugReport, long endTime)
			throws Exception;

	public abstract Task startNewShutdownSystemTask(Application application,
			String systemName) throws Exception;

	public abstract Task findTaskByDataid(String unitid) throws Exception;

	public abstract List<Task> findPartitionTasksByMission(String missionid)
			throws Exception;

	public abstract List<Task> findUnitTasksByPartition(String partitionid)
			throws Exception;

	public abstract List<Task> getUnacceptedTasks() throws Exception;

}