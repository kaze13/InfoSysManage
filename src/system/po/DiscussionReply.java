package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_discussion_reply")
public class DiscussionReply implements Printable {

	@Id("id")
	private String id;
	@Column("belonged_subject_id")
	private String belongedSubjectId;
	@Column("author_name")
	private String authorName;
	@Column("time")
	private long time;
	@Column("content")
	private String content;
	@Column("file_id")
	private String fileId;

	public DiscussionReply(String belongedSubjectId, String authorName,
			long time, String content, String fileId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.belongedSubjectId = belongedSubjectId;
		this.authorName = authorName;
		this.time = time;
		this.content = content;
		this.fileId = fileId;
	}

	public DiscussionReply(DiscussionReply discussionReply) {
		super();
		this.id = discussionReply.id;
		this.belongedSubjectId = discussionReply.belongedSubjectId;
		this.authorName = discussionReply.authorName;
		this.time = discussionReply.time;
		this.content = discussionReply.content;
		this.fileId = discussionReply.fileId;
	}

	public DiscussionReply() {
		super();
		this.id = UUID.randomUUID().toString();
		this.fileId = "none";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBelongedSubjectId() {
		return belongedSubjectId;
	}

	public void setBelongedSubjectId(String belongedSubjectId) {
		this.belongedSubjectId = belongedSubjectId;
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
	public DiscussionReply clone() throws CloneNotSupportedException {
		return new DiscussionReply(this);
	}

	@Override
	public String toString() {
		return "DiscussionReply [id=" + id + ", belongedSubjectId="
				+ belongedSubjectId + ", authorName=" + authorName + ", time="
				+ time + ", content=" + content + ", fileId=" + fileId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authorName == null) ? 0 : authorName.hashCode());
		result = prime
				* result
				+ ((belongedSubjectId == null) ? 0 : belongedSubjectId
						.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
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
		DiscussionReply other = (DiscussionReply) obj;
		if (authorName == null) {
			if (other.authorName != null)
				return false;
		} else if (!authorName.equals(other.authorName))
			return false;
		if (belongedSubjectId == null) {
			if (other.belongedSubjectId != null)
				return false;
		} else if (!belongedSubjectId.equals(other.belongedSubjectId))
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
		return true;
	}

	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return null;
	}

}
