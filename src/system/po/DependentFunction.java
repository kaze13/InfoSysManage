package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_dependent_function")
public class DependentFunction implements Printable{

	@Id("id")
	private String id;
	@Column("type")
	private String type;
	@Column("target_id")
	private String targetId;
	@Column("function_id")
	private String functionId;


	public DependentFunction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DependentFunction(DependentFunction dependentFunction) {
		this.id = dependentFunction.getId();
		this.type = dependentFunction.getType();
		this.targetId = dependentFunction.getTargetId();
		this.functionId = dependentFunction.getFunctionId();
	}

	public DependentFunction(String type, String targetId,
			String functionId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.type = type;
		this.targetId = targetId;
		this.functionId = functionId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}


	@Override
	public DependentFunction clone() throws CloneNotSupportedException {
		return new DependentFunction(this);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((functionId == null) ? 0 : functionId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		DependentFunction other = (DependentFunction) obj;
		if (functionId == null) {
			if (other.functionId != null)
				return false;
		} else if (!functionId.equals(other.functionId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (targetId == null) {
			if (other.targetId != null)
				return false;
		} else if (!targetId.equals(other.targetId))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DependentFunction [id=" + id + ", type=" + type + ", targetId="
				+ targetId + ", functionId=" + functionId + "]";
	}
	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return null;
	}


}
