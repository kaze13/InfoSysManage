package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.po.Message;

public class MessageListDataModel  extends ListDataModel<Message> implements
SelectableDataModel<Message>, Serializable{




	/**
	 *
	 */
	private static final long serialVersionUID = 2693133957849032059L;

	public MessageListDataModel(List<Message> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Message getRowData(String rowKey) {
		String MessageId = rowKey;
		List<Message> MessageList = (List<Message>)getWrappedData();
		for (Message Message : MessageList) {
			if(Message.getId().equals(MessageId)){
				return Message;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(Message Message) {
		return Message.getId();
	}

}
