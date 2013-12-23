package system.dao.impl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.GenericDaoInject;
import system.annotation.Id;
import system.dao.spec.GenericDao;
import system.dao.spec.Transaction;
import system.interceptor.GetCacheInterceptor;
import system.interceptor.SetCacheInterceptor;
import system.manager.impl.GlobalCacheManagerImpl;
import system.po.spec.Printable;

@GenericDaoInject
public class GenericDaoImpl<T extends Printable> implements GenericDao<T> {

	private static final String TABLE_ALIAS = "t";
	Logger logger = Logger.getLogger("DB ACCESS");

	@Inject
	GlobalCacheManagerImpl cacheManager;

	@RolesAllowed("admin")
	@Override
	@Interceptors(SetCacheInterceptor.class)
	public void save(Class<T> clazz, T t, Transaction transaction)
			throws Exception {
		// Class<?> clazz = t.getClass();
		String tableName = getTableName(clazz);
		StringBuilder fieldNames = new StringBuilder();
		List<Object> fieldValues = new ArrayList<Object>();
		StringBuilder placeholders = new StringBuilder();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			PropertyDescriptor pd = new PropertyDescriptor(field.getName(),
					t.getClass());
			if (field.isAnnotationPresent(Id.class)) {
				fieldNames.append(field.getAnnotation(Id.class).value())
						.append(",");
				fieldValues.add(pd.getReadMethod().invoke(t));
				placeholders.append("?").append(",");
			} else if (field.isAnnotationPresent(Column.class)) {
				fieldNames.append(field.getAnnotation(Column.class).value())
						.append(",");
				fieldValues.add(pd.getReadMethod().invoke(t));
				placeholders.append("?").append(",");
			} else {
				;
			}

		}

		fieldNames.deleteCharAt(fieldNames.length() - 1);
		placeholders.deleteCharAt(placeholders.length() - 1);

		StringBuilder sql = new StringBuilder("");
		sql.append("insert into ").append(tableName).append(" (")
				.append(fieldNames.toString()).append(") values (")
				.append(placeholders).append(")");
		transaction.begin();
		Connection conn = transaction.getResource(Connection.class);
		PreparedStatement ps = conn.prepareStatement(sql.toString());
		setParameter(fieldValues, ps, false);
		ps.execute();
		ps.close();

		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (cachePool.containsKey(clazz)) {
			List<T> cache = (List<T>) cachePool.get(clazz);
			cache.add((T) t.clone());
		}
		logger.log(Level.WARNING, "SAVE SINGLE  " + clazz.getName());
	}

	@Interceptors(SetCacheInterceptor.class)
	public void save(Class<T> clazz, List<T> list, Transaction transaction)
			throws Exception {
		// Class<?> clazz = t.getClass();
		if (list.size() == 0)
			return;
		String tableName = getTableName(clazz);
		StringBuilder fieldNames = new StringBuilder();
		List<ArrayList<Object>> fieldValues = new ArrayList<ArrayList<Object>>();
		for (int i = 0; i < list.size(); ++i)
			fieldValues.add(new ArrayList<Object>());
		StringBuilder placeholders = new StringBuilder();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			PropertyDescriptor pd = new PropertyDescriptor(field.getName(),
					list.get(0).getClass());
			if (field.isAnnotationPresent(Id.class)) {
				fieldNames.append(field.getAnnotation(Id.class).value())
						.append(",");
				placeholders.append("?").append(",");
			} else if (field.isAnnotationPresent(Column.class)) {
				fieldNames.append(field.getAnnotation(Column.class).value())
						.append(",");
				placeholders.append("?").append(",");
			} else {
				;
			}

		}

		for (int i = 0; i < list.size(); ++i) {
			for (int j = 0; j < fields.length; ++j) {
				PropertyDescriptor pd = new PropertyDescriptor(
						fields[j].getName(), list.get(0).getClass());
				fieldValues.get(i).add(pd.getReadMethod().invoke(list.get(i)));
			}
		}

		fieldNames.deleteCharAt(fieldNames.length() - 1);
		placeholders.deleteCharAt(placeholders.length() - 1);

		StringBuilder sql = new StringBuilder("");
		sql.append("insert into ").append(tableName).append(" (")
				.append(fieldNames.toString()).append(") values (")
				.append(placeholders).append(")");
		transaction.begin();
		Connection conn = transaction.getResource(Connection.class);

		PreparedStatement ps = conn.prepareStatement(sql.toString());
		for (int i = 0; i < list.size(); ++i) {
			setParameter(fieldValues.get(i), ps, false);
			ps.addBatch();
		}
		ps.executeBatch();
		ps.close();
		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (cachePool.containsKey(clazz)) {
			List<T> cache = (List<T>) cachePool.get(clazz);
			for (T t : list)
				cache.add((T) t.clone());
		}
		logger.log(Level.WARNING, "SAVE BATCH  " + clazz.getName());
	}

	@Override
	@Interceptors(SetCacheInterceptor.class)
	public void delete(Class<T> clazz, Object id, Transaction transaction)
			throws Exception {
		String tableName = getTableName(clazz);
		String idFieldName = "";
		boolean flag = false;
		Field[] fields = clazz.getDeclaredFields();
		PropertyDescriptor pd = null;
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				pd = new PropertyDescriptor(field.getName(), clazz);
				idFieldName = field.getAnnotation(Id.class).value();
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new Exception(clazz.getName()
					+ " object not found id property.");
		}

		String sql = "delete from " + tableName + " where " + idFieldName
				+ "=?";
		transaction.begin();
		Connection conn = transaction.getResource(Connection.class);

		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setObject(1, id);
		ps.execute();
		ps.close();
		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (cachePool.containsKey(clazz)) {
			List<T> cache = (List<T>) cachePool.get(clazz);
			for (int i = cache.size() - 1; i >= 0; --i) {
				if (pd.getReadMethod().invoke(cache.get(i)).equals(id))
					cache.remove(i);
			}
		}
		logger.log(Level.WARNING, "DELETE SINGLE  " + clazz.getName());
	}

	@Override
	@Interceptors(SetCacheInterceptor.class)
	public void delete(Class<T> clazz, Object id, Transaction transaction, boolean clearCache)
			throws Exception {
		String tableName = getTableName(clazz);
		String idFieldName = "";
		boolean flag = false;
		Field[] fields = clazz.getDeclaredFields();
		PropertyDescriptor pd = null;
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				pd = new PropertyDescriptor(field.getName(), clazz);
				idFieldName = field.getAnnotation(Id.class).value();
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new Exception(clazz.getName()
					+ " object not found id property.");
		}



		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (cachePool.containsKey(clazz)) {
			List<T> cache = (List<T>) cachePool.get(clazz);
			for (int i = cache.size() - 1; i >= 0; --i) {
				if (pd.getReadMethod().invoke(cache.get(i)).equals(id))
					cache.remove(i);
			}
		}
		logger.log(Level.WARNING, "DELETE SINGLE cache " + clazz.getName());
	}


	@Override
	@Interceptors(SetCacheInterceptor.class)
	public void delete(Class<T> clazz, String[] ids, Transaction transaction)
			throws Exception {
		if (ids.length == 0)
			return;
		String tableName = getTableName(clazz);
		String idFieldName = "";
		boolean flag = false;
		Field[] fields = clazz.getDeclaredFields();
		PropertyDescriptor pd = null;
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				pd = new PropertyDescriptor(field.getName(), clazz);
				idFieldName = field.getAnnotation(Id.class).value();
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new Exception(clazz.getName()
					+ " object not found id property.");
		}

		String sql = "delete from " + tableName + " where " + idFieldName
				+ "=?";
		transaction.begin();
		Connection conn = transaction.getResource(Connection.class);

		PreparedStatement ps = conn.prepareStatement(sql);

		for (String id : ids) {
			ps.setObject(1, id);
			ps.addBatch();
		}
		ps.executeBatch();
		ps.close();

		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (cachePool.containsKey(clazz)) {
			List<T> cache = (List<T>) cachePool.get(clazz);
			for (String id : ids) {
				for (int i = cache.size() - 1; i >= 0; --i) {
					if (pd.getReadMethod().invoke(cache.get(i)).equals(id))
						cache.remove(i);
				}
			}

		}
		logger.log(Level.WARNING, "DELETE BATCH  " + clazz.getName());
	}

	@Override
	@Interceptors(SetCacheInterceptor.class)
	public void update(Class<T> clazz, T t, Transaction transaction)
			throws Exception {
		// Class<?> clazz = t.getClass();
		String tableName = getTableName(clazz);
		List<Object> fieldNames = new ArrayList<Object>();
		List<Object> fieldValues = new ArrayList<Object>();
		List<String> placeholders = new ArrayList<String>();
		String idFieldName = "";
		Object idFieldValue = "";
		Field[] fields = clazz.getDeclaredFields();
		PropertyDescriptor idpd = null;
		for (Field field : fields) {
			PropertyDescriptor pd = new PropertyDescriptor(field.getName(),
					t.getClass());
			if (field.isAnnotationPresent(Id.class)) {
				idpd = new PropertyDescriptor(field.getName(), clazz);
				idFieldName = field.getAnnotation(Id.class).value();
				idFieldValue = pd.getReadMethod().invoke(t);
			} else if (field.isAnnotationPresent(Column.class)) {
				fieldNames.add(field.getAnnotation(Column.class).value());
				fieldValues.add(pd.getReadMethod().invoke(t));
				placeholders.add("?");
			}
		}
		fieldNames.add(idFieldName);
		fieldValues.add(idFieldValue);
		placeholders.add("?");

		StringBuilder sql = new StringBuilder("");
		sql.append("update ").append(tableName).append(" set ");
		int index = fieldNames.size() - 1;
		for (int i = 0; i < index; i++) {
			sql.append(fieldNames.get(i)).append("=")
					.append(placeholders.get(i)).append(",");
		}
		sql.deleteCharAt(sql.length() - 1).append(" where ")
				.append(fieldNames.get(index)).append("=").append("?");
		transaction.begin();
		Connection conn = transaction.getResource(Connection.class);

		PreparedStatement ps = conn.prepareStatement(sql.toString());
		setParameter(fieldValues, ps, false);

		ps.execute();
		ps.close();

		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (cachePool.containsKey(clazz)) {
			List<T> cache = (List<T>) cachePool.get(clazz);
			for (int i = cache.size() - 1; i >= 0; --i) {
				if (idpd.getReadMethod().invoke(cache.get(i))
						.equals(idpd.getReadMethod().invoke(t)))
					cache.set(i, (T) t.clone());
			}
		}
		logger.log(Level.WARNING, "UPDATE SINGLE  " + clazz.getName());

	}

	@Override
	@Interceptors(SetCacheInterceptor.class)
	public void update(Class<T> clazz, List<T> list, Transaction transaction)
			throws Exception {
		if (list.size() == 0)
			return;
		String tableName = getTableName(clazz);
		List<Object> fieldNames = new ArrayList<Object>();
		List<ArrayList<Object>> fieldValues = new ArrayList<ArrayList<Object>>();
		for (int i = 0; i < list.size(); ++i)
			fieldValues.add(new ArrayList<Object>());
		List<String> placeholders = new ArrayList<String>();
		String idFieldName = "";
		Object idFieldValue = "";
		Field[] fields = clazz.getDeclaredFields();
		PropertyDescriptor idpd = null;
		for (Field field : fields) {
			PropertyDescriptor pd = new PropertyDescriptor(field.getName(),
					list.get(0).getClass());
			if (field.isAnnotationPresent(Id.class)) {
				idpd = new PropertyDescriptor(field.getName(), clazz);
				idFieldName = field.getAnnotation(Id.class).value();
				idFieldValue = pd.getReadMethod().invoke(list.get(0));
			} else if (field.isAnnotationPresent(Column.class)) {
				fieldNames.add(field.getAnnotation(Column.class).value());
				placeholders.add("?");
			}
		}
		fieldNames.add(idFieldName);
		for (int i = 0; i < list.size(); ++i) {
			for (int j = 0; j < fields.length; ++j) {
				PropertyDescriptor pd = new PropertyDescriptor(
						fields[j].getName(), list.get(0).getClass());
				if (fields[j].isAnnotationPresent(Id.class))
					idFieldValue = pd.getReadMethod().invoke(list.get(0));
				else if (fields[j].isAnnotationPresent(Column.class)) {
					fieldValues.get(i).add(
							pd.getReadMethod().invoke(list.get(i)));
				}
			}
			fieldValues.get(i).add(idFieldValue);
		}

		placeholders.add("?");

		StringBuilder sql = new StringBuilder("");
		sql.append("update ").append(tableName).append(" set ");
		int index = fieldNames.size() - 1;
		for (int i = 0; i < index; i++) {
			sql.append(fieldNames.get(i)).append("=")
					.append(placeholders.get(i)).append(",");
		}
		sql.deleteCharAt(sql.length() - 1).append(" where ")
				.append(fieldNames.get(index)).append("=").append("?");
		transaction.begin();
		Connection conn = transaction.getResource(Connection.class);

		PreparedStatement ps = conn.prepareStatement(sql.toString());
		for (int i = 0; i < list.size(); ++i) {
			setParameter(fieldValues.get(i), ps, false);
			ps.addBatch();
		}

		ps.executeBatch();
		ps.close();

		Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
		if (cachePool.containsKey(clazz)) {
			List<T> cache = (List<T>) cachePool.get(clazz);
			for (T t : list) {
				for (int i = cache.size() - 1; i >= 0; --i) {
					if (idpd.getReadMethod().invoke(cache.get(i))
							.equals(idpd.getReadMethod().invoke(t)))
						cache.set(i, (T) t.clone());
				}
			}

		}
		logger.log(Level.WARNING, "UPDATE BATCH  " + clazz.getName());

	}

	@Override
	@Interceptors(GetCacheInterceptor.class)
	public T get(Class<T> clazz, Object id, Transaction transaction)
			throws Exception {
		String idFieldName = "";
		Field[] fields = clazz.getDeclaredFields();
		boolean flag = false;
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				idFieldName = field.getAnnotation(Id.class).value();
				flag = true;
				break;
			}
		}

		if (!flag) {
			throw new Exception(clazz.getName()
					+ " object not found id property.");
		}

		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put(TABLE_ALIAS + "." + idFieldName, id);

		List<T> list = findAllByConditions(clazz, sqlWhereMap, "and", true,
				transaction);
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	@Interceptors(GetCacheInterceptor.class)
	public List<T> get(Class<T> clazz, String[] ids, Transaction transaction)
			throws Exception {
		if (ids.length == 0)
			return null;
		String idFieldName = "";
		Field[] fields = clazz.getDeclaredFields();
		boolean flag = false;
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				idFieldName = field.getAnnotation(Id.class).value();
				flag = true;
				break;
			}
		}

		if (!flag) {
			throw new Exception(clazz.getName()
					+ " object not found id property.");
		}

		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();

		List<T> list = new ArrayList<T>();
		for (String id : ids) {
			sqlWhereMap.clear();
			sqlWhereMap.put(TABLE_ALIAS + "." + idFieldName, id);
			list.addAll(findAllByConditions(clazz, sqlWhereMap, "and", true,
					transaction));
		}
		return list;
	}

	@Override
	@Interceptors(GetCacheInterceptor.class)
	public List<T> findAllByConditions(Class<T> clazz,
			Map<String, Object> sqlWhereMap, String type, boolean useCache,
			Transaction transaction) throws Exception {

		List<T> list = new ArrayList<T>();
		String tableName = getTableName(clazz);
		String idFieldName = "";
		StringBuffer fieldNames = new StringBuffer();
		Field[] fields = clazz.getDeclaredFields();
		List<Field> dealedFields = new LinkedList<Field>();

		if (useCache) {
			Map<Class<?>, List<?>> cachePool = cacheManager.getCachePool();
			if (!cachePool.containsKey(clazz)) {
				List<T> allData = this.findAllByConditions(clazz, null, type,
						false, transaction);
				List<T> copy = new ArrayList<T>(allData);
				cachePool.put(clazz, (List<Object>) copy);
			}
			List<T> cache = (List<T>) cachePool.get(clazz);
			List<T> result = new ArrayList<T>(cache);

			if (sqlWhereMap == null) {
				for (int i = 0; i < result.size(); ++i)
					result.set(i, (T) result.get(i).clone());

				return result;
			}
			for (T t : cache) {
				for (Field field : fields) {
					PropertyDescriptor pd = new PropertyDescriptor(
							field.getName(), t.getClass());
					String annotation = null;
					if (field.isAnnotationPresent(Id.class))
						annotation = field.getAnnotation(Id.class).value();
					if (field.isAnnotationPresent(Column.class))
						annotation = field.getAnnotation(Column.class).value();
					if (sqlWhereMap.containsKey("t." + annotation)) {
						if (!sqlWhereMap.get("t." + annotation).equals(
								pd.getReadMethod().invoke(t)))
							result.remove(t);
					}
					if (sqlWhereMap.containsKey(annotation)) {
						Object consultValue = sqlWhereMap.get(annotation);
						Object resultValue = pd.getReadMethod().invoke(t);
						if (resultValue == null) {
							result.remove(t);
							break;
						}
						if (resultValue.getClass().isEnum())
							resultValue = ((Enum) resultValue).ordinal();
						if (consultValue.getClass().isEnum())
							consultValue = ((Enum) consultValue).ordinal();
						if (!resultValue.equals(consultValue))
							result.remove(t);
					}
				}
			}

			for (int i = 0; i < result.size(); ++i)
				result.set(i, (T) result.get(i).clone());
			return result;
		}

		for (Field field : fields) {
			String propertyName = field.getName();
			if (field.isAnnotationPresent(Id.class)) {
				idFieldName = field.getAnnotation(Id.class).value();
				fieldNames.append(TABLE_ALIAS + "." + idFieldName)
						.append(" as ").append(propertyName).append(",");
				dealedFields.add(field);
			} else if (field.isAnnotationPresent(Column.class)) {
				fieldNames
						.append(TABLE_ALIAS + "."
								+ field.getAnnotation(Column.class).value())
						.append(" as ").append(propertyName).append(",");
				dealedFields.add(field);
			} else {
				;
			}
		}
		fieldNames.deleteCharAt(fieldNames.length() - 1);

		String sql = "select " + fieldNames + " from " + tableName + " "
				+ TABLE_ALIAS;
		PreparedStatement ps = null;
		List<Object> values = null;
		if (sqlWhereMap != null) {
			List<Object> sqlWhereWithValues = getSqlWhereWithValues(
					sqlWhereMap, type);
			if (sqlWhereWithValues != null) {
				String sqlWhere = (String) sqlWhereWithValues.get(0);
				sql += sqlWhere;
				values = (List<Object>) sqlWhereWithValues.get(1);
			}
		}
		transaction.begin();
		Connection conn = transaction.getResource(Connection.class);

		if (values != null) {
			ps = conn.prepareStatement(sql);
			setParameter(values, ps, false);
		} else {
			ps = conn.prepareStatement(sql);
		}

		ResultSet rs = ps.executeQuery();
		logger.log(Level.WARNING, "FIND ALL  " + clazz.getName());
		while (rs.next()) {
			T t = clazz.newInstance();
			initObject(t, dealedFields, rs);
			list.add(t);
		}
		ps.close();

		return list;
	}

	@Override
	public List<T> generalExecute(Class<T> clazz, List<Object> params,
			String sql, Transaction transaction) throws Exception {
		List<T> list = new ArrayList<T>();
		Field[] fields = clazz.getDeclaredFields();
		List<Field> dealedFields = new LinkedList<Field>();
		for (Field f : fields) {
			if (f.isAnnotationPresent(Id.class)) {
				dealedFields.add(f);
			} else if (f.isAnnotationPresent(Column.class)) {
				dealedFields.add(f);
			} else {
				;
			}
			// dealedFields.add(f);
		}
		transaction.begin();
		Connection conn = transaction.getResource(Connection.class);
		PreparedStatement ps = conn.prepareStatement(sql);
		setParameter(params, ps, false);
		ResultSet rs = ps.executeQuery();
		logger.log(Level.WARNING, "GENERAL EXECUTE  " + clazz.getName());
		while (rs.next()) {
			T t = clazz.newInstance();
			initObject(t, dealedFields, rs);
			list.add(t);
		}
		ps.close();

		return list;
	}

	protected void initObject(T t, List<Field> fields, ResultSet rs)
			throws SQLException, IntrospectionException,
			IllegalAccessException, InvocationTargetException {
		for (Field field : fields) {
			if (field == null)
				continue;
			String propertyName = field.getName();
			Object paramVal = null;
			Class<?> clazzField = field.getType();
			if (clazzField == String.class) {
				paramVal = rs.getString(propertyName);
				if(paramVal == null)
					paramVal = "";
			} else if (clazzField == short.class || clazzField == Short.class) {
				paramVal = rs.getShort(propertyName);
			} else if (clazzField == int.class || clazzField == Integer.class) {
				paramVal = rs.getInt(propertyName);
			} else if (clazzField == long.class || clazzField == Long.class) {
				paramVal = rs.getLong(propertyName);
			} else if (clazzField == float.class || clazzField == Float.class) {
				paramVal = rs.getFloat(propertyName);
			} else if (clazzField == double.class || clazzField == Double.class) {
				paramVal = rs.getDouble(propertyName);
			} else if (clazzField == boolean.class
					|| clazzField == Boolean.class) {
				paramVal = rs.getBoolean(propertyName);
			} else if (clazzField == byte.class || clazzField == Byte.class) {
				paramVal = rs.getByte(propertyName);
			} else if (clazzField == char.class
					|| clazzField == Character.class) {
				paramVal = rs.getCharacterStream(propertyName);
			} else if (clazzField == Date.class) {
				paramVal = rs.getTimestamp(propertyName);
			} else if (clazzField.isArray()) {
				paramVal = rs.getString(propertyName).split(",");
			} else if (clazzField.isEnum()) {
				paramVal = rs.getInt(propertyName);
			}
			PropertyDescriptor pd = new PropertyDescriptor(propertyName,
					t.getClass());
			if (!clazzField.isEnum())
				pd.getWriteMethod().invoke(t, paramVal);
			else
				pd.getWriteMethod().invoke(t,
						clazzField.getEnumConstants()[(Integer) paramVal]);
		}
	}

	private List<Object> getSqlWhereWithValues(Map<String, Object> sqlWhereMap,
			String type) {
		if (sqlWhereMap.size() < 1)
			return null;
		List<Object> list = new ArrayList<Object>();
		List<Object> fieldValues = new ArrayList<Object>();
		StringBuffer sqlWhere = new StringBuffer(" where ");
		Set<Entry<String, Object>> entrySets = sqlWhereMap.entrySet();
		for (Iterator<Entry<String, Object>> iteraotr = entrySets.iterator(); iteraotr
				.hasNext();) {
			Entry<String, Object> entrySet = iteraotr.next();
			fieldValues.add(entrySet.getValue());
			Object value = entrySet.getValue();
			if (value.getClass() == String.class) {
				sqlWhere.append(entrySet.getKey()).append("=").append("?")
						.append(" " + type + " ");
			} else {
				sqlWhere.append(entrySet.getKey()).append("=").append("?")
						.append(" " + type + " ");
			}
		}
		sqlWhere.delete(sqlWhere.lastIndexOf(type), sqlWhere.length());
		list.add(sqlWhere.toString());
		list.add(fieldValues);
		return list;
	}

	private String getTableName(Class<?> clazz) throws Exception {
		if (clazz.isAnnotationPresent(Entity.class)) {
			Entity entity = clazz.getAnnotation(Entity.class);
			return entity.value();
		} else {
			throw new Exception(clazz.getName() + " is not Entity Annotation.");
		}
	}

	private void setParameter(List<Object> values, PreparedStatement ps,
			boolean isSearch) throws SQLException {
		for (int i = 1; i <= values.size(); i++) {
			Object fieldValue = values.get(i - 1);
			Class<?> clazzValue = fieldValue.getClass();
			if (clazzValue == String.class) {
				if (isSearch)
					ps.setString(i, "%" + (String) fieldValue + "%");
				else {
					ps.setString(i, (String) fieldValue);
				}

			} else if (clazzValue == boolean.class
					|| clazzValue == Boolean.class) {
				ps.setBoolean(i, (Boolean) fieldValue);

			} else if (clazzValue == long.class || clazzValue == Long.class) {
				ps.setLong(i, (Long) fieldValue);

			} else if (clazzValue == byte.class || clazzValue == Byte.class) {
				ps.setByte(i, (Byte) fieldValue);
			} else if (clazzValue == char.class
					|| clazzValue == Character.class) {
				ps.setObject(i, fieldValue, Types.CHAR);
			} else if (clazzValue == Date.class) {
				ps.setTimestamp(i, new Timestamp(((Date) fieldValue).getTime()));
			} else if (clazzValue.isArray()) {
				Object[] arrayValue = (Object[]) fieldValue;
				StringBuffer sb = new StringBuffer();
				for (int j = 0; j < arrayValue.length; j++) {
					sb.append(arrayValue[j]).append("¡¢");
				}
				ps.setString(i, sb.deleteCharAt(sb.length() - 1).toString());
			} else if (clazzValue.isEnum())
				ps.setInt(i, ((Enum) fieldValue).ordinal());
			else {
				ps.setObject(i, fieldValue, Types.NUMERIC);
			}
		}
	}
}