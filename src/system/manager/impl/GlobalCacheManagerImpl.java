package system.manager.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Singleton;

@Singleton
public class GlobalCacheManagerImpl implements Serializable{

	private Map<Class<?>, List<?>> cachePool = new HashMap<Class<?>,List<?>>();

	public Map<Class<?>, List<?>> getCachePool() {
		return cachePool;
	}

	public void clear()
	{
		cachePool.clear();
	}

}
