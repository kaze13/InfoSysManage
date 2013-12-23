package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.vo.UserWrap;


public class UserWrapListDataModel extends ListDataModel<UserWrap> implements
		SelectableDataModel<UserWrap>, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 57071661550137076L;

	public UserWrapListDataModel(List<UserWrap> list) {
		super(list);
	}

	@Override
	public Object getRowKey(UserWrap user) {
		return user.getName();
	}

	@Override
	public UserWrap getRowData(String rowKey) {
		String userName = rowKey;
		List<UserWrap> userList = (List<UserWrap>)getWrappedData();
		for (UserWrap user : userList) {
			if(user.getName().equals(userName)){
				return user;
			}
		}
		return null;
	}


}