package system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.DiscussionReply;

@Interceptors(TimerInterceptor.class)
public class DiscussionReplyServiceImpl extends AbstractDataAccessService<DiscussionReply>
		implements Serializable {



	public DiscussionReplyServiceImpl() {
		super(DiscussionReply.class);
	}


}
