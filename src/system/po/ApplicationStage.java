package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Downloadable;
import system.po.spec.Printable;

@Entity("rd4_application_stage")
public class ApplicationStage implements Printable, Downloadable{

	@Id("id")
	private String id;
	@Column("name")
	private String name;
	@Column("serial")
	private int serial;
	@Column("application_class_id")
	private String applicationClazzId;
	@Column("dealer_name")
	private String dealerName;
	@Column("comments")
	private String comments;
	@Column("status")
	private Status status;
	@Column("file_id")
	private String fileId;

	public enum Status{
		WAIT, PASS, FAIL
	}

	public ApplicationStage() {
		super();
		this.id = UUID.randomUUID().toString();
	}
	public ApplicationStage(ApplicationStage applicationStage) {
		super();
		this.id = applicationStage.getId();
		this.serial = applicationStage.getSerial();
		this.name = applicationStage.getName();
		this.applicationClazzId = applicationStage.getApplicationClazzId();
		this.dealerName = applicationStage.getDealerName();
		this.comments = applicationStage.getComments();
		this.status = applicationStage.getStatus();
		this.fileId = applicationStage.getFileId();
	}
	public ApplicationStage(String name, int serial, String applicationId, String dealerName,
			String comment, String fileId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.serial = serial;
		this.applicationClazzId = applicationId;
		this.dealerName = dealerName;
		this.comments = comment;
		this.status = Status.PASS;
		this.fileId = fileId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getApplicationClazzId() {
		return applicationClazzId;
	}
	public void setApplicationClazzId(String applicationClazzId) {
		this.applicationClazzId = applicationClazzId;
	}
	public int getSerial() {
		return serial;
	}
	public void setSerial(int serial) {
		this.serial = serial;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	@Override
	public ApplicationStage clone() throws CloneNotSupportedException {
		return new ApplicationStage(this);
	}






	@Override
	public String toString() {
		return "ApplicationStage [id=" + id + ", name=" + name + ", serial="
				+ serial + ", applicationClazzId=" + applicationClazzId
				+ ", dealerName=" + dealerName + ", comment=" + comments
				+ ", status=" + status + ", fileId=" + fileId + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((applicationClazzId == null) ? 0 : applicationClazzId
						.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((dealerName == null) ? 0 : dealerName.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + serial;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicationStage other = (ApplicationStage) obj;
		if (applicationClazzId == null) {
			if (other.applicationClazzId != null)
				return false;
		} else if (!applicationClazzId.equals(other.applicationClazzId))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (dealerName == null) {
			if (other.dealerName != null)
				return false;
		} else if (!dealerName.equals(other.dealerName))
			return false;
		if (fileId == null) {
			if (other.fileId != null)
				return false;
		} else if (!fileId.equals(other.fileId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (serial != other.serial)
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return null;
	}


}
