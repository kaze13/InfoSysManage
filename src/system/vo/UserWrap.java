package system.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import system.po.Role;
import system.po.System;
import system.po.SystemFunction;
import system.po.TemporaryAuthority;
import system.po.User;

public class UserWrap implements Serializable{

	private User user;
	private Role role;
	private List<SystemFunction> authorisedFunctions;
	private List<TemporaryAuthorityWrap> temporaryAuthority;
	private List<String> authority;
	private List<System> dependentSystems;


	public UserWrap() {
		super();
		this.user = new User();
	}

	public UserWrap(User user)
	{
		super();
		this.user = user;
	}
	public UserWrap(User user, List<String> authority) {
		super();
		this.user = user;
		this.authority = authority;
	}

	public UserWrap(UserWrap user)
	{
		this.user = user.getUser();
		this.authority = user.getAuthority();
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<System> getDependentSystems() {
		return dependentSystems;
	}

	public void setDependentSystems(List<System> dependentSystems) {
		this.dependentSystems = dependentSystems;
	}


	public List<SystemFunction> getAuthorisedFunctions() {
		return authorisedFunctions;
	}

	public void setAuthorisedFunctions(List<SystemFunction> authorisedFunctions) {
		this.authorisedFunctions = authorisedFunctions;
	}



	public List<TemporaryAuthorityWrap> getTemporaryAuthority() {
		return temporaryAuthority;
	}

	public void setTemporaryAuthority(
			List<TemporaryAuthorityWrap> temporaryAuthority) {
		this.temporaryAuthority = temporaryAuthority;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getAuthority() {
		return authority;
	}

	public void setAuthority(List<String> authority) {
		this.authority = authority;
	}

	public String getName() {
		if(user == null)
			return null;
		return user.getName();
	}

	public void setName(String name) {
		user.setName(name);
	}

	public String getPassword() {
		if(user == null)
			return null;
		return user.getPassword();
	}

	public void setPassword(String password) {
		user.setPassword(password);
	}

	public String getRoleName() {
		if(user == null)
			return null;
		return user.getRoleName();
	}

	public void setRoleName(String roleName) {
		user.setRoleName(roleName);
	}

	public long getExpireTime() {
		if(user == null)
			return 0;
		return user.getExpireTime();
	}

	public void setExpireTime(long expireTime) {
		user.setExpireTime(expireTime);
	}

	public String getRealName() {
		if(user == null)
			return null;
		return user.getRealName();
	}

	public void setRealName(String realName) {
		user.setRealName(realName);
	}

	public String getIdentityNumber() {
		if(user == null)
			return null;
		return user.getIdentityNumber();
	}

	public void setIdentityNumber(String identityNumber) {
		user.setIdentityNumber(identityNumber);
	}

	public String getDescription() {
		if(user == null)
			return null;
		return user.getDescription();
	}

	public void setDescription(String description) {
		user.setDescription(description);
	}

	public String getDepartment() {
		if(user == null)
			return null;
		return user.getDepartment();
	}

	public void setDepartment(String department) {
		user.setDepartment(department);
	}


	public static List<UserWrap> convert(List<User> list)
	{
		List<UserWrap> results = new ArrayList<UserWrap>();
		for(User u:list)
		{
			results.add(new UserWrap(u));
		}
		return results;
	}

	@Override
	public String toString() {
		return "UserWrap [user=" + user + ", authority=" + authority + "]";
	}




}
