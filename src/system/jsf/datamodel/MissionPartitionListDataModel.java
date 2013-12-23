package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.po.MissionPartition;

public class MissionPartitionListDataModel  extends ListDataModel<MissionPartition> implements
SelectableDataModel<MissionPartition>, Serializable{



	/**
	 *
	 */
	private static final long serialVersionUID = 5595385449193648511L;

	public MissionPartitionListDataModel(List<MissionPartition> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MissionPartition getRowData(String rowKey) {
		String missionPartitionId = rowKey;
		List<MissionPartition> missionPartitionList = (List<MissionPartition>)getWrappedData();
		for (MissionPartition missionPartition : missionPartitionList) {
			if(missionPartition.getId().equals(missionPartitionId)){
				return missionPartition;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(MissionPartition missionPartition) {
		return missionPartition.getId();
	}

}
