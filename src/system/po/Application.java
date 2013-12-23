package system.po;

import java.util.Date;
import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_application")
public class Application implements Printable{

	@Id("id")
	private String id;
	@Column("sender_name")
	private String senderName;
	@Column("reciever_name")
	private String recieverName;
	@Column("reciever_role")
	private String recieverRole;
	@Column("type")
	private Type type;
	@Column("title")
	private String title;
	@Column("body")
	private String body;
	@Column("data")
	private String data;
	@Column("time")
	private Long time;
	@Column("file_id")
	private String fileId;
	@Column("statue")
	private StatueType statue;
	@Column("class")
	private String clazz;
	@Column("stage")
	private int stage;
	public enum Type {
		PROMOTE_AUTHORITY, VERIFY_MISSION, REPORT_ERROR, ABANDON_TASK,
		TRANSFER_TASK_BOSS, TRANSFER_TASK_TARGET, REJECT_TASK, CHANGE_SCHEDULE
		,SHUTDOWN_SYSTEM, CREATE_GUEST, ASK_FOR_LEAVE, NEW_EMPLOYEE, PROMOTE_MAIN_AUTHORITY
	}

	public enum StatueType {
		NOT_DEALED, ACCEPTED, REJECTED
	}

	public Application() {
		super();
		this.id = UUID.randomUUID().toString();
		this.fileId = "none";
		this.recieverName = "none";
		this.recieverRole = "none";

	}

	public Application(Application application) {
		super();
		this.id = application.getId();
		this.senderName = application.getSenderName();
		this.recieverName = application.getRecieverName();
		this.recieverRole = application.getRecieverRole();
		this.type = application.getType();
		this.title = application.getTitle();
		this.body = application.getBody();
		this.data = application.getData();
		this.time = application.getTime();
		this.fileId = application.getFileId();
		this.statue = application.getStatue();
		this.clazz = application.getClazz();
		this.stage = application.getStage();
	}

	public Application(String senderName, String recieverName, Type type,
			String title, String body, String data, Long time, String fileId, int stage) {
		super();
		this.id = UUID.randomUUID().toString();
		this.senderName = senderName;
		this.recieverName = recieverName;
		this.type = type;
		this.title = title;
		this.body = body;
		this.data = data;
		this.time = time;
		if(fileId == null)
			this.fileId = "none";
		this.fileId = fileId;
		this.statue = StatueType.NOT_DEALED;
		this.clazz = "none";
		this.stage = stage;
		this.recieverRole = "none";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
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

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
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


	public String getRecieverRole() {
		return recieverRole;
	}

	public void setRecieverRole(String recieverRole) {
		this.recieverRole = recieverRole;
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



	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	@Override
	public Application clone() throws CloneNotSupportedException {
		return new Application(this);
	}




	@Override
	public String toString() {
		return "Application [id=" + id + ", senderName=" + senderName
				+ ", recieverName=" + recieverName + ", recieverRole="
				+ recieverRole + ", type=" + type + ", title=" + title
				+ ", body=" + body + ", data=" + data + ", time=" + time
				+ ", fileId=" + fileId + ", statue=" + statue + ", clazz="
				+ clazz + ", stage=" + stage + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((recieverName == null) ? 0 : recieverName.hashCode());
		result = prime * result
				+ ((recieverRole == null) ? 0 : recieverRole.hashCode());
		result = prime * result
				+ ((senderName == null) ? 0 : senderName.hashCode());
		result = prime * result + stage;
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
		Application other = (Application) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
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
		if (recieverName == null) {
			if (other.recieverName != null)
				return false;
		} else if (!recieverName.equals(other.recieverName))
			return false;
		if (recieverRole == null) {
			if (other.recieverRole != null)
				return false;
		} else if (!recieverRole.equals(other.recieverRole))
			return false;
		if (senderName == null) {
			if (other.senderName != null)
				return false;
		} else if (!senderName.equals(other.senderName))
			return false;
		if (stage != other.stage)
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
		// TODO Auto-generated method stub
		return
		"\n*Application*\n" +
		"Sender name: " + senderName + "\n" +
		"Reciever name: " + recieverName + "\n" +
		"Type: " + type + "\n" +
		"Title: " + title + "\n" +
		"Body: " + body + "\n" +
		"Time: " + (new Date(time)).toGMTString() + "\n" +
		"Statue: " + statue + "\n";
	}

	public String toSignatureString()
	{
		return title + "-----" + body;
	}
}
