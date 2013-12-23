package system.po;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;
import system.util.ToolBox;

@Entity("rd4_mission_unit")
public class MissionUnit implements Printable , Serializable{

	@Id("id")
	private String id;

	@Column("partition_id")
	private String partitionid;
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

	// public static final int NOT_DELIVERED = -1;
	// public static final int WAITING = -2;
	// public static final int REJECTED = -3;
	// public static final int SUBMITTED = -4;
	// completed=-5

	public MissionUnit(MissionUnit missionUnit) {
		this.id = missionUnit.getId();
		this.partitionid = missionUnit.getPartitionid();
		this.leaderName = missionUnit.getLeaderName();
		this.title = missionUnit.getTitle();
		this.description = missionUnit.getDescription();
		this.progress = missionUnit.getProgress();
		this.fileId = missionUnit.getFileId();
	}

	public MissionUnit() {
		super();
		this.id = UUID.randomUUID().toString();
		this.progress = -1;
	}

	public MissionUnit(String partitionid, String leaderName, String title,
			String description, String fileId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.partitionid = partitionid;
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

	public String getPartitionid() {
		return partitionid;
	}

	public void setPartitionid(String partitionid) {
		this.partitionid = partitionid;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@Override
	public MissionUnit clone() throws CloneNotSupportedException {
		return new MissionUnit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((leaderName == null) ? 0 : leaderName.hashCode());
		result = prime * result
				+ ((partitionid == null) ? 0 : partitionid.hashCode());
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
		MissionUnit other = (MissionUnit) obj;
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
		if (partitionid == null) {
			if (other.partitionid != null)
				return false;
		} else if (!partitionid.equals(other.partitionid))
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

	public String toString() {
		try {
			return "MissionUnit [id=" + id + ", partitionid=" + partitionid
					+ ", leaderName=" + leaderName + ", title=" + title
					+ ", description="
					+ URLEncoder.encode(description, "UTF-8") + ", fileId="
					+ fileId + ", progress=" + progress + "]";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return "\n*MISSION UNIT*\n" +
		"Leader name: " + leaderName + "\n" +
		"Title: " + title + "\n" +
		"Description: " + description + "\n" +
		"Progress: " + ToolBox.progressStr(progress) + "\n";
	}
}
