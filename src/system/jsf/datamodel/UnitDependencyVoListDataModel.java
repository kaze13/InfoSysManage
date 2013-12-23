package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.vo.UnitDependencyVo;


public class UnitDependencyVoListDataModel  extends ListDataModel<UnitDependencyVo> implements
SelectableDataModel<UnitDependencyVo>, Serializable{






	/**
	 *
	 */
	private static final long serialVersionUID = 2767499095906775359L;

	public UnitDependencyVoListDataModel(List<UnitDependencyVo> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public UnitDependencyVo getRowData(String rowKey) {
		String id = rowKey;
		List<UnitDependencyVo> dependencyList = (List<UnitDependencyVo>)getWrappedData();
		for (UnitDependencyVo dependency : dependencyList) {
			if(dependency.getUnitDependency().getId().equals(id)){
				return dependency;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(UnitDependencyVo dependency) {
		return dependency.getUnitDependency().getId();
	}

}
