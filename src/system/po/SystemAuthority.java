package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_system_authority")
public class SystemAuthority implements Printable{

	@Id("id")
	private String id;
	@Column("role_name")
	private String roleName;
	@Column("system_function_id")
	private String systemFunctionId;


	public SystemAuthority() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SystemAuthority(SystemAuthority systemAuthority) {
		this.id = systemAuthority.getId();
		this.roleName = systemAuthority.getRoleName();
		this.systemFunctionId = systemAuthority.getSystemFunctionId();
	}
	public SystemAuthority(String roleName, String systemFunctionId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.roleName = roleName;
		this.systemFunctionId = systemFunctionId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getSystemFunctionId() {
		return systemFunctionId;
	}
	public void setSystemFunctionId(String systemFunctionId) {
		this.systemFunctionId = systemFunctionId;
	}



	@Override
	public SystemAuthority clone() throws CloneNotSupportedException {
		return new SystemAuthority(this);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
		result = prime
				* result
				+ ((systemFunctionId == null) ? 0 : systemFunctionId.hashCode());
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
		SystemAuthority other = (SystemAuthority) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		if (systemFunctionId == null) {
			if (other.systemFunctionId != null)
				return false;
		} else if (!systemFunctionId.equals(other.systemFunctionId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SystemAuthority [id=" + id + ", roleName=" + roleName
				+ ", systemFunctionId=" + systemFunctionId + "]";
	}

	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return null;
	}


}
