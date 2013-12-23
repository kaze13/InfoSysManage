package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.po.Mission;

public class MissionListDataModel  extends ListDataModel<Mission> implements
SelectableDataModel<Mission>, Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = -5236538072771978166L;

	public MissionListDataModel(List<Mission> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Mission getRowData(String rowKey) {
		String missionId = rowKey;
		List<Mission> missionList = (List<Mission>)getWrappedData();
		for (Mission mission : missionList) {
			if(mission.getId().equals(missionId)){
				return mission;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(Mission mission) {
		return mission.getId();
	}

}
