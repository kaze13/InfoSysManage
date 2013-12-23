package system.service;

import java.io.Serializable;

import system.po.Authority;
import system.service.spec.AuthorityService;

public class AuthorityServiceImpl extends AbstractDataAccessService<Authority>
implements Serializable, AuthorityService{



	public AuthorityServiceImpl() {
		super(Authority.class);
	}

}
