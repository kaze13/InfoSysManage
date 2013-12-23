package system.manager.spec;

import java.util.Map;

public interface GlobalManager {

	public Map<Class<?>, Long> getGlobalCacheSignature();
}
