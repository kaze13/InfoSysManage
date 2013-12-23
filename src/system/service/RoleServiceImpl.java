package system.service;

import java.io.Serializable;

import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.Role;
import system.service.spec.RoleService;

@Interceptors(TimerInterceptor.class)
public class RoleServiceImpl extends AbstractDataAccessService<Role>
implements Serializable, RoleService{



	public RoleServiceImpl() {
		super(Role.class);
	}

}
