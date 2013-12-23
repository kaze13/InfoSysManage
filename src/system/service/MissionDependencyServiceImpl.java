package system.service;

import java.io.Serializable;

import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.MissionDependency;
import system.service.spec.MissionDependencyService;

@Interceptors(TimerInterceptor.class)
public class MissionDependencyServiceImpl extends AbstractDataAccessService<MissionDependency>
implements Serializable, MissionDependencyService{




	public MissionDependencyServiceImpl() {
		super(MissionDependency.class);
	}

}
