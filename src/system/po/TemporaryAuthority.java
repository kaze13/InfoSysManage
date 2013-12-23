package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;


@Entity("rd4_temporary_authority")
public class TemporaryAuthority implements Printable{

	@Id("id")
	private String id;
	@Column("user_name")
	private String userName;
	@Column("system_function_id")
	private String systemFunctionId;
	@Column("description")
	private String description;
	@Column("expire_time")
	private long expireTime;


	public TemporaryAuthority(String userName,
			String systemFunctionId, String description, long expireTime) {
		super();
		this.id = UUID.randomUUID().toString();
		this.userName = userName;
		this.systemFunctionId = systemFunctionId;
		this.description = description;
		this.expireTime = expireTime;
	}
	public TemporaryAuthority() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TemporaryAuthority(TemporaryAuthority temporaryAuthority) {
		this.id = temporaryAuthority.getId();
		this.userName = temporaryAuthority.getUserName();
		this.systemFunctionId = temporaryAuthority.getSystemFunctionId();
		this.description = temporaryAuthority.getDescription();
		this.expireTime = temporaryAuthority.getExpireTime();
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
	public String getSystemFunctionId() {
		return systemFunctionId;
	}
	public void setSystemFunctionId(String systemFunctionId) {
		this.systemFunctionId = systemFunctionId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (expireTime ^ (expireTime >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((systemFunctionId == null) ? 0 : systemFunctionId.hashCode());
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
		TemporaryAuthority other = (TemporaryAuthority) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (expireTime != other.expireTime)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (systemFunctionId == null) {
			if (other.systemFunctionId != null)
				return false;
		} else if (!systemFunctionId.equals(other.systemFunctionId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}


	@Override
	public TemporaryAuthority clone() throws CloneNotSupportedException {
		return new TemporaryAuthority(this);
	}
	@Override
	public String toString() {
		return "TemporaryAuthority [id=" + id + ", userName=" + userName
				+ ", systemFunctionId=" + systemFunctionId + ", description="
				+ description + ", expireTime=" + expireTime + "]";
	}
	@Override
	public String toFormatString() {
		return null;
	}




}
