package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.po.User;

public class UserListDataModel extends ListDataModel<User> implements
		SelectableDataModel<User>, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 57071661550137076L;

	public UserListDataModel(List<User> list) {
		super(list);
	}

	@Override
	public Object getRowKey(User user) {
		return user.getName();
	}

	@Override
	public User getRowData(String rowKey) {
		String userName = rowKey;
		List<User> userList = (List<User>)getWrappedData();
		for (User user : userList) {
			if(user.getName().equals(userName)){
				return user;
			}
		}
		return null;
	}


}