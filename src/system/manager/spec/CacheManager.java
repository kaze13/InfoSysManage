package system.manager.spec;

import java.util.Map;

public interface CacheManager {

	
	public Map<Class<?>, Map<String, Object>> getCache();
	public Map<Class<?>, Long> getSignature();
}
