package system.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import system.po.Mission;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.System;
import system.po.SystemFunction;
import system.po.Task;
import system.po.TaskReport;
import system.po.User;
import system.po.spec.Downloadable;

public class PartitionWrap implements Downloadable, Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 2997010981036707830L;
	private MissionPartition partition;
	private List<UnitWrap> unitList;
	private Task task;
	private Mission belongedMission;
	private List<System> dependentSystems;
	private List<SystemFunction> dependentFunctions;
	private UserWrap leader;
	private List<PartitionWrap> dependentObj;
	private TaskReport taskReport;



	public PartitionWrap() {
		super();
		partition = new MissionPartition();
	}
	public PartitionWrap(MissionPartition partition) {
		super();
		this.partition = partition;
	}
	public MissionPartition getPartition() {
		return partition;
	}
	public void setPartition(MissionPartition partition) {
		this.partition = partition;
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
	public List<UnitWrap> getUnitList() {
		return unitList;
	}

	public Task getTask() {
		return task;
	}
	public void setUnitList(List<UnitWrap> unitList) {
		this.unitList = unitList;
	}
	public void setLeader(UserWrap leader) {
		this.leader = leader;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Mission getBelongedMission() {
		return belongedMission;
	}
	public void setBelongedMission(Mission belongedMission) {
		this.belongedMission = belongedMission;
	}
	public List<System> getDependentSystems() {
		return dependentSystems;
	}
	public void setDependentSystems(List<System> dependentSystems) {
		this.dependentSystems = dependentSystems;
	}

	public List<PartitionWrap> getDependentObj() {
		return dependentObj;
	}
	public void setDependentObj(List<PartitionWrap> dependentObj) {
		this.dependentObj = dependentObj;
	}
	public TaskReport getTaskReport() {
		return taskReport;
	}
	public void setTaskReport(TaskReport taskReport) {
		this.taskReport = taskReport;
	}


	public int getProgress() {
		return partition.getProgress();
	}
	public void setProgress(int progress) {
		partition.setProgress(progress);
	}
	public String getId() {
		return partition.getId();
	}
	public void setId(String id) {
		partition.setId(id);
	}
	public String getLeaderName() {
		return partition.getLeaderName();
	}
	public void setLeaderName(String leaderName) {
		partition.setLeaderName(leaderName);
	}
	public String getTitle() {
		return partition.getTitle();
	}
	public void setTitle(String title) {
		partition.setTitle(title);
	}
	public String getDescription() {
		return partition.getDescription();
	}
	public void setDescription(String description) {
		partition.setDescription(description);
	}
	public String getMissionid() {
		return partition.getMissionid();
	}
	public void setMissionid(String missionid) {
		partition.setMissionid(missionid);
	}

	public String getFileId() {
		return partition.getFileId();
	}
	public void setFileId(String fileId) {
		partition.setFileId(fileId);
	}
	public static List<PartitionWrap> convert(List<MissionPartition> list)
	{
		List<PartitionWrap> results = new ArrayList<PartitionWrap>();
		for(MissionPartition mp:list)
		{
			results.add(new PartitionWrap(mp));
		}

		return results;
	}

	public List<PartitionWrap> getPartitionList()
	{
		return null;
	}

	public static PartitionWrap convert(MissionPartition partition)
	{
		return new PartitionWrap(partition);
	}
	@Override
	public String toString() {
		return "PartitionWarp [partition=" + partition + ", unitList="
				+ unitList + ", task=" + task + ", belongedMission="
				+ belongedMission + ", dependentSystems=" + dependentSystems
				+ ", dependentUnit=" + dependentObj + ", taskReport="
				+ taskReport + "]";
	}

	public String toFormatString()
	{
		return partition.toFormatString();
	}

}
