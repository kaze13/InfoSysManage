package system.controler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.RoleListDataModel;
import system.jsf.datamodel.UserWrapListDataModel;
import system.po.Authority;
import system.po.MainAuthority;
import system.po.Role;
import system.po.User;
import system.service.AuthorityServiceImpl;
import system.service.MainAuthorityServiceImpl;
import system.service.RoleServiceImpl;
import system.service.UserServiceImpl;
import system.vo.UserWrap;

@Named
@SessionScoped
public class UserControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3238009136930869369L;
	@Inject
	private UserServiceImpl userService;
	@Inject
	private RoleServiceImpl roleService;
	@Inject
	private MainAuthorityServiceImpl mainAuthorityService;
	@Inject
	private AuthorityServiceImpl authorityService;
	private UserWrap[] selectedUsers;
	private UserWrapListDataModel userListModel;
	private UserWrap newUser = new UserWrap();
	private UserWrap selectedUser;

	private Date expireDate;
	private List<Authority> authorityList;
	private RoleListDataModel roleListModel;
	private boolean firstTimeLoad = true;

	@Interceptors(TransactionInterceptor.class)
	public void onRefresh(boolean refresh) throws Exception {
		if (firstTimeLoad || refresh) {
			firstTimeLoad = false;
			List<UserWrap> baseUserList = UserWrap.convert(userService
					.findAllByCondition(null));
			for (int i = baseUserList.size() - 1; i >= 0; --i) {
				if (baseUserList.get(i).getName().contains("_new_comer_"))
					baseUserList.remove(i);
			}
			userListModel = new UserWrapListDataModel(baseUserList);
			List<Role> baseRoleList = roleService.findAllByCondition(null);
			authorityList = authorityService.findAllByCondition(null);
			roleListModel = new RoleListDataModel(baseRoleList);
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void doSaveUser() throws Exception {

		User saveUser = new User(newUser.getUser());
		if (expireDate == null)
			saveUser.setExpireTime(Long.MAX_VALUE);
		else {
			saveUser.setExpireTime(expireDate.getTime());
			expireDate = null;
		}

		userService.save(saveUser);

		List<MainAuthority> saveMainAuthorityList = new ArrayList<MainAuthority>();
		for (int i = 0; i < newUser.getAuthority().size(); ++i) {
			saveMainAuthorityList.add(new MainAuthority(newUser.getName(),
					newUser.getAuthority().get(i)));
		}
		mainAuthorityService.save(saveMainAuthorityList);

		List<UserWrap> baseUserList = (List<UserWrap>) userListModel
				.getWrappedData();
		baseUserList.add(new UserWrap(newUser));
		saveUser = null;
		newUser = new UserWrap();
	}

	@Interceptors(TransactionInterceptor.class)
	public void loadUser() throws Exception {
		if (selectedUser.getAuthority() == null) {
			Map<String, Object> sqlMap = new HashMap<String, Object>();
			sqlMap.put("user_name", selectedUser.getName());
			List<MainAuthority> mainAuthorityList = mainAuthorityService
					.findAllByCondition(sqlMap);
			List<String> authorityList = new ArrayList<String>();
			for (MainAuthority m : mainAuthorityList) {
				authorityList.add(m.getAuthorityName());
			}
			selectedUser.setAuthority(authorityList);
		}

	}

	@Interceptors(TransactionInterceptor.class)
	public void doUpdateUser() throws Exception {

		User updateUser = new User(selectedUser.getUser());
		if (expireDate == null)
			updateUser.setExpireTime(Long.MAX_VALUE);
		else {
			updateUser.setExpireTime(expireDate.getTime());
			expireDate = null;
		}
		userService.update(updateUser);

		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("user_name", selectedUser.getName());
		List<MainAuthority> oldAuthority = mainAuthorityService
				.findAllByCondition(sqlMap);
		if (oldAuthority.size() > 0) {
			String ids[] = new String[oldAuthority.size()];
			for (int i = 0; i < oldAuthority.size(); ++i) {
				ids[i] = oldAuthority.get(i).getId();
			}
			mainAuthorityService.delete(ids);
		}
		List<MainAuthority> newMainAuthorityList = new ArrayList<MainAuthority>();
		for (int i = 0; i < selectedUser.getAuthority().size(); ++i) {
			newMainAuthorityList.add(new MainAuthority(selectedUser.getName(),
					selectedUser.getAuthority().get(i)));
		}
		mainAuthorityService.save(newMainAuthorityList);

		replaceUserData(selectedUser);

		updateUser = null;
	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeleteUser() throws Exception {

		userService.delete(selectedUser.getName());
		List<UserWrap> baseUserList = (List<UserWrap>) userListModel
				.getWrappedData();
		int index = findUserData(selectedUser.getName());
		if (index >= 0) {
			baseUserList.remove(index);
		}

	}

	private void replaceUserData(UserWrap updatedUser) {
		int index = findUserData(updatedUser.getName());
		if (index >= 0) {
			List<UserWrap> baseUserList = (List<UserWrap>) userListModel
					.getWrappedData();
			baseUserList.set(index, new UserWrap(updatedUser));
		}
	}

	private int findUserData(String name) {
		List<UserWrap> baseUserList = (List<UserWrap>) userListModel
				.getWrappedData();
		for (int i = 0; i < baseUserList.size(); i++) {
			UserWrap user = baseUserList.get(i);
			if (user.getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	public String redirect() {
		return "asd";
	}

	// getter/setter

	public UserWrap[] getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(UserWrap[] selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public UserWrap getNewUser() {
		return newUser;
	}

	public void setNewUser(UserWrap newUser) {
		this.newUser = newUser;
	}

	public RoleListDataModel getRoleListModel() {
		return roleListModel;
	}

	public void setRoleListModel(RoleListDataModel roleListModel) {
		this.roleListModel = roleListModel;
	}

	public UserWrapListDataModel getUserVoListModel() {
		return userListModel;
	}

	public void setUserVoListModel(UserWrapListDataModel userVoListModel) {
		this.userListModel = userVoListModel;
	}

	public List<Authority> getAuthorityList() {
		return authorityList;
	}

	public void setAuthorityList(List<Authority> authorityList) {
		this.authorityList = authorityList;
	}

	public UserWrap getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(UserWrap selectedUser) {
		this.selectedUser = selectedUser;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

}
