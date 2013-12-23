package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;


@Entity("rd4_dependent_system")
public class DependentSystem implements Printable{

	@Id("id")
	private String id;
	@Column("type")
	private Type type;
	@Column("target_id")
	private String targetId;
	@Column("System_name")
	private String systemName;



	public DependentSystem(Type type, String targetId,
			String systemName) {
		super();
		this.id = UUID.randomUUID().toString();
		this.type = type;
		this.targetId = targetId;
		this.systemName = systemName;
	}


	public DependentSystem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DependentSystem(DependentSystem dependentSystem) {
		super();
		this.id = dependentSystem.getId();
		this.type = dependentSystem.getType();
		this.targetId = dependentSystem.getTargetId();
		this.systemName = dependentSystem.getSystemName();
	}

	public enum Type {
		MISSION, PARTITION, UNIT
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Type getType() {
		return type;
	}


	public void setType(Type type) {
		this.type = type;
	}


	public String getTargetId() {
		return targetId;
	}


	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}


	public String getSystemName() {
		return systemName;
	}


	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}



	@Override
	public DependentSystem clone() throws CloneNotSupportedException {
		return new DependentSystem(this);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((systemName == null) ? 0 : systemName.hashCode());
		result = prime * result
				+ ((targetId == null) ? 0 : targetId.hashCode());
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
		DependentSystem other = (DependentSystem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (systemName == null) {
			if (other.systemName != null)
				return false;
		} else if (!systemName.equals(other.systemName))
			return false;
		if (targetId == null) {
			if (other.targetId != null)
				return false;
		} else if (!targetId.equals(other.targetId))
			return false;
		if (type != other.type)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "DependentSystem [id=" + id + ", type=" + type + ", targetId="
				+ targetId + ", systemName=" + systemName + "]";
	}


	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return null;
	}


}
