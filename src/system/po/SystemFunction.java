package system.po;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_system_function")
public class SystemFunction implements Printable,Serializable{

	@Id("id")
	private String id;
	@Column("system_name")
	private String systemName;
	@Column("function_name")
	@NotNull
	private String functionName;
	@Column("description")
	private String description;

	public SystemFunction() {
		super();
		this.id = UUID.randomUUID().toString();
	}

	public SystemFunction(SystemFunction systemFunction) {
		super();
		this.id = systemFunction.getId();
		this.systemName = systemFunction.getSystemName();
		this.functionName = systemFunction.getFunctionName();
		this.description = systemFunction.getDescription();
	}

	public SystemFunction(String systemName, String functionName,
			String description) {
		super();
		this.id = UUID.randomUUID().toString();
		this.systemName = systemName;
		this.functionName = functionName;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	@Override
	public SystemFunction clone() throws CloneNotSupportedException {
		return new SystemFunction(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((functionName == null) ? 0 : functionName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((systemName == null) ? 0 : systemName.hashCode());
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
		SystemFunction other = (SystemFunction) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (functionName == null) {
			if (other.functionName != null)
				return false;
		} else if (!functionName.equals(other.functionName))
			return false;
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
		return true;
	}

	@Override
	public String toString() {
		return "SystemFunction [id=" + id + ", systemName=" + systemName
				+ ", functionName=" + functionName + ", description="
				+ description + "]";
	}

	@Override
	public String toFormatString() {
		return
		"\n*SYSTEM FUNCTION*\n" +
		"System name: " + systemName + "\n" +
		"Function name: " + functionName + "\n" +
		"Description: " + description + "\n";
	}

}
