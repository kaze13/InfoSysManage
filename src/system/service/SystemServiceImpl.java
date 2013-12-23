package system.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.annotation.SystemInject;
import system.dao.impl.SystemDaoImpl;
import system.interceptor.TimerInterceptor;
import system.po.DependentSystem;
import system.po.System;
import system.service.spec.SystemService;

@Interceptors(TimerInterceptor.class)
public class SystemServiceImpl extends AbstractDataAccessService<System>
		implements Serializable ,SystemService{


	@SystemInject @Inject
	private SystemDaoImpl systemDao;

	@Inject
	private AbstractDataAccessService<DependentSystem> dependentSystemDataAccessService;
	public SystemServiceImpl() {
		super(System.class);
	}

	public List<System> findDependentSystems(String id) throws Exception
	{
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("target_id", id);
		List<DependentSystem> dsList = dependentSystemDataAccessService.findAllByCondition(sqlWhereMap);
		List<System> result = new ArrayList<System>();
		for(DependentSystem ds:dsList)
		{
			result.add(this.get(ds.getSystemName()));
		}
		return result;
	}

	public List<System> findSystemByAdmin(String adminName) throws Exception
	{
		return systemDao.findSystemByAdmin(System.class, adminName, transaction);
	}

	public void shutdownSystem(System system) throws Exception
	{
		FacesContext context = FacesContext.getCurrentInstance();
		system.setStatus(System.Status.OFF);
		update(system);
		context.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "System shutdown successfully",
				""));
	}

	public void startupSystem(System system) throws Exception
	{
		FacesContext context = FacesContext.getCurrentInstance();
		system.setStatus(System.Status.ON);
		update(system);
		context.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "System start up successfully",
				""));
	}
}
