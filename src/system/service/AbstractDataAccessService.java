package system.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;


import system.annotation.GenericDaoInject;
import system.dao.spec.GenericDao;
import system.dao.spec.Transaction;

public abstract class AbstractDataAccessService<T> implements Serializable{

	private Class<T> type;
	@Inject
	protected Transaction transaction;
	@Inject
	@GenericDaoInject
	protected GenericDao<T> genericDao;

	public AbstractDataAccessService(Class<T> type) {
		super();
		this.type = type;
	}

	public AbstractDataAccessService() {
		super();
	}

	public void save(T t) throws Exception {
		genericDao.save(type, t, transaction);

	}

	public void save(List<T> list) throws Exception {
		if (list.size() == 0)
			return;
		genericDao.save(type, list, transaction);
	}

	public void delete(String id) throws Exception {
		genericDao.delete(type, id, transaction);

	}
	public void delete(String id, boolean cache) throws Exception {
		genericDao.delete(type, id, transaction, cache);

	}

	public void delete(String[] ids) throws Exception {
		if (ids.length == 0)
			return;
		genericDao.delete(type, ids, transaction);

	}

	public void update(T t) throws Exception {
		genericDao.update(type, t, transaction);

	}

	public void update(List<T> list) throws Exception {
		if (list.size() == 0)
			return;
		genericDao.update(type, list, transaction);

	}

	public T get(String id) throws Exception {

		T t = genericDao.get(type, id, transaction);
		return t;

	}

	public List<T> get(String[] list) throws Exception {
		if (list.length == 0)
			return new ArrayList<T>();
		List<T> result = genericDao.get(type, list, transaction);

		return result;

	}

	// @Interceptors(CachingInterceptor.class)
	public List<T> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception {

		List<T> results = genericDao.findAllByConditions(type, sqlWhereMap,
				"and", true, transaction);
		return results;

	}

}
