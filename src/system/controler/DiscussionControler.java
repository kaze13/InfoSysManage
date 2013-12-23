package system.controler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.model.UploadedFile;

import system.interceptor.TransactionInterceptor;
import system.manager.spec.SessionManager;
import system.po.Discussion;
import system.po.DiscussionReply;
import system.po.UploadFile;
import system.service.DiscussionReplyServiceImpl;
import system.service.DiscussionServiceImpl;
import system.service.UploadFileServiceImpl;
import system.util.PropertyManager;
import system.vo.DiscussionReplyWrap;
import system.vo.DiscussionWrap;

@Named
@SessionScoped
public class DiscussionControler implements Serializable {

	@Inject
	private DiscussionServiceImpl discussionService;
	@Inject
	private DiscussionReplyServiceImpl discussionReplyService;

	@Inject
	private SessionManager sessionManager;
	private List<DiscussionWrap> currentDiscussionForum;
	private DiscussionWrap newDiscussion = new DiscussionWrap();
	private String objectId;
	private DiscussionWrap currentDiscussion;
	private DiscussionReplyWrap newReply = new DiscussionReplyWrap();;

	public void initDiscussion(String objectId) throws Exception {
		this.objectId = objectId;
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("belonged_object_id", objectId);
		currentDiscussionForum = DiscussionWrap.convert(discussionService
				.findAllByCondition(sqlWhereMap));
		for (DiscussionWrap discussion : currentDiscussionForum) {
			sqlWhereMap.clear();
			sqlWhereMap.put("belonged_subject_id", discussion.getId());
			discussion.setReplyList(DiscussionReplyWrap
					.convert(discussionReplyService
							.findAllByCondition(sqlWhereMap)));
		}
	}

	public void addNewSubject() throws Exception {
		newDiscussion.setBelongedObjectId(objectId);
		newDiscussion.setAuthorName(sessionManager.getLoginUser().getName());
		newDiscussion.setTime(System.currentTimeMillis());
		discussionService.save(newDiscussion.getDiscussion());
		currentDiscussionForum.add(newDiscussion);
		newDiscussion = new DiscussionWrap();
	}

	public void addNewReply() throws Exception
	{
		newReply.setBelongedSubjectId(currentDiscussion.getId());
		newReply.setAuthorName(sessionManager.getLoginUser().getName());
		newReply.setTime(System.currentTimeMillis());
		discussionReplyService.save(newReply.getDiscussionReply());
		currentDiscussion.getReplyList().add(newReply);
		newReply = new DiscussionReplyWrap();
	}
	public List<DiscussionWrap> getCurrentDiscussionForum() {
		return currentDiscussionForum;
	}

	public void setCurrentDiscussionForum(
			List<DiscussionWrap> currentDiscussionForum) {
		this.currentDiscussionForum = currentDiscussionForum;
	}

	public DiscussionWrap getNewDiscussion() {
		return newDiscussion;
	}

	public void setNewDiscussion(DiscussionWrap newDiscussion) {
		this.newDiscussion = newDiscussion;
	}


	public DiscussionWrap getCurrentDiscussion() {
		return currentDiscussion;
	}

	public void setCurrentDiscussion(DiscussionWrap currentDiscussion) {
		this.currentDiscussion = currentDiscussion;
	}

	public DiscussionReplyWrap getNewReply() {
		return newReply;
	}

	public void setNewReply(DiscussionReplyWrap newReply) {
		this.newReply = newReply;
	}

}
