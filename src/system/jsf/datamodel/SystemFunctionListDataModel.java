package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.po.SystemFunction;

public class SystemFunctionListDataModel  extends ListDataModel<SystemFunction> implements
SelectableDataModel<SystemFunction>, Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = -7090805141727160324L;

	public SystemFunctionListDataModel(List<SystemFunction> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SystemFunction getRowData(String rowKey) {
		String systemFunctionId = rowKey;
		List<SystemFunction> systemFunctionList = (List<SystemFunction>)getWrappedData();
		for (SystemFunction systemFunction : systemFunctionList) {
			if(systemFunction.getId().equals(systemFunctionId)){
				return systemFunction;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(SystemFunction systemFunction) {
		return systemFunction.getId();
	}

}
