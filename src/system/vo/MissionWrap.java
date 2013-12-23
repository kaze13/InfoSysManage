package system.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import system.po.DependentSystem;
import system.po.Mission;
import system.po.SystemFunction;
import system.po.Task;
import system.po.TaskReport;
import system.po.User;
import system.po.System;
import system.po.spec.Downloadable;
public class MissionWrap implements Downloadable,Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 383663492291252534L;
	private Mission mission;
	private UserWrap leader;
	private List<PartitionWrap> partitionList;
	private Task task;
	private TaskReport taskReport;

	private List<System> dependentSystems;
	private List<SystemFunction> dependentFunctions;



	public MissionWrap() {
		super();
		mission = new Mission();
	}

	public MissionWrap(Mission mission)
	{
		this.mission = mission;
	}






	public List<System> getDependentSystems() {
		return dependentSystems;
	}

	public void setDependentSystems(List<System> dependentSystems) {
		this.dependentSystems = dependentSystems;
	}

	public List<SystemFunction> getDependentFunctions() {
		return dependentFunctions;
	}

	public void setDependentFunctions(List<SystemFunction> dependentFunctions) {
		this.dependentFunctions = dependentFunctions;
	}
	public UserWrap getLeader() {
		return leader;
	}

	public void setLeader(UserWrap leader) {
		this.leader = leader;
	}

	public Mission getMission() {
		return mission;
	}
	public void setMission(Mission mission) {
		this.mission = mission;
	}
	public List<PartitionWrap> getPartitionList() {
		return partitionList;
	}
	public void setPartitionList(List<PartitionWrap> partitionList) {
		this.partitionList = partitionList;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public TaskReport getTaskReport() {
		return taskReport;
	}
	public void setTaskReport(TaskReport taskReport) {
		this.taskReport = taskReport;
	}
	public int getProgress() {
		return mission.getProgress();
	}
	public void setProgress(int progress) {
		mission.setProgress(progress);
	}
	public String getId() {
		return mission.getId();
	}
	public void setId(String id) {
		mission.setId(id);
	}
	public String getLeaderName() {
		return mission.getLeaderName();
	}
	public void setLeaderName(String leaderName) {
		mission.setLeaderName(leaderName);
	}
	public String getTitle() {
		return mission.getTitle();
	}
	public void setTitle(String title) {
		mission.setTitle(title);
	}
	public String getDescription() {
		return mission.getDescription();
	}
	public void setDescription(String description) {
		mission.setDescription(description);
	}
	public String getCreatorName() {
		return mission.getCreatorName();
	}
	public void setCreatorName(String creatorName) {
		mission.setCreatorName(creatorName);
	}

	public String getFileId() {
		return mission.getFileId();
	}

	public void setFileId(String fileId) {
		mission.setFileId(fileId);
	}

	public List<MissionWrap> getDependentObj()
	{
		return null;
	}
	public static List<MissionWrap> convert(List<Mission> list)
	{
		List<MissionWrap> results = new ArrayList<MissionWrap>();
		for(Mission m:list)
		{
			results.add(new MissionWrap(m));
		}
		return results;
	}

	@Override
	public String toString() {
		return "MissionWrap [mission=" + mission + ", partitionList="
				+ partitionList + ", task=" + task + ", taskReport="
				+ taskReport + "]";
	}

	public String toFormatString()
	{
		return mission.toFormatString();
	}

}
