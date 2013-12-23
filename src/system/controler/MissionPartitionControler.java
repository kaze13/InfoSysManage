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
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.PartitionDependencyVoListDataModel;
import system.jsf.datamodel.SystemFunctionListDataModel;
import system.manager.spec.SessionManager;
import system.po.DependentFunction;
import system.po.DependentSystem;
import system.po.Mission;
import system.po.MissionUnit;
import system.po.PartitionDependency;
import system.po.System;
import system.po.SystemFunction;
import system.po.Task;
import system.po.TaskReport;
import system.po.UploadFile;
import system.service.DependentFunctionServiceImpl;
import system.service.DependentSystemServiceImpl;
import system.service.MissionServiceImpl;
import system.service.MissionUnitServiceImpl;
import system.service.NotificationServiceImpl;
import system.service.PartitionDependencyServiceImpl;
import system.service.MissionPartitionServiceImpl;
import system.service.SystemFunctionServiceImpl;
import system.service.SystemServiceImpl;
import system.service.TaskReportServiceImpl;
import system.service.TaskServiceImpl;
import system.service.UploadFileServiceImpl;
import system.util.PropertyManager;
import system.vo.MissionWrap;
import system.vo.PartitionDependencyVo;
import system.vo.PartitionWrap;
import system.vo.UnitWrap;
import system.vo.manager.MissionWrapGetter;

import org.primefaces.model.DualListModel;

@Named
@SessionScoped
public class MissionPartitionControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3466576596797654121L;

	private String missionId;
	@Inject
	private MissionPartitionServiceImpl missionPartitionService;
	@Inject
	private MissionServiceImpl missionService;
	@Inject
	private PartitionDependencyServiceImpl partitionDependencyService;
	@Inject
	private MissionUnitServiceImpl missionUnitService;
	@Inject
	private DependentSystemServiceImpl dependentSystemService;
	@Inject
	private SystemServiceImpl systemService;
	@Inject
	private SystemFunctionServiceImpl systemFunctionService;
	@Inject
	private DependentFunctionServiceImpl dependentFunctionService;
	@Inject
	private NotificationServiceImpl notificationService;
	@Inject
	private TaskServiceImpl taskService;
	@Inject
	private TaskReportServiceImpl taskReportService;
	@Inject
	private UploadFileServiceImpl uploadFileService;
	@Inject
	private SessionManager sessionManager;
	@Inject
	private MissionWrapGetter missinoWrapGetter;
	private PartitionWrap editMissionPartition = new PartitionWrap();
	// private PartitionWarp selectedMissionPartition;
	private MissionWrap currentMission;

	private PartitionDependencyVo[] selectedPartitionDependencys;
	private PartitionDependencyVoListDataModel partitionDependencyVoListModel;
	private PartitionDependency newPartitionDependency = new PartitionDependency();
	private PartitionDependencyVo selectedPartitionDependency;

	private List<PartitionDependencyVo> basePartitionDependencyVoList;
	private List<PartitionWrap> baseMissionPartitionList;
	private UploadedFile file;
	private DualListModel<PartitionWrap> dependencyDual;
	private TreeNode root;
	private TreeNode selectedNode;
	private StreamedContent downloadFile;
	private DualListModel<System> dependentSystem;
	private List<System> baseSystemList;
	private SystemFunctionListDataModel sourceFunctionModel;
	private SystemFunctionListDataModel targetFunctionModel;
	private boolean skip;
	private boolean addMode;
	private boolean updateMode;

	private String report;
	private boolean submittedPartitionRejected = false;

	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		Map<String, String> varMap = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String tmp = varMap.get("missionid");
		if (tmp != null)
			missionId = tmp;
		// else
		// throw new Exception("Illegal session state");
		currentMission = missinoWrapGetter.get(missionId);

		Map<String, Object> dependencySql = new HashMap<String, Object>();
		dependencySql.put("belonged_mission_id", missionId);
		List<PartitionDependency> baseDependencyList = partitionDependencyService
				.findAllByCondition(dependencySql);
		basePartitionDependencyVoList = new ArrayList<PartitionDependencyVo>();
		for (PartitionDependency p : baseDependencyList) {
			basePartitionDependencyVoList.add(partitionDependencyService
					.generateVo(p));
		}
		partitionDependencyVoListModel = new PartitionDependencyVoListDataModel(
				basePartitionDependencyVoList);

		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("mission_id", missionId);
		baseMissionPartitionList = PartitionWrap
				.convert(missionPartitionService
						.findAllByCondition(sqlWhereMap));
		buildTree(baseMissionPartitionList);
		dependencyDual = new DualListModel<PartitionWrap>(
				baseMissionPartitionList, new ArrayList<PartitionWrap>());

		baseSystemList = systemService.findAllByCondition(null);
		dependentSystem = new DualListModel<System>();
		dependentSystem.setSource(baseSystemList);
		dependentSystem.setTarget(new ArrayList<System>());
		sourceFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		targetFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
	}

	@Interceptors(TransactionInterceptor.class)
	private TreeNode buildTree(List<PartitionWrap> partitionList)
			throws Exception {
		root = new DefaultTreeNode("root", null);

		for (PartitionWrap partition : partitionList) {
			TreeNode newPartitionNode = new DefaultTreeNode("partition",
					partition, root);
			List<UnitWrap> unitList = UnitWrap.convert(missionUnitService
					.findUnitWithPartition(partition.getId()));
			for (UnitWrap unit : unitList) {
				TreeNode newUnitNode = new DefaultTreeNode("unit", unit,
						newPartitionNode);
			}
		}
		return root;
	}

	public void startCreateNewMissionPartition() {
		dependentSystem = new DualListModel<System>();
		dependentSystem.setSource(baseSystemList);
		dependentSystem.setTarget(new ArrayList<System>());
		editMissionPartition = new PartitionWrap();
		sourceFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		targetFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		dependencyDual.setSource(baseMissionPartitionList);
		dependencyDual.setTarget(new ArrayList<PartitionWrap>());
		addMode = true;
		updateMode = false;
	}

	@Interceptors(TransactionInterceptor.class)
	public void startUpdateMissionPartition() throws Exception {
		PartitionWrap selectedPartition = (PartitionWrap) selectedNode
				.getData();
		dependencyDual.setTarget(selectedPartition.getDependentObj());
		List<PartitionWrap> sourcePartitionList = new ArrayList<PartitionWrap>(
				baseMissionPartitionList);
		for (PartitionWrap partition : baseMissionPartitionList) {
			if (partition.getPartition().equals(
					selectedPartition.getPartition()))
				sourcePartitionList.remove(partition);
			for (PartitionWrap target : selectedPartition.getDependentObj()) {
				if (partition.getPartition().equals(target.getPartition()))
					sourcePartitionList.remove(partition);
			}
		}
		dependencyDual.setSource(sourcePartitionList);
		String uploadFileid = getUploadFileId();
		if (!uploadFileid.equals("none") && uploadFileid != null)
			selectedPartition.setFileId(uploadFileid);
		selectedPartition.setFileId(uploadFileid);
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("target_id", selectedPartition.getId());
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
		List<PartitionWrap> noSelfList = new ArrayList<PartitionWrap>(
				baseMissionPartitionList);
		noSelfList.remove(selectedPartition);
		dependentSystem.setSource(source);
		dependentSystem.setTarget(target);
		// List<SystemFunction> list = systemFunctionService
		// .findFunctionBySystems(dependentSystem.getTarget());
		// sourceFunctionModel = new SystemFunctionListDataModel(list);
		targetFunctionModel = new SystemFunctionListDataModel(
				systemFunctionService.findFunctionByTarget(selectedPartition
						.getId()));
		editMissionPartition = selectedPartition;
		addMode = false;
		updateMode = true;
	}

	@Interceptors(TransactionInterceptor.class)
	public void doSaveMissionPartition() throws Exception {
		editMissionPartition.setMissionid(missionId);
		editMissionPartition.setFileId(getUploadFileId());
		missionPartitionService.save(editMissionPartition.getPartition());
		baseMissionPartitionList.add(editMissionPartition);
		TreeNode newNode = new DefaultTreeNode("partition",
				editMissionPartition, root);
		List<PartitionDependency> dependencyList = new ArrayList<PartitionDependency>();
		List<PartitionWrap> targetList = dependencyDual.getTarget();
		for (PartitionWrap p : targetList) {
			PartitionDependency tmp = new PartitionDependency();
			tmp.setBelongedMissionId(currentMission.getId());
			tmp.setPrePartitionId(p.getId());
			tmp.setPostPartitionId(editMissionPartition.getId());
			dependencyList.add(tmp);
		}
		doSaveDependencys(dependencyList);
		// for (PartitionDependency ud : dependencyList) {
		// basePartitionDependencyVoList.add(partitionDependencyService.generateVo(ud));
		// }
		List<DependentSystem> dsList = new ArrayList<DependentSystem>();
		List<System> targetSystemList = dependentSystem.getTarget();
		for (System s : targetSystemList) {
			DependentSystem tmp = new DependentSystem(
					DependentSystem.Type.PARTITION,
					editMissionPartition.getId(), s.getName());
			dsList.add(tmp);
		}
		dependentSystemService.save(dsList);

		List<SystemFunction> targetFunctions = (List<SystemFunction>) targetFunctionModel
				.getWrappedData();
		List<DependentFunction> dependentFunctions = new ArrayList<DependentFunction>();
		for (SystemFunction function : targetFunctions) {
			dependentFunctions.add(new DependentFunction("partition",
					editMissionPartition.getId(), function.getId()));
		}
		dependentFunctionService.save(dependentFunctions);

		editMissionPartition = new PartitionWrap();
	}

	@Interceptors(TransactionInterceptor.class)
	public void doSaveDependency() throws Exception {

		newPartitionDependency.setBelongedMissionId(currentMission.getId());
		FacesContext context = FacesContext.getCurrentInstance();
		for (PartitionDependencyVo vo : basePartitionDependencyVoList) {
			if (vo.getPartitionDependency().getPostPartitionId()
					.equals(newPartitionDependency.getPostPartitionId())
					&& vo.getPartitionDependency().getPrePartitionId()
							.equals(newPartitionDependency.getPrePartitionId())) {
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Operation Fail",
						"Invalid dependency:" + newPartitionDependency.getId()
								+ " Duplicate data."));
				return;
			}
		}
		if (partitionDependencyService.check(newPartitionDependency)) {
			basePartitionDependencyVoList.add(partitionDependencyService
					.generateVo(newPartitionDependency));
			partitionDependencyService.save(newPartitionDependency);
			newPartitionDependency = new PartitionDependency();
		} else {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Operation Fail",
					"Invalid dependency, circle found."));
		}
	}

	@Interceptors(TransactionInterceptor.class)
	private void doSaveDependencys(List<PartitionDependency> list)
			throws Exception {
		FacesContext context = FacesContext.getCurrentInstance();
		for (PartitionDependency p : list) {
			for (PartitionDependencyVo vo : basePartitionDependencyVoList) {
				if (vo.getPartitionDependency().getPostPartitionId()
						.equals(p.getPostPartitionId())
						&& vo.getPartitionDependency().getPrePartitionId()
								.equals(p.getPrePartitionId())) {
					context.addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Operation Fail",
							"Invalid dependency:" + p.getId()
									+ " Duplicate data."));
					return;
				}
			}
			if (!partitionDependencyService.check(p)) {
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Operation Fail",
						"Invalid dependency:" + p.getId() + " Circle found."));
				return;
			} else
				continue;
		}
		for (PartitionDependency p : list) {
			basePartitionDependencyVoList.add(partitionDependencyService
					.generateVo(p));
		}
		partitionDependencyService.save(list);
	}

	// public void startUpdateMissionPartition() throws Exception {
	// PartitionWarp selectedPartition = (PartitionWarp) selectedNode
	// .getData();
	// Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
	// sqlWhereMap.put("target_id", selectedPartition.getId());
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
	//
	//
	// }

	@Interceptors(TransactionInterceptor.class)
	public void showPartitionReport() throws Exception {
		PartitionWrap selectedPartition = (PartitionWrap) selectedNode
				.getData();
		if (selectedPartition.getTask() == null) {
			selectedPartition.setTask(taskService
					.findTaskByDataid(selectedPartition.getId()));
		}
		if (selectedPartition.getTaskReport() == null) {
			TaskReport report = taskReportService
					.findReportWithTask(selectedPartition.getTask().getId());
			selectedPartition.setTaskReport(report);
		}

		if (selectedPartition.getUnitList() == null) {
			List<MissionUnit> unitList = missionUnitService
					.findUnitWithPartition(selectedPartition.getId());
			selectedPartition.setUnitList(UnitWrap.convert(unitList));
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void onReportToggle(ToggleEvent event) throws Exception {
		UnitWrap currentUnit = (UnitWrap) event.getData();
		if (currentUnit.getTaskReport() == null)
			currentUnit.setTaskReport(taskReportService
					.findReportWithTarget(currentUnit.getId()));

	}

	@Interceptors(TransactionInterceptor.class)
	public String printDependentSystem() throws Exception {
		TreeNode node = selectedNode;
		if (node == null)
			return null;
		Object obj = node.getData();
		String id = null;
		if (obj instanceof Mission)
			id = ((Mission) obj).getId();
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
		if (event.getNewStep().equals("dependentItems")) {

		}
		if (event.getNewStep().equals("dependentFunctions")) {
			List<System> targetSystems = dependentSystem.getTarget();
			List<SystemFunction> list = systemFunctionService
					.findFunctionBySystems(dependentSystem.getTarget());
			List<SystemFunction> parentList = new ArrayList<SystemFunction>(
					systemFunctionService.findFunctionByTarget(missionId));
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

	public boolean isPartition(Object obj) {
		return obj instanceof PartitionWrap;
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
	public void doUpdateMissionPartition() throws Exception {
		PartitionWrap selectedPartition = (PartitionWrap) selectedNode
				.getData();
		String uploadFileid = getUploadFileId();
		if (!uploadFileid.equals("none") && uploadFileid != null)
			selectedPartition.setFileId(uploadFileid);
		missionPartitionService.update(selectedPartition.getPartition());

		List<DependentSystem> dsList = new ArrayList<DependentSystem>();
		List<System> targetList = dependentSystem.getTarget();
		for (System s : targetList) {
			DependentSystem tmp = new DependentSystem(
					DependentSystem.Type.PARTITION, selectedPartition.getId(),
					s.getName());
			dsList.add(tmp);
		}
		dependentSystemService.refresh(dsList, selectedPartition.getId());
		List<SystemFunction> targetFunctions = (List<SystemFunction>) targetFunctionModel
				.getWrappedData();
		List<DependentFunction> dependentFunctions = new ArrayList<DependentFunction>();
		for (SystemFunction function : targetFunctions) {
			dependentFunctions.add(new DependentFunction("partition",
					editMissionPartition.getId(), function.getId()));
		}
		dependentFunctionService.refresh(dependentFunctions,
				selectedPartition.getId());
	}

	@Interceptors(TransactionInterceptor.class)
	public void doUpdatePartitionDependency() throws Exception {

		newPartitionDependency.setBelongedMissionId(currentMission.getId());
		FacesContext context = FacesContext.getCurrentInstance();
		if (partitionDependencyService.check(selectedPartitionDependency
				.getPartitionDependency())) {
			partitionDependencyService.update(selectedPartitionDependency
					.getPartitionDependency());
			replacePartitionDependencyVoData(partitionDependencyService
					.generateVo(selectedPartitionDependency
							.getPartitionDependency()));
		} else {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Operation Fail",
					"Invalid dependency, circle found."));
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeleteMissionPartition() throws Exception {
		PartitionWrap selectedPartition = (PartitionWrap) selectedNode
				.getData();
		missionPartitionService.delete(selectedPartition.getId());
		selectedNode.setParent(null);

		baseMissionPartitionList.remove(selectedPartition);

		List<PartitionDependency> deleteList = new ArrayList<PartitionDependency>();

		List<PartitionDependencyVo> copyTmp = new ArrayList<PartitionDependencyVo>(
				basePartitionDependencyVoList);
		for (PartitionDependencyVo dependency : copyTmp) {
			if (dependency.getPostPartition().equals(
					selectedPartition.getPartition())
					|| dependency.getPrePartition().equals(
							selectedPartition.getPartition())) {

				basePartitionDependencyVoList.remove(dependency);
				deleteList.add(dependency.getPartitionDependency());
			}

		}
		if(deleteList.size() > 0)
		{
			String[] ids = new String[deleteList.size()];
			for(int i = 0; i < deleteList.size(); ++i)
			{
				ids[i] = deleteList.get(i).getId();
			}
			partitionDependencyService.delete(ids);
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeletePartitionDependency() throws Exception {

		partitionDependencyService.delete(selectedPartitionDependency
				.getPartitionDependency().getId());
		List<PartitionDependencyVo> basePartitionDependencyList = (List<PartitionDependencyVo>) partitionDependencyVoListModel
				.getWrappedData();
		int index = findPartitionDependencyVoData(selectedPartitionDependency
				.getPartitionDependency().getId());
		if (index >= 0) {
			basePartitionDependencyList.remove(index);
		}

	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeliverMissionPartition() throws Exception {
		missionPartitionService.deliverPartition(
				((PartitionWrap) selectedNode.getData()).getPartition(), 0,
				"none");
		notificationService
				.sendStartPartitionNotification(
						((PartitionWrap) selectedNode.getData()).getPartition(),
						"none");
		taskService.startNewPartitionTask(
				((PartitionWrap) selectedNode.getData()).getPartition(), 0,
				"none", true);
	}

	private void replacePartitionDependencyVoData(
			PartitionDependencyVo updatedPartitionDependency) {
		int index = findPartitionDependencyVoData(updatedPartitionDependency
				.getPartitionDependency().getId());
		if (index >= 0) {
			List<PartitionDependencyVo> basePartitionDependencyList = (List<PartitionDependencyVo>) partitionDependencyVoListModel
					.getWrappedData();
			basePartitionDependencyList.set(index, new PartitionDependencyVo(
					updatedPartitionDependency));
		}
	}

	private int findPartitionDependencyVoData(String name) {
		List<PartitionDependencyVo> basePartitionDependencyList = (List<PartitionDependencyVo>) partitionDependencyVoListModel
				.getWrappedData();
		for (int i = 0; i < basePartitionDependencyList.size(); i++) {
			PartitionDependencyVo dependency = basePartitionDependencyList
					.get(i);
			if (dependency.getPartitionDependency().getId().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	@Interceptors(TransactionInterceptor.class)
	public void acceptPartitionReport() throws Exception {
		PartitionWrap selectedPartition = (PartitionWrap) selectedNode
				.getData();
		missionPartitionService.acceptPartition(selectedPartition
				.getPartition());

		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("data", selectedPartition.getId());
		sqlMap.put("statue", Task.StatueType.SUBMITTED.ordinal());
		List<Task> results = taskService.findAllByCondition(sqlMap);
		Task task = results.get(0);
		task.setStatue(Task.StatueType.COMPLETED);
		taskService.update(task);

		notificationService
				.sendAcceptSubmittedPartitionNotification(selectedPartition
						.getPartition());
	}

	@Interceptors(TransactionInterceptor.class)
	public void submitRejectReport() throws Exception {
		PartitionWrap selectedPartition = (PartitionWrap) selectedNode
				.getData();
		submittedPartitionRejected = true;
		// notificationService.sendRejectSubmittedUnitNotification(selectedMissionUnit,
		// report);
		missionPartitionService.rejectCompletePartition(
				selectedPartition.getPartition(), report);

		Task task = taskService.findTaskByDataid(selectedPartition.getId());
		task.setStatue(Task.StatueType.SUBMISSION_REJECTED);
		taskService.update(task);

		List<Task> unitTasks = taskService
				.findUnitTasksByPartition(selectedPartition.getId());
		for (Task t : unitTasks) {
			t.setStatue(Task.StatueType.SUBMITTED);
		}
		taskService.update(unitTasks);
		List<MissionUnit> units = missionUnitService
				.findUnitWithPartition(selectedPartition.getId());
		for (MissionUnit u : units) {
			u.setProgress(-4); // change to submitted.
		}
		missionUnitService.update(units);
		notificationService.sendRejectSubmittedPartitionNotification(
				selectedPartition.getPartition(), report, "none");
		report = null;
	}

	@Interceptors(TransactionInterceptor.class)
	public void submitMission() throws Exception {
		for (PartitionWrap partition : baseMissionPartitionList) {
			if (partition.getProgress() != 100) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Submission failed.",
										"You can not submit the mission since there exist some unfinished tasks."));
				return;
			}
		}

		Task currentTask = taskService.findTaskByDataid(missionId);
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
		missionService.submitMission(currentMission.getCreatorName(),
				currentMission.getMission(), report, uploadFileId);
		notificationService.sendSubmitMissionNotification(
				currentMission.getCreatorName(), currentMission.getMission(),
				report, uploadFileId);
		report = null;
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
		PartitionWrap selectedPartition = (PartitionWrap) selectedNode
				.getData();
		if (!selectedPartition.getTaskReport().getFileId().equals("none")) {
			UploadFile file = uploadFileService.get(selectedPartition
					.getTaskReport().getFileId());
			InputStream stream = new FileInputStream(file.getFilePath()
					+ file.getFileName());
			downloadFile = new DefaultStreamedContent(stream, "test",
					file.getFileName());
		}
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

	// getter/setter

	public String getMissionId() {
		return missionId;
	}

	public void setMissionId(String missionId) {
		this.missionId = missionId;
	}

	public PartitionDependencyVo[] getSelectedPartitionDependencys() {
		return selectedPartitionDependencys;
	}

	public void setSelectedPartitionDependencys(
			PartitionDependencyVo[] selectedPartitionDependencys) {
		this.selectedPartitionDependencys = selectedPartitionDependencys;
	}

	public PartitionDependencyVoListDataModel getPartitionDependencyVoListModel() {
		return partitionDependencyVoListModel;
	}

	public void setPartitionDependencyVoListModel(
			PartitionDependencyVoListDataModel partitionDependencyVoListModel) {
		this.partitionDependencyVoListModel = partitionDependencyVoListModel;
	}

	public PartitionDependency getNewPartitionDependency() {
		return newPartitionDependency;
	}

	public void setNewPartitionDependency(
			PartitionDependency newPartitionDependency) {
		this.newPartitionDependency = newPartitionDependency;
	}

	public PartitionDependencyVo getSelectedPartitionDependency() {
		return selectedPartitionDependency;
	}

	public void setSelectedPartitionDependency(
			PartitionDependencyVo selectedPartitionDependency) {
		this.selectedPartitionDependency = selectedPartitionDependency;
	}

	public List<PartitionDependencyVo> getBasePartitionDependencyVoList() {
		return basePartitionDependencyVoList;
	}

	public void setBasePartitionDependencyVoList(
			List<PartitionDependencyVo> basePartitionDependencyVoList) {
		this.basePartitionDependencyVoList = basePartitionDependencyVoList;
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

	public List<PartitionWrap> getBaseMissionPartitionList() {
		return baseMissionPartitionList;
	}

	public void setBaseMissionPartitionList(
			List<PartitionWrap> baseMissionPartitionList) {
		this.baseMissionPartitionList = baseMissionPartitionList;
	}

	public DualListModel<PartitionWrap> getDependencyDual() {
		return dependencyDual;
	}

	public void setDependencyDual(DualListModel<PartitionWrap> dependencyDual) {
		this.dependencyDual = dependencyDual;
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

	public PartitionWrap getEditMissionPartition() {
		return editMissionPartition;
	}

	public void setEditMissionPartition(PartitionWrap editMissionPartition) {
		this.editMissionPartition = editMissionPartition;
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

	public StreamedContent getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(StreamedContent downloadFile) {
		this.downloadFile = downloadFile;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public MissionWrap getCurrentMission() {
		return currentMission;
	}

	public void setCurrentMission(MissionWrap currentMission) {
		this.currentMission = currentMission;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

}
