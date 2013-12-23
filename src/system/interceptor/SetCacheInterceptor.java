package system.interceptor;

import java.io.Serializable;
import java.util.HashMap;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import system.manager.spec.CacheManager;
import system.manager.spec.GlobalManager;

public class SetCacheInterceptor implements Serializable{

//	@Inject
//	private GlobalManager globalManager;
//	@Inject
//	private CacheManager cacheManager;

	@AroundInvoke
	public Object caching(InvocationContext ctx)
	{
		try {

//			Object[] params = ctx.getParameters();
//			Class<?> clazz = (Class<?>) params[0];
//			globalManager.getGlobalCacheSignature().put(clazz, System.currentTimeMillis());
//			cacheManager.getCache().put(clazz, new HashMap<String, Object>());
			return ctx.proceed();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
