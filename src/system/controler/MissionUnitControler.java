package system.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
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

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import system.interceptor.TimerInterceptor;
import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.SystemFunctionListDataModel;
import system.jsf.datamodel.UnitDependencyVoListDataModel;
import system.manager.spec.SessionManager;
import system.po.Application;
import system.po.DependentFunction;
import system.po.DependentSystem;
import system.po.Mission;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.PartitionDependency;
import system.po.System;
import system.po.SystemFunction;
import system.po.Task;
import system.po.TaskReport;
import system.po.UnitDependency;
import system.po.UploadFile;
import system.po.Application.StatueType;
import system.po.Application.Type;
import system.service.ApplicationServiceImpl;
import system.service.DependentFunctionServiceImpl;
import system.service.MissionPartitionServiceImpl;
import system.service.MissionServiceImpl;
import system.service.MissionUnitServiceImpl;
import system.service.NotificationServiceImpl;
import system.service.SystemFunctionServiceImpl;
import system.service.SystemServiceImpl;
import system.service.DependentSystemServiceImpl;
import system.service.TaskReportServiceImpl;
import system.service.TaskServiceImpl;
import system.service.UnitDependencyServiceImpl;
import system.service.UploadFileServiceImpl;
import system.util.PropertyManager;
import system.vo.PartitionDependencyVo;
import system.vo.PartitionWrap;
import system.vo.UnitDependencyVo;
import system.vo.UnitWrap;
import system.vo.manager.PartitionWrapGetter;

@Named
@SessionScoped
@Interceptors(TimerInterceptor.class)
public class MissionUnitControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7294481655770499430L;
	private String partitionId;
	@Inject
	private MissionUnitServiceImpl missionUnitService;
	@Inject
	private MissionPartitionServiceImpl missionPartitionService;
	@Inject
	private MissionServiceImpl missionService;
	@Inject
	private UnitDependencyServiceImpl unitDependencyService;
	@Inject
	private TaskReportServiceImpl taskReportService;
	@Inject
	private UploadFileServiceImpl uploadFileService;
	@Inject
	private NotificationServiceImpl notificationService;
	@Inject
	private TaskServiceImpl taskService;
	@Inject
	private SystemServiceImpl systemService;
	@Inject
	private DependentSystemServiceImpl dependentSystemService;
	@Inject
	private SystemFunctionServiceImpl systemFunctionService;
	@Inject
	private DependentFunctionServiceImpl dependentFunctionService;
	@Inject
	private ApplicationServiceImpl applicationService;
	@Inject
	private PartitionWrapGetter partitionWrapGetter;
	@Inject
	private SessionManager sessionManager;
	// private UnitWrap[] selectedMissionUnits;
	// private MissionUnitListDataModel missionUnitListModel;
	// private UnitWrap editMissionUnit = new UnitWrap();
	private UnitWrap editMissionUnit = new UnitWrap();
	// private UnitWrap selectedMissionUnit;
	private Mission currentMission;
	private PartitionWrap currentPartition;

	private UnitDependencyVo[] selectedUnitDependencys;
	private UnitDependencyVoListDataModel unitDependencyVoListModel;
	private UnitDependency newUnitDependency = new UnitDependency();
	private UnitDependencyVo selectedUnitDependency;
	private TaskReport taskReport;
	private String report;
	private StreamedContent downloadFile;
	private boolean submittedUnitRejected = false;
	private List<UnitDependencyVo> baseUnitDependencyVoList;
	private List<UnitWrap> baseMissionUnitList;

	private DualListModel<UnitWrap> dependencyDual;
	private TreeNode root;
	private TreeNode selectedNode;

	private DualListModel<System> dependentSystem;
	private List<System> baseSystemList;

	private SystemFunctionListDataModel sourceFunctionModel;
	private SystemFunctionListDataModel targetFunctionModel;
	private boolean skip;
	private boolean addMode;
	private boolean updateMode;

	private UploadedFile file;

	private boolean checkPassed = false;
	private long authorityExpireTime;
	private String authorityPromotionReport;
	private List<SystemFunction> lackedFunctions = new ArrayList<SystemFunction>();

	private MissionUnit test;

	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		Map<String, String> varMap = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String tmp = varMap.get("partitionid");
		if (tmp != null)
			partitionId = tmp;
		else {

		}
		// throw new Exception("Illegal session state");
		currentPartition = partitionWrapGetter.get(partitionId);
		currentMission = missionService.get(currentPartition.getMissionid());
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("partition_id", partitionId);
		baseMissionUnitList = UnitWrap.convert(missionUnitService
				.findAllByCondition(sqlWhereMap));

		List<UnitDependency> baseDependencyList = unitDependencyService
				.findAllByCondition(null);
		List<UnitDependency> dependencyCopy = new ArrayList<UnitDependency>();

		for (UnitWrap unit : baseMissionUnitList) {
			for (UnitDependency ud : baseDependencyList) {
				if (ud.getPreUnitId().equals(unit.getId())
						|| ud.getPostUnitId().equals(unit.getId())) {
					if (!dependencyCopy.contains(ud)) {
						dependencyCopy.add(ud);
						break;
					}

				}
			}
		}
		baseDependencyList = dependencyCopy;
		baseUnitDependencyVoList = new ArrayList<UnitDependencyVo>();
		for (UnitDependency p : baseDependencyList) {
			baseUnitDependencyVoList.add(unitDependencyService.generateVo(p));
		}
		unitDependencyVoListModel = new UnitDependencyVoListDataModel(
				baseUnitDependencyVoList);
		buildTree(baseMissionUnitList);
		dependencyDual = new DualListModel<UnitWrap>(baseMissionUnitList,
				new ArrayList<UnitWrap>());

		baseSystemList = systemService.findDependentSystems(partitionId);
		dependentSystem = new DualListModel<System>();
		dependentSystem.setSource(baseSystemList);
		dependentSystem.setTarget(new ArrayList<System>());
		sourceFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		targetFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
	}

	private TreeNode buildTree(List<UnitWrap> unitList) throws Exception {
		root = new DefaultTreeNode("root", null);

		for (UnitWrap unit : unitList) {
			new DefaultTreeNode("unit", unit, root);
		}

		return root;
	}

	public void startCreateNewMissionUnit() {
		dependentSystem = new DualListModel<System>();
		dependentSystem.setSource(baseSystemList);
		dependentSystem.setTarget(new ArrayList<System>());
		editMissionUnit = new UnitWrap();
		sourceFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		targetFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		dependencyDual.setSource(baseMissionUnitList);
		dependencyDual.setTarget(new ArrayList<UnitWrap>());
		addMode = true;
		updateMode = false;
	}

	@Interceptors(TransactionInterceptor.class)
	public void startUpdateMissionUnit() throws Exception {
		UnitWrap selectedUnit = (UnitWrap) selectedNode.getData();
		dependencyDual.setTarget(selectedUnit.getDependentObj());
		List<UnitWrap> sourceUnitList = new ArrayList<UnitWrap>(
				baseMissionUnitList);
		for (UnitWrap unit : baseMissionUnitList) {
			if (unit.getUnit().equals(selectedUnit.getUnit()))
				sourceUnitList.remove(unit);
			for (UnitWrap target : selectedUnit.getDependentObj()) {
				if (unit.getUnit().equals(target.getUnit()))
					sourceUnitList.remove(unit);
			}
		}
		dependencyDual.setSource(sourceUnitList);
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("target_id", selectedUnit.getId());
		List<DependentSystem> dsList = dependentSystemService
				.findAllByCondition(sqlWhereMap);
		dependentSystem = new DualListModel<System>();
		List<System> target = new ArrayList<System>();
		for (DependentSystem ds : dsList) {
			target.add(systemService.get(ds.getSystemName()));
		}
		List<System> source = new ArrayList<System>(baseSystemList);
		// Collections.copy(source, baseSystemList);
		for (System s : baseSystemList) {
			for (System sys : target) {
				if (sys.equals(s)) {
					source.remove(s);
					break;
				}
			}

		}
		dependentSystem.setSource(source);
		dependentSystem.setTarget(target);
		// List<SystemFunction> list = systemFunctionService
		// .findFunctionBySystems(dependentSystem.getTarget());
		// sourceFunctionModel = new SystemFunctionListDataModel(list);
		targetFunctionModel = new SystemFunctionListDataModel(
				systemFunctionService.findFunctionByTarget(selectedUnit.getId()));
		editMissionUnit = selectedUnit;
		addMode = false;
		updateMode = true;
	}

	@Interceptors(TransactionInterceptor.class)
	public void doSaveMissionUnit() throws Exception {
		editMissionUnit.setPartitionid(partitionId);
		editMissionUnit.setFileId(getUploadFileId());
		missionUnitService.save(editMissionUnit.getUnit());
		baseMissionUnitList.add(editMissionUnit);
		new DefaultTreeNode("unit", editMissionUnit, root);
		List<UnitDependency> dependencyList = new ArrayList<UnitDependency>();
		List<UnitWrap> targetList = dependencyDual.getTarget();
		for (UnitWrap p : targetList) {
			UnitDependency tmp = new UnitDependency();
			tmp.setBelongedPartitionId(currentPartition.getId());
			tmp.setPreUnitId(p.getId());
			tmp.setPostUnitId(editMissionUnit.getId());
			dependencyList.add(tmp);
		}
		doSaveDependencys(dependencyList);

		// for (UnitDependency ud : dependencyList) {
		// baseUnitDependencyVoList.add(unitDependencyService.generateVo(ud));
		// }

		List<DependentSystem> dsList = new ArrayList<DependentSystem>();
		List<System> targetSystemList = dependentSystem.getTarget();
		for (System s : targetSystemList) {
			DependentSystem tmp = new DependentSystem(
					DependentSystem.Type.UNIT, editMissionUnit.getId(),
					s.getName());
			dsList.add(tmp);
		}
		dependentSystemService.save(dsList);
		List<SystemFunction> targetFunctions = (List<SystemFunction>) targetFunctionModel
				.getWrappedData();
		List<DependentFunction> dependentFunctions = new ArrayList<DependentFunction>();
		for (SystemFunction function : targetFunctions) {
			dependentFunctions.add(new DependentFunction("unit",
					editMissionUnit.getId(), function.getId()));
		}
		dependentFunctionService.save(dependentFunctions);

		editMissionUnit = new UnitWrap();
	}

	@Interceptors(TransactionInterceptor.class)
	public void doSaveDependency() throws Exception {
		newUnitDependency.setBelongedPartitionId(currentPartition.getId());
		FacesContext context = FacesContext.getCurrentInstance();
		for (UnitDependencyVo vo : baseUnitDependencyVoList) {
			if (vo.getUnitDependency().getPostUnitId()
					.equals(newUnitDependency.getPostUnitId())
					&& vo.getUnitDependency().getPreUnitId()
							.equals(newUnitDependency.getPreUnitId())) {
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Operation Fail",
						"Invalid dependency:" + newUnitDependency.getId()
								+ " Duplicate data."));
				return;
			}
		}
		if (unitDependencyService.check(newUnitDependency)) {
			baseUnitDependencyVoList.add(unitDependencyService
					.generateVo(newUnitDependency));
			unitDependencyService.save(newUnitDependency);
			newUnitDependency = new UnitDependency();
		} else {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Operation Fail",
					"Invalid dependency, circle found."));
		}
	}

	@Interceptors(TransactionInterceptor.class)
	private void doSaveDependencys(List<UnitDependency> list) throws Exception {
		FacesContext context = FacesContext.getCurrentInstance();
		for (UnitDependency p : list) {
			for (UnitDependencyVo vo : baseUnitDependencyVoList) {
				if (vo.getUnitDependency().getPostUnitId()
						.equals(p.getPostUnitId())
						&& vo.getUnitDependency().getPreUnitId()
								.equals(p.getPreUnitId())) {
					context.addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Operation Fail",
							"Invalid dependency:" + p.getId()
									+ " Duplicate data."));
					return;
				}
			}
			if (!unitDependencyService.check(p)) {
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Operation Fail",
						"Invalid dependency:" + p + "Circle found."));
				return;
			} else
				continue;
		}
		for (UnitDependency p : list) {
			UnitDependencyVo tmp = unitDependencyService.generateVo(p);
			baseUnitDependencyVoList.add(tmp);
			test = tmp.getPreUnit();
		}
		unitDependencyService.save(list);
	}

	//
	// public void startUpdateMissionUnit() throws Exception {
	// UnitWrap selectedUnit = (UnitWrap) selectedNode.getData();
	// Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
	// sqlWhereMap.put("target_id", selectedUnit.getId());
	// List<DependentSystem> dsList = dependentSystemService
	// .findAllByCondition(sqlWhereMap);
	// dependentSystem = new DualListModel<System>();
	// List<System> target = new ArrayList<System>();
	// for (DependentSystem ds : dsList) {
	// target.add(systemService.get(ds.getSystemName()));
	// }
	// List<System> source = new ArrayList<System>(baseSystemList);
	// // Collections.copy(source, baseSystemList);
	// for (System s : baseSystemList) {
	// for (System sys : target) {
	// if (sys.equals(s)) {
	// source.remove(s);
	// break;
	// }
	// }
	//
	// }
	// dependentSystem.setSource(source);
	// dependentSystem.setTarget(target);
	//
	// }

	@Interceptors(TransactionInterceptor.class)
	public String printDependentSystem() throws Exception {
		TreeNode node = selectedNode;
		if (node == null)
			return null;
		Object obj = node.getData();
		String id = null;
		if (obj instanceof Mission)
			id = ((Mission) obj).getId();
		if (obj instanceof MissionPartition)
			id = ((MissionPartition) obj).getId();
		if (obj instanceof UnitWrap)
			id = ((UnitWrap) obj).getId();

		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("target_id", id);
		List<DependentSystem> dsList = dependentSystemService
				.findAllByCondition(sqlWhereMap);
		String result = "";
		for (DependentSystem ds : dsList) {
			result += ds.getSystemName();
			result += ";";
		}
		if (result.equals(""))
			result = "No dependent system.";
		return result;
	}

	@Interceptors(TransactionInterceptor.class)
	public String onFlowProcess(FlowEvent event) throws Exception {
		if (event.getNewStep().equals("dependentSystems")) {
			// dependentSystem = new DualListMode l<System>();
			// dependentSystem.setSource(baseSystemList);
			// dependentSystem.setTarget(new ArrayList<System>());
		}
		if (event.getNewStep().equals("dependentFunctions")) {
			List<System> targetSystems = dependentSystem.getTarget();
			List<SystemFunction> list = systemFunctionService
					.findFunctionBySystems(dependentSystem.getTarget());
			List<SystemFunction> parentList = systemFunctionService
					.findFunctionByTarget(partitionId);
			List<SystemFunction> copyParentList = new ArrayList<SystemFunction>(
					parentList);
			for (SystemFunction sf : copyParentList) {
				for (int i = 0; i < list.size(); ++i) {

					if (sf.getId().equals(list.get(i).getId())) {
						break;
					} else {
						if (i == list.size() - 1)
							parentList.remove(sf);
					}
				}

			}
			if (list.size() == 0)
				parentList.clear();
			sourceFunctionModel = new SystemFunctionListDataModel(parentList);

			List<SystemFunction> baseTargetFunction = (List<SystemFunction>) targetFunctionModel
					.getWrappedData();
			for (int i = baseTargetFunction.size() - 1; i >= 0; --i) {
				if (targetSystems.size() == 0)
					baseTargetFunction.clear();
				for (int j = 0; j < targetSystems.size(); ++j) {
					if (baseTargetFunction.get(i).getSystemName()
							.equals(targetSystems.get(j).getName())) {
						break;
					}
					if (j == targetSystems.size() - 1)
						baseTargetFunction.remove(i);
				}
			}
			List<SystemFunction> baseSourceFunction = (List<SystemFunction>) sourceFunctionModel
					.getWrappedData();
			List<SystemFunction> copySource = new ArrayList<SystemFunction>(
					baseSourceFunction);
			for (SystemFunction sf : copySource) {
				for (SystemFunction targetSf : baseTargetFunction) {
					if (sf.equals(targetSf)) {
						baseSourceFunction.remove(sf);
						break;
					}
				}
			}
		}
		if (skip) {
			skip = false; // reset in case user goes back
			return "description";
		}
		return event.getNewStep();
	}

	public void deleteTargetFunction(SystemFunction function) {
		((List<SystemFunction>) targetFunctionModel.getWrappedData())
				.remove(function);

		List<SystemFunction> source = (List<SystemFunction>) sourceFunctionModel
				.getWrappedData();
		source.add(function);
	}

	@SuppressWarnings("unchecked")
	public void onFunctionDroped(DragDropEvent ddEvent) {
		SystemFunction function = ((SystemFunction) ddEvent.getData());
		((List<SystemFunction>) sourceFunctionModel.getWrappedData())
				.remove(function);
		List<SystemFunction> targetList = ((List<SystemFunction>) targetFunctionModel
				.getWrappedData());
		for (SystemFunction f : targetList) {
			if (f.getId().equals(function.getId()))
				return;
		}

		((List<SystemFunction>) targetFunctionModel.getWrappedData())
				.add(function);

	}

	@Interceptors(TransactionInterceptor.class)
	public void doUpdateMissionUnit() throws Exception {
		UnitWrap selectedUnit = (UnitWrap) selectedNode.getData();
		String uploadFileid = getUploadFileId();
		if (!uploadFileid.equals("none") && uploadFileid != null)
			selectedUnit.setFileId(uploadFileid);
		missionUnitService.update(selectedUnit.getUnit());

		List<DependentSystem> dsList = new ArrayList<DependentSystem>();
		List<System> targetList = dependentSystem.getTarget();
		for (System s : targetList) {
			DependentSystem tmp = new DependentSystem(
					DependentSystem.Type.PARTITION, selectedUnit.getId(),
					s.getName());
			dsList.add(tmp);
		}
		dependentSystemService.refresh(dsList, selectedUnit.getId());
		List<SystemFunction> targetFunctions = (List<SystemFunction>) targetFunctionModel
				.getWrappedData();
		List<DependentFunction> dependentFunctions = new ArrayList<DependentFunction>();
		for (SystemFunction function : targetFunctions) {
			dependentFunctions.add(new DependentFunction("unit",
					editMissionUnit.getId(), function.getId()));
		}
		dependentFunctionService.refresh(dependentFunctions,
				editMissionUnit.getId());
	}

	@Interceptors(TransactionInterceptor.class)
	public void doUpdateUnitDependency() throws Exception {

		newUnitDependency.setBelongedPartitionId(currentPartition.getId());
		FacesContext context = FacesContext.getCurrentInstance();
		if (unitDependencyService.check(selectedUnitDependency
				.getUnitDependency())) {
			unitDependencyService.update(selectedUnitDependency
					.getUnitDependency());
			replaceUnitDependencyVoData(unitDependencyService
					.generateVo(selectedUnitDependency.getUnitDependency()));
		} else {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Operation Fail",
					"Invalid dependency, circle found."));
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeleteMissionUnit() throws Exception {
		UnitWrap selectedUnit = (UnitWrap) selectedNode.getData();
		missionUnitService.delete(selectedUnit.getId());
		selectedNode.setParent(null);

		baseMissionUnitList.remove(selectedUnit);

		List<UnitDependency> deleteList = new ArrayList<UnitDependency>();
		List<UnitDependencyVo> copyTmp = new ArrayList<UnitDependencyVo>(
				baseUnitDependencyVoList);
		for (UnitDependencyVo dependency : copyTmp) {
			if (dependency.getPostUnit().equals(selectedUnit.getUnit())
					|| dependency.getPreUnit().equals(selectedUnit.getUnit())) {
				baseUnitDependencyVoList.remove(dependency);
				deleteList.add(dependency.getUnitDependency());
			}

		}

		if (deleteList.size() > 0) {
			String[] ids = new String[deleteList.size()];
			for (int i = 0; i < deleteList.size(); ++i) {
				ids[i] = deleteList.get(i).getId();
			}
			unitDependencyService.delete(ids);
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeleteUnitDependency() throws Exception {
		unitDependencyService.delete(selectedUnitDependency.getUnitDependency()
				.getId());
		List<UnitDependencyVo> baseUnitDependencyList = (List<UnitDependencyVo>) unitDependencyVoListModel
				.getWrappedData();
		int index = findUnitDependencyVoData(selectedUnitDependency
				.getUnitDependency().getId());
		if (index >= 0) {
			baseUnitDependencyList.remove(index);
		}

	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeliverMissionUnit() throws Exception {
		// missionUnitService.startMissionUnit(selectedMissionUnit);
		// messageService.sendMessage(
		// selectedMissionUnit.getLeaderName(),
		// StringTemplate.startUnitMsgTitle,
		// StringTemplate.startUnitMsgBody, "none");
		// taskService.startNewUnitTask(
		// selectedMissionUnit);
		UnitWrap selectedUnit = (UnitWrap) selectedNode.getData();
		missionUnitService.deliverUnit(selectedUnit.getUnit(), 0, "none");
		notificationService.sendStartUnitNotification(selectedUnit.getUnit(),
				"none");
		taskService.startNewUnitTask(selectedUnit.getUnit(), 0, "none", true);
	}

	private void replaceUnitDependencyVoData(
			UnitDependencyVo updatedUnitDependency) {
		int index = findUnitDependencyVoData(updatedUnitDependency
				.getUnitDependency().getId());
		if (index >= 0) {
			List<UnitDependencyVo> baseUnitDependencyList = (List<UnitDependencyVo>) unitDependencyVoListModel
					.getWrappedData();
			baseUnitDependencyList.set(index, new UnitDependencyVo(
					updatedUnitDependency));
		}
	}

	private int findUnitDependencyVoData(String name) {
		List<UnitDependencyVo> baseUnitDependencyList = (List<UnitDependencyVo>) unitDependencyVoListModel
				.getWrappedData();
		for (int i = 0; i < baseUnitDependencyList.size(); i++) {
			UnitDependencyVo dependency = baseUnitDependencyList.get(i);
			if (dependency.getUnitDependency().getId().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	@Interceptors(TransactionInterceptor.class)
	public void showTaskReport() throws Exception {
		UnitWrap selectedMissionUnit = (UnitWrap) selectedNode.getData();
		taskReport = taskReportService.findReportWithTarget(selectedMissionUnit
				.getId());
		submittedUnitRejected = false;
	}

	@Interceptors(TransactionInterceptor.class)
	public void processDownload() throws Exception {
		if (!taskReport.getFileId().equals("none")) {
			UploadFile file = uploadFileService.get(taskReport.getFileId());
			InputStream stream = new FileInputStream(file.getFilePath()
					+ file.getFileName());
			downloadFile = new DefaultStreamedContent(stream, "test",
					file.getFileName());
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void acceptTaskReport() throws Exception {
		UnitWrap selectedMissionUnit = (UnitWrap) selectedNode.getData();
		missionUnitService.acceptUnit(selectedMissionUnit.getUnit());

		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("data", selectedMissionUnit.getId());
		sqlMap.put("statue", Task.StatueType.SUBMITTED.ordinal());
		List<Task> results = taskService.findAllByCondition(sqlMap);

		Task task = results.get(0);
		task.setStatue(Task.StatueType.COMPLETED);
		taskService.update(task);

		notificationService
				.sendAcceptSubmittedUnitNotification(selectedMissionUnit
						.getUnit());
	}

	@Interceptors(TransactionInterceptor.class)
	public void submitRejectReport() throws Exception {
		UnitWrap selectedMissionUnit = (UnitWrap) selectedNode.getData();
		submittedUnitRejected = true;
		// notificationService.sendRejectSubmittedUnitNotification(selectedMissionUnit,
		// report);
		missionUnitService.rejectCompleteUnit(selectedMissionUnit.getUnit(),
				report);

		Task task = taskService.findTaskByDataid(selectedMissionUnit.getId());
		task.setStatue(Task.StatueType.SUBMISSION_REJECTED);
		taskService.update(task);

		notificationService.sendRejectSubmittedUnitNotification(
				selectedMissionUnit.getUnit(), report, "none");
		report = null;
	}

	@Interceptors(TransactionInterceptor.class)
	public void preDeliveryCheck() throws Exception {
		UnitWrap selectedMissionUnit = (UnitWrap) selectedNode.getData();
		List<SystemFunction> dependentFunctions = systemFunctionService
				.findFunctionByTarget(selectedMissionUnit.getId());

		List<SystemFunction> avaliableFunctions = systemFunctionService
				.findFunctionByUser(selectedMissionUnit.getLeaderName());

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
		if (lackedFunctions.size() != 0)
			checkPassed = false;
		else
			checkPassed = true;
	}

	@Interceptors(TransactionInterceptor.class)
	public void promoteAuthority() throws Exception {
		UnitWrap selectedMissionUnit = (UnitWrap) selectedNode.getData();
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("sender_name", selectedMissionUnit.getLeaderName());
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
			authorityPromotionReport = null;
			return;
		}
		applicationService.sendPromoteAuthorityApplication(lackedFunctions,
				selectedMissionUnit.getLeaderName(), authorityPromotionReport,
				"none");
		// selectedMissionUnit.setProgress(-10);
		// missionUnitService.save(selectedMissionUnit);
		doDeliverMissionUnit();
		authorityPromotionReport = null;
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

	public String getTypeStr(Object obj) {
		if (obj instanceof Mission)
			return "Mission";
		if (obj instanceof PartitionWrap)
			return "Partition";
		if (obj instanceof UnitWrap)
			return "Unit";
		return "error";
	}

	public boolean preSubmissionCheck() {
		List<PartitionWrap> dependentPartition = currentPartition
				.getDependentObj();
		for (PartitionWrap partition : dependentPartition) {
			if (partition.getProgress() != 100)
				return false;
		}
		return true;
	}

	@Interceptors(TransactionInterceptor.class)
	public void submitPartition() throws Exception {
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
		for (UnitWrap unit : baseMissionUnitList) {
			if (unit.getProgress() != 100) {
				context.addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Submission failed.",
								"You can not submit the partition since there exist some unfinished tasks."));
				return;
			}
		}

		Task currentTask = taskService.findTaskByDataid(partitionId);
		currentTask.setStatue(Task.StatueType.SUBMITTED);
		taskService.update(currentTask);
		String uploadFileId = getUploadFileId();
		TaskReport taskReport = taskReportService
				.findReportWithTask(currentTask.getId());
		if (taskReport == null) {
			taskReport = new TaskReport(currentTask.getId(), report,
					uploadFileId);
			taskReportService.save(taskReport);
		} else {
			taskReport.setReport(report);
			taskReport.setFileId(uploadFileId);
			taskReportService.update(taskReport);
		}

		missionPartitionService.submitPartition(currentMission.getLeaderName(),
				currentPartition.getPartition(), report, uploadFileId);
		notificationService.sendSubmitPartitionNotification(
				currentMission.getLeaderName(),
				currentPartition.getPartition(), report, uploadFileId);
		report = null;
	}

	// getter/setter

	public String getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(String partitionId) {
		this.partitionId = partitionId;
	}

	public Mission getCurrentMission() {
		return currentMission;
	}

	public void setCurrentMission(Mission currentMission) {
		this.currentMission = currentMission;
	}

	public PartitionWrap getCurrentPartition() {
		return currentPartition;
	}

	public void setCurrentPartition(PartitionWrap currentPartition) {
		this.currentPartition = currentPartition;
	}

	public UnitDependencyVo[] getSelectedUnitDependencys() {
		return selectedUnitDependencys;
	}

	public void setSelectedUnitDependencys(
			UnitDependencyVo[] selectedUnitDependencys) {
		this.selectedUnitDependencys = selectedUnitDependencys;
	}

	public UnitDependencyVoListDataModel getUnitDependencyVoListModel() {
		return unitDependencyVoListModel;
	}

	public void setUnitDependencyVoListModel(
			UnitDependencyVoListDataModel unitDependencyVoListModel) {
		this.unitDependencyVoListModel = unitDependencyVoListModel;
	}

	public UnitDependency getNewUnitDependency() {
		return newUnitDependency;
	}

	public void setNewUnitDependency(UnitDependency newUnitDependency) {
		this.newUnitDependency = newUnitDependency;
	}

	public UnitDependencyVo getSelectedUnitDependency() {
		return selectedUnitDependency;
	}

	public void setSelectedUnitDependency(
			UnitDependencyVo selectedUnitDependency) {
		this.selectedUnitDependency = selectedUnitDependency;
	}

	public TaskReport getTaskReport() {
		return taskReport;
	}

	public void setTaskReport(TaskReport taskReport) {
		this.taskReport = taskReport;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public StreamedContent getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(StreamedContent downloadFile) {
		this.downloadFile = downloadFile;
	}

	public boolean isSubmittedUnitRejected() {
		return submittedUnitRejected;
	}

	public void setSubmittedUnitRejected(boolean submittedUnitRejected) {
		this.submittedUnitRejected = submittedUnitRejected;
	}

	public List<UnitDependencyVo> getBaseUnitDependencyVoList() {
		return baseUnitDependencyVoList;
	}

	public void setBaseUnitDependencyVoList(
			List<UnitDependencyVo> baseUnitDependencyVoList) {
		this.baseUnitDependencyVoList = baseUnitDependencyVoList;
	}

	public DualListModel<UnitWrap> getDependencyDual() {
		return dependencyDual;
	}

	public void setDependencyDual(DualListModel<UnitWrap> dependencyDual) {
		this.dependencyDual = dependencyDual;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public DualListModel<System> getDependentSystem() {
		return dependentSystem;
	}

	public void setDependentSystem(DualListModel<System> dependentSystem) {
		this.dependentSystem = dependentSystem;
	}

	public List<System> getBaseSystemList() {
		return baseSystemList;
	}

	public void setBaseSystemList(List<System> baseSystemList) {
		this.baseSystemList = baseSystemList;
	}

	public List<UnitWrap> getBaseMissionUnitList() {
		return baseMissionUnitList;
	}

	public void setBaseMissionUnitList(List<UnitWrap> baseMissionUnitList) {
		this.baseMissionUnitList = baseMissionUnitList;
	}

	public UnitWrap getEditMissionUnit() {
		return editMissionUnit;
	}

	public void setEditMissionUnit(UnitWrap editMissionUnit) {
		this.editMissionUnit = editMissionUnit;
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

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public boolean isAddMode() {
		return addMode;
	}

	public void setAddMode(boolean addMode) {
		this.addMode = addMode;
	}

	public boolean isUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}

	public boolean isCheckPassed() {
		return checkPassed;
	}

	public void setCheckPassed(boolean checkPassed) {
		this.checkPassed = checkPassed;
	}

	public List<SystemFunction> getLackedFunctions() {
		return lackedFunctions;
	}

	public void setLackedFunctions(List<SystemFunction> lackedFunctions) {
		this.lackedFunctions = lackedFunctions;
	}

	public long getAuthorityExpireTime() {
		return authorityExpireTime;
	}

	public void setAuthorityExpireTime(long authorityExpireTime) {
		this.authorityExpireTime = authorityExpireTime;
	}

	public String getAuthorityPromotionReport() {
		return authorityPromotionReport;
	}

	public void setAuthorityPromotionReport(String authorityPromotionReport) {
		this.authorityPromotionReport = authorityPromotionReport;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

}
