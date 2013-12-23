package system.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import system.po.Mission;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.SystemFunction;
import system.po.Task;
import system.po.System;
import system.po.TaskReport;
import system.po.User;
import system.po.spec.Downloadable;

public class UnitWrap implements Downloadable, Serializable{

	private Task task;
	private MissionUnit unit;
	private MissionPartition belongedPartition;
	private Mission belongedMission;
	private List<System> dependentSystems;
	private List<UnitWrap> dependentObj;
	private TaskReport taskReport;
	private UserWrap leader;
	private List<SystemFunction> dependentFunctions;

	public UnitWrap()
	{
		super();
		unit = new MissionUnit();
	}
	public UnitWrap(MissionUnit unit)
	{
		this.unit = unit;
	}
	public UnitWrap(Task task, MissionUnit unit,
			MissionPartition belongedPartition, Mission belongedMission,
			List<System> dependentSystems, List<UnitWrap> dependentObj, TaskReport taskReport) {
		super();
		this.task = task;
		this.unit = unit;
		this.belongedPartition = belongedPartition;
		this.belongedMission = belongedMission;
		this.dependentSystems = dependentSystems;
		this.dependentObj = dependentObj;
		this.taskReport = taskReport;
	}


	public UserWrap getLeader() {
		return leader;
	}
	public void setLeader(UserWrap leader) {
		this.leader = leader;
	}
	public List<SystemFunction> getDependentFunctions() {
		return dependentFunctions;
	}
	public void setDependentFunctions(List<SystemFunction> dependentFunctions) {
		this.dependentFunctions = dependentFunctions;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public MissionUnit getUnit() {
		return unit;
	}
	public void setUnit(MissionUnit unit) {
		this.unit = unit;
	}
	public MissionPartition getBelongedPartition() {
		return belongedPartition;
	}
	public void setBelongedPartition(MissionPartition belongedPartition) {
		this.belongedPartition = belongedPartition;
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


	public List<UnitWrap> getDependentObj() {
		return dependentObj;
	}
	public void setDependentObj(List<UnitWrap> dependentObj) {
		this.dependentObj = dependentObj;
	}
	public int getProgress() {
		return unit.getProgress();
	}
	public void setProgress(int progress) {
		unit.setProgress(progress);
	}
	public String getId() {
		return unit.getId();
	}
	public void setId(String id) {
		unit.setId(id);
	}
	public String getLeaderName() {
		return unit.getLeaderName();
	}
	public void setLeaderName(String leaderName) {
		unit.setLeaderName(leaderName);
	}
	public String getTitle() {
		return unit.getTitle();
	}
	public void setTitle(String title) {
		unit.setTitle(title);
	}
	public String getDescription() {
		return unit.getDescription();
	}
	public void setDescription(String description) {
		unit.setDescription(description);
	}
	public String getPartitionid() {
		return unit.getPartitionid();
	}
	public void setPartitionid(String partitionid) {
		unit.setPartitionid(partitionid);
	}
	public TaskReport getTaskReport() {
		return taskReport;
	}
	public void setTaskReport(TaskReport taskReport) {
		this.taskReport = taskReport;
	}

	public String getFileId() {
		return unit.getFileId();
	}
	public void setFileId(String fileId) {
		unit.setFileId(fileId);
	}
	public List<UnitWrap> getUnitList()
	{
		return null;
	}
	public List<PartitionWrap> getPartitionList()
	{
		return null;
	}
	public static List<UnitWrap> convert(List<MissionUnit> list)
	{
		List<UnitWrap> results = new ArrayList<UnitWrap>();
		for(MissionUnit mu:list)
		{
			results.add(new UnitWrap(mu));
		}

		return results;
	}
	@Override
	public String toString() {
		return unit.toString();
	}

	public String toFormatString()
	{
		return unit.toFormatString();
	}



}
