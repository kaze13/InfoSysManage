package system.jsf.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import system.vo.TaskWrap;


public class TaskVoListDataModel extends ListDataModel<TaskWrap> implements
		SelectableDataModel<TaskWrap>, Serializable {



	/**
	 *
	 */
	private static final long serialVersionUID = -8886596901086130813L;

	public TaskVoListDataModel(List<TaskWrap> list) {
		super(list);
	}

	@Override
	public Object getRowKey(TaskWrap task) {
		return task.getId();
	}

	@Override
	public TaskWrap getRowData(String rowKey) {
		String taskId = rowKey;
		List<TaskWrap> taskList = (List<TaskWrap>)getWrappedData();
		for (TaskWrap task : taskList) {
			if(task.getId().equals(taskId)){
				return task;
			}
		}
		return null;
	}


}