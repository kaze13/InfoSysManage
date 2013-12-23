package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.po.Application;

public class ApplicationListDataModel  extends ListDataModel<Application> implements
SelectableDataModel<Application>, Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = -5236538072771978166L;

	public ApplicationListDataModel(List<Application> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Application getRowData(String rowKey) {
		String missionId = rowKey;
		List<Application> missionList = (List<Application>)getWrappedData();
		for (Application mission : missionList) {
			if(mission.getId().equals(missionId)){
				return mission;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(Application mission) {
		return mission.getId();
	}

}
