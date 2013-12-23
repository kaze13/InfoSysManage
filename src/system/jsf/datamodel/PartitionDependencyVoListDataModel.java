package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.vo.PartitionDependencyVo;


public class PartitionDependencyVoListDataModel  extends ListDataModel<PartitionDependencyVo> implements
SelectableDataModel<PartitionDependencyVo>, Serializable{






	/**
	 *
	 */
	private static final long serialVersionUID = 2767499095906775359L;

	public PartitionDependencyVoListDataModel(List<PartitionDependencyVo> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PartitionDependencyVo getRowData(String rowKey) {
		String id = rowKey;
		List<PartitionDependencyVo> dependencyList = (List<PartitionDependencyVo>)getWrappedData();
		for (PartitionDependencyVo dependency : dependencyList) {
			if(dependency.getPartitionDependency().getId().equals(id)){
				return dependency;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(PartitionDependencyVo dependency) {
		return dependency.getPartitionDependency().getId();
	}

}
