package system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.Discussion;

@Interceptors(TimerInterceptor.class)
public class DiscussionServiceImpl extends AbstractDataAccessService<Discussion>
		implements Serializable {



	public DiscussionServiceImpl() {
		super(Discussion.class);
	}


}
