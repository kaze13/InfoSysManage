package system.service;

import java.io.Serializable;

import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.MainAuthority;
import system.service.spec.MainAuthorityService;

@Interceptors(TimerInterceptor.class)
public class MainAuthorityServiceImpl extends AbstractDataAccessService<MainAuthority>
implements Serializable, MainAuthorityService{




	public MainAuthorityServiceImpl() {
		super(MainAuthority.class);
	}

}
