package system.service;

import java.io.Serializable;
import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.SystemAuthority;
import system.service.spec.SystemAuthorityService;

@Interceptors(TimerInterceptor.class)
public class SystemAuthorityServiceImpl extends AbstractDataAccessService<SystemAuthority>
implements Serializable, SystemAuthorityService{




	public SystemAuthorityServiceImpl() {
		super(SystemAuthority.class);
	}

}
