package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_message")
public class Message implements Printable, Cloneable, Comparable<Message>{

	@Id("id")
	private String id;
	@Column("sender_name")
	private String senderName;
	@Column("reciever_name")
	private String recieverName;
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

	public enum StatueType {
		NOT_READ, READ
	}

	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Message(Message message) {
		super();
		this.id = message.getId();
		this.senderName = message.getSenderName();
		this.recieverName = message.getRecieverName();

		this.title = message.getTitle();
		this.body = message.getBody();
		this.time = message.getTime();
		this.fileId = message.getFileId();
		this.statue = message.getStatue();
	}

	public Message(String senderName, String recieverName, String title,
			String body, Long time, String fileId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.senderName = senderName;
		this.recieverName = recieverName;

		this.title = title;
		this.body = body;
		this.time = time;
		this.fileId = fileId;
		this.statue = StatueType.NOT_READ;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
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

	public StatueType getStatue() {
		return statue;
	}

	public void setStatue(StatueType statue) {
		this.statue = statue;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}



	@Override
	public Message clone() throws CloneNotSupportedException {
		return new Message(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((recieverName == null) ? 0 : recieverName.hashCode());
		result = prime * result
				+ ((senderName == null) ? 0 : senderName.hashCode());
		result = prime * result + ((statue == null) ? 0 : statue.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Message other = (Message) obj;
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
		if (recieverName == null) {
			if (other.recieverName != null)
				return false;
		} else if (!recieverName.equals(other.recieverName))
			return false;
		if (senderName == null) {
			if (other.senderName != null)
				return false;
		} else if (!senderName.equals(other.senderName))
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
		return true;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", senderName=" + senderName
				+ ", recieverName=" + recieverName + ", title=" + title
				+ ", body=" + body + ", time=" + time + ", fileId=" + fileId
				+ ", statue=" + statue + "]";
	}

	public String toSignatureString()
	{
		return title + "----" + body;
	}
	@Override
	public String toFormatString() {
		return
		"\n*MESSAGE*\n" +
		"Sender name: " + senderName + "\n" +
		"Reciever name: " + recieverName + "\n" +
		"Title: " + title + "\n" +
		"Body: " + body + "\n" +
		"Time: " + time + "\n" +
		"Statue: " + statue + "\n";
	}

	@Override
	public int compareTo(Message o) {
		return o.getTime().compareTo(this.getTime());
	}

}
