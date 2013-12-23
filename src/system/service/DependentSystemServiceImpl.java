package system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.DependentSystemInject;
import system.dao.impl.DependentSystemDaoImpl;
import system.interceptor.TimerInterceptor;
import system.po.DependentSystem;
import system.service.spec.DependentSystemService;

@Interceptors(TimerInterceptor.class)
public class DependentSystemServiceImpl extends
		AbstractDataAccessService<DependentSystem> implements Serializable,
		DependentSystemService {

	@Inject
	@DependentSystemInject
	DependentSystemDaoImpl dependentSystemDao;

	public DependentSystemServiceImpl() {
		super(DependentSystem.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see system.service.DependentSystemService#refresh(java.util.List,
	 * java.lang.String)
	 */
	@Override
	public void refresh(List<DependentSystem> list, String missionid)
			throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("target_id", missionid);
		List<DependentSystem> oldlist = genericDao.findAllByConditions(
				DependentSystem.class, sqlWhereMap, "and", true, transaction);

		String[] ids = new String[oldlist.size()];
		for (int i = 0; i < oldlist.size(); ++i) {
			ids[i] = oldlist.get(i).getId();
		}
		this.delete(ids);
		this.save(list);
	}
}
