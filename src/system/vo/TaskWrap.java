package system.vo;

import java.util.ArrayList;
import java.util.List;

import system.po.Task;
import system.po.Task.StatueType;
import system.po.Task.Type;

public class TaskWrap implements Comparable<TaskWrap>{

	private Task task;
	private Object taskObject;


	public TaskWrap(Task task) {
		super();
		this.task = task;
	}


	public TaskWrap() {
		super();
		this.task = new Task();
	}


	public TaskWrap(Task task, Object taskObject) {
		super();
		this.task = task;
		this.taskObject = taskObject;
	}

	public String getId() {
		return task.getId();
	}

	public void setId(String id) {
		task.setId(id);
	}

	public String getOwnerName() {
		return task.getOwnerName();
	}

	public void setOwnerName(String ownerName) {
		task.setOwnerName(ownerName);
	}

	public String getTitle() {
		return task.getTitle();
	}

	public void setTitle(String title) {
		task.setTitle(title);
	}

	public String getDescription() {
		return task.getDescription();
	}

	public void setDescription(String description) {
		task.setDescription(description);
	}

	public Type getType() {
		return task.getType();
	}

	public void setType(Type type) {
		task.setType(type);
	}

	public StatueType getStatue() {
		return task.getStatue();
	}

	public void setStatue(StatueType statue) {
		task.setStatue(statue);
	}

	public Long getStartTime() {
		return task.getStartTime();
	}

	public void setStartTime(Long startTime) {
		task.setStartTime(startTime);
	}

	public Long getEndTime() {
		return task.getEndTime();
	}

	public void setEndTime(Long endTime) {
		task.setEndTime(endTime);
	}

	public String getFileid() {
		return task.getFileid();
	}

	public void setFileid(String fileid) {
		task.setFileid(fileid);
	}

	public String getData() {
		return task.getData();
	}

	public void setData(String data) {
		task.setData(data);
	}

	public String getCreatorName() {
		return task.getCreatorName();
	}

	public void setCreatorName(String creatorName) {
		task.setCreatorName(creatorName);
	}

	public Object getTaskObject() {
		return taskObject;
	}

	public void setTaskObject(Object taskObject) {
		this.taskObject = taskObject;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public static List<TaskWrap> convert(List<Task> list)
	{
		List<TaskWrap> results = new ArrayList<TaskWrap>();
		for(Task t:list)
		{
			results.add(new TaskWrap(t));
		}

		return results;
	}


	@Override
	public int compareTo(TaskWrap o) {
		return o.getStartTime().compareTo(this.getStartTime());
	}

}
