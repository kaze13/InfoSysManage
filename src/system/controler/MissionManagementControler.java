package system.controler;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.MissionListDataModel;
import system.jsf.datamodel.MissionWrapListDataModel;
import system.manager.spec.SessionManager;
import system.po.User;
import system.service.MissionServiceImpl;
import system.vo.MissionWrap;

@Named
@SessionScoped
public class MissionManagementControler implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 7448753889018508591L;
	@Inject
	private MissionServiceImpl missionService;
	@Inject
	private SessionManager sessionManager;

	private MissionWrapListDataModel missionDataModel;
	private List<MissionWrap> baseMissionList;
	private User currentUser;
	private MissionWrap selectedMission;


	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		currentUser = sessionManager.getLoginUser();
		baseMissionList = MissionWrap.convert(missionService.findMissionByLeader(currentUser
				.getName()));
		missionDataModel = new MissionWrapListDataModel(baseMissionList);
	}

	public String abstraction(String text, int length) {
		if (text.length() <= length)
			return text;
		else {
			return text.substring(0, length) + "....";
		}
	}

	public String redirect(MissionWrap mission) {
		return "./missionwizard.jsf?faces-redirect=true&missionid="
				+ mission.getId();

	}



	public MissionWrapListDataModel getMissionDataModel() {
		return missionDataModel;
	}

	public void setMissionDataModel(MissionWrapListDataModel missionDataModel) {
		this.missionDataModel = missionDataModel;
	}

	public List<MissionWrap> getBaseMissionList() {
		return baseMissionList;
	}

	public void setBaseMissionList(List<MissionWrap> baseMissionList) {
		this.baseMissionList = baseMissionList;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public MissionWrap getSelectedMission() {
		return selectedMission;
	}

	public void setSelectedMission(MissionWrap selectedMission) {
		this.selectedMission = selectedMission;
	}

}
