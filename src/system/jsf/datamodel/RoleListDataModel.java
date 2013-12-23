package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.po.Role;

public class RoleListDataModel  extends ListDataModel<Role> implements
SelectableDataModel<Role>, Serializable{

	/**
	 *
	 */

	private static final long serialVersionUID = 2922707289881822550L;

	public RoleListDataModel(List<Role> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Role getRowData(String rowKey) {
		String roleName = rowKey;
		List<Role> roleList = (List<Role>)getWrappedData();
		for (Role role : roleList) {
			if(role.getName().equals(roleName)){
				return role;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(Role role) {
		return role.getName();
	}

}
