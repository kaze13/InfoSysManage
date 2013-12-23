package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_notification")
public class Notification implements Printable{

	@Id("id")
	private String id;
	@Column("reciever_name")
	private String recieverName;
	@Column("type")
	private Type type;
	@Column("title")
	private String title;
	@Column("body")
	private String body;
	@Column("time")
	private Long time;
	@Column("file_id")
	private String fileId;
	@Column("statue")
	private StatueType statue;
	@Column("link")
	private String link;
	public enum Type {
		NORMAL, NEW_MISSION, NEW_PARTITION, NEW_TASK,ACCEPT_TASK, REJECT_TASK,
		SUBMIT_TASK, ACCEPT_ABANDON_TASK, ACCEPT_MISSION, REJECT_MISSION, REJECT_PARTITION, REJECT_UNIT,
		REJECT_REJECT_TASK, REJECT_SUBMITTED_UNIT, ACCEPT_SUBMITTED_UNIT, ACCEPT_AUTHORITY_PROMOTION,
		ACCEPT_FIX_BUG, REJECT_FIX_BUG, BUG_FIXED, TRANSFER_TASK, SCHEDULE_CHANGED, SHUTDOWN_SYSTEM
		,CREATE_GUEST,REJECT_TRANSFER_TASK, SUBMIT_PARTITION, REJECT_CHANGE_SCHEDULE,NEW_EMPLOYEE
	}

	public enum StatueType {
		UNREAD, READ
	}
	public Notification(Notification notification) {
		super();
		this.id = notification.getId();
		this.recieverName = notification.getRecieverName();
		this.type = notification.getType();
		this.title = notification.getTitle();
		this.body = notification.getBody();
		this.time = notification.getTime();
		this.fileId = notification.getFileId();
		this.statue = notification.getStatue();
		this.link = notification.link;
	}

	public Notification(String recieverName, Type type, String title,
			String body, Long time, String fileId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.recieverName = recieverName;
		this.type = type;
		this.title = title;
		this.body = body;
		this.time = time;
		this.fileId = fileId;
		this.statue = StatueType.UNREAD;
		this.link = "none";
	}

	public Notification() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRecieverName() {
		return recieverName;
	}

	public void setRecieverName(String recieverName) {
		this.recieverName = recieverName;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}


	public StatueType getStatue() {
		return statue;
	}

	public void setStatue(StatueType statue) {
		this.statue = statue;
	}


	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public Notification clone() throws CloneNotSupportedException {
		return new Notification(this);
	}



	@Override
	public String toString() {
		return "Notification [id=" + id + ", recieverName=" + recieverName
				+ ", type=" + type + ", title=" + title + ", body=" + body
				+ ", time=" + time + ", fileId=" + fileId + ", statue="
				+ statue + ", link=" + link + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result
				+ ((recieverName == null) ? 0 : recieverName.hashCode());
		result = prime * result + ((statue == null) ? 0 : statue.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Notification other = (Notification) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
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
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (recieverName == null) {
			if (other.recieverName != null)
				return false;
		} else if (!recieverName.equals(other.recieverName))
			return false;
		if (statue != other.statue)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toFormatString() {
		return
		"\n*NOTIFICATION*\n" +
		"Reciever name: " + recieverName + "\n" +
		"Title: " + title + "\n" +
		"Body: " + body + "\n" +
		"Time: " + time + "\n" +
		"Statue: " + statue + "\n";
	}

	public String toSignatureString()
	{
		return title + "-----" + body;
	}
}
