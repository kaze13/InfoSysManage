package system.vo;

import system.po.Task;
import system.po.Task.StatueType;
import system.po.Task.Type;

public class TaskVo {

	private Task task;
	private Object taskObject;


	public TaskVo() {
		super();
		task = new Task();
	}

	public TaskVo(Task task, Object taskObject) {
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

	public long getStartTime() {
		return task.getStartTime();
	}

	public void setStartTime(long startTime) {
		task.setStartTime(startTime);
	}

	public long getEndTime() {
		return task.getEndTime();
	}

	public void setEndTime(long endTime) {
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

}
