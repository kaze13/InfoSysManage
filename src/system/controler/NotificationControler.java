package system.controler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.component.layout.LayoutUnit;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.NotificationListDataModel;
import system.manager.spec.SessionManager;
import system.po.Application;
import system.po.Notification;
import system.po.Notification.Type;
import system.po.UploadFile;
import system.service.NotificationServiceImpl;
import system.service.UploadFileServiceImpl;
import system.service.spec.TemporaryAuthorityService;
import system.service.spec.UploadFileService;
import system.vo.ApplicationWrap;

@Named
@SessionScoped
public class NotificationControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2161542396341215615L;
	@Inject
	private NotificationServiceImpl notificationService;
	@Inject
	private UploadFileServiceImpl uploadFileService;
	@Inject
	TemporaryAuthorityService temporaryAuthorityService;

	private Notification[] selectedNotifications;
	private NotificationListDataModel notificationListModel;
	private Notification selectedNotification;
	private StreamedContent downloadFile;
	private String rejectReason;
	private Date acceptExpireDate;
	private List<Notification> baseNotificationList;
	private List<Notification> allNotificationBackup;
	@Inject
	private SessionManager sessionManager;

	private LayoutUnit dataUnit;
	private LayoutUnit detialUnit;

	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("reciever_name", sessionManager.getLoginUser().getName());
		baseNotificationList = notificationService.findAllByCondition(sqlMap);
		allNotificationBackup = new ArrayList<Notification>(
				baseNotificationList);
		notificationListModel = new NotificationListDataModel(
				baseNotificationList);

		Map<String, String> varMap = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String id = varMap.get("id");
		if (id != null) {
			for (Notification notification : baseNotificationList) {
				if (notification.getId().equals(id)) {
					selectedNotification = notification;
					break;
				}
			}
		}

	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeleteNotification() throws Exception {

		notificationService.delete(selectedNotification.getId());
		@SuppressWarnings("unchecked")
		List<Notification> baseNotificationList = (List<Notification>) notificationListModel
				.getWrappedData();

		int index = findNotificationData(selectedNotification.getId());
		if (index >= 0) {
			baseNotificationList.remove(index);
		}
	}
	@Interceptors(TransactionInterceptor.class)
	public void markAllAsRead() throws Exception
	{
		List<Notification> unreadNotification = new ArrayList<Notification>();
		for(Notification n:baseNotificationList)
		{
			if(n.getStatue() == Notification.StatueType.UNREAD)
			{
				n.setStatue(Notification.StatueType.READ);
				unreadNotification.add(n);
			}
		}
		notificationService.update(unreadNotification);
	}
	public String redirect() throws IOException
	{
		String link = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestContextPath()
				+ "/application.jsf?id=" + selectedNotification.getLink();
		if(selectedNotification.getType() == Type.NEW_MISSION)
			return "./task.jsf?faces-redirect=true&id=" + selectedNotification.getLink();
		return "./application.jsf?faces-redirect=true&id=" + selectedNotification.getLink();
	}
	public void showAll() {

		baseNotificationList = new ArrayList<Notification>(
				allNotificationBackup);
		notificationListModel.setWrappedData(baseNotificationList);
	}

	public void showRead() {
		baseNotificationList = new ArrayList<Notification>(
				allNotificationBackup);
		notificationListModel.setWrappedData(baseNotificationList);
		for (int i = baseNotificationList.size() - 1; i >= 0; --i) {
			if (baseNotificationList.get(i).getStatue() != Notification.StatueType.READ)
				baseNotificationList.remove(i);
		}
		notificationListModel.setWrappedData(baseNotificationList);
	}

	public void showNotRead() {
		baseNotificationList = new ArrayList<Notification>(
				allNotificationBackup);
		notificationListModel.setWrappedData(baseNotificationList);
		for (int i = baseNotificationList.size() - 1; i >= 0; --i) {
			if (baseNotificationList.get(i).getStatue() != Notification.StatueType.UNREAD)
				baseNotificationList.remove(i);
		}
		notificationListModel.setWrappedData(baseNotificationList);
	}

	@Interceptors(TransactionInterceptor.class)
	public void readItem() throws Exception {

		if (selectedNotification.getStatue() == Notification.StatueType.UNREAD) {
			selectedNotification.setStatue(Notification.StatueType.READ);
			notificationService.update(selectedNotification);
			replaceNotificationData(selectedNotification);
		}

	}

	public String abstraction(String text, int length) {
		if (text.length() <= length)
			return text;
		else {
			return text.substring(0, length) + "....";
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void processDownload() throws Exception {
		if (!selectedNotification.getFileId().equals("none")) {
			UploadFile file = uploadFileService.get(selectedNotification
					.getFileId());
			InputStream stream = new FileInputStream(file.getFilePath()
					+ file.getFileName());
			downloadFile = new DefaultStreamedContent(stream, "test",
					file.getFileName());
		}
	}

	@SuppressWarnings("deprecation")
	public String convertTime(Long second) {
		return new Date(second).toGMTString();
	}

	private int findNotificationData(String name) {
		@SuppressWarnings("unchecked")
		List<Notification> baseNotificationList = (List<Notification>) notificationListModel
				.getWrappedData();
		for (int i = 0; i < baseNotificationList.size(); i++) {
			Notification Notification = baseNotificationList.get(i);
			if (Notification.getId().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	private void replaceNotificationData(Notification updatedNotification) {
		int index = findNotificationData(updatedNotification.getId());
		if (index >= 0) {
			List<Notification> baseNotificationList = (List<Notification>) notificationListModel
					.getWrappedData();
			baseNotificationList.set(index, new Notification(
					updatedNotification));
		}
	}

	public void changeLayoutHorizontal() {
		dataUnit.setPosition("west");
		detialUnit.setPosition("center");
	}

	public void changeLayoutVertical() {
		dataUnit.setPosition("center");
		detialUnit.setPosition("south");
	}
	// getter/setter

	public UploadFileService getUploadFileService() {
		return uploadFileService;
	}

	public void setUploadFileService(UploadFileServiceImpl uploadFileService) {
		this.uploadFileService = uploadFileService;
	}

	public Notification[] getSelectedNotifications() {
		return selectedNotifications;
	}

	public void setSelectedNotifications(Notification[] selectedNotifications) {
		this.selectedNotifications = selectedNotifications;
	}

	public NotificationListDataModel getNotificationListModel() {
		return notificationListModel;
	}

	public void setNotificationListModel(
			NotificationListDataModel notificationListModel) {
		this.notificationListModel = notificationListModel;
	}

	public Notification getSelectedNotification() {
		return selectedNotification;
	}

	public void setSelectedNotification(Notification selectedNotification) {
		this.selectedNotification = selectedNotification;
	}

	public StreamedContent getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(StreamedContent downloadFile) {
		this.downloadFile = downloadFile;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Date getAcceptExpireDate() {
		return acceptExpireDate;
	}

	public void setAcceptExpireDate(Date acceptExpireDate) {
		this.acceptExpireDate = acceptExpireDate;
	}

	public int getSelectedNotificationsSize() {
		if (selectedNotifications == null)
			return 0;
		else
			return selectedNotifications.length;
	}

	public LayoutUnit getDataUnit() {
		return dataUnit;
	}

	public void setDataUnit(LayoutUnit dataUnit) {
		this.dataUnit = dataUnit;
	}

	public LayoutUnit getDetialUnit() {
		return detialUnit;
	}

	public void setDetialUnit(LayoutUnit detialUnit) {
		this.detialUnit = detialUnit;
	}

}
