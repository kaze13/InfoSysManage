package system.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.manager.spec.SessionManager;
import system.po.Message;
import system.service.spec.MessageService;

@Interceptors(TimerInterceptor.class)
public class MessageServiceImpl extends AbstractDataAccessService<Message>
		implements Serializable, MessageService {


	@Inject
	private SessionManager sessionManager;
	public MessageServiceImpl() {
		super(Message.class);
	}

	/* (non-Javadoc)
	 * @see system.service.MessageService#getUnreadMessages()
	 */
	@Override
	public List<Message> getUnreadMessages() throws Exception
	{
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("statue", 0);
		sqlWhereMap.put("reciever_name", sessionManager.getLoginUser().getName());
		return this.findAllByCondition(sqlWhereMap);
	}


	/* (non-Javadoc)
	 * @see system.service.MessageService#sendMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessage(String reciever, String title, String body,
			String filePath) throws Exception {
		Message message = new Message(sessionManager.getLoginUser().getName(),
				reciever, title, body,
				System.currentTimeMillis(), filePath);
		this.save(message);
	}

	/* (non-Javadoc)
	 * @see system.service.MessageService#sendMessageBatch(java.util.ArrayList, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessageBatch(ArrayList<String> recievers, String title,
			String body, String filePath) throws Exception {
		List<Message> messages = new ArrayList<Message>();
		for (String u : recievers) {
			messages.add(new Message(sessionManager.getLoginUser().getName(),
					u, title, body, System
							.currentTimeMillis(), filePath));
		}
		this.save(messages);
	}


}
