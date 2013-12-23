package system.vo;

import java.util.ArrayList;
import java.util.List;

import system.po.Application;
import system.po.ApplicationStage;
import system.po.ApplicationStage.Status;
import system.po.spec.Downloadable;

public class ApplicationStageWrap implements Downloadable{

	private ApplicationStage applicationStage;
	private UserWrap dealerWrap;



	public ApplicationStageWrap() {
		super();
		this.applicationStage = new ApplicationStage();
	}
	public ApplicationStageWrap(ApplicationStage applicationStage) {
		super();
		this.applicationStage = applicationStage;
	}
	public ApplicationStageWrap(ApplicationStage applicationStage,
			UserWrap dealerWrap) {
		super();
		this.applicationStage = applicationStage;
		this.dealerWrap = dealerWrap;
	}
	public ApplicationStage getApplicationStage() {
		return applicationStage;
	}
	public void setApplicationStage(ApplicationStage applicationStage) {
		this.applicationStage = applicationStage;
	}
	public UserWrap getDealerWrap() {
		return dealerWrap;
	}
	public void setDealerWrap(UserWrap dealerWrap) {
		this.dealerWrap = dealerWrap;
	}
	public String getId() {
		return applicationStage.getId();
	}
	public void setId(String id) {
		applicationStage.setId(id);
	}
	public String getApplicationClazzId() {
		return applicationStage.getApplicationClazzId();
	}
	public void setApplicationClazzId(String applicationClazzId) {
		applicationStage.setApplicationClazzId(applicationClazzId);
	}
	public int getSerial() {
		return applicationStage.getSerial();
	}
	public void setSerial(int serial) {
		applicationStage.setSerial(serial);
	}
	public String getDealerName() {
		return applicationStage.getDealerName();
	}
	public void setDealerName(String dealerName) {
		applicationStage.setDealerName(dealerName);
	}

	public String getComments() {
		return applicationStage.getComments();
	}
	public void setComments(String comments) {
		applicationStage.setComments(comments);
	}
	public String getFileId() {
		return applicationStage.getFileId();
	}
	public void setFileId(String fileId) {
		applicationStage.setFileId(fileId);
	}
	public String getName() {
		return applicationStage.getName();
	}
	public void setName(String name) {
		applicationStage.setName(name);
	}


	public Status getStatus() {
		return applicationStage.getStatus();
	}
	public void setStatus(Status status) {
		applicationStage.setStatus(status);
	}
	public static List<ApplicationStageWrap> convert(List<ApplicationStage> list)
	{
		List<ApplicationStageWrap> results = new ArrayList<ApplicationStageWrap>();
		for(ApplicationStage t:list)
		{
			results.add(new ApplicationStageWrap(t));
		}

		return results;
	}
	@Override
	public String toString() {
		return "ApplicationStageWrap [applicationStage=" + applicationStage
				+ ", dealerWrap=" + dealerWrap + "]";
	}


}
