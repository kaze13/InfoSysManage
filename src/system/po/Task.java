package system.po;

import java.io.Serializable;
import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;
import system.util.ToolBox;


@Entity("rd4_task")
public class Task implements Printable, Serializable{
	@Id("id")
	private String id;
	@Column("creator_name")
	private String creatorName;
	@Column("owner_name")
	private String ownerName;
	@Column("type")
	private Type type;
	@Column("title")
	private String title;
	@Column("description")
	private String description;
	@Column("start_time")
	private long startTime;
	@Column("end_time")
	private long endTime;
	@Column("statue")
	private StatueType statue;
	@Column("data")
	private String data;
	@Column("file_id")
	private String fileid;

	public enum Type {
		MISSION, MISSION_PARTITION, MISSION_UNIT, TODO, ASSIGNMENT, FIX_BUG, SHUTDOWN_SYSTEM
	}

	public enum StatueType {
		NOT_COMPLETE, COMPLETED, UNACCEPTED, WAITING, SUBMITTED, REJECTED,NOT_DELIVERED,SUBMISSION_REJECTED
		,ABANDONED
	}
	public Task() {
		super();
		this.id = UUID.randomUUID().toString();
		this.data = "";
		this.fileid = "none";
		// TODO Auto-generated constructor stub
	}

	public Task(Task task)
	{
		this.id = task.getId();
		this.creatorName = task.getCreatorName();
		this.ownerName = task.getOwnerName();
		this.type = task.getType();
		this.title = task.getTitle();
		this.description = task.getDescription();
		this.startTime = task.getStartTime();
		this.endTime = task.getEndTime();
		this.statue = task.getStatue();
		this.data = task.getData();
		this.fileid = task.getFileid();
	}

	public Task(String creatorName, String ownerName, Type type,
			String title, String description, long startTime, long endTime,
			String data, String fileid) {
		super();
		this.id = UUID.randomUUID().toString();
		this.creatorName = creatorName;
		this.ownerName = ownerName;
		this.type = type;
		this.title = title;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.statue = StatueType.UNACCEPTED;
		this.data = data;
		this.fileid = fileid;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public StatueType getStatue() {
		return statue;
	}

	public void setStatue(StatueType statue) {
		this.statue = statue;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}


	public long getStartTime() {
		return startTime;
	}


	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}


	public long getEndTime() {
		return endTime;
	}


	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}


	public String getFileid() {
		return fileid;
	}


	public void setFileid(String fileid) {
		this.fileid = fileid;
	}


	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}


	@Override
	public Task clone() throws CloneNotSupportedException {
		return new Task(this);
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", creatorName=" + creatorName
				+ ", ownerName=" + ownerName + ", type=" + type + ", title="
				+ title + ", description=" + description + ", startTime="
				+ startTime + ", endTime=" + endTime + ", statue=" + statue
				+ ", data=" + data + ", fileid=" + fileid + "]";
	}

	public String toSignatureString()
	{
		return title + "-----" + description;
	}
	public String toFormatString()
	{
		return
		"\n*TASK*\n" +
		"Creator name: " + creatorName + "\n" +
		"Owner name: " + ownerName + "\n" +
		"Type: " + type + "\n" +
		"Title: " + title + "\n" +
		"Description: " + description + "\n" +
		"Start time: "+ ToolBox.convertTime(startTime) + "\n" +
		"End time: " + ToolBox.convertTime(endTime) + "\n" +
		"Statue: " + statue + "\n";
	}

}
