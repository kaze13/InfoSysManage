package system.controler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DualListModel;

import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.SystemFunctionListDataModel;
import system.po.SystemFunction;
import system.po.System;
import system.service.ApplicationServiceImpl;
import system.service.SystemServiceImpl;
import system.service.spec.SystemFunctionService;

@Named
@SessionScoped
public class GuestControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6687451481974166023L;
	@Inject
	private SystemServiceImpl systemService;
	@Inject
	private SystemFunctionService systemFunctionService;
	@Inject
	private ApplicationServiceImpl applicationService;
	private String guestName;
	private String guestDescription;
	private String businessDescription;
	private List<System> baseSystemList;
	private DualListModel<System> dependentSystem;
	private boolean skip = false;
	private Date expireDate;
	private SystemFunctionListDataModel sourceFunctionModel;
	private SystemFunctionListDataModel targetFunctionModel;

	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		baseSystemList = systemService.findAllByCondition(null);
		dependentSystem = new DualListModel<System>();
		dependentSystem.setSource(baseSystemList);
		dependentSystem.setTarget(new ArrayList<System>());
		sourceFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		targetFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
	}

	@Interceptors(TransactionInterceptor.class)
	public String onFlowProcess(FlowEvent event) throws Exception {

		if (event.getNewStep().equals("guestDependentFunctions")) {
			List<SystemFunction> list = systemFunctionService
					.findFunctionBySystems(dependentSystem.getTarget());
			sourceFunctionModel = new SystemFunctionListDataModel(list);
		}
		if (skip) {
			skip = false; // reset in case user goes back
			return "guestConfirm";
		}
		return event.getNewStep();
	}

	public void deleteTargetFunction(SystemFunction function) {
		((List<SystemFunction>) targetFunctionModel.getWrappedData())
				.remove(function);
	}

	@SuppressWarnings("unchecked")
	public void onFunctionDroped(DragDropEvent ddEvent) {
		SystemFunction function = ((SystemFunction) ddEvent.getData());
		((List<SystemFunction>) sourceFunctionModel.getWrappedData())
				.remove(function);
		List<SystemFunction> targetList = ((List<SystemFunction>) targetFunctionModel
				.getWrappedData());
		for (SystemFunction f : targetList) {
			if (f.getId().equals(function.getId()))
				return;
		}

		((List<SystemFunction>) targetFunctionModel.getWrappedData())
				.add(function);

	}

	@Interceptors(TransactionInterceptor.class)
	public void applyForCreateGuest() throws Exception {
		applicationService.sendCreateGuestApplication(guestName,
				guestDescription, businessDescription,
				(List<SystemFunction>)targetFunctionModel.getWrappedData(), expireDate);
		guestName = null;
		guestDescription = null;
		businessDescription = null;
		sourceFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		targetFunctionModel = new SystemFunctionListDataModel(
				new ArrayList<SystemFunction>());
		expireDate = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Application sent"));
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getGuestDescription() {
		return guestDescription;
	}

	public void setGuestDescription(String guestDescription) {
		this.guestDescription = guestDescription;
	}

	public String getBusinessDescription() {
		return businessDescription;
	}

	public void setBusinessDescription(String businessDescription) {
		this.businessDescription = businessDescription;
	}

	public List<System> getBaseSystemList() {
		return baseSystemList;
	}

	public void setBaseSystemList(List<System> baseSystemList) {
		this.baseSystemList = baseSystemList;
	}

	public DualListModel<System> getDependentSystem() {
		return dependentSystem;
	}

	public void setDependentSystem(DualListModel<System> dependentSystem) {
		this.dependentSystem = dependentSystem;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public SystemFunctionListDataModel getSourceFunctionModel() {
		return sourceFunctionModel;
	}

	public void setSourceFunctionModel(
			SystemFunctionListDataModel sourceFunctionModel) {
		this.sourceFunctionModel = sourceFunctionModel;
	}

	public SystemFunctionListDataModel getTargetFunctionModel() {
		return targetFunctionModel;
	}

	public void setTargetFunctionModel(
			SystemFunctionListDataModel targetFunctionModel) {
		this.targetFunctionModel = targetFunctionModel;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

}
