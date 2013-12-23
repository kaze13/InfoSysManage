package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_unit_dependency")
public class UnitDependency implements Printable{

	@Id("id")
	private String id;
	@Column("belonged_partition_id")
	private String belongedPartitionId;
	@Column("pre_unit_id")
	private String preUnitId;
	@Column("post_unit_id")
	private String postUnitId;

	public UnitDependency() {
		super();
		this.id = UUID.randomUUID().toString();
	}

	public UnitDependency(UnitDependency unitDependency) {
		this.id = unitDependency.getId();
		this.belongedPartitionId = unitDependency.getBelongedPartitionId();
		this.preUnitId = unitDependency.getPreUnitId();
		this.postUnitId = unitDependency.getPostUnitId();
	}


	public UnitDependency(String belongedPartitionId, String preMissionId,
			String postMissionId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.belongedPartitionId = belongedPartitionId;
		this.preUnitId = preMissionId;
		this.postUnitId = postMissionId;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}



	public String getPreUnitId() {
		return preUnitId;
	}


	public void setPreUnitId(String preUnitId) {
		this.preUnitId = preUnitId;
	}


	public String getPostUnitId() {
		return postUnitId;
	}


	public void setPostUnitId(String postUnitId) {
		this.postUnitId = postUnitId;
	}


	public String getBelongedPartitionId() {
		return belongedPartitionId;
	}


	public void setBelongedPartitionId(String belongedPartitionId) {
		this.belongedPartitionId = belongedPartitionId;
	}


	@Override
	public UnitDependency clone() throws CloneNotSupportedException {
		return new UnitDependency(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((belongedPartitionId == null) ? 0 : belongedPartitionId
						.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((postUnitId == null) ? 0 : postUnitId.hashCode());
		result = prime * result
				+ ((preUnitId == null) ? 0 : preUnitId.hashCode());
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
		UnitDependency other = (UnitDependency) obj;
		if (belongedPartitionId == null) {
			if (other.belongedPartitionId != null)
				return false;
		} else if (!belongedPartitionId.equals(other.belongedPartitionId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (postUnitId == null) {
			if (other.postUnitId != null)
				return false;
		} else if (!postUnitId.equals(other.postUnitId))
			return false;
		if (preUnitId == null) {
			if (other.preUnitId != null)
				return false;
		} else if (!preUnitId.equals(other.preUnitId))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "UnitDependency [id=" + id + ", belongedPartitionId="
				+ belongedPartitionId + ", preUnitId=" + preUnitId
				+ ", postUnitId=" + postUnitId + "]";
	}

	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return null;
	}





}
