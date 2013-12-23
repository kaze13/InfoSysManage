package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.vo.ApplicationWrap;


public class ApplicationWrapListDataModel  extends ListDataModel<ApplicationWrap> implements
SelectableDataModel<ApplicationWrap>, Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = -5236538072771978166L;

	public ApplicationWrapListDataModel(List<ApplicationWrap> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ApplicationWrap getRowData(String rowKey) {
		String missionId = rowKey;
		List<ApplicationWrap> missionList = (List<ApplicationWrap>)getWrappedData();
		for (ApplicationWrap mission : missionList) {
			if(mission.getId().equals(missionId)){
				return mission;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(ApplicationWrap mission) {
		return mission.getId();
	}

}
