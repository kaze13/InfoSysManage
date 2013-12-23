package system.service.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import system.po.Message;

public interface MessageService {

	public void save(Message t) throws Exception;

	public void save(List<Message> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(Message t) throws Exception;

	public Message get(String id) throws Exception;

	public List<Message> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;


	public abstract List<Message> getUnreadMessages() throws Exception;

	public abstract void sendMessage(String reciever, String title,
			String body, String filePath) throws Exception;

	public abstract void sendMessageBatch(ArrayList<String> recievers,
			String title, String body, String filePath) throws Exception;

}