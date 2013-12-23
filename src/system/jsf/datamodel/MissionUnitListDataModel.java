package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.po.MissionUnit;

public class MissionUnitListDataModel  extends ListDataModel<MissionUnit> implements
SelectableDataModel<MissionUnit>, Serializable{





	/**
	 *
	 */
	private static final long serialVersionUID = -3322617058179255202L;

	public MissionUnitListDataModel(List<MissionUnit> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MissionUnit getRowData(String rowKey) {
		String missionPartitionId = rowKey;
		List<MissionUnit> missionPartitionList = (List<MissionUnit>)getWrappedData();
		for (MissionUnit missionPartition : missionPartitionList) {
			if(missionPartition.getId().equals(missionPartitionId)){
				return missionPartition;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(MissionUnit missionPartition) {
		return missionPartition.getId();
	}

}
