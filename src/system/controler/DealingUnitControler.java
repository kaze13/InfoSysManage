package system.controler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.model.UploadedFile;

import system.interceptor.TransactionInterceptor;
import system.manager.spec.SessionManager;
import system.po.Application;
import system.po.BugReport;
import system.po.Mission;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.System;
import system.po.SystemFunction;
import system.po.Task;
import system.po.TaskReport;
import system.po.UploadFile;
import system.po.Application.StatueType;
import system.po.Application.Type;
import system.service.ApplicationServiceImpl;
import system.service.BugReportServiceImpl;
import system.service.MissionPartitionServiceImpl;
import system.service.MissionServiceImpl;
import system.service.MissionUnitServiceImpl;
import system.service.NotificationServiceImpl;
import system.service.SystemFunctionServiceImpl;
import system.service.SystemServiceImpl;
import system.service.TaskReportServiceImpl;
import system.service.TaskServiceImpl;
import system.service.UploadFileServiceImpl;
import system.service.spec.SystemFunctionService;
import system.util.PropertyManager;
import system.vo.MissionWrap;
import system.vo.PartitionWrap;
import system.vo.TaskWrap;
import system.vo.UnitWrap;
import system.vo.manager.MissionWrapGetter;
import system.vo.manager.PartitionWrapGetter;

@Named
@SessionScoped
public class DealingUnitControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5645375388476235581L;

	@Inject
	private MissionUnitServiceImpl missionUnitService;
	@Inject
	private ApplicationServiceImpl applicationService;
	@Inject
	private MissionPartitionServiceImpl missionPartitionService;
	@Inject
	private UploadFileServiceImpl uploadFileService;
	@Inject
	private MissionServiceImpl missionService;
	@Inject
	private SystemServiceImpl systemService;
	@Inject
	private TaskServiceImpl taskService;
	@Inject
	private TaskReportServiceImpl taskReportService;
	@Inject
	private SystemFunctionServiceImpl systemFunctionService;
	@Inject
	private BugReportServiceImpl bugReportService;
	@Inject
	private NotificationServiceImpl notificationService;
	@Inject
	private MissionWrapGetter missionWrapGetter;
	@Inject
	private PartitionWrapGetter partitionWrapGetter;

	private UnitWrap currentTask;
	// private SystemListDataModel systemListModel;
	// private System selectedSystem;

	// private MissionUnit dealingUnit;
	// private MissionPartition belongedPartition;
	// private Mission belongedMission;
	private String transferTarget;
	private String unitid;
	private String report;
	private UploadedFile file;

	private List<SystemFunction> dependentFunctions;
	private List<SystemFunction> avaliableFunctions;
	private List<SystemFunction> lackedFunctions;
	private String errorSystemName;
	private boolean submitTaskMode = false;
	private boolean abandonTaskMode = false;
	private boolean reportErrorMode = false;
	private boolean transferMode = false;
	private boolean authorityMode = false;

	private Map<String, Boolean> appliedAuthority = new HashMap<String, Boolean>();
	private MissionWrap belongedMissionWrap;
	private PartitionWrap belongedPartitionWrap;
	@Inject
	private SessionManager sessionManager;

	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		Map<String, String> varMap = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String tmp = varMap.get("unitid");
		if (tmp != null)
			unitid = tmp;
		// else
		// throw new Exception("Illegal session state");
		else {
			//do nothing

		}
		Task task = taskService.findTaskByDataid(unitid);
		MissionUnit dealingUnit = missionUnitService.get(unitid);
		MissionPartition belongedPartition = missionPartitionService
				.get(dealingUnit.getPartitionid());
		Mission belongedMission = missionService.get(belongedPartition
				.getMissionid());
		List<System> systemList = systemService.findDependentSystems(unitid);
		List<MissionUnit> dependentUnitList = missionUnitService
				.findDependentUnit(unitid);
		currentTask = new UnitWrap(task, dealingUnit, belongedPartition,
				belongedMission, systemList,
				UnitWrap.convert(dependentUnitList), null);
		submitTaskMode = false;
		abandonTaskMode = false;
		dependentFunctions = systemFunctionService.findFunctionByTarget(unitid);

		belongedMissionWrap = missionWrapGetter.get(currentTask
				.getBelongedMission().getId());
		belongedPartitionWrap = partitionWrapGetter.get(currentTask
				.getBelongedPartition().getId());
		AuthorityCheck();

	}

	private void AuthorityCheck() throws Exception {

		avaliableFunctions = systemFunctionService
				.findFunctionByUser(currentTask.getUnit().getLeaderName());

		lackedFunctions = new ArrayList<SystemFunction>();
		for (SystemFunction function : dependentFunctions) {
			for (int i = 0; i < avaliableFunctions.size(); ++i) {
				if (function.getId().equals(avaliableFunctions.get(i).getId()))
					break;
				else {
					if (i == avaliableFunctions.size() - 1) {
						lackedFunctions.add(function);
					}
				}
			}
		}
		if (avaliableFunctions.size() == 0)
			lackedFunctions = dependentFunctions;

	}

	public boolean haveAppliedThisSession() {
		if (!appliedAuthority.containsKey(currentTask.getId()))
			return false;
		else if (appliedAuthority.get(currentTask.getId()) == true)
			return true;
		else
			return false;
	}

	public boolean preSubmissionCheck() {
		List<UnitWrap> dependentUnit = currentTask.getDependentObj();
		for (UnitWrap unit : dependentUnit) {
			if (unit.getProgress() != 100)
				return false;
		}
		return true;
	}

	@Interceptors(TransactionInterceptor.class)
	public void applyForAuthorityPromotion() throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("sender_name", sessionManager.getLoginUser().getName());
		sqlWhereMap.put("type", Type.PROMOTE_AUTHORITY);
		sqlWhereMap.put("statue", StatueType.NOT_DEALED);
		List<Application> undealed = applicationService
				.findAllByCondition(sqlWhereMap);
		if (undealed.size() > 0) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage("Caution",
									"You can't apply for authority promotion since there exist some unfinished promotion application."));
			return;
		}
		applicationService.sendPromoteAuthorityApplication(lackedFunctions,
				sessionManager.getLoginUser().getName(), report,
				getUploadFileId());
		report = null;
		appliedAuthority.put(currentTask.getId(), true);
	}

	public boolean checkAuthorityApplied() {
		// TODO add field to task.
		return false;
	}

	@Interceptors(TransactionInterceptor.class)
	public void submitTask() throws Exception {
		FacesContext context = FacesContext.getCurrentInstance();

		if (!preSubmissionCheck()) {
			context.addMessage(
					null,
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Submission failed.",
							"You can not submit the task since there exist some unfinished dependent tasks."));
			return;
		}
		currentTask.getTask().setStatue(Task.StatueType.SUBMITTED);
		taskService.update(currentTask.getTask());
		TaskReport taskReport = taskReportService
				.findReportWithTask(currentTask.getTask().getId());
		if (taskReport == null) {
			taskReport = new TaskReport(currentTask.getTask().getId(), report,
					getUploadFileId());
			taskReportService.save(taskReport);
		} else {
			taskReport.setReport(report);
			taskReport.setFileId(getUploadFileId());
			taskReportService.update(taskReport);
		}
		String uploadFileId = getUploadFileId();
		missionUnitService.submitUnit(currentTask.getBelongedPartition()
				.getLeaderName(), currentTask.getUnit(), report, uploadFileId);
		notificationService.sendSubmitTaskNotification(currentTask
				.getBelongedPartition().getLeaderName(), currentTask.getUnit(),
				report, uploadFileId);
		// submitTaskMode = false;
		// abandonTaskMode = false;
		report = null;
	}

	@Interceptors(TransactionInterceptor.class)
	public void transferTask() throws Exception {
		String uploadFileId = getUploadFileId();
		applicationService.sendTaskTransferApplication(transferTarget,
				currentTask.getTask(), report, uploadFileId);

		currentTask.getTask().setStatue(Task.StatueType.WAITING);
		taskService.update(currentTask.getTask());
		transferMode = false;
		report = null;
		transferTarget = null;
	}

	@Interceptors(TransactionInterceptor.class)
	public void abandonTask() throws Exception {
		// missionUnitService.abandonUnit(dealingUnit);
		String uploadFileId = getUploadFileId();
		applicationService.sendAbandonUnitTaskApplication(currentTask, report,
				uploadFileId);

		currentTask.getTask().setStatue(Task.StatueType.WAITING);
		taskService.update(currentTask.getTask());
		currentTask.getUnit().setProgress(-3); // abandon evaluation
		missionUnitService.update(currentTask.getUnit());
		submitTaskMode = false;
		abandonTaskMode = false;
		report = null;
	}

	@Interceptors(TransactionInterceptor.class)
	public void reportError() throws Exception {
		BugReport bugReport = new BugReport(errorSystemName, sessionManager
				.getLoginUser().getName(), report, "none", getUploadFileId());
		bugReportService.save(bugReport);
		applicationService.sendCheckErrorApplication(bugReport);
		reportErrorMode = false;
		report = null;
	}

	public String buildTransferBody() {
		return "transfer";
	}

	public List<SystemFunction> getCorrespondingFunction(System system) {
		List<SystemFunction> result = new ArrayList<SystemFunction>();
		for (SystemFunction function : dependentFunctions) {
			if (system.getName().equals(function.getSystemName()))
				result.add(function);
		}
		return result;
	}

	public void enterReportErrorMode() {
		abandonTaskMode = false;
		submitTaskMode = false;
		reportErrorMode = true;
		transferMode = false;
		authorityMode = false;

	}

	public void enterAuthorityMode() {
		abandonTaskMode = false;
		submitTaskMode = false;
		reportErrorMode = false;
		transferMode = false;
		authorityMode = true;
	}

	public void enterSubmitMode() {
		submitTaskMode = true;
		abandonTaskMode = false;
		reportErrorMode = false;
		transferMode = false;
		authorityMode = false;
	}

	public void enterAbandonMode() {
		abandonTaskMode = true;
		submitTaskMode = false;
		reportErrorMode = false;
		transferMode = false;
		authorityMode = false;
	}

	public void enterTransferMode() {
		abandonTaskMode = false;
		submitTaskMode = false;
		reportErrorMode = false;
		transferMode = true;
		authorityMode = false;
	}

	public String buildAbandonBody() {
		return report;
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

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public boolean isSubmitTaskMode() {
		return submitTaskMode;
	}

	public void setSubmitTaskMode(boolean submitTaskMode) {
		this.submitTaskMode = submitTaskMode;
	}

	public boolean isAbandonTaskMode() {
		return abandonTaskMode;
	}

	public void setAbandonTaskMode(boolean abandonTaskMode) {
		this.abandonTaskMode = abandonTaskMode;
	}

	public String getErrorSystemName() {
		return errorSystemName;
	}

	public void setErrorSystemName(String errorSystemName) {
		this.errorSystemName = errorSystemName;
	}

	public String getTransferTarget() {
		return transferTarget;
	}

	public void setTransferTarget(String transferTarget) {
		this.transferTarget = transferTarget;
	}

	public boolean isReportErrorMode() {
		return reportErrorMode;
	}

	public void setReportErrorMode(boolean reportErrorMode) {
		this.reportErrorMode = reportErrorMode;
	}

	public boolean isTransferMode() {
		return transferMode;
	}

	public void setTransferMode(boolean transferMode) {
		this.transferMode = transferMode;
	}

	public UnitWrap getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(UnitWrap currentTask) {
		this.currentTask = currentTask;
	}

	public List<SystemFunction> getDependentFunctions() {
		return dependentFunctions;
	}

	public void setDependentFunctions(List<SystemFunction> dependentFunctions) {
		this.dependentFunctions = dependentFunctions;
	}

	public List<SystemFunction> getAvaliableFunctions() {
		return avaliableFunctions;
	}

	public void setAvaliableFunctions(List<SystemFunction> avaliableFunctions) {
		this.avaliableFunctions = avaliableFunctions;
	}

	public List<SystemFunction> getLackedFunctions() {
		return lackedFunctions;
	}

	public void setLackedFunctions(List<SystemFunction> lackedFunctions) {
		this.lackedFunctions = lackedFunctions;
	}

	public boolean isAuthorityMode() {
		return authorityMode;
	}

	public void setAuthorityMode(boolean authorityMode) {
		this.authorityMode = authorityMode;
	}

	public MissionWrap getBelongedMissionWrap() {
		return belongedMissionWrap;
	}

	public void setBelongedMissionWrap(MissionWrap belongedMissionWrap) {
		this.belongedMissionWrap = belongedMissionWrap;
	}

	public PartitionWrap getBelongedPartitionWrap() {
		return belongedPartitionWrap;
	}

	public void setBelongedPartitionWrap(PartitionWrap belongedPartitionWrap) {
		this.belongedPartitionWrap = belongedPartitionWrap;
	}

	public Map<String, Boolean> getAppliedAuthority() {
		return appliedAuthority;
	}

	public void setAppliedAuthority(Map<String, Boolean> appliedAuthority) {
		this.appliedAuthority = appliedAuthority;
	}

}
