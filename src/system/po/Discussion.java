package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_discussion")
public class Discussion implements Printable{

	@Id("id")
	private String id;
	@Column("belonged_object_id")
	private String belongedObjectId;
	@Column("author_name")
	private String authorName;
	@Column("time")
	private long time;
	@Column("title")
	private String title;
	@Column("content")
	private String content;
	@Column("file_id")
	private String fileId;



	public Discussion() {
		super();
		this.id = UUID.randomUUID().toString();
		this.fileId = "none";
	}

	public Discussion(Discussion discussion)
	{
		this.id = discussion.getId();
		this.belongedObjectId = discussion.belongedObjectId;
		this.authorName = discussion.authorName;
		this.time = discussion.time;
		this.title = discussion.title;
		this.content = discussion.content;
		this.fileId = discussion.fileId;
	}
	public Discussion(String belongedObjectId, String authorName,
			long time, String title, String content, String fileId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.belongedObjectId = belongedObjectId;
		this.authorName = authorName;
		this.time = time;
		this.title = title;
		this.content = content;
		this.fileId = fileId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBelongedObjectId() {
		return belongedObjectId;
	}
	public void setBelongedObjectId(String belongedObjectId) {
		this.belongedObjectId = belongedObjectId;
	}

	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Discussion clone() throws CloneNotSupportedException {
		return new Discussion(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authorName == null) ? 0 : authorName.hashCode());
		result = prime
				* result
				+ ((belongedObjectId == null) ? 0 : belongedObjectId.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
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
		Discussion other = (Discussion) obj;
		if (authorName == null) {
			if (other.authorName != null)
				return false;
		} else if (!authorName.equals(other.authorName))
			return false;
		if (belongedObjectId == null) {
			if (other.belongedObjectId != null)
				return false;
		} else if (!belongedObjectId.equals(other.belongedObjectId))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
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
		if (time != other.time)
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
		return "Discussion [id=" + id + ", belongedObjectId="
				+ belongedObjectId + ", authorName=" + authorName + ", time="
				+ time + ", title=" + title + ", content=" + content
				+ ", fileId=" + fileId + "]";
	}




}
