package system.controler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import system.dao.spec.Transaction;
import system.interceptor.TransactionInterceptor;
import system.po.BugReport;
import system.po.System;
import system.po.Task;
import system.po.UploadFile;
import system.service.BugReportServiceImpl;
import system.service.NotificationServiceImpl;
import system.service.SystemServiceImpl;
import system.service.TaskServiceImpl;
import system.service.UploadFileServiceImpl;

@Named
@SessionScoped
public class FixBugControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5289491295934922231L;
	@Inject
	private BugReportServiceImpl bugReportService;
	@Inject
	private SystemServiceImpl systemService;
	@Inject
	private UploadFileServiceImpl uploadFileService;
	@Inject
	private NotificationServiceImpl notificationService;
	@Inject
	private TaskServiceImpl taskService;
	private String reportid;
	private BugReport bugReport;
	private System system;
	private StreamedContent downloadFile;
	private String comment;

	@Inject
	Transaction transaction;

	@PostConstruct
	public void onRefresh() throws Exception {
		try {
			transaction.begin();
			Map<String, String> varMap = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap();
			reportid = varMap.get("reportid");
			bugReport = bugReportService.get(reportid);
			system = systemService.get(bugReport.getSystemName());
			Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
			sqlWhereMap.put("data", bugReport.getId());
			Task task = taskService.findAllByCondition(sqlWhereMap).get(0);
			task.setStatue(Task.StatueType.COMPLETED);
			taskService.update(task);
			transaction.commit();
		} catch (IOException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void processDownload() throws Exception {
		if (!bugReport.getFileId().equals("none")) {
			UploadFile file = uploadFileService.get(bugReport.getFileId());
			InputStream stream = new FileInputStream(file.getFilePath()
					+ file.getFileName());
			downloadFile = new DefaultStreamedContent(stream, "test",
					file.getFileName());
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void submitFix() throws Exception {
		bugReportService.bugFixed(bugReport, comment);
		notificationService.sendBugFixedNotification(bugReport);
	}

	public BugReport getBugReport() {
		return bugReport;
	}

	public void setBugReport(BugReport bugReport) {
		this.bugReport = bugReport;
	}

	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}

	public StreamedContent getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(StreamedContent downloadFile) {
		this.downloadFile = downloadFile;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
