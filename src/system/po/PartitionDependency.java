package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_partition_dependency")
public class PartitionDependency implements Printable{

	@Id("id")
	private String id;
	@Column("belonged_mission_id")
	private String belongedMissionId;
	@Column("pre_partition_id")
	private String prePartitionId;
	@Column("post_partition_id")
	private String postPartitionId;

	public PartitionDependency() {
		super();
		this.id = UUID.randomUUID().toString();
	}

	public PartitionDependency(PartitionDependency dependency)
	{
		this.id = dependency.getId();
		this.belongedMissionId = dependency.getBelongedMissionId();
		this.prePartitionId = dependency.getPrePartitionId();
		this.postPartitionId = dependency.getPostPartitionId();
	}

	public PartitionDependency(String belongedMissionId, String preMissionId,
			String postMissionId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.belongedMissionId = belongedMissionId;
		this.prePartitionId = preMissionId;
		this.postPartitionId = postMissionId;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}



	public String getPrePartitionId() {
		return prePartitionId;
	}


	public void setPrePartitionId(String prePartitionId) {
		this.prePartitionId = prePartitionId;
	}


	public String getPostPartitionId() {
		return postPartitionId;
	}


	public void setPostPartitionId(String postPartitionId) {
		this.postPartitionId = postPartitionId;
	}


	public String getBelongedMissionId() {
		return belongedMissionId;
	}

	public void setBelongedMissionId(String belongedMissionId) {
		this.belongedMissionId = belongedMissionId;
	}


	@Override
	public PartitionDependency clone() throws CloneNotSupportedException {
		return new PartitionDependency(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((belongedMissionId == null) ? 0 : belongedMissionId
						.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((postPartitionId == null) ? 0 : postPartitionId.hashCode());
		result = prime * result
				+ ((prePartitionId == null) ? 0 : prePartitionId.hashCode());
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
		PartitionDependency other = (PartitionDependency) obj;
		if (belongedMissionId == null) {
			if (other.belongedMissionId != null)
				return false;
		} else if (!belongedMissionId.equals(other.belongedMissionId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (postPartitionId == null) {
			if (other.postPartitionId != null)
				return false;
		} else if (!postPartitionId.equals(other.postPartitionId))
			return false;
		if (prePartitionId == null) {
			if (other.prePartitionId != null)
				return false;
		} else if (!prePartitionId.equals(other.prePartitionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PartitionDependency [id=" + id + ", belongedMissionId="
				+ belongedMissionId + ", prePartitionId=" + prePartitionId
				+ ", postPartitionId=" + postPartitionId + "]";
	}

	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return null;
	}




}
