package system.controler;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import system.vo.UserWrap;
import system.po.System;

@Named
@SessionScoped
public class GlobalDlgBean implements Serializable{

	private System selectedSystem;
	private UserWrap selectedUserWrap;
	private Object selectedMissionObject;
	public System getSelectedSystem() {
		return selectedSystem;
	}
	public void setSelectedSystem(System selectedSystem) {
		this.selectedSystem = selectedSystem;
	}
	public UserWrap getSelectedUserWrap() {
		return selectedUserWrap;
	}
	public void setSelectedUserWrap(UserWrap selectedUserWrap) {
		this.selectedUserWrap = selectedUserWrap;
	}
	public Object getSelectedMissionObject() {
		return selectedMissionObject;
	}
	public void setSelectedMissionObject(Object selectedMissionObject) {
		this.selectedMissionObject = selectedMissionObject;
	}


}
