package system.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.layout.Layout;
import org.primefaces.component.layout.LayoutUnit;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.ApplicationWrapListDataModel;
import system.jsf.datamodel.SystemFunctionListDataModel;
import system.jsf.datamodel.UserListDataModel;
import system.manager.spec.SessionManager;
import system.po.Application;
import system.po.Application.StatueType;
import system.po.Application.Type;
import system.po.ApplicationResult;
import system.po.MainAuthority;
import system.po.SysAdmin;
import system.po.SystemFunction;
import system.po.UploadFile;
import system.po.User;
import system.service.ApplicationResultServiceImpl;
import system.service.ApplicationServiceImpl;
import system.service.ApplicationStageServiceImpl;
import system.service.MainAuthorityServiceImpl;
import system.service.SysAdminServiceImpl;
import system.service.SystemFunctionServiceImpl;
import system.service.TemporaryAuthorityServiceImpl;
import system.service.UploadFileServiceImpl;
import system.service.UserServiceImpl;
import system.service.spec.UploadFileService;
import system.util.PropertyManager;
import system.util.ToolBox;
import system.util.XmlBuilder;
import system.vo.ApplicationStageWrap;
import system.vo.ApplicationWrap;
import system.vo.TemporaryAuthorityWrap;
import system.vo.UserWrap;
import system.vo.manager.UserWrapGetter;

@Named
@SessionScoped
public class ApplicationControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2161542396341215615L;
	@Inject
	private ApplicationServiceImpl applicationService;
	@Inject
	private UploadFileServiceImpl uploadFileService;
	@Inject
	private ApplicationStageServiceImpl applicationStageService;
	@Inject
	private UserServiceImpl userService;
	@Inject
	private ApplicationResultServiceImpl applicationResultService;
	@Inject
	private SystemFunctionServiceImpl systemFunctionService;
	@Inject
	private UserWrapGetter userWrapGetter;
	@Inject
	private SysAdminServiceImpl sysAdminService;
	@Inject
	private MainAuthorityServiceImpl mainAuthorityService;
	private ApplicationWrap[] selectedApplications;
	private ApplicationWrapListDataModel applicationListModel;
	private ApplicationWrap selectedApplication;
	private String comment;
	private Date acceptExpireDate;
	private List<ApplicationWrap> filteredApplications;
	private List<ApplicationWrap> allApplicationsBackup;
	private List<ApplicationWrap> baseApplicationList;
	private ApplicationStageWrap selectedStage;
	private List<ApplicationWrap> mybaseApplicationList;
	private List<ApplicationWrap> myAllApplicationsBackup;
	private UploadedFile file;
	private boolean recievedApplicationView = true;

	private UserListDataModel newEmployeeDataList;
	private List<User> newEmployeeList;
	private User[] approvedNewEmployee;
	private UserWrap newGuest;

	private List<User> newEmployeeSuccessResult;
	private List<User> newEmployeeFailResult;

	private List<SystemFunction> appliedFunctions;
	private SystemFunctionListDataModel appliedFunctionDataModel;
	private SystemFunction[] approvedFunctionsArray;
	private List<SystemFunction> approvedFunctions = new ArrayList<SystemFunction>();
	private List<SystemFunction> rejectedFunctions = new ArrayList<SystemFunction>();

	private List<SysAdmin> leaveSysAdmin;
	private List<SysAdmin> newTempSysAdmin;
	@Inject
	private SessionManager sessionManager;

	private LayoutUnit dataUnit;
	private LayoutUnit detialUnit;

	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		recievedApplicationView = true;
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("user_name", sessionManager.getLoginUser().getName());
		List<MainAuthority> currentAuthority = mainAuthorityService
				.findAllByCondition(sqlMap);
		sqlMap.clear();
		sqlMap.put("reciever_name", sessionManager.getLoginUser().getName());
		baseApplicationList = ApplicationWrap.convert(applicationService
				.findAllByCondition(sqlMap));
		for (MainAuthority m : currentAuthority) {
			sqlMap.clear();
			sqlMap.put("reciever_role", m.getAuthorityName());
			List<ApplicationWrap> groupApplication = ApplicationWrap
					.convert(applicationService.findAllByCondition(sqlMap));
			baseApplicationList.addAll(groupApplication);
		}

		List<ApplicationWrap> tmp = new ArrayList<ApplicationWrap>();
		for (int i = 0; i < baseApplicationList.size(); ++i) {
			ApplicationWrap largest = baseApplicationList.get(i);
			for (int j = i + 1; j < baseApplicationList.size(); ++j) {
				if (largest.getClazz().equals(
						baseApplicationList.get(j).getClazz())
						&& largest.getTime() < baseApplicationList.get(j)
								.getTime()) {
					largest = baseApplicationList.get(j);
				}
			}
			if (!tmp.contains(largest))
				tmp.add(largest);
		}
		baseApplicationList = tmp;
		Collections.sort(baseApplicationList);

		allApplicationsBackup = new ArrayList<ApplicationWrap>(
				baseApplicationList);
		applicationListModel = new ApplicationWrapListDataModel(
				baseApplicationList);

		sqlMap.clear();
		sqlMap.put("sender_name", sessionManager.getLoginUser().getName());
		mybaseApplicationList = ApplicationWrap.convert(applicationService
				.findAllByCondition(sqlMap));
		tmp = new ArrayList<ApplicationWrap>();
		for (int i = 0; i < mybaseApplicationList.size(); ++i) {
			ApplicationWrap largest = mybaseApplicationList.get(i);
			for (int j = i + 1; j < mybaseApplicationList.size(); ++j) {
				if (largest.getClazz().equals(
						mybaseApplicationList.get(j).getClazz())
						&& largest.getTime() < mybaseApplicationList.get(j)
								.getTime()) {
					largest = mybaseApplicationList.get(j);
				}
			}
			if (!tmp.contains(largest))
				tmp.add(largest);
		}
		mybaseApplicationList = tmp;
		Collections.sort(mybaseApplicationList);

		myAllApplicationsBackup = new ArrayList<ApplicationWrap>(
				mybaseApplicationList);

		Map<String, String> varMap = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String id = varMap.get("id");
		if (id != null) {
			for (ApplicationWrap app : mybaseApplicationList) {
				if (app.getId().equals(id)) {

					showSentApplication();
					selectedApplication = app;
					break;
				}
			}
			for (ApplicationWrap app : baseApplicationList) {
				if (app.getId().equals(id)) {

					showRecievedApplication();
					selectedApplication = app;
					break;
				}
			}
			initApplication();
		}

	}

	@Interceptors(TransactionInterceptor.class)
	public void initApplication() throws Exception {
		// if (selectedApplication.getStages() == null) {
		selectedApplication.setApplication(applicationService
				.get(selectedApplication.getId()));
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("application_class_id", selectedApplication.getClazz());
		selectedApplication.setStages(ApplicationStageWrap
				.convert(applicationStageService
						.findAllByCondition(sqlWhereMap)));
		// }

		if (selectedApplication.getType() == Type.NEW_EMPLOYEE) {
			UploadFile employeeFile = uploadFileService.get(selectedApplication
					.getData());
			FileInputStream inStream = new FileInputStream(
					employeeFile.getFilePath() + employeeFile.getFileName());
			ObjectInputStream objectInputStream = new ObjectInputStream(
					inStream);
			newEmployeeList = (List<User>) objectInputStream.readObject();
			for (User user : newEmployeeList) {
				user.setName("_new_comer_"
						+ UUID.randomUUID().toString().substring(0, 7));
			}
			newEmployeeDataList = new UserListDataModel(newEmployeeList);
		}

		if (selectedApplication.getType() == Type.PROMOTE_AUTHORITY) {
			String[] idlist = selectedApplication.getData().split(";");
			appliedFunctions = systemFunctionService.get(idlist);
			appliedFunctionDataModel = new SystemFunctionListDataModel(
					appliedFunctions);
		}

		if (selectedApplication.getType() == Type.ASK_FOR_LEAVE) {
			String adminName = selectedApplication.getSenderName();
			sqlWhereMap.clear();
			sqlWhereMap.put("user_name", adminName);
			leaveSysAdmin = sysAdminService.findAllByCondition(sqlWhereMap);
			for (SysAdmin s : leaveSysAdmin) {
				s.setId(UUID.randomUUID().toString());
				s.setIsTemporary(1);
				s.setOriginAdmin(s.getUserName());
				s.setUserName("");
				s.setCondition("normal");

			}
		}
		if (selectedApplication.getStatue() != StatueType.NOT_DEALED)
			initResult();
	}

	@Interceptors(TransactionInterceptor.class)
	public void initResult() throws Exception {
		if (selectedApplication.getResult() == null) {
			Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
			sqlWhereMap.put("application_class_id",
					selectedApplication.getClazz());
			List<ApplicationResult> result = applicationResultService
					.findAllByCondition(sqlWhereMap);
			if (result.size() < 1)
				return;
			if (result.size() > 1)
				throw new Exception("more than one result");
			if (result.size() == 1)
				selectedApplication.setResult(result.get(0));
			if (selectedApplication.getType() == Type.CREATE_GUEST) {
				newGuest = userWrapGetter.get(selectedApplication.getResult()
						.getData1());
			}
			if (selectedApplication.getType() == Type.NEW_EMPLOYEE) {
				UploadFile employeeFile = uploadFileService
						.get(selectedApplication.getResult().getData1());
				FileInputStream inStream = new FileInputStream(
						employeeFile.getFilePath() + employeeFile.getFileName());
				ObjectInputStream objectInputStream = new ObjectInputStream(
						inStream);
				newEmployeeSuccessResult = (List<User>) objectInputStream
						.readObject();
				newEmployeeFailResult = (List<User>) objectInputStream
						.readObject();
			}
			if (selectedApplication.getType() == Type.PROMOTE_AUTHORITY) {
				if (selectedApplication.getResult().getData1() != null) {
					String[] appliedStrs = selectedApplication.getResult()
							.getData1().split("\\;");
					approvedFunctions = systemFunctionService.get(appliedStrs);
				}
				if (selectedApplication.getResult().getData2() != null) {
					String[] rejectedStrs = selectedApplication.getResult()
							.getData2().split("\\;");
					rejectedFunctions = systemFunctionService.get(rejectedStrs);
				}
			}
			if (selectedApplication.getType() == Type.ASK_FOR_LEAVE) {
				String[] list = selectedApplication.getResult().getData1()
						.split("\\;");
				newTempSysAdmin = sysAdminService.get(list);
			}

		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeleteApplication() throws Exception {

		applicationService.delete(selectedApplication.getId());
		@SuppressWarnings("unchecked")
		List<ApplicationWrap> baseApplicationList = (List<ApplicationWrap>) applicationListModel
				.getWrappedData();

		int index = findApplicationData(selectedApplication.getId());
		if (index >= 0) {
			baseApplicationList.remove(index);
		}

		mybaseApplicationList.remove(selectedApplication);

	}

	public void readItem(ApplicationWrap application) throws Exception {
		selectedApplication = application;
	}

	public String abstraction(String text, int length) {
		if (text.length() <= length)
			return text;
		else {
			return text.substring(0, length) + "....";
		}
	}

	public void showSentApplication() {
		applicationListModel.setWrappedData(myAllApplicationsBackup);
		recievedApplicationView = false;
	}

	public void showRecievedApplication() {
		applicationListModel.setWrappedData(allApplicationsBackup);
		recievedApplicationView = true;
	}

	public void showAll() {
		if (recievedApplicationView)
			baseApplicationList = new ArrayList<ApplicationWrap>(
					allApplicationsBackup);
		else
			baseApplicationList = new ArrayList<ApplicationWrap>(
					myAllApplicationsBackup);
		applicationListModel.setWrappedData(baseApplicationList);

	}

	public void showAccepted() {
		if (recievedApplicationView)
			baseApplicationList = new ArrayList<ApplicationWrap>(
					allApplicationsBackup);
		else
			baseApplicationList = new ArrayList<ApplicationWrap>(
					myAllApplicationsBackup);
		for (int i = baseApplicationList.size() - 1; i >= 0; --i) {
			if (baseApplicationList.get(i).getStatue() != Application.StatueType.ACCEPTED)
				baseApplicationList.remove(i);
		}
		applicationListModel.setWrappedData(baseApplicationList);
	}

	public void showRejected() {
		if (recievedApplicationView)
			baseApplicationList = new ArrayList<ApplicationWrap>(
					allApplicationsBackup);
		else
			baseApplicationList = new ArrayList<ApplicationWrap>(
					myAllApplicationsBackup);
		for (int i = baseApplicationList.size() - 1; i >= 0; --i) {
			if (baseApplicationList.get(i).getStatue() != Application.StatueType.REJECTED)
				baseApplicationList.remove(i);
		}
		applicationListModel.setWrappedData(baseApplicationList);
	}

	public void showNotDealed() {
		if (recievedApplicationView)
			baseApplicationList = new ArrayList<ApplicationWrap>(
					allApplicationsBackup);
		else
			baseApplicationList = new ArrayList<ApplicationWrap>(
					myAllApplicationsBackup);
		for (int i = baseApplicationList.size() - 1; i >= 0; --i) {
			if (baseApplicationList.get(i).getStatue() != Application.StatueType.NOT_DEALED)
				baseApplicationList.remove(i);
		}
		applicationListModel.setWrappedData(baseApplicationList);
	}

	//

	public void doApproveNewEmployeeApplication() {
	}


	@Interceptors(TransactionInterceptor.class)
	public void doAcceptApplication() throws Exception {

		FacesContext context = FacesContext.getCurrentInstance();
		selectedApplication.setApplication(applicationService
				.get(selectedApplication.getId()));
		if (selectedApplication.getStatue() != Application.StatueType.NOT_DEALED) {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Operation Fail",
					"Application has already been dealed by others."));
			initApplication();
			comment = null;
			return;
		}
		Object[] args = new Object[10];

		args[0] = comment;
		args[1] = getUploadFileId();
		if (selectedApplication.getType() == Type.PROMOTE_AUTHORITY) {
			if(acceptExpireDate.getTime() < System.currentTimeMillis())
			{
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Operation Fail",
						"You shouldn't pick a data before now."));
				comment = null;
				return;
			}
			args[4] = acceptExpireDate;
			approvedFunctions.clear();
			for (SystemFunction func : approvedFunctionsArray) {
				approvedFunctions.add(func);
			}
			args[2] = approvedFunctions;
			rejectedFunctions = new ArrayList<SystemFunction>(appliedFunctions);
			for (int i = rejectedFunctions.size() - 1; i >= 0; --i) {
				if (approvedFunctions.contains(rejectedFunctions.get(i)))
					rejectedFunctions.remove(i);
			}
			args[3] = rejectedFunctions;
		}
		if (selectedApplication.getType() == Type.NEW_EMPLOYEE) {
			List<User> approvedNewEmployeeList = new ArrayList<User>();
			for (User user : approvedNewEmployee)
				approvedNewEmployeeList.add(user);
			args[2] = approvedNewEmployeeList;
			List<User> rejectedEmployee = new ArrayList<User>(newEmployeeList);
			for (int i = rejectedEmployee.size() - 1; i >= 0; --i) {
				if (approvedNewEmployeeList.contains(rejectedEmployee.get(i)))
					rejectedEmployee.remove(i);

			}
			args[3] = rejectedEmployee;
		}

		if (selectedApplication.getType() == Type.PROMOTE_AUTHORITY) {

		}
		if (selectedApplication.getType() == Type.ASK_FOR_LEAVE) {
			args[2] = leaveSysAdmin;
		}
		if (!applicationService.acceptApplication(
				selectedApplication.getApplication(), args)) {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Operation Fail",
					"Fail to approve the application."));
		} else {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Operation Success",
					"Application approved."));
			selectedApplication.setStatue(StatueType.ACCEPTED);
			applicationService.update(selectedApplication.getApplication());
			// notificationService.sendNotification(
			// selectedApplication.getSenderName(),
			// "ApplicationWrap  accepted.", "detial test ", "");

		}
		initApplication();
		acceptExpireDate = null;
		comment = null;

	}

	@Interceptors(TransactionInterceptor.class)
	public void doRejectApplication() throws Exception {
		Object[] args = new Object[10];
		FacesContext context = FacesContext.getCurrentInstance();
		selectedApplication.setApplication(applicationService
				.get(selectedApplication.getId()));
		if (selectedApplication.getStatue() != Application.StatueType.NOT_DEALED) {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Operation Fail",
					"Application has already been dealed by others."));
			initApplication();
			comment = null;
			return;
		}

		args[0] = comment;
		args[1] = getUploadFileId();
		args[2] = selectedApplication.getData();
		if (!applicationService.rejectApplication(
				selectedApplication.getApplication(), args)) {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Operation Fail",
					"Fail to reject the application."));
		} else {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Application rejected", ""));
			selectedApplication.setStatue(StatueType.REJECTED);
			applicationService.update(selectedApplication.getApplication());
			// notificationService.sendNotification(
			// selectedApplication.getSenderName(),
			// "ApplicationWrap for authority promotion rejected.",
			// "detial test ", "");
		}
		initApplication();
		comment = null;
	}

	@SuppressWarnings("deprecation")
	public String convertTime(Long second) {
		return new Date(second).toGMTString();
	}

	private int findApplicationData(String name) {
		@SuppressWarnings("unchecked")
		List<ApplicationWrap> baseApplicationList = (List<ApplicationWrap>) applicationListModel
				.getWrappedData();
		for (int i = 0; i < baseApplicationList.size(); i++) {
			ApplicationWrap ApplicationWrap = baseApplicationList.get(i);
			if (ApplicationWrap.getId().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	private void replaceApplicationData(ApplicationWrap updatedApplication) {
		int index = findApplicationData(updatedApplication.getId());
		if (index >= 0) {
			List<ApplicationWrap> baseApplicationList = (List<ApplicationWrap>) applicationListModel
					.getWrappedData();
			baseApplicationList.set(index, new ApplicationWrap(
					updatedApplication.getApplication()));
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public String getUploadFileId() throws Exception {
		String absolutePath = "";
		if (file != null) {
			Properties property = PropertyManager.getApplicationProperties();
			String path = property.getProperty("uploadpath");
			String newFileName = file.getFileName();
			UploadFile uploadFile = new UploadFile(sessionManager
					.getLoginUser().getName(), path, newFileName);
			uploadFileService.save(uploadFile);

			absolutePath = path + newFileName;
			File newFile = new File(absolutePath);

			FileOutputStream a = new FileOutputStream(newFile);
			a.write(file.getContents());
			a.close();
			return uploadFile.getId();
		} else
			return "none";
	}

	public void changeLayoutHorizontal() {
		dataUnit.setPosition("west");
		detialUnit.setPosition("center");
	}

	public void changeLayoutVertical() {
		dataUnit.setPosition("center");
		detialUnit.setPosition("south");
	}

	// getter/setter

	public UploadFileService getUploadFileService() {
		return uploadFileService;
	}

	public void setUploadFileService(UploadFileServiceImpl uploadFileService) {
		this.uploadFileService = uploadFileService;
	}

	public ApplicationWrap[] getSelectedApplications() {
		return selectedApplications;
	}

	public void setSelectedApplications(ApplicationWrap[] selectedApplications) {
		this.selectedApplications = selectedApplications;
	}

	public ApplicationWrapListDataModel getApplicationListModel() {
		return applicationListModel;
	}

	public void setApplicationListModel(
			ApplicationWrapListDataModel applicationListModel) {
		this.applicationListModel = applicationListModel;
	}

	public ApplicationWrap getSelectedApplication() {
		return selectedApplication;
	}

	public void setSelectedApplication(ApplicationWrap selectedApplication) {
		this.selectedApplication = selectedApplication;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getAcceptExpireDate() {
		return acceptExpireDate;
	}

	public void setAcceptExpireDate(Date acceptExpireDate) {
		this.acceptExpireDate = acceptExpireDate;
	}

	public List<ApplicationWrap> getFilteredApplications() {
		return filteredApplications;
	}

	public void setFilteredApplications(
			List<ApplicationWrap> filteredApplications) {
		this.filteredApplications = filteredApplications;
	}

	public int getSelectedApplicationsSize() {
		if (selectedApplications == null)
			return 0;
		else
			return selectedApplications.length;
	}

	public List<ApplicationWrap> getAllApplicationsBackup() {
		return allApplicationsBackup;
	}

	public void setAllApplicationsBackup(
			List<ApplicationWrap> allApplicationsBackup) {
		this.allApplicationsBackup = allApplicationsBackup;
	}

	public List<ApplicationWrap> getBaseApplicationList() {
		return baseApplicationList;
	}

	public void setBaseApplicationList(List<ApplicationWrap> baseApplicationList) {
		this.baseApplicationList = baseApplicationList;
	}

	public List<ApplicationWrap> getMybaseApplicationList() {
		return mybaseApplicationList;
	}

	public void setMybaseApplicationList(
			List<ApplicationWrap> mybaseApplicationList) {
		this.mybaseApplicationList = mybaseApplicationList;
	}

	public List<ApplicationWrap> getMyAllApplicationsBackup() {
		return myAllApplicationsBackup;
	}

	public void setMyAllApplicationsBackup(
			List<ApplicationWrap> myAllApplicationsBackup) {
		this.myAllApplicationsBackup = myAllApplicationsBackup;
	}

	public boolean isRecievedApplicationView() {
		return recievedApplicationView;
	}

	public void setRecievedApplicationView(boolean recievedApplicationView) {
		this.recievedApplicationView = recievedApplicationView;
	}

	public ApplicationStageWrap getSelectedStage() {
		return selectedStage;
	}

	public void setSelectedStage(ApplicationStageWrap selectedStage) {
		this.selectedStage = selectedStage;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public List<User> getNewEmployeeList() {
		return newEmployeeList;
	}

	public void setNewEmployeeList(List<User> newEmployeeList) {
		this.newEmployeeList = newEmployeeList;
	}

	public UserListDataModel getNewEmployeeDataList() {
		return newEmployeeDataList;
	}

	public void setNewEmployeeDataList(UserListDataModel newEmployeeDataList) {
		this.newEmployeeDataList = newEmployeeDataList;
	}

	public User[] getApprovedNewEmployee() {
		return approvedNewEmployee;
	}

	public void setApprovedNewEmployee(User[] approvedNewEmployee) {
		this.approvedNewEmployee = approvedNewEmployee;
	}

	public UserWrap getNewGuest() {
		return newGuest;
	}

	public void setNewGuest(UserWrap newGuest) {
		this.newGuest = newGuest;
	}

	public List<User> getNewEmployeeSuccessResult() {
		return newEmployeeSuccessResult;
	}

	public void setNewEmployeeSuccessResult(List<User> newEmployeeSuccessResult) {
		this.newEmployeeSuccessResult = newEmployeeSuccessResult;
	}

	public List<User> getNewEmployeeFailResult() {
		return newEmployeeFailResult;
	}

	public void setNewEmployeeFailResult(List<User> newEmployeeFailResult) {
		this.newEmployeeFailResult = newEmployeeFailResult;
	}

	public LayoutUnit getDataUnit() {
		return dataUnit;
	}

	public void setDataUnit(LayoutUnit dataUnit) {
		this.dataUnit = dataUnit;
	}

	public LayoutUnit getDetialUnit() {
		return detialUnit;
	}

	public void setDetialUnit(LayoutUnit detialUnit) {
		this.detialUnit = detialUnit;
	}

	public List<SystemFunction> getAppliedFunctions() {
		return appliedFunctions;
	}

	public void setAppliedFunctions(List<SystemFunction> appliedFunctions) {
		this.appliedFunctions = appliedFunctions;
	}

	public SystemFunction[] getApprovedFunctionsArray() {
		return approvedFunctionsArray;
	}

	public void setApprovedFunctionsArray(
			SystemFunction[] approvedFunctionsArray) {
		this.approvedFunctionsArray = approvedFunctionsArray;
	}

	public List<SystemFunction> getApprovedFunctions() {
		return approvedFunctions;
	}

	public void setApprovedFunctions(List<SystemFunction> approvedFunctions) {
		this.approvedFunctions = approvedFunctions;
	}

	public List<SystemFunction> getRejectedFunctions() {
		return rejectedFunctions;
	}

	public void setRejectedFunctions(List<SystemFunction> rejectedFunctions) {
		this.rejectedFunctions = rejectedFunctions;
	}

	public SystemFunctionListDataModel getAppliedFunctionDataModel() {
		return appliedFunctionDataModel;
	}

	public void setAppliedFunctionDataModel(
			SystemFunctionListDataModel appliedFunctionDataModel) {
		this.appliedFunctionDataModel = appliedFunctionDataModel;
	}

	public List<SysAdmin> getLeaveSysAdmin() {
		return leaveSysAdmin;
	}

	public void setLeaveSysAdmin(List<SysAdmin> leaveSysAdmin) {
		this.leaveSysAdmin = leaveSysAdmin;
	}

	public List<SysAdmin> getNewTempSysAdmin() {
		return newTempSysAdmin;
	}

	public void setNewTempSysAdmin(List<SysAdmin> newTempSysAdmin) {
		this.newTempSysAdmin = newTempSysAdmin;
	}

}
