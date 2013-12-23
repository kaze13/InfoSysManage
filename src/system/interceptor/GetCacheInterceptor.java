package system.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import system.dao.spec.Transaction;
import system.manager.spec.CacheManager;
import system.manager.spec.GlobalManager;
import system.po.spec.Printable;

public class GetCacheInterceptor implements Serializable {

	// @Inject
	// private GlobalManager globalManager;
	// @Inject
	// private CacheManager cacheManager;

	@AroundInvoke
	public Object caching(InvocationContext ctx) throws Exception {
		// try {
		// // TODO eliminate the pass by reference problem
		// Logger logger = Logger.getLogger("mytimer");
		// Object[] params = ctx.getParameters();
		// Class<?> clazz = (Class<?>) params[0];
		// Method currentMethod = ctx.getMethod();
		//
		// Long globalSignature = globalManager.getGlobalCacheSignature().get(
		// clazz);
		// Long personalSignature = cacheManager.getSignature().get(clazz);
		// Object returnVal = null;
		// String cacheStamp = ctx.getMethod().toString();
		// for (Object obj : params) {
		// if (obj instanceof Transaction)
		// continue;
		// if (obj == null) {
		// cacheStamp += " null ";
		// continue;
		// }
		// String tmp = obj.toString();
		// if (tmp.contains("@"))
		// logger.log(Level.SEVERE, "ILLIGAL STRING FOUND!!!!!!!!");
		// cacheStamp += tmp;
		// cacheStamp += " ";
		// }
		//
		// if (globalSignature == null)
		// globalSignature = (long) 1;
		// if (personalSignature == null)
		// personalSignature = (long) 0;
		// if (globalSignature > personalSignature) {
		// logger.log(Level.INFO, "Data class" + clazz + " expired.");
		// returnVal = ctx.proceed();
		// if (returnVal == null)
		// logger.log(Level.WARNING, "Null value returned,");
		// Map<String, Object> targetCacheMap = cacheManager.getCache()
		// .get(clazz);
		// if (targetCacheMap == null) {
		// cacheManager.getCache().put(clazz,
		// new HashMap<String, Object>());
		// targetCacheMap = cacheManager.getCache().get(clazz);
		// }
		//
		// targetCacheMap.put(cacheStamp, returnVal);
		// Long newSugnature = System.currentTimeMillis();
		// cacheManager.getSignature().put(clazz, newSugnature);
		// globalManager.getGlobalCacheSignature()
		// .put(clazz, newSugnature);
		// } else {
		// returnVal = cacheManager.getCache().get(clazz).get(cacheStamp);
		//
		// logger.log(Level.INFO, "Data class" + clazz + " NOT expire.");
		// if (returnVal == null) {
		// logger.log(Level.SEVERE, "cache not hit!");
		// returnVal = ctx.proceed();
		// Map<String, Object> targetCacheMap = cacheManager
		// .getCache().get(clazz);
		// if (targetCacheMap == null)
		// cacheManager.getCache().put(clazz,
		// new HashMap<String, Object>());
		// targetCacheMap.put(cacheStamp, returnVal);
		//
		// }
		//
		// try {
		// if (returnVal instanceof Collection) {
		// Object resultCollection = returnVal.getClass()
		// .newInstance();
		// for (Object obj : (Collection<?>) returnVal) {
		// ((Collection<Printable>) resultCollection)
		// .add(((Printable) obj).clone());
		// }
		// returnVal = resultCollection;
		// } else if (returnVal instanceof Printable) {
		// returnVal = ((Printable) returnVal).clone();
		// } else if (returnVal == null) {
		// // do nothing
		// } else {
		// throw new Exception(
		// "Error in solving reference of printable.");
		//
		// }
		// } catch (Exception e) {
		// FacesContext.getCurrentInstance().addMessage(
		// null,
		// new FacesMessage("Error in solving reference", e
		// .getMessage()));
		// e.printStackTrace();
		// }
		//
		// }
		//
		// return returnVal;
		// } catch (Exception e) {
		// e.printStackTrace();
		// return null;
		// }
		return ctx.proceed();
	}
}
