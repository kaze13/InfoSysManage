package system.controler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.SysAdminListDataModel;
import system.jsf.datamodel.SystemFunctionListDataModel;
import system.jsf.datamodel.SystemListDataModel;
import system.po.SysAdmin;
import system.po.System;
import system.po.SystemFunction;
import system.service.SysAdminServiceImpl;
import system.service.SystemFunctionServiceImpl;
import system.service.SystemServiceImpl;

@Named
@SessionScoped
public class SystemControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2385373275044125485L;

	@Inject
	private SystemServiceImpl systemService;
	@Inject
	private SystemFunctionServiceImpl systemFunctionService;
	@Inject
	private SysAdminServiceImpl sysAdminService;

	private System[] selectedSystems;
	private SystemListDataModel systemListModel;
	private System newSystem = new System();
	// private SystemFunctionListDataModel newSystemFunctionList;

	private System selectedSystem;
	private SystemFunction selectedFunction;
	private SystemFunctionListDataModel temporaryFunctionList;
	private List<SystemFunction> baseTemporaryFunctionList = new ArrayList<SystemFunction>();
	private List<SystemFunction> newBaseTemporaryFunctionList = new ArrayList<SystemFunction>();
	private String selectedSystemState;

	private boolean addSystemMode = false;
	private boolean editSystemMode = false;
	private boolean browseSystemMode = false;

	private System editSystem;
	private SystemFunction editFunction;

	private SysAdmin newSysAdmin;
	private SysAdmin selectedSysAdmin;
	private SysAdminListDataModel sysAdminListModel;

	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		List<System> baseSystemList = systemService.findAllByCondition(null);
		systemListModel = new SystemListDataModel(baseSystemList);
		temporaryFunctionList = new SystemFunctionListDataModel(
				baseTemporaryFunctionList);

		List<SysAdmin> baseSysAdminList = sysAdminService
				.findAllByCondition(null);
		sysAdminListModel = new SysAdminListDataModel(baseSysAdminList);
		newSysAdmin = new SysAdmin();
	}

	@Interceptors(TransactionInterceptor.class)
	public void doSaveSystem() throws Exception {

		List<System> baseSystemList = (List<System>) systemListModel
				.getWrappedData();
		for (System obj : baseSystemList) {
			if (obj.equals(newSystem)) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Duplicate data", "System already exist."));
				return;
			}

		}

		systemService.save(newSystem);

		baseSystemList.add(new System(newSystem));

	}

	@Interceptors(TransactionInterceptor.class)
	public void doSaveSysAdmin() throws Exception {
		List<SysAdmin> baseSysAdminList = (List<SysAdmin>) sysAdminListModel
				.getWrappedData();
		for (SysAdmin obj : baseSysAdminList) {
			if (obj.equals(newSysAdmin)) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Duplicate data",
								"Admin relation already exist."));
				newSysAdmin = new SysAdmin();
				return;
			}
			if (newSysAdmin.getSystemName().equals(obj.getSystemName())) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Duplicate admin",
								"One system has only one admin."));
				newSysAdmin = new SysAdmin();
				return;
			}
		}
		sysAdminService.save(newSysAdmin);

		baseSysAdminList.add(new SysAdmin(newSysAdmin));
		newSysAdmin = new SysAdmin();
	}

	@Interceptors(TransactionInterceptor.class)
	public void doUpdateSystem() throws Exception {
		systemService.update(selectedSystem);
		replaceSystemData(selectedSystem);
	}

	@Interceptors(TransactionInterceptor.class)
	public void doUpdateSysAdmin() throws Exception {
		sysAdminService.update(selectedSysAdmin);
		replaceSysAdminData(selectedSysAdmin);
	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeleteSystem() throws Exception {

		systemService.delete(selectedSystem.getName());
		List<System> baseSystemList = (List<System>) systemListModel
				.getWrappedData();
		List<SysAdmin> adminDeleteList = new ArrayList<SysAdmin>();
		for (SysAdmin s : (List<SysAdmin>) sysAdminListModel.getWrappedData()) {
			if (s.getSystemName().equals(selectedSystem.getName()))
				adminDeleteList.add(s);
		}
		if (adminDeleteList.size() > 0)
			sysAdminService.delete(adminDeleteList.get(0).getId());

		int index = findSystemData(selectedSystem.getName());
		if (index >= 0) {
			baseSystemList.remove(index);
		}

	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeleteSysAdmin() throws Exception {
		sysAdminService.delete(selectedSysAdmin.getId());
		List<SysAdmin> baseSysAdminList = (List<SysAdmin>) sysAdminListModel
				.getWrappedData();
		int index = findSysAdminData(selectedSysAdmin.getId());
		if (index >= 0) {
			baseSysAdminList.remove(index);
		}
	}

	public void addFunction() throws Exception {
		// editFunction.setFunctionName(selectedSystem.getName());

		// if(editFunction.getFunctionName() == null ||
		// editFunction.getDescription() == null)
		// {
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage(FacesMessage.SEVERITY_INFO,"Sample info message",
		// "PrimeFaces rocks!"));
		// return;
		// }
		if (editFunction.getFunctionName() == null
				|| editFunction.getFunctionName().trim().equals("")) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Invalid function", "Function name required."));
			return;
		}
		baseTemporaryFunctionList.add(editFunction);
		newBaseTemporaryFunctionList.add(editFunction);
		editFunction = new SystemFunction();

	}

	public void deleteFunction() throws Exception {
		baseTemporaryFunctionList.remove(selectedFunction);

	}

	// role

	public void quitAllSystemMode() {
		editSystemMode = false;
		addSystemMode = false;
		browseSystemMode = false;

	}

	@Interceptors(TransactionInterceptor.class)
	public void startEditSystem() throws Exception {
		editSystem = selectedSystem;
		editFunction = new SystemFunction();
		editSystemMode = true;
		browseSystemMode = false;
		// baseTemporaryFunctionList = systemFunctionService
		// .findFunctionBySystem(editSystem.getName());
		newBaseTemporaryFunctionList.clear();
	}

	public void startAddSystem() {
		editSystem = new System();
		editFunction = new SystemFunction();
		addSystemMode = true;
		browseSystemMode = false;
		baseTemporaryFunctionList.clear();
		newBaseTemporaryFunctionList.clear();
	}

	@Interceptors(TransactionInterceptor.class)
	public void startBrowseSystem() throws Exception {
		browseSystemMode = true;
		baseTemporaryFunctionList = systemFunctionService
				.findFunctionBySystem(selectedSystem.getName());

		temporaryFunctionList = new SystemFunctionListDataModel(
				baseTemporaryFunctionList);
	}

	private void replaceSystemData(System updatedSystem) {
		int index = findSystemData(updatedSystem.getName());
		if (index >= 0) {
			List<System> baseSystemList = (List<System>) systemListModel
					.getWrappedData();
			baseSystemList.set(index, new System(updatedSystem));
		}
	}

	private int findSystemData(String name) {
		List<System> baseSystemList = (List<System>) systemListModel
				.getWrappedData();
		for (int i = 0; i < baseSystemList.size(); i++) {
			System system = baseSystemList.get(i);
			if (system.getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	private void replaceSysAdminData(SysAdmin updatedSysAdmin) {
		int index = findSysAdminData(updatedSysAdmin.getId());
		if (index >= 0) {
			List<SysAdmin> baseSysAdminList = (List<SysAdmin>) sysAdminListModel
					.getWrappedData();
			baseSysAdminList.set(index, new SysAdmin(updatedSysAdmin));
		}
	}

	private int findSysAdminData(String id) {
		List<SysAdmin> baseSysAdminList = (List<SysAdmin>) sysAdminListModel
				.getWrappedData();
		for (int i = 0; i < baseSysAdminList.size(); i++) {
			SysAdmin system = baseSysAdminList.get(i);
			if (system.getId().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	@Interceptors(TransactionInterceptor.class)
	public void applySystem() throws Exception {

		if (editSystemMode) {
			systemService.update(editSystem);
			List<SystemFunction> tmp = new ArrayList<SystemFunction>();
			baseTemporaryFunctionList = (List<SystemFunction>) temporaryFunctionList
					.getWrappedData();
			for (SystemFunction function : baseTemporaryFunctionList) {
				if (!newBaseTemporaryFunctionList.contains(function))
					tmp.add(function);
			}
			if (newBaseTemporaryFunctionList.size() != 0) {
				for (SystemFunction function : newBaseTemporaryFunctionList)
					function.setSystemName(editSystem.getName());
				systemFunctionService.save(newBaseTemporaryFunctionList);
			}
			systemFunctionService.update(tmp);
			replaceSystemData(editSystem);
		} else if (addSystemMode) {

			List<System> baseSystemList = (List<System>) systemListModel
					.getWrappedData();
			for (System obj : baseSystemList) {
				if (obj.equals(editSystem)) {
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Duplicate data", "System already exist."));

					return;
				}
			}
			systemService.save(editSystem);

			baseSystemList.add(new System(editSystem));
			for (SystemFunction function : baseTemporaryFunctionList)
				function.setSystemName(editSystem.getName());
			systemFunctionService.save(baseTemporaryFunctionList);
		}
		newBaseTemporaryFunctionList.clear();
		baseTemporaryFunctionList.clear();
		editSystem = null;
		editSystemMode = false;
		addSystemMode = false;
		browseSystemMode = false;
	}

	public void discardSystem() {
		newBaseTemporaryFunctionList.clear();
		baseTemporaryFunctionList.clear();
		editSystem = null;
		editSystemMode = false;
		addSystemMode = false;
		browseSystemMode = false;
	}

	// getter/setter

	public SystemListDataModel getSystemListModel() {
		return systemListModel;
	}

	public String getSelectedSystemState() {
		return selectedSystemState;
	}

	public void setSelectedSystemState(String selectedSystemState) {
		this.selectedSystemState = selectedSystemState;
	}

	public System[] getSelectedSystems() {
		return selectedSystems;
	}

	public void setSelectedSystems(System[] selectedSystems) {
		this.selectedSystems = selectedSystems;
	}

	public void setSystemListModel(SystemListDataModel systemListModel) {
		this.systemListModel = systemListModel;
	}

	public System getNewSystem() {
		return newSystem;
	}

	public void setNewSystem(System newSystem) {
		this.newSystem = newSystem;
	}

	public System getSelectedSystem() {
		return selectedSystem;
	}

	public void setSelectedSystem(System selectedSystem) {
		this.selectedSystem = selectedSystem;
	}

	public SystemFunction getSelectedFunction() {
		return selectedFunction;
	}

	public void setSelectedFunction(SystemFunction selectedFunction) {
		this.selectedFunction = selectedFunction;
	}

	public SystemFunctionListDataModel getSelectedSystemFunctionList() {
		return temporaryFunctionList;
	}

	public void setSelectedSystemFunctionList(
			SystemFunctionListDataModel selectedSystemFunctionList) {
		this.temporaryFunctionList = selectedSystemFunctionList;
	}

	public boolean isAddSystemMode() {
		return addSystemMode;
	}

	public void setAddSystemMode(boolean addSystemMode) {
		this.addSystemMode = addSystemMode;
	}

	public boolean isEditSystemMode() {
		return editSystemMode;
	}

	public void setEditSystemMode(boolean editSystemMode) {
		this.editSystemMode = editSystemMode;
	}

	public boolean isBrowseSystemMode() {
		return browseSystemMode;
	}

	public void setBrowseSystemMode(boolean browseSystemMode) {
		this.browseSystemMode = browseSystemMode;
	}

	public System getEditSystem() {
		return editSystem;
	}

	public void setEditSystem(System editSystem) {
		this.editSystem = editSystem;
	}

	public SystemFunction getEditFunction() {
		return editFunction;
	}

	public void setEditFunction(SystemFunction editFunction) {
		this.editFunction = editFunction;
	}

	public SystemFunctionListDataModel getTemporaryFunctionList() {
		return temporaryFunctionList;
	}

	public void setTemporaryFunctionList(
			SystemFunctionListDataModel temporaryFunctionList) {
		this.temporaryFunctionList = temporaryFunctionList;
	}

	public SysAdmin getNewSysAdmin() {
		return newSysAdmin;
	}

	public void setNewSysAdmin(SysAdmin newSysAdmin) {
		this.newSysAdmin = newSysAdmin;
	}

	public SysAdmin getSelectedSysAdmin() {
		return selectedSysAdmin;
	}

	public void setSelectedSysAdmin(SysAdmin selectedSysAdmin) {
		this.selectedSysAdmin = selectedSysAdmin;
	}

	public SysAdminListDataModel getSysAdminListModel() {
		return sysAdminListModel;
	}

	public void setSysAdminListModel(SysAdminListDataModel sysAdminListModel) {
		this.sysAdminListModel = sysAdminListModel;
	}

}
