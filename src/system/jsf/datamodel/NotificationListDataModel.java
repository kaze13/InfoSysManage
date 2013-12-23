package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.po.Notification;

public class NotificationListDataModel  extends ListDataModel<Notification> implements
SelectableDataModel<Notification>, Serializable{




	/**
	 *
	 */
	private static final long serialVersionUID = 5351674757461404399L;

	public NotificationListDataModel(List<Notification> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Notification getRowData(String rowKey) {
		String missionId = rowKey;
		List<Notification> missionList = (List<Notification>)getWrappedData();
		for (Notification mission : missionList) {
			if(mission.getId().equals(missionId)){
				return mission;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(Notification mission) {
		return mission.getId();
	}

}
