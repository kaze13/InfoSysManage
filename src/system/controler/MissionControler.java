package system.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
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

import org.primefaces.component.layout.LayoutUnit;
import org.primefaces.component.tree.UITreeNode;
import org.primefaces.component.treetable.TreeTable;
import org.primefaces.component.wizard.Wizard;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import sun.org.mozilla.javascript.internal.Node;
import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.SystemFunctionListDataModel;
import system.manager.spec.SessionManager;
import system.po.DependentFunction;
import system.po.DependentSystem;
import system.po.MissionPartition;
import system.po.MissionUnit;
import system.po.PartitionDependency;
import system.po.System;
import system.po.SystemFunction;
import system.po.Task;
import system.po.TaskReport;
import system.po.UnitDependency;
import system.po.UploadFile;
import system.po.User;
import system.service.ApplicationServiceImpl;
import system.service.DependentFunctionServiceImpl;
import system.service.DependentSystemServiceImpl;
import system.service.MissionPartitionServiceImpl;
import system.service.MissionServiceImpl;
import system.service.MissionUnitServiceImpl;
import system.service.NotificationServiceImpl;
import system.service.PartitionDependencyServiceImpl;
import system.service.SystemFunctionServiceImpl;
import system.service.SystemServiceImpl;
import system.service.TaskReportServiceImpl;
import system.service.TaskServiceImpl;
import system.service.UnitDependencyServiceImpl;
import system.service.UploadFileServiceImpl;
import system.service.UserServiceImpl;
import system.util.PropertyManager;
import system.util.ToolBox;
import system.util.XmlBuilder;
import system.vo.MissionWrap;
import system.vo.PartitionWrap;
import system.vo.UnitWrap;
import system.vo.UserWrap;

@Named
@SessionScoped
public class MissionControler implements Serializable {

	/**
	 * s
	 *
	 */
	private static final long serialVersionUID = -3429814736642495213L;

	@Inject
	private MissionServiceImpl missionService;
	@Inject
	private MissionPartitionServiceImpl missionPartitionService;
	@Inject
	private MissionUnitServiceImpl missionUnitService;
	@Inject
	private SystemServiceImpl systemService;
	@Inject
	private DependentSystemServiceImpl dependentSystemService;
	@Inject
	private DependentFunctionServiceImpl dependentFunctionService;
	@Inject
	private SystemFunctionServiceImpl systemFunctionService;
	@Inject
	private ApplicationServiceImpl applicationService;
	@Inject
	private UploadFileServiceImpl uploadFileService;
	@Inject
	private NotificationServiceImpl notificationService;
	@Inject
	private TaskServiceImpl taskService;
	@Inject
	private TaskReportServiceImpl taskReportService;
	@Inject
	private UserServiceImpl userService;
	@Inject
	private PartitionDependencyServiceImpl partitionDependencyService;
	@Inject
	private UnitDependencyServiceImpl unitDependencyService;
	@Inject
	private XmlBuilder xmlBuilder;
	private SystemFunctionListDataModel sourceFunctionModel;
	private SystemFunctionListDataModel targetFunctionModel;
	// private MissionWrap[] selectedMissions;
	// private MissionListDataModel missionListModel;
	private MissionWrap editMission = new MissionWrap();
	// private MissionWrap selectedMission;

	private StreamedContent downloadFile;
	private TreeNode root;
	private TreeNode selectedNode;

	private boolean updateMode = false;
	private boolean addMode = false;

	private DualListModel<System> dependentSystem;
	private List<System> baseSystemList;
	private Wizard wizard;
	private boolean skip;
	private UploadedFile file;
	private String report;
	private UnitWrap selectedUnit;
	private TreeNode exportRoot;
	private Object selectedExportData;

	// private List<User> allUserList;
	@Inject
	private SessionManager sessionManager;

	private LayoutUnit dataUnit;
	private LayoutUnit detialUnit;

	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("creator_name", sessionManager.getLoginUser().getName());
		List<MissionWrap> baseMissionList = MissionWrap.convert(missionService
				.findAllByCondition(sqlWhereMap));
		baseSystemList = systemService.findAllByCondition(null);
		root = buildTree(baseMissionList);

		dependentSystem = new DualListModel<System>();
		dependentSystem.setSource(baseSystemList);
		dependentSystem.setTarget(new ArrayList<System>());
		sourceFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		targetFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		// allUserList = userService.findAllByCondition(null);
	}

	@Interceptors(TransactionInterceptor.class)
	private TreeNode buildTree(List<MissionWrap> missionList) throws Exception {
		TreeNode theRoot = new DefaultTreeNode("root", null);
		for (MissionWrap mission : missionList) {
			TreeNode editMissionNode = new DefaultTreeNode("mission", mission,
					theRoot);
			List<PartitionWrap> partitionList = PartitionWrap
					.convert(missionPartitionService
							.findPartitionWithMission(mission.getId()));
			for (PartitionWrap partition : partitionList) {
				TreeNode newPartitionNode = new DefaultTreeNode("partition",
						partition, editMissionNode);
				List<UnitWrap> unitList = UnitWrap.convert(missionUnitService
						.findUnitWithPartition(partition.getId()));
				for (UnitWrap unit : unitList) {
					new DefaultTreeNode("unit", unit, newPartitionNode);
				}
			}

		}
		return theRoot;
	}

	public void startImportMission(MissionWrap mission) throws Exception {
		editMission = mission;
		editMission.setId(UUID.randomUUID().toString());
		editMission.setProgress(-1);

		addMode = true;
		dependentSystem = new DualListModel<System>();
		List<System> target = editMission.getDependentSystems();
		if (target.size() > 0) {
			for (int i = target.size() - 1; i >= 0; --i) {
				System theSystem = systemService.get(target.get(i).getName());
				if (theSystem == null)
					target.remove(i);
			}
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

		List<SystemFunction> targetFunction = new ArrayList<SystemFunction>(
				editMission.getDependentFunctions());
		if (targetFunction.size() > 0) {
			for (int i = targetFunction.size() - 1; i >= 0; --i) {
				SystemFunction theFunction = systemFunctionService
						.get(targetFunction.get(i).getId());
				if (theFunction == null)
					targetFunction.remove(i);
			}
		}
		targetFunctionModel = new SystemFunctionListDataModel(targetFunction);
	}

	public void startCreateNewMission() {
		dependentSystem = new DualListModel<System>();
		dependentSystem.setSource(baseSystemList);
		dependentSystem.setTarget(new ArrayList<System>());
		editMission = new MissionWrap();
		sourceFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		targetFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		addMode = true;
		updateMode = false;
	}

	@Interceptors(TransactionInterceptor.class)
	public void startUpdateMission() throws Exception {
		MissionWrap selectedMission = (MissionWrap) selectedNode.getData();
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("target_id", selectedMission.getId());
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
				systemFunctionService.findFunctionByTarget(selectedMission
						.getId()));

		editMission = selectedMission;
		addMode = false;
		updateMode = true;
	}

	@Interceptors(TransactionInterceptor.class)
	public String printDependentSystem() throws Exception {
		TreeNode node = selectedNode;
		if (node == null)
			return null;
		Object obj = node.getData();
		String id = null;
		if (obj instanceof MissionWrap)
			id = ((MissionWrap) obj).getId();
		if (obj instanceof PartitionWrap)
			id = ((PartitionWrap) obj).getId();
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
			// dependentSystem = new DualListModel<System>();
			// dependentSystem.setSource(baseSystemList);
			// dependentSystem.setTarget(new ArrayList<System>());
		}

		if (event.getNewStep().equals("dependentFunctions")) {
			List<System> targetSystems = dependentSystem.getTarget();
			List<SystemFunction> list = systemFunctionService
					.findFunctionBySystems(dependentSystem.getTarget());
			sourceFunctionModel = new SystemFunctionListDataModel(list);
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
	public void doSaveMission() throws Exception {
		editMission.setCreatorName(sessionManager.getLoginUser().getName());
		editMission.setFileId(getUploadFileId());
		missionService.save(editMission.getMission());

		if (editMission.getPartitionList() != null) {
			List<MissionPartition> partitionList = new ArrayList<MissionPartition>();
			List<MissionUnit> unitList = new ArrayList<MissionUnit>();
			List<DependentFunction> dependentFunctionList = new ArrayList<DependentFunction>();
			List<DependentSystem> dependentSystemList = new ArrayList<DependentSystem>();
			List<PartitionDependency> partitionDependencyList = partitionDependencyService
					.findAllByCondition(null);
			List<PartitionDependency> partitionDependencyListCopy = new ArrayList<PartitionDependency>(
					partitionDependencyList);
			List<UnitDependency> unitDependencyList = unitDependencyService
					.findAllByCondition(null);
			List<UnitDependency> unitDependencyListCopy = new ArrayList<UnitDependency>(
					unitDependencyList);
			for (PartitionWrap partition : editMission.getPartitionList()) {
				MissionPartition partitionTmp = partition.getPartition();
				String newPartitionId = UUID.randomUUID().toString();
				for (PartitionDependency pd : partitionDependencyList) {
					if (pd.getPrePartitionId().equals(partition.getId())) {
						PartitionDependency newItem = new PartitionDependency(
								pd);
						partitionDependencyList.set(
								partitionDependencyList.indexOf(pd), newItem);
						newItem.setPrePartitionId(newPartitionId);
						newItem.setBelongedMissionId(editMission.getId());
						newItem.setId(UUID.randomUUID().toString());
					}
					if (pd.getPostPartitionId().equals(partition.getId())) {
						PartitionDependency newItem = new PartitionDependency(
								pd);
						partitionDependencyList.set(
								partitionDependencyList.indexOf(pd), newItem);
						newItem.setPostPartitionId(newPartitionId);
						newItem.setBelongedMissionId(editMission.getId());
						newItem.setId(UUID.randomUUID().toString());
					}
				}
				partitionTmp.setId(newPartitionId);
				partitionTmp.setProgress(-1);
				partitionTmp.setMissionid(editMission.getId());
				partitionTmp.setFileId("none");
				partitionList.add(partitionTmp);
				if (partition.getDependentFunctions() != null) {
					for (SystemFunction sf : partition.getDependentFunctions()) {
						dependentFunctionList.add(new DependentFunction(
								"partition", partition.getId(), sf.getId()));
					}
				}
				if (partition.getDependentSystems() != null) {
					for (System s : partition.getDependentSystems()) {
						dependentSystemList.add(new DependentSystem(
								DependentSystem.Type.PARTITION, partition
										.getId(), s.getName()));
					}
				}
				if (partition.getUnitList() != null) {
					for (UnitWrap unit : partition.getUnitList()) {
						MissionUnit unitTmp = unit.getUnit();

						String newUnitId = UUID.randomUUID().toString();
						for (UnitDependency pd : unitDependencyList) {
							if (pd.getPreUnitId().equals(unit.getId())) {
								UnitDependency newItem = new UnitDependency(pd);
								unitDependencyList
										.set(unitDependencyList.indexOf(pd),
												newItem);
								newItem.setPreUnitId(newUnitId);
								newItem.setBelongedPartitionId(partitionTmp
										.getId());
								newItem.setId(UUID.randomUUID().toString());
							}
							if (pd.getPostUnitId().equals(unit.getId())) {
								UnitDependency newItem = new UnitDependency(pd);
								unitDependencyList
										.set(unitDependencyList.indexOf(pd),
												newItem);
								newItem.setPostUnitId(newUnitId);
								newItem.setBelongedPartitionId(partitionTmp
										.getId());
								newItem.setId(UUID.randomUUID().toString());
							}
						}
						unitTmp.setId(newUnitId);
						unitTmp.setProgress(-1);
						unitTmp.setPartitionid(partitionTmp.getId());
						unitTmp.setFileId("none");
						unitList.add(unitTmp);
						if (unit.getDependentFunctions() != null) {
							for (SystemFunction sf : unit
									.getDependentFunctions()) {
								dependentFunctionList
										.add(new DependentFunction("unit", unit
												.getId(), sf.getId()));
							}
						}
						if (unit.getDependentSystems() != null) {
							for (System s : unit.getDependentSystems()) {
								dependentSystemList.add(new DependentSystem(
										DependentSystem.Type.UNIT,
										unit.getId(), s.getName()));
							}
						}
					}
				}
			}
			missionPartitionService.save(partitionList);
			missionUnitService.save(unitList);
			dependentFunctionService.save(dependentFunctionList);
			dependentSystemService.save(dependentSystemList);

			for (PartitionDependency pd : partitionDependencyListCopy) {
				if (partitionDependencyList.contains(pd))
					partitionDependencyList.remove(pd);
			}
			for (UnitDependency pd : unitDependencyListCopy) {
				if (unitDependencyList.contains(pd))
					unitDependencyList.remove(pd);
			}
			partitionDependencyService.save(partitionDependencyList);
			unitDependencyService.save(unitDependencyList);
		}
		new DefaultTreeNode("mission", editMission, root);

		List<DependentSystem> dsList = new ArrayList<DependentSystem>();
		List<System> targetList = dependentSystem.getTarget();
		for (System s : targetList) {
			DependentSystem tmp = new DependentSystem(
					DependentSystem.Type.MISSION, editMission.getId(),
					s.getName());
			dsList.add(tmp);
		}
		dependentSystemService.save(dsList);
		List<SystemFunction> targetFunctions = (List<SystemFunction>) targetFunctionModel
				.getWrappedData();
		List<DependentFunction> dependentFunctions = new ArrayList<DependentFunction>();
		for (SystemFunction function : targetFunctions) {
			dependentFunctions.add(new DependentFunction("mission", editMission
					.getId(), function.getId()));
		}
		dependentFunctionService.save(dependentFunctions);
		editMission = new MissionWrap();
	}

	@Interceptors(TransactionInterceptor.class)
	public void doUpdateMission() throws Exception {
		MissionWrap selectedMission = (MissionWrap) selectedNode.getData();
		String uploadFileid = getUploadFileId();
		if (!uploadFileid.equals("none") && uploadFileid != null)
			selectedMission.setFileId(uploadFileid);
		missionService.update(selectedMission.getMission());
		List<DependentSystem> dsList = new ArrayList<DependentSystem>();
		List<System> targetList = dependentSystem.getTarget();
		for (System s : targetList) {
			DependentSystem tmp = new DependentSystem(
					DependentSystem.Type.MISSION, selectedMission.getId(),
					s.getName());
			dsList.add(tmp);
		}
		dependentSystemService.refresh(dsList, selectedMission.getId());
		// dependentSystemService.save(dsList);
		List<SystemFunction> targetFunctions = (List<SystemFunction>) targetFunctionModel
				.getWrappedData();
		List<DependentFunction> dependentFunctions = new ArrayList<DependentFunction>();
		for (SystemFunction function : targetFunctions) {
			dependentFunctions.add(new DependentFunction("mission", editMission
					.getId(), function.getId()));
		}
		dependentFunctionService.refresh(dependentFunctions,
				selectedMission.getId());
	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeleteMission() throws Exception {
		MissionWrap selectedMission = (MissionWrap) selectedNode.getData();
		List<PartitionWrap> partitionDeleteList = PartitionWrap
				.convert((missionPartitionService
						.findPartitionWithMission(selectedMission.getId())));
		List<UnitWrap> unitDeleteList = new ArrayList<UnitWrap>();
		for (PartitionWrap p : partitionDeleteList) {
			List<UnitWrap> currentUnitList = UnitWrap
					.convert(missionUnitService.findUnitWithPartition(p.getId()));

			unitDeleteList.addAll(currentUnitList);

		}
		String[] partitionIds = new String[partitionDeleteList.size()];
		String[] unitIds = new String[unitDeleteList.size()];
		for (int i = 0; i < partitionDeleteList.size(); ++i) {
			partitionIds[i] = partitionDeleteList.get(i).getId();
		}
		for (int i = 0; i < unitDeleteList.size(); ++i) {
			unitIds[i] = unitDeleteList.get(i).getId();
		}
		missionService.delete(selectedMission.getId());
		missionPartitionService.delete(partitionIds);
		missionUnitService.delete(unitIds);
		root.getChildren().remove(selectedNode);
		selectedNode.setParent(null);
		selectedNode = null;
	}

	@Interceptors(TransactionInterceptor.class)
	public void doVerifyMission() throws Exception {
		List<SystemFunction> dependentFunctions = (List<SystemFunction>) targetFunctionModel
				.getWrappedData();
		missionService.verifyMission(((MissionWrap) selectedNode.getData())
				.getMission());
		applicationService.sendVerifyMissionApplication(
				((MissionWrap) selectedNode.getData()).getMission(),
				((MissionWrap) selectedNode.getData()).getDependentFunctions());
	}

	@Interceptors(TransactionInterceptor.class)
	public void acceptMissionReport() throws Exception {
		MissionWrap selectedMission = (MissionWrap) selectedNode.getData();
		missionService.acceptMission(selectedMission.getMission());

		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("data", selectedMission.getId());
		sqlMap.put("statue", Task.StatueType.SUBMITTED.ordinal());
		List<Task> results = taskService.findAllByCondition(sqlMap);

		Task task = results.get(0);
		task.setStatue(Task.StatueType.COMPLETED);
		taskService.update(task);

		notificationService
				.sendAcceptSubmittedMissionNotification(selectedMission
						.getMission());
	}

	@Interceptors(TransactionInterceptor.class)
	public void submitRejectReport() throws Exception {
		MissionWrap selectedMission = (MissionWrap) selectedNode.getData();
		// notificationService.sendRejectSubmittedUnitNotification(selectedMissionUnit,
		// report);
		missionService.rejectCompleteMission(selectedMission.getMission(),
				report);

		Task task = taskService.findTaskByDataid(selectedMission.getId());
		task.setStatue(Task.StatueType.SUBMISSION_REJECTED);
		taskService.update(task);

		List<Task> unitTasks = taskService
				.findPartitionTasksByMission(selectedMission.getId());
		for (Task t : unitTasks) {
			t.setStatue(Task.StatueType.SUBMITTED);
		}
		taskService.update(unitTasks);
		List<MissionPartition> partitions = missionPartitionService
				.findPartitionWithMission(selectedMission.getId());
		for (MissionPartition p : partitions) {
			p.setProgress(-4); // change to submitted.
		}
		missionPartitionService.update(partitions);
		notificationService.sendRejectSubmittedMissionNotification(
				selectedMission.getMission(), report, "none");
		report = null;
	}

	@Interceptors(TransactionInterceptor.class)
	public void showMissionReport() throws Exception {
		MissionWrap selectedMission = (MissionWrap) selectedNode.getData();
		if (selectedMission.getTask() == null) {
			selectedMission.setTask(taskService
					.findTaskByDataid(selectedMission.getId()));
		}
		if (selectedMission.getTaskReport() == null) {
			TaskReport report = taskReportService
					.findReportWithTask(selectedMission.getTask().getId());
			selectedMission.setTaskReport(report);
		}

		if (selectedMission.getPartitionList() == null) {
			List<MissionPartition> partitionList = missionPartitionService
					.findPartitionWithMission(selectedMission.getId());
			selectedMission.setPartitionList(PartitionWrap
					.convert(partitionList));
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void onPartitionToggle(ToggleEvent event) throws Exception {
		PartitionWrap currentPartition = (PartitionWrap) event.getData();
		if (currentPartition.getUnitList() == null) {
			List<MissionUnit> unitList = missionUnitService
					.findUnitWithPartition(currentPartition.getId());
			currentPartition.setUnitList(UnitWrap.convert(unitList));
		}
		if (currentPartition.getTaskReport() == null) {
			currentPartition.setTaskReport(taskReportService
					.findReportWithTarget(currentPartition.getId()));
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void showUnitReport(UnitWrap currentUnit) throws Exception {
		selectedUnit = currentUnit;
		if (selectedUnit.getTaskReport() == null)
			selectedUnit.setTaskReport(taskReportService
					.findReportWithTarget(selectedUnit.getId()));
	}

	// public void doDeliverMission() throws Exception {
	// MissionWrap selectedMission = (MissionWrap) selectedNode.getData();
	// missionService.startMission(selectedMission);
	//
	// notificationService.sendStartMissionNotification(
	// selectedMission.getLeaderName(), selectedMission, "");
	// taskService.startNewMissionTask(selectedMission.getLeaderName(),
	// selectedMission);
	// }

	public void doModifyMission() {

	}

	public void doAbortMission() {

	}

	public String buildMissionBody() {
		return "body test";
	}

	public String getTypeStr(Object obj) {
		if (obj instanceof MissionWrap)
			return "Mission";
		if (obj instanceof PartitionWrap)
			return "Partition";
		if (obj instanceof UnitWrap)
			return "Unit";
		return "error";
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

	@Interceptors(TransactionInterceptor.class)
	public void processDownload() throws Exception {
		MissionWrap selectedMission = (MissionWrap) selectedNode.getData();
		if (!selectedMission.getTaskReport().getFileId().equals("none")) {
			UploadFile file = uploadFileService.get(selectedMission
					.getTaskReport().getFileId());
			InputStream stream = new FileInputStream(file.getFilePath()
					+ file.getFileName());
			downloadFile = new DefaultStreamedContent(stream, "test",
					file.getFileName());
		}
	}

	public boolean isMission(Object obj) {
		return obj instanceof MissionWrap;
	}

	public String getColumnColorCss(Object obj) {
		if ((obj instanceof PartitionWrap))
			return "background-color: #e3e0e3";
		if ((obj instanceof UnitWrap))
			return "background-color: #c9c7c9";
		List<TreeNode> nodes = root.getChildren();
		for (int i = 0; i < nodes.size(); ++i) {
			if (nodes.get(i).getData().equals(obj)) {
				if (i % 2 == 0)
					return null;
				else
					return "background-color: #F9F9F9";
			}
		}

		return null;
	}

	@Interceptors(TransactionInterceptor.class)
	public void exportAsXml() throws Exception {
		MissionWrap selectedMission = (MissionWrap) selectedNode.getData();
		UploadFile file = xmlBuilder.export(selectedMission.getMission());
		InputStream stream = new FileInputStream(file.getFilePath()
				+ file.getFileName());
		downloadFile = new DefaultStreamedContent(stream, "test",
				file.getFileName());

	}

	@Interceptors(TransactionInterceptor.class)
	public void exportAsObjectFile() throws Exception {
		FacesContext context = FacesContext.getCurrentInstance();
		Properties property = PropertyManager.getApplicationProperties();
		String realPath = property.getProperty("MissionTemplatePath");
		MissionWrap selectedMission = (MissionWrap) selectedNode.getData();
		if (selectedMission.getDependentSystems() == null)
			selectedMission.setDependentSystems(systemService
					.findDependentSystems(selectedMission.getId()));
		if (selectedMission.getDependentFunctions() == null)
			selectedMission.setDependentFunctions(systemFunctionService
					.findFunctionByTarget(selectedMission.getId()));
		List<PartitionWrap> partitionList = PartitionWrap
				.convert(missionPartitionService
						.findPartitionWithMission(selectedMission.getId()));
		selectedMission.setPartitionList(partitionList);
		for (PartitionWrap partition : partitionList) {
			if (partition.getDependentSystems() == null)
				partition.setDependentSystems(systemService
						.findDependentSystems(partition.getId()));
			if (partition.getDependentFunctions() == null)
				partition.setDependentFunctions(systemFunctionService
						.findFunctionByTarget(partition.getId()));
			if (partition.getDependentObj() == null)
				partition.setDependentObj(PartitionWrap
						.convert(missionPartitionService
								.findDependentPartition(partition.getId())));
			List<UnitWrap> unitList = UnitWrap.convert(missionUnitService
					.findUnitWithPartition(partition.getId()));
			partition.setUnitList(unitList);
			for (UnitWrap unit : unitList) {
				if (unit.getDependentSystems() == null)
					unit.setDependentSystems(systemService
							.findDependentSystems(unit.getId()));
				if (unit.getDependentFunctions() == null)
					unit.setDependentFunctions(systemFunctionService
							.findFunctionByTarget(unit.getId()));
				if (unit.getDependentObj() == null)
					unit.setDependentObj(UnitWrap.convert(missionUnitService
							.findDependentUnit(unit.getId())));
			}
		}

		String fileName = "missionTemplate" + selectedMission.getId();

		FileOutputStream outStream = new FileOutputStream(realPath + fileName);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				outStream);
		UploadFile newFile = new UploadFile(sessionManager.getLoginUser()
				.getName(), realPath, fileName);
		uploadFileService.save(newFile);
		objectOutputStream.writeObject(selectedMission);
		outStream.close();
		context.addMessage(null, new FacesMessage("Mission template saved"));
	}

	@Interceptors(TransactionInterceptor.class)
	public void getMissionObjectList() throws Exception {
		Properties property = PropertyManager.getApplicationProperties();
		String realPath = property.getProperty("MissionTemplatePath");
		File folder = new File(realPath);
		folder.mkdirs();
		File[] objFiles = folder.listFiles();
		List<MissionWrap> missionList = new ArrayList<MissionWrap>();
		for (int i = 0; i < objFiles.length; ++i) {
			if (objFiles[i].isFile()) {
				FileInputStream inStream = new FileInputStream(objFiles[i]);
				ObjectInputStream objectInputStream = new ObjectInputStream(
						inStream);
				MissionWrap obj = (MissionWrap) objectInputStream.readObject();
				missionList.add(obj);
			}
		}

		exportRoot = new DefaultTreeNode("root", null);
		for (MissionWrap mission : missionList) {
			TreeNode editMissionNode = new DefaultTreeNode("mission", mission,
					exportRoot);
			List<PartitionWrap> partitionList = mission.getPartitionList();
			for (PartitionWrap p : partitionList) {
				User ower = userService.get(p.getLeaderName());
				if (ower != null)
					p.setLeader(new UserWrap(ower));
				else
					p.setLeader(null);
			}
			if (partitionList != null) {
				for (PartitionWrap partition : partitionList) {
					TreeNode newPartitionNode = new DefaultTreeNode(
							"partition", partition, editMissionNode);
					List<UnitWrap> unitList = partition.getUnitList();
					for (UnitWrap u : unitList) {
						User ower = userService.get(u.getLeaderName());
						if (ower != null)
							u.setLeader(new UserWrap(ower));
						else
							u.setLeader(null);
					}
					if (unitList != null) {
						for (UnitWrap unit : unitList) {
							new DefaultTreeNode("unit", unit, newPartitionNode);
						}
					}

				}
			}

		}
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

	public MissionWrap getNewMission() {
		return editMission;
	}

	public void setNewMission(MissionWrap editMission) {
		this.editMission = editMission;
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

	public MissionWrap getEditMission() {
		return editMission;
	}

	public void setEditMission(MissionWrap editMission) {
		this.editMission = editMission;
	}

	public Wizard getWizard() {
		return wizard;
	}

	public void setWizard(Wizard wizard) {
		this.wizard = wizard;
	}

	public boolean isUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}

	public boolean isAddMode() {
		return addMode;
	}

	public void setAddMode(boolean addMode) {
		this.addMode = addMode;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public UnitWrap getSelectedUnit() {
		return selectedUnit;
	}

	public void setSelectedUnit(UnitWrap selectedUnit) {
		this.selectedUnit = selectedUnit;
	}

	public StreamedContent getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(StreamedContent downloadFile) {
		this.downloadFile = downloadFile;
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

	public TreeNode getExportRoot() {
		return exportRoot;
	}

	public void setExportRoot(TreeNode exportRoot) {
		this.exportRoot = exportRoot;
	}

	public Object getSelectedExportData() {
		return selectedExportData;
	}

	public void setSelectedExportData(Object selectedExportData) {
		this.selectedExportData = selectedExportData;
	}

}
