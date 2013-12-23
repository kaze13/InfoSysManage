package system.vo;
import java.util.Date;
import java.util.List;

import system.po.System;
import system.po.System.Status;
import system.po.SystemFunction;
public class SystemVo {

	private System system;
	private List<SystemFunction> functionList;


	public SystemVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SystemVo(System system, List<SystemFunction> functionList) {
		super();
		this.system = system;
		this.functionList = functionList;
	}
	public System getSystem() {
		return system;
	}
	public void setSystem(System system) {
		this.system = system;
	}
	public List<SystemFunction> getFunctionList() {
		return functionList;
	}
	public void setFunctionList(List<SystemFunction> functionList) {
		this.functionList = functionList;
	}
	public Date getStartTime() {
		return system.getStartTime();
	}
	public Date getStopTime() {
		return system.getStopTime();
	}

	public Status getStatus() {
		return system.getStatus();
	}
	public void setStatus(Status status) {
		system.setStatus(status);
	}
	public String getDescription() {
		return system.getDescription();
	}
	public void setDescription(String description) {
		system.setDescription(description);
	}
	public String getWarPath() {
		return system.getWarPath();
	}
	public void setWarPath(String warPath) {
		system.setWarPath(warPath);
	}
	public String getName() {
		return system.getName();
	}
	public void setName(String name) {
		system.setName(name);
	}
	public String getUrl() {
		return system.getUrl();
	}
	public void setUrl(String url) {
		system.setUrl(url);
	}
	public String getDuration() {
		return system.getDuration();
	}
	public void setDuration(String duration) {
		system.setDuration(duration);
	}
	@Override
	public String toString() {
		return "SystemVo [system=" + system + ", functionList=" + functionList
				+ "]";
	}


}
