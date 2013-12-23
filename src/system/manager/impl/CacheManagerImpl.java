package system.manager.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.SessionScoped;

import system.manager.spec.CacheManager;

@SessionScoped
public class CacheManagerImpl implements CacheManager, Serializable{

	private Map<Class<?>,Map<String, Object>> cache;
	private Map<Class<?>,Long> signature;
	
	
	public Map<Class<?>, Map<String, Object>> getCache() {
		return cache;
	}

	public Map<Class<?>, Long> getSignature() {
		return signature;
	}
	
	public CacheManagerImpl() {
		cache = new HashMap<Class<?>,Map<String, Object>>();
		signature = new HashMap<Class<?>,Long>();
	}
}
