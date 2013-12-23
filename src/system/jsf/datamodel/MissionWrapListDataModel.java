package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.vo.MissionWrap;


public class MissionWrapListDataModel  extends ListDataModel<MissionWrap> implements
SelectableDataModel<MissionWrap>, Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = -5236538072771978166L;

	public MissionWrapListDataModel(List<MissionWrap> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MissionWrap getRowData(String rowKey) {
		String missionId = rowKey;
		List<MissionWrap> missionList = (List<MissionWrap>)getWrappedData();
		for (MissionWrap mission : missionList) {
			if(mission.getId().equals(missionId)){
				return mission;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(MissionWrap mission) {
		return mission.getId();
	}

}
