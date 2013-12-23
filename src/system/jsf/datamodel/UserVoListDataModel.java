package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.vo.UserVo;


public class UserVoListDataModel extends ListDataModel<UserVo> implements
		SelectableDataModel<UserVo>, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 57071661550137076L;

	public UserVoListDataModel(List<UserVo> list) {
		super(list);
	}

	@Override
	public Object getRowKey(UserVo user) {
		return user.getName();
	}

	@Override
	public UserVo getRowData(String rowKey) {
		String userName = rowKey;
		List<UserVo> userList = (List<UserVo>)getWrappedData();
		for (UserVo user : userList) {
			if(user.getName().equals(userName)){
				return user;
			}
		}
		return null;
	}


}