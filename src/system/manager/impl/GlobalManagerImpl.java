package system.manager.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import system.manager.spec.GlobalManager;

@Singleton
public class GlobalManagerImpl implements GlobalManager, Serializable {

	private Map<Class<?>, Long> globalCacheSignature;

	public Map<Class<?>, Long> getGlobalCacheSignature() {
		return globalCacheSignature;
	}

	public GlobalManagerImpl() {
		globalCacheSignature = new HashMap<Class<?>, Long>();
	}


}
