package system.controler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import system.interceptor.TransactionInterceptor;
import system.po.User;
import system.service.UserServiceImpl;

@Named
@SessionScoped
public class GlobalBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8343377103870475906L;
	@Inject
	private UserServiceImpl userService;
	private List<User> allUserList;

	@Interceptors(TransactionInterceptor.class)
	public List<String> completeUser(String query) throws Exception {
		if (allUserList == null)
			allUserList = userService.findAllByCondition(null);
		if (allUserList.size() > 0) {
			for (int i = allUserList.size() - 1; i >= 0; --i) {
				if (allUserList.get(i).getName().contains("_new_comer_"))
					allUserList.remove(i);
			}

		}
		List<String> results = new ArrayList<String>();
		for (User user : allUserList) {
			if (!user.getName().equals("")) {
				if (user.getName().contains(query)) {
					String str = user.getName();
					results.add(str);
				}

			}
		}
		return results;
	}
}
