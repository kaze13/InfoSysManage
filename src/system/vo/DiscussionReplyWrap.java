package system.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import system.po.DiscussionReply;
import system.po.DiscussionReply;
import system.po.spec.Downloadable;

public class DiscussionReplyWrap implements Downloadable,Serializable{

	private DiscussionReply discussionReply;
	private UserWrap author;


	public DiscussionReplyWrap(DiscussionReply discussionReply) {
		super();
		this.discussionReply = discussionReply;
	}


	public DiscussionReplyWrap() {
		super();
		this.discussionReply = new DiscussionReply();
	}


	public DiscussionReply getDiscussionReply() {
		return discussionReply;
	}
	public void setDiscussionReply(DiscussionReply discussionReply) {
		this.discussionReply = discussionReply;
	}
	public UserWrap getAuthor() {
		return author;
	}
	public void setAuthor(UserWrap author) {
		this.author = author;
	}
	public String getId() {
		return discussionReply.getId();
	}
	public void setId(String id) {
		discussionReply.setId(id);
	}
	public String getBelongedSubjectId() {
		return discussionReply.getBelongedSubjectId();
	}
	public void setBelongedSubjectId(String belongedSubjectId) {
		discussionReply.setBelongedSubjectId(belongedSubjectId);
	}
	public String getAuthorName() {
		return discussionReply.getAuthorName();
	}
	public void setAuthorName(String authorName) {
		discussionReply.setAuthorName(authorName);
	}
	public long getTime() {
		return discussionReply.getTime();
	}
	public void setTime(long time) {
		discussionReply.setTime(time);
	}
	public String getContent() {
		return discussionReply.getContent();
	}
	public void setContent(String content) {
		discussionReply.setContent(content);
	}
	public String getFileId() {
		return discussionReply.getFileId();
	}
	public void setFileId(String fileId) {
		discussionReply.setFileId(fileId);
	}

	public static List<DiscussionReplyWrap> convert(List<DiscussionReply> list)
	{
		List<DiscussionReplyWrap> results = new ArrayList<DiscussionReplyWrap>();
		for(DiscussionReply m:list)
		{
			results.add(new DiscussionReplyWrap(m));
		}
		return results;
	}
	@Override
	public String toString() {
		return "DiscussionReplyWrap [discussionReply=" + discussionReply
				+ ", author=" + author + "]";
	}


}
