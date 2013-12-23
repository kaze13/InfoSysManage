package system.controler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.event.DragDropEvent;
import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.RoleListDataModel;
import system.jsf.datamodel.SystemFunctionListDataModel;
import system.po.Role;
import system.po.System;
import system.po.SystemAuthority;
import system.po.SystemFunction;
import system.service.RoleServiceImpl;
import system.service.SystemAuthorityServiceImpl;
import system.service.SystemServiceImpl;
import system.service.spec.SystemFunctionService;

@Named
@SessionScoped
public class RoleControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8364046651575124860L;

	@Inject
	private RoleServiceImpl roleService;
	@Inject
	private SystemServiceImpl systemService;
	@Inject
	private SystemFunctionService systemFunctionService;
	@Inject
	private SystemAuthorityServiceImpl systemAuthorityService;

	private Role[] selectedRoles;
	private RoleListDataModel roleListModel;
	private Role newRole = new Role();
	private Role selectedRole;
	private String selectedSystemName;
	private List<System> systemList;
	private SystemFunctionListDataModel sourceFunctionModel;
	private SystemFunctionListDataModel targetFunctionModel;
	private List<SystemAuthority> baseNewAuthorityList;
	private boolean firstTimeLoad = true;

	// private SystemAuthorityListDataModel newAuthorityListModel;
	// private SystemAuthority selectedAuthority;

	@Interceptors(TransactionInterceptor.class)
	public void onRefresh(boolean refresh) throws Exception {
//		if (firstTimeLoad || refresh) {
//			firstTimeLoad = false;
			List<Role> baseRoleList = roleService.findAllByCondition(null);

			roleListModel = new RoleListDataModel(baseRoleList);
			systemList = systemService.findAllByCondition(null);
			// newAuthorityListModel = new SystemAuthorityListDataModel(new
			// ArrayList<SystemAuthority>());
			sourceFunctionModel = new SystemFunctionListDataModel(
					new ArrayList<SystemFunction>());
			targetFunctionModel = new SystemFunctionListDataModel(
					new ArrayList<SystemFunction>());
			newRole = new Role();
		//}
	}

	@Interceptors(TransactionInterceptor.class)
	public void onSelectRole() throws Exception {
		List<SystemFunction> functions = systemFunctionService
				.findFunctionByRole(selectedRole.getName());
		targetFunctionModel.setWrappedData(functions);
	}

	@Interceptors(TransactionInterceptor.class)
	public void doSaveRole() throws Exception {
		roleService.save(newRole);
		List<Role> baseRoleList = (List<Role>) roleListModel.getWrappedData();
		baseRoleList.add(new Role(newRole));
		List<SystemFunction> targetList = ((List<SystemFunction>) targetFunctionModel
				.getWrappedData());

		List<SystemAuthority> newAuthorityList = new ArrayList<SystemAuthority>();
		for (SystemFunction function : targetList) {
			SystemAuthority tmp = new SystemAuthority(newRole.getName(),
					function.getId());
			newAuthorityList.add(tmp);
		}
		systemAuthorityService.save(newAuthorityList);
		sourceFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		targetFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		newRole = new Role();
	}

	@Interceptors(TransactionInterceptor.class)
	public void doUpdateRole() throws Exception {
		roleService.update(selectedRole);
		replaceRoleData(selectedRole);

		List<SystemFunction> targetList = ((List<SystemFunction>) targetFunctionModel
				.getWrappedData());

		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("role_name", selectedRole.getName());
		List<SystemAuthority> oldAuthority = systemAuthorityService
				.findAllByCondition(sqlWhereMap);
		String[] oldIds = new String[oldAuthority.size()];
		for (int i = 0; i < oldAuthority.size(); ++i)
			oldIds[i] = oldAuthority.get(i).getId();

		systemAuthorityService.delete(oldIds);
		List<SystemAuthority> newAuthorityList = new ArrayList<SystemAuthority>();
		for (SystemFunction function : targetList) {
			SystemAuthority tmp = new SystemAuthority(selectedRole.getName(),
					function.getId());
			newAuthorityList.add(tmp);
		}
		systemAuthorityService.save(newAuthorityList);
	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeleteRole() throws Exception {
		roleService.delete(selectedRole.getName());
		List<Role> baseRoleList = (List<Role>) roleListModel.getWrappedData();

		int index = findRoleData(selectedRole.getName());
		if (index >= 0) {
			baseRoleList.remove(index);
		}

	}

	public void deleteTargetFunction(SystemFunction function) {
		((List<SystemFunction>) targetFunctionModel.getWrappedData())
				.remove(function);

		// List<SystemFunction> source = (List<SystemFunction>)
		// sourceFunctionModel
		// .getWrappedData();
		// source.add(function);
	}

	@Interceptors(TransactionInterceptor.class)
	public void onSystemChanged() throws Exception {
		List<SystemFunction> source = systemFunctionService
				.findFunctionBySystem(selectedSystemName);
		sourceFunctionModel = new SystemFunctionListDataModel(source);

	}

	@SuppressWarnings("unchecked")
	public void onFunctionDroped(DragDropEvent ddEvent) {
		SystemFunction function = ((SystemFunction) ddEvent.getData());
		// ((List<SystemFunction>) sourceFunctionModel.getWrappedData())
		// .remove(function);
		List<SystemFunction> targetList = ((List<SystemFunction>) targetFunctionModel
				.getWrappedData());
		for (SystemFunction f : targetList) {
			if (f.getId().equals(function.getId()))
				return;
		}

		((List<SystemFunction>) targetFunctionModel.getWrappedData())
				.add(function);

	}

	public void addAuthority() {
		baseNewAuthorityList = new ArrayList<SystemAuthority>();
		@SuppressWarnings("unchecked")
		List<SystemFunction> targetFunctions = (List<SystemFunction>) targetFunctionModel
				.getWrappedData();
		for (SystemFunction function : targetFunctions) {
			baseNewAuthorityList
					.add(new SystemAuthority(null, function.getId()));
		}
	}

	private void replaceRoleData(Role updatedRole) {
		int index = findRoleData(updatedRole.getName());
		if (index >= 0) {
			@SuppressWarnings("unchecked")
			List<Role> baseRoleList = (List<Role>) roleListModel
					.getWrappedData();
			baseRoleList.set(index, new Role(updatedRole));
		}
	}

	private int findRoleData(String name) {
		List<Role> baseRoleList = (List<Role>) roleListModel.getWrappedData();
		for (int i = 0; i < baseRoleList.size(); i++) {
			Role role = baseRoleList.get(i);
			if (role.getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	// getter/setter

	public RoleListDataModel getRoleListModel() {
		return roleListModel;
	}

	public Role[] getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(Role[] selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public void setRoleListModel(RoleListDataModel roleListModel) {
		this.roleListModel = roleListModel;
	}

	public Role getNewRole() {
		return newRole;
	}

	public void setNewRole(Role newRole) {
		this.newRole = newRole;
	}

	public Role getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(Role selectedRole) {
		this.selectedRole = selectedRole;
	}

	public String getSelectedSystemName() {
		return selectedSystemName;
	}

	public void setSelectedSystemName(String selectedSystemName) {
		this.selectedSystemName = selectedSystemName;
	}

	public List<System> getSystemList() {
		return systemList;
	}

	public void setSystemList(List<System> systemList) {
		this.systemList = systemList;
	}

	public SystemFunctionListDataModel getSourceFunctionModel() {
		return sourceFunctionModel;
	}

	public void setSourceFunctionModel(
			SystemFunctionListDataModel sourceFunctionModel) {
		this.sourceFunctionModel = sourceFunctionModel;
	}

	public SystemFunctionListDataModel getTargetFunctionModel() {
		return targetFunctionModel;
	}

	public void setTargetFunctionModel(
			SystemFunctionListDataModel targetFunctionModel) {
		this.targetFunctionModel = targetFunctionModel;
	}

	public List<SystemAuthority> getBaseNewAuthorityList() {
		return baseNewAuthorityList;
	}

	public void setBaseNewAuthorityList(
			List<SystemAuthority> baseNewAuthorityList) {
		this.baseNewAuthorityList = baseNewAuthorityList;
	}

}
