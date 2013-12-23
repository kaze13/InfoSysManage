package system.vo;

import java.util.List;

import system.po.User;

public class UserVo {

	private User user;

	private List<String> authority;




	public UserVo() {
		super();
		this.user = new User();
	}

	public UserVo(User user, List<String> authority) {
		super();
		this.user = user;
		this.authority = authority;
	}

	public UserVo(UserVo user)
	{
		this.user = user.getUser();
		this.authority = user.getAuthority();
	}
//	public UserVo(String name, String password, String roleName,
//			List<String> authority) {
//		super();
//		this.name = name;
//		this.password = password;
//		this.roleName = roleName;
//		this.authority = authority;
//	}



	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return user.getName();
	}

	public void setName(String name) {
		user.setName(name);
	}

	public String getPassword() {
		return user.getPassword();
	}

	public void setPassword(String password) {
		user.setPassword(password);
	}

	public String getRoleName() {
		return user.getRoleName();
	}

	public void setRoleName(String roleName) {
		user.setRoleName(roleName);
	}

	public long getExpireTime() {
		return user.getExpireTime();
	}

	public void setExpireTime(long expireTime) {
		user.setExpireTime(expireTime);
	}

	public String getRealName() {
		return user.getRealName();
	}

	public void setRealName(String realName) {
		user.setRealName(realName);
	}

	public String getIdentityNumber() {
		return user.getIdentityNumber();
	}

	public void setIdentityNumber(String identityNumber) {
		user.setIdentityNumber(identityNumber);
	}

	public String getDescription() {
		return user.getDescription();
	}

	public void setDescription(String description) {
		user.setDescription(description);
	}

	public String getDepartment() {
		return user.getDepartment();
	}

	public void setDepartment(String department) {
		user.setDepartment(department);
	}

	public List<String> getAuthority() {
		return authority;
	}

	public void setAuthority(List<String> authority) {
		this.authority = authority;
	}



}
