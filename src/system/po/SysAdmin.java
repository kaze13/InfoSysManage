package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;


@Entity("rd4_system_admin")
public class SysAdmin implements Printable{

	@Id("id")
	private String id;
	@Column("user_name")
	private String userName;
	@Column("system_name")
	private String systemName;
	@Column("condition")
	private String condition;
	@Column("is_temporary")
	private int isTemporary;
	@Column("origin_admin")
	private String originAdmin;

	public SysAdmin() {
		super();
		this.id = UUID.randomUUID().toString();
		isTemporary = 0;
		this.condition = "normal";
		this.originAdmin = "none";
		this.isTemporary = 0;
	}

	public SysAdmin(SysAdmin sysAdmin)
	{
		this.id = sysAdmin.getId();
		this.userName = sysAdmin.getUserName();
		this.systemName = sysAdmin.getSystemName();
		this.condition = sysAdmin.getCondition();
		this.isTemporary = sysAdmin.getIsTemporary();
		this.originAdmin = sysAdmin.getOriginAdmin();
	}


	public SysAdmin(String id, String userName, String systemName,
			int isTemporary, String originAdmin) {
		super();
		this.id = id;
		this.userName = userName;
		this.systemName = systemName;
		this.condition = "normal";
		this.isTemporary = isTemporary;
		this.originAdmin = originAdmin;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public int getIsTemporary() {
		return isTemporary;
	}

	public void setIsTemporary(int isTemporary) {
		this.isTemporary = isTemporary;
	}

	@Override
	public SysAdmin clone() throws CloneNotSupportedException {
		return new SysAdmin(this);
	}



	public String getOriginAdmin() {
		return originAdmin;
	}

	public void setOriginAdmin(String originAdmin) {
		this.originAdmin = originAdmin;
	}

	@Override
	public String toFormatString() {
		return
		"\n*SYSTEM ADMIN*\n" +
		"User name: " + userName + "\n" +
		"System name: " + systemName + "\n";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + isTemporary;
		result = prime * result
				+ ((originAdmin == null) ? 0 : originAdmin.hashCode());
		result = prime * result
				+ ((systemName == null) ? 0 : systemName.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		SysAdmin other = (SysAdmin) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isTemporary != other.isTemporary)
			return false;
		if (originAdmin == null) {
			if (other.originAdmin != null)
				return false;
		} else if (!originAdmin.equals(other.originAdmin))
			return false;
		if (systemName == null) {
			if (other.systemName != null)
				return false;
		} else if (!systemName.equals(other.systemName))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SysAdmin [id=" + id + ", userName=" + userName
				+ ", systemName=" + systemName + ", condition=" + condition
				+ ", isTemporary=" + isTemporary + ", originAdmin="
				+ originAdmin + "]";
	}




}
