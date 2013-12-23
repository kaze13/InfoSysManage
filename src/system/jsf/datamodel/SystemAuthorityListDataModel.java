package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.po.SystemAuthority;

public class SystemAuthorityListDataModel  extends ListDataModel<SystemAuthority> implements
SelectableDataModel<SystemAuthority>, Serializable{




	/**
	 *
	 */
	private static final long serialVersionUID = 1783868775282236816L;

	public SystemAuthorityListDataModel(List<SystemAuthority> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SystemAuthority getRowData(String rowKey) {
		String systemAuthorityId = rowKey;
		List<SystemAuthority> systemAuthorityList = (List<SystemAuthority>)getWrappedData();
		for (SystemAuthority systemAuthority : systemAuthorityList) {
			if(systemAuthority.getId().equals(systemAuthorityId)){
				return systemAuthority;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(SystemAuthority systemAuthority) {
		return systemAuthority.getId();
	}

}
