package system.controler;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.MissionPartitionListDataModel;
import system.manager.spec.SessionManager;
import system.po.MissionPartition;
import system.po.User;
import system.service.spec.MissionPartitionService;

@Named
@SessionScoped
public class PartitionManagementControler implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 7448753889018508591L;
	@Inject
	private MissionPartitionService missionPartitionService;
	@Inject
	private SessionManager sessionManager;

	private MissionPartitionListDataModel missionPartitionDataModel;
	private List<MissionPartition> baseMissionPartitionList;
	private User currentUser;
	private MissionPartition selectedMissionPartition;


	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		currentUser = sessionManager.getLoginUser();
		baseMissionPartitionList = missionPartitionService.findMissionPartitionByLeader(currentUser
				.getName());
		missionPartitionDataModel = new MissionPartitionListDataModel(baseMissionPartitionList);
	}

	public String abstraction(String text, int length) {
		if (text.length() <= length)
			return text;
		else {
			return text.substring(0, length) + "....";
		}
	}

	public String redirect(MissionPartition missionPartition) {
		return "./partitionwizard.jsf?faces-redirect=true&partitionid="
				+ missionPartition.getId();

	}

	public MissionPartitionListDataModel getMissionPartitionDataModel() {
		return missionPartitionDataModel;
	}

	public void setMissionPartitionDataModel(MissionPartitionListDataModel missionPartitionDataModel) {
		this.missionPartitionDataModel = missionPartitionDataModel;
	}

	public List<MissionPartition> getBaseMissionPartitionList() {
		return baseMissionPartitionList;
	}

	public void setBaseMissionPartitionList(List<MissionPartition> baseMissionPartitionList) {
		this.baseMissionPartitionList = baseMissionPartitionList;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public MissionPartition getSelectedMissionPartition() {
		return selectedMissionPartition;
	}

	public void setSelectedMissionPartition(MissionPartition selectedMissionPartition) {
		this.selectedMissionPartition = selectedMissionPartition;
	}

}
