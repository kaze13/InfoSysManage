package system.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.ApplicationStage;

@Interceptors(TimerInterceptor.class)
public class ApplicationStageServiceImpl extends AbstractDataAccessService<ApplicationStage>
		implements Serializable {



	public ApplicationStageServiceImpl() {
		super(ApplicationStage.class);
	}

}
