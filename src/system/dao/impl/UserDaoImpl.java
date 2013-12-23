package system.dao.impl;

import java.util.List;

import system.annotation.UserInject;
import system.po.User;

@UserInject
public class UserDaoImpl extends GenericDaoImpl<User> {

	public List<User> findAbnormalAuthorityUser() {
		String sql = "select b.user_name as userName,b.role_name as roleName,b.password as password,b.expire_time as expireTime  from RD4_TEMPORARY_AUTHORITY a inner join RD4_USER b on a.user_name=b.user_name and a.PARTITION_ID=?";
		return null;
	}
}
