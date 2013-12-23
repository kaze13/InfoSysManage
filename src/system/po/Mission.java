package system.po;

import java.io.Serializable;
import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;
import system.util.ToolBox;

@Entity("rd4_mission")
public class Mission implements Printable,Serializable{

	@Id("id")
	private String id;
	@Column("creator_name")
	private String creatorName;
	@Column("leader_name")
	private String leaderName;
	@Column("title")
	private String title;
	@Column("description")
	private String description;
	@Column("file_id")
	private String fileId;
	@Column("progress")
	private int progress;

	public Mission() {
		super();
		this.id = UUID.randomUUID().toString();
		this.progress = -1;
	}

	public Mission(Mission mission) {
		super();
		this.creatorName = mission.getCreatorName();
		this.id = mission.getId();
		this.leaderName = mission.getLeaderName();
		this.title = mission.getTitle();
		this.description = mission.getDescription();
		this.progress = mission.getProgress();
		this.fileId = mission.getFileId();
	}

	public Mission(String creatorName, String leaderName, String title,
			String description, String fileId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.creatorName = creatorName;
		this.leaderName = leaderName;
		this.title = title;
		this.description = description;
		this.progress = -1;
		this.fileId = fileId;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
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

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	@Override
	public Mission clone() throws CloneNotSupportedException {
		return new Mission(this);
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creatorName == null) ? 0 : creatorName.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((leaderName == null) ? 0 : leaderName.hashCode());
		result = prime * result + progress;
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
		Mission other = (Mission) obj;
		if (creatorName == null) {
			if (other.creatorName != null)
				return false;
		} else if (!creatorName.equals(other.creatorName))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
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
		if (leaderName == null) {
			if (other.leaderName != null)
				return false;
		} else if (!leaderName.equals(other.leaderName))
			return false;
		if (progress != other.progress)
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
		return "Mission [id=" + id + ", creatorName=" + creatorName
				+ ", leaderName=" + leaderName + ", title=" + title
				+ ", description=" + description + ", fileId=" + fileId
				+ ", progress=" + progress + "]";
	}

	@Override
	public String toFormatString() {
		return
		"\n*MISSION*\n" +
		"Creator name: " + creatorName + "\n" +
		"Leader name: " + leaderName + "\n" +
		"Title: " + title + "\n" +
		"Description: " + description + "\n" +
		"Progress: " + ToolBox.progressStr(progress) + "\n";
	}

}
