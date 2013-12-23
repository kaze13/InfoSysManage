package system.po;

import java.io.Serializable;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_user")
public class User implements Printable, Serializable {

	@Id("user_name")
	private String name;
	@Column("password")
	private String password;
	@Column("role_name")
	private String roleName;
	@Column("expire_time")
	private long expireTime;

	@Column("real_name")
	private String realName;
	@Column("identity_number")
	private String identityNumber;
	@Column("description")
	private String description;
	@Column("department")
	private String department;
	@Column("condition")
	private String condition;
	public User(String name, String password, String roleName, long expireTime,
			String realName, String identityNumber, String description,
			String department) {
		super();
		this.name = name;
		this.password = password;
		this.roleName = roleName;
		this.expireTime = expireTime;
		this.realName = realName;
		this.identityNumber = identityNumber;
		this.description = description;
		this.department = department;
		this.condition = "normal";
	}

	public User(User user) {
		super();
		this.name = user.name;
		this.password = user.password;
		this.roleName = user.roleName;
		this.expireTime = user.expireTime;
		this.realName = user.realName;
		this.identityNumber = user.identityNumber;
		this.description = user.description;
		this.department = user.department;
		this.condition = user.condition;
	}

	public User() {
		super();
		this.password = "none";
		this.roleName = "employee";
		this.expireTime = 0;
		this.realName = "none";
		this.identityNumber = "none";
		this.description = "none";
		this.department = "none";
		this.condition = "normal";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public User clone() throws CloneNotSupportedException {
		return new User(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((condition == null) ? 0 : condition.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (expireTime ^ (expireTime >>> 32));
		result = prime * result
				+ ((identityNumber == null) ? 0 : identityNumber.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((realName == null) ? 0 : realName.hashCode());
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
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
		User other = (User) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (expireTime != other.expireTime)
			return false;
		if (identityNumber == null) {
			if (other.identityNumber != null)
				return false;
		} else if (!identityNumber.equals(other.identityNumber))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (realName == null) {
			if (other.realName != null)
				return false;
		} else if (!realName.equals(other.realName))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", password=" + password + ", roleName="
				+ roleName + ", expireTime=" + expireTime + ", realName="
				+ realName + ", identityNumber=" + identityNumber
				+ ", description=" + description + ", department=" + department
				+ "]";
	}

	public String toSignatureString() {
		return name + "---" + realName + "---" + description + "---";
	}

	@Override
	public String toFormatString() {
		return "\n*USER*\n" + "Name: 				" + name + "\n" + "Password: 			"
				+ password + "\n" + "Role name: 		" + roleName + "\n"
				+ "Expire time: 		" + expireTime + "\n" + "Real name: 		"
				+ realName + "\n" + "Identity number: 	" + identityNumber
				+ "\n" + "Description: 		" + description + "\n"
				+ "Department: 		" + department + "\n";
	}

}
