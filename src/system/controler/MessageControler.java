package system.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.component.layout.LayoutUnit;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import system.interceptor.TransactionInterceptor;
import system.jsf.datamodel.MessageListDataModel;
import system.manager.spec.SessionManager;
import system.po.Message;
import system.po.Message;
import system.po.Notification;
import system.po.UploadFile;
import system.service.MessageServiceImpl;
import system.service.UploadFileServiceImpl;
import system.util.PropertyManager;
import system.vo.ApplicationWrap;

@Named
@SessionScoped
public class MessageControler implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2385373275044125485L;

	@Inject
	private MessageServiceImpl messageService;
	@Inject
	private UploadFileServiceImpl uploadFileService;
	private Message[] selectedMessages;
	private MessageListDataModel messageListModel;
	private Message newMessage = new Message();
	private Message selectedMessage;
	private UploadedFile file;
	private StreamedContent downloadFile;

	private List<Message> baseMessageList;
	private List<Message> allMessageBackup;

	private List<Message> mybaseMessageList;
	private List<Message> myAllMessageBackup;
	private boolean recievedMessageView = true;
	private String replyName;
	@Inject
	private SessionManager sessionManager;

	private LayoutUnit dataUnit;
	private LayoutUnit detialUnit;
	@Interceptors(TransactionInterceptor.class)
	public void onRefresh() throws Exception {
		recievedMessageView = true;
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		sqlMap.put("reciever_name", sessionManager.getLoginUser().getName());
		baseMessageList = messageService
				.findAllByCondition(sqlMap);
		Collections.sort(baseMessageList);
		allMessageBackup = new ArrayList<Message>(baseMessageList);
		messageListModel = new MessageListDataModel(baseMessageList);

		sqlMap.clear();
		sqlMap.put("sender_name", sessionManager.getLoginUser().getName());
		mybaseMessageList = messageService.findAllByCondition(sqlMap);
		Collections.sort(mybaseMessageList);
		myAllMessageBackup = new ArrayList<Message>(
				mybaseMessageList);

		Map<String, String> varMap = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String id = varMap.get("id");
		if (id != null) {
			for (Message msg : mybaseMessageList) {
				if (msg.getId().equals(id)) {

					showSentMessage();
					selectedMessage = msg;
					break;
				}
			}
			for (Message msg : baseMessageList) {
				if (msg.getId().equals(id)) {

					showRecievedMessage();
					selectedMessage = msg;
					break;
				}
			}
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void doSendMessage() throws Exception {

		String absolutePath = "";
		if (file != null) {
			Properties property = PropertyManager.getApplicationProperties();
			String path = property.getProperty("uploadpath");
			String newFileName = file.getFileName();
			UploadFile uploadFile = new UploadFile(sessionManager
					.getLoginUser().getName(), path, newFileName);
			uploadFileService.save(uploadFile);

			absolutePath = path + newFileName;
			File newFile = new File(absolutePath);

			FileOutputStream a = new FileOutputStream(newFile);
			a.write(file.getContents());
			a.close();
			messageService.sendMessage(newMessage.getRecieverName(),
					newMessage.getTitle(), newMessage.getBody(),
					uploadFile.getId());
		} else
			messageService.sendMessage(newMessage.getRecieverName(),
					newMessage.getTitle(), newMessage.getBody(), "none");
		newMessage = new Message();
	}

	@Interceptors(TransactionInterceptor.class)
	public void markAllAsRead() throws Exception
	{
		List<Message> unreadMessage = new ArrayList<Message>();
		for(Message n:baseMessageList)
		{
			if(n.getStatue() == Message.StatueType.NOT_READ)
			{
				n.setStatue(Message.StatueType.READ);
				unreadMessage.add(n);
			}
		}
		messageService.update(unreadMessage);
	}


	public void doReplyMessage() {
		replyName = selectedMessage.getSenderName();
	}

	public void showSentMessage() {
		messageListModel.setWrappedData(myAllMessageBackup);
		recievedMessageView = false;
	}

	public void showRecievedMessage() {
		messageListModel.setWrappedData(allMessageBackup);
		recievedMessageView = true;
	}
	public void showAll() {

		baseMessageList = new ArrayList<Message>(
				allMessageBackup);
		messageListModel.setWrappedData(baseMessageList);
	}

	public void showRead() {
		baseMessageList = new ArrayList<Message>(
				allMessageBackup);
		messageListModel.setWrappedData(baseMessageList);
		for (int i = baseMessageList.size() - 1; i >= 0; --i) {
			if (baseMessageList.get(i).getStatue() != Message.StatueType.READ)
				baseMessageList.remove(i);
		}
		messageListModel.setWrappedData(baseMessageList);
	}

	public void showNotRead() {
		baseMessageList = new ArrayList<Message>(
				allMessageBackup);
		messageListModel.setWrappedData(baseMessageList);
		for (int i = baseMessageList.size() - 1; i >= 0; --i) {
			if (baseMessageList.get(i).getStatue() != Message.StatueType.NOT_READ)
				baseMessageList.remove(i);
		}
		messageListModel.setWrappedData(baseMessageList);
	}

	@Interceptors(TransactionInterceptor.class)
	public void doDeleteMessage() throws Exception {

		messageService.delete(selectedMessage.getId());
		@SuppressWarnings("unchecked")
		List<Message> baseMessageList = (List<Message>) messageListModel
				.getWrappedData();

		int index = findMessageData(selectedMessage.getId());
		if (index >= 0) {
			baseMessageList.remove(index);
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void readItem() throws Exception {
		// selectedMessage = message;
		if(!recievedMessageView)
			return;
		if (selectedMessage.getStatue() == Message.StatueType.NOT_READ) {
			selectedMessage.setStatue(Message.StatueType.READ);
			messageService.update(selectedMessage);
			replaceMessageData(selectedMessage);
		}
	}

	@Interceptors(TransactionInterceptor.class)
	public void processDownload() throws Exception {
		if (!selectedMessage.getFileId().equals("none")) {
			UploadFile file = uploadFileService
					.get(selectedMessage.getFileId());
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

	private int findMessageData(String name) {
		@SuppressWarnings("unchecked")
		List<Message> baseMessageList = (List<Message>) messageListModel
				.getWrappedData();
		for (int i = 0; i < baseMessageList.size(); i++) {
			Message Message = baseMessageList.get(i);
			if (Message.getId().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	private void replaceMessageData(Message updatedMessage) {
		int index = findMessageData(updatedMessage.getId());
		if (index >= 0) {
			List<Message> baseMessageList = (List<Message>) messageListModel
					.getWrappedData();
			baseMessageList.set(index, new Message(updatedMessage));
		}
	}

	public String abstraction(String text, int length) {
		if (text.length() <= length)
			return text;
		else {
			return text.substring(0, length) + "....";
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

	public MessageListDataModel getMessageListModel() {
		return messageListModel;
	}

	public Message[] getSelectedMessages() {
		return selectedMessages;
	}

	public void setSelectedMessages(Message[] selectedMessages) {
		this.selectedMessages = selectedMessages;
	}

	public void setMessageListModel(MessageListDataModel MessageListModel) {
		this.messageListModel = MessageListModel;
	}

	public Message getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(Message newMessage) {
		this.newMessage = newMessage;
	}

	public Message getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(Message selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	public String getReplyName() {
		return replyName;
	}

	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public StreamedContent getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(StreamedContent downloadFile) {
		this.downloadFile = downloadFile;
	}

	public int getSelectedMessagesSize() {
		if (selectedMessages == null)
			return 0;
		else
			return selectedMessages.length;
	}

	public boolean isRecievedMessageView() {
		return recievedMessageView;
	}

	public void setRecievedMessageView(boolean recievedMessageView) {
		this.recievedMessageView = recievedMessageView;
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
