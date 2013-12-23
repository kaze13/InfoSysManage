package system.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import system.po.Discussion;
import system.po.Discussion;
import system.po.spec.Downloadable;

public class DiscussionWrap implements Downloadable, Serializable {

	private Discussion discussion;
	private UserWrap author;
	private List<DiscussionReplyWrap> replyList;

	public DiscussionWrap(Discussion discussion) {
		super();
		this.discussion = discussion;

	}

	public DiscussionWrap() {
		super();
		this.discussion = new Discussion();
	}

	public Discussion getDiscussion() {
		return discussion;
	}

	public void setDiscussion(Discussion discussion) {
		this.discussion = discussion;
	}

	public UserWrap getAuthor() {
		return author;
	}

	public void setAuthor(UserWrap author) {
		this.author = author;
	}

	public List<DiscussionReplyWrap> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<DiscussionReplyWrap> replyList) {
		this.replyList = replyList;
	}

	public String getId() {
		return discussion.getId();
	}

	public void setId(String id) {
		discussion.setId(id);
	}

	public String getBelongedObjectId() {
		return discussion.getBelongedObjectId();
	}

	public void setBelongedObjectId(String belongedObjectId) {
		discussion.setBelongedObjectId(belongedObjectId);
	}

	public String getAuthorName() {
		return discussion.getAuthorName();
	}

	public void setAuthorName(String authorName) {
		discussion.setAuthorName(authorName);
	}

	public long getTime() {
		return discussion.getTime();
	}

	public void setTime(long time) {
		discussion.setTime(time);
	}

	public String getTitle() {
		return discussion.getTitle();
	}

	public void setTitle(String title) {
		discussion.setTitle(title);
	}

	public String getContent() {
		return discussion.getContent();
	}

	public void setContent(String content) {
		discussion.setContent(content);
	}

	public String getFileId() {
		return discussion.getFileId();
	}

	public void setFileId(String fileId) {
		discussion.setFileId(fileId);
	}

	public static List<DiscussionWrap> convert(List<Discussion> list) {
		List<DiscussionWrap> results = new ArrayList<DiscussionWrap>();
		for (Discussion m : list) {
			results.add(new DiscussionWrap(m));
		}
		return results;
	}

	@Override
	public String toString() {
		return "DiscussionWrap [discussion=" + discussion + ", author="
				+ author + ", replyList=" + replyList + "]";
	}

}
