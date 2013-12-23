package system.service;

import java.io.Serializable;


import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.UploadFile;
import system.service.spec.UploadFileService;
@Interceptors(TimerInterceptor.class)
public class UploadFileServiceImpl extends AbstractDataAccessService<UploadFile>
implements Serializable, UploadFileService{

	public UploadFileServiceImpl() {
		super(UploadFile.class);
	}

}
