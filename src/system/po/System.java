package system.po;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_system")
public class System implements Printable, Serializable{

	@Id("system_name")
	@NotNull
	private String name;
	@Column("url")
	private String url;
	@Column("description")
	private String description;
	@Column("war_path")
	private String warPath;
	@Column("duration")
	private String duration; // 99:99-99:99

	@Column("status")
	private Status status = Status.OFF;

	public enum Status{
		 OFF,ON
	}
	public System(System system) {
		this.name = system.getName();
		this.url = system.getUrl();
		this.description = system.getDescription();
		this.warPath = system.getWarPath();
		this.duration = system.getDuration();
		this.status = system.getStatus();
	}



	public System() {
		super();
		this.name = " ";
		this.url = " ";
		this.description = " ";
		this.warPath = " ";
		this.duration = " ";
		status=Status.OFF;
	}

	public System(String name, String url, String description, String warPath,
			String duration, Status status) {
		super();
		this.name = name;
		this.url = url;
		this.description = description;
		this.warPath = warPath;
		this.duration = duration;
		this.status = status;
	}

	public Date getStartTime() {
		String startStr = duration.split("\\-")[0];
		int hour = Integer.valueOf(startStr.split("\\:")[0]);
		int minute = Integer.valueOf(startStr.split("\\:")[1]);
		Date result = new Date(1900, 12, 21, hour, minute, 0);
		return result;
	}

	public Date getStopTime() {
		String stopStr = duration.split("\\-")[1];
		int hour = Integer.valueOf(stopStr.split("\\:")[0]);
		int minute = Integer.valueOf(stopStr.split("\\:")[1]);
		Date result = new Date(1900, 12, 21, hour, minute, 0);
		return result;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWarPath() {
		return warPath;
	}

	public void setWarPath(String warPath) {
		this.warPath = warPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}



	@Override
	public System clone() throws CloneNotSupportedException {
		return new System(this);
	}

	@Override
	public boolean equals(Object obj) {
		System right = (System)obj;
		return this.name.equals(right.getName());
	}

	@Override
	public String toString() {
		return "System [name=" + name + ", url=" + url + ", description="
				+ description + ", warPath=" + warPath + ", duration="
				+ duration + ", status=" + status + "]";
	}

	@Override
	public String toFormatString() {
		return
		"\n*SYSTEM*\n" +
		"Name: " + name + "\n" +
		"Url: " + url + "\n" +
		"Description: " + description + "\n" +
		"Duration: " + duration + "\n" +
		"Status: " + status + "\n";
	}

}
