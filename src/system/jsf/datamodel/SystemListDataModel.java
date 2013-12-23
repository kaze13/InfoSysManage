package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.po.System;

public class SystemListDataModel extends ListDataModel<System> implements
SelectableDataModel<System>, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2127459823131101081L;


	public SystemListDataModel(List<System> list) {
		super(list);
	}

	@Override
	public Object getRowKey(System system) {
		return system.getName();
	}

	@Override
	public System getRowData(String rowKey) {
		String systemName = rowKey;
		List<System> systemList = (List<System>)getWrappedData();
		for (System system : systemList) {
			if(system.getName().equals(systemName)){
				return system;
			}
		}
		return null;
	}
}
