package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_mission_dependency")
public class MissionDependency implements Printable{


	public MissionDependency() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MissionDependency(MissionDependency missionDependency) {
		this.id = missionDependency.getId();
		this.preMissionId = missionDependency.getPreMissionId();
		this.postMissionId = missionDependency.getPostMissionId();
	}

	public MissionDependency(String preMissionId,
			String postMissionId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.preMissionId = preMissionId;
		this.postMissionId = postMissionId;
	}


	@Id("id")
	private String id;
	@Column("pre_mission_id")
	private String preMissionId;
	@Column("post_mission_id")
	private String postMissionId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPreMissionId() {
		return preMissionId;
	}

	public void setPreMissionId(String preMissionId) {
		this.preMissionId = preMissionId;
	}

	public String getPostMissionId() {
		return postMissionId;
	}

	public void setPostMissionId(String postMissionId) {
		this.postMissionId = postMissionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((postMissionId == null) ? 0 : postMissionId.hashCode());
		result = prime * result
				+ ((preMissionId == null) ? 0 : preMissionId.hashCode());
		return result;
	}



	@Override
	public MissionDependency clone() throws CloneNotSupportedException {
		return new MissionDependency(this);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MissionDependency other = (MissionDependency) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (postMissionId == null) {
			if (other.postMissionId != null)
				return false;
		} else if (!postMissionId.equals(other.postMissionId))
			return false;
		if (preMissionId == null) {
			if (other.preMissionId != null)
				return false;
		} else if (!preMissionId.equals(other.preMissionId))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "MissionDependency [id=" + id + ", preMissionId=" + preMissionId
				+ ", postMissionId=" + postMissionId + "]";
	}

	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return null;
	}


}
