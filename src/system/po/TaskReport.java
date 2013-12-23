package system.po;

import java.io.Serializable;
import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;


@Entity("rd4_task_report")
public class TaskReport implements Printable, Serializable{

	@Id("id")
	private String id;
	@Column("task_id")
	private String taskId;
	@Column("report")
	private String report;
	@Column("file_id")
	private String fileId;


	public TaskReport(String taskId, String report, String fileId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.taskId = taskId;
		this.report = report;
		this.fileId = fileId;
	}
	public TaskReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TaskReport(TaskReport taskReport) {
		this.id = taskReport.getId();
		this.taskId = taskReport.getTaskId();
		this.report = taskReport.getReport();
		this.fileId = taskReport.getFileId();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}


	@Override
	public TaskReport clone() throws CloneNotSupportedException {
		return new TaskReport(this);
	}
	@Override
	public String toString() {
		return "TaskReport [id=" + id + ", taskId=" + taskId + ", report="
				+ report + ", fileId=" + fileId + "]";
	}
	@Override
	public String toFormatString() {
		return
		"\n*TASK REPORT*\n" +
		"Report: " + report + "\n" ;
	}
}
