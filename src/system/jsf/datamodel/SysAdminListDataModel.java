package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.po.SysAdmin;

public class SysAdminListDataModel  extends ListDataModel<SysAdmin> implements
SelectableDataModel<SysAdmin>, Serializable{






	/**
	 * 
	 */
	private static final long serialVersionUID = 2726095401735316341L;

	public SysAdminListDataModel(List<SysAdmin> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SysAdmin getRowData(String rowKey) {
		String sysAdminId = rowKey;
		List<SysAdmin> sysAdminList = (List<SysAdmin>)getWrappedData();
		for (SysAdmin sysAdmin : sysAdminList) {
			if(sysAdmin.getId().equals(sysAdminId)){
				return sysAdmin;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(SysAdmin sysAdmin) {
		return sysAdmin.getId();
	}

}
