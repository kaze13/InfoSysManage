package system.service;

import java.io.Serializable;

import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.TemporaryAuthority;
import system.service.spec.TemporaryAuthorityService;

@Interceptors(TimerInterceptor.class)
public class TemporaryAuthorityServiceImpl extends AbstractDataAccessService<TemporaryAuthority>
implements Serializable, TemporaryAuthorityService{






	public TemporaryAuthorityServiceImpl() {
		super(TemporaryAuthority.class);
	}

}
