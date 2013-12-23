package system.service;

import java.io.Serializable;
import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.ApplicationResult;

@Interceptors(TimerInterceptor.class)
public class ApplicationResultServiceImpl extends AbstractDataAccessService<ApplicationResult>
		implements Serializable{



	public ApplicationResultServiceImpl() {
		super(ApplicationResult.class);
	}

}
