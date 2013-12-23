package system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.DependentFunction;
import system.service.spec.DependentFunctionService;

@Interceptors(TimerInterceptor.class)
public class DependentFunctionServiceImpl extends
		AbstractDataAccessService<DependentFunction> implements Serializable,
		DependentFunctionService {

	public DependentFunctionServiceImpl() {
		super(DependentFunction.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see system.service.DependentFunctionService#refresh(java.util.List,
	 * java.lang.String)
	 */
	@Override
	public void refresh(List<DependentFunction> list, String id)
			throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("target_id", id);
		List<DependentFunction> oldlist = genericDao.findAllByConditions(
				DependentFunction.class, sqlWhereMap, "and",true, transaction);
		String[] ids = new String[oldlist.size()];
		for (int i = 0; i < oldlist.size(); ++i) {
			ids[i] = oldlist.get(i).getId();
		}
		this.delete(ids);
		this.save(list);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * system.service.DependentFunctionService#findFunctionByTarget(java.lang
	 * .String)
	 */
	@Override
	public List<DependentFunction> findFunctionByTarget(String id)
			throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("target_id", id);
		return this.findAllByCondition(sqlWhereMap);
	}
}
