package system.vo;

import java.util.ArrayList;
import java.util.List;

import system.po.Application;
import system.po.ApplicationResult;
import system.po.Task;
import system.po.Application.StatueType;
import system.po.Application.Type;
import system.po.spec.Downloadable;
import system.po.ApplicationStage;

public class ApplicationWrap implements Comparable<ApplicationWrap>, Downloadable{

	private Application application;
	private List<ApplicationStageWrap> stages;
	private ApplicationResult result;


	public ApplicationWrap() {
		super();
		this.application = new Application();
	}
	public ApplicationWrap(Application application) {
		super();
		this.application = application;
	}

	public ApplicationWrap(Application application,
			List<ApplicationStageWrap> stages, ApplicationResult result) {
		super();
		this.application = application;
		this.stages = stages;
		this.result = result;
	}
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	public List<ApplicationStageWrap> getStages() {
		return stages;
	}
	public void setStages(List<ApplicationStageWrap> stages) {
		this.stages = stages;
	}
	public String getId() {
		return application.getId();
	}
	public void setId(String id) {
		application.setId(id);
	}
	public String getSenderName() {
		return application.getSenderName();
	}
	public void setSenderName(String senderName) {
		application.setSenderName(senderName);
	}
	public String getRecieverName() {
		return application.getRecieverName();
	}
	public void setRecieverName(String recieverName) {
		application.setRecieverName(recieverName);
	}
	public Type getType() {
		return application.getType();
	}
	public void setType(Type type) {
		application.setType(type);
	}
	public String getTitle() {
		return application.getTitle();
	}
	public void setTitle(String title) {
		application.setTitle(title);
	}
	public int getStage() {
		return application.getStage();
	}
	public void setStage(int stage) {
		application.setStage(stage);
	}
	public String getBody() {
		return application.getBody();
	}
	public void setBody(String body) {
		application.setBody(body);
	}
	public Long getTime() {
		return application.getTime();
	}
	public void setTime(Long time) {
		application.setTime(time);
	}
	public String getFileId() {
		return application.getFileId();
	}
	public void setFileId(String fileId) {
		application.setFileId(fileId);
	}
	public StatueType getStatue() {
		return application.getStatue();
	}
	public void setStatue(StatueType statue) {
		application.setStatue(statue);
	}
	public String getData() {
		return application.getData();
	}
	public void setData(String data) {
		application.setData(data);
	}
	public String getClazz() {
		return application.getClazz();
	}
	public void setClazz(String clazz) {
		application.setClazz(clazz);
	}

	public ApplicationResult getResult() {
		return result;
	}
	public void setResult(ApplicationResult result) {
		this.result = result;
	}

	public String getRecieverRole() {
		return application.getRecieverRole();
	}
	public void setRecieverRole(String recieverRole) {
		application.setRecieverRole(recieverRole);
	}
	public static List<ApplicationWrap> convert(List<Application> list)
	{
		if(list == null)
			return new ArrayList<ApplicationWrap>();
		List<ApplicationWrap> results = new ArrayList<ApplicationWrap>();
		for(Application t:list)
		{
			results.add(new ApplicationWrap(t));
		}

		return results;
	}
	@Override
	public String toString() {
		return "ApplicationWrap [application=" + application + ", stages="
				+ stages + ", result=" + result + "]";
	}
	@Override
	public int compareTo(ApplicationWrap o) {
		return o.getTime().compareTo(this.getTime());
	}


}
