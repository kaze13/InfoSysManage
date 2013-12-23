package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.User;
import system.vo.UserWrap;

public interface UserService {

	public void save(User t) throws Exception;

	public void save(List<User> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(User t) throws Exception;

	public User get(String id) throws Exception;

	public List<User> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;

	public abstract List<User> findUserByRole(String roleName) throws Exception;

	public abstract UserWrap generateUserVo(User user) throws Exception;

}