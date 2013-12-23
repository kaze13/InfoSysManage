package system.dao.spec;

import java.util.List;
import java.util.Map;

import javax.interceptor.Interceptors;

import system.interceptor.GetCacheInterceptor;

public interface GenericDao<T> {

	public void save(Class<T> clazz, T t, Transaction transaction)
			throws Exception;

	public void save(Class<T> clazz, List<T> list, Transaction transaction)
			throws Exception;

	public void delete(Class<T> clazz, Object id, Transaction transaction)
			throws Exception;
	public void delete(Class<T> clazz, Object id, Transaction transaction, boolean cache)
			throws Exception;
	public void delete(Class<T> clazz, String[] id, Transaction transaction)
			throws Exception;

	public void update(Class<T> clazz, T t, Transaction transaction)
			throws Exception;

	public void update(Class<T> clazz, List<T> t, Transaction transaction)
			throws Exception;

	public T get(Class<T> clazz, Object id, Transaction transaction)
			throws Exception;

	public List<T> get(Class<T> clazz, String[] ids, Transaction transaction)
			throws Exception;

	public List<T> findAllByConditions(Class<T> clazz,
			Map<String, Object> sqlWhereMap, String type, boolean useCache, Transaction transaction)
			throws Exception;

	@Interceptors(GetCacheInterceptor.class)
	public List<T> generalExecute(Class<T> clazz, List<Object> params,
			String sql, Transaction transaction) throws Exception;
}