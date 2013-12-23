package system.service.spec;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.interceptor.Interceptors;

import system.interceptor.ApplicationInterceptor;
import system.po.Application;
import system.po.BugReport;
import system.po.Mission;
import system.po.SystemFunction;
import system.po.Task;
import system.vo.UnitWrap;

public interface ApplicationService {

	public void save(Application t) throws Exception;

	public void save(List<Application> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(Application t) throws Exception;

	public Application get(String id) throws Exception;

	public List<Application> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;

	public abstract List<Application> findApplicationByClass(String clazz)
			throws Exception;

	public abstract List<Application> getUndealedApplications()
			throws Exception;

	@Interceptors(ApplicationInterceptor.class)
	public abstract Application[] sendTaskTransferApplication(String reciever,
			Task task, String report, String fileid) throws Exception;

	@Interceptors(ApplicationInterceptor.class)
	public abstract Application sendAbandonUnitTaskApplication(UnitWrap task, String report, String fileid)
			throws Exception;

	@Interceptors(ApplicationInterceptor.class)
	public abstract Application[] sendPromoteAuthorityApplication(
			List<SystemFunction> functions, String userName, String reason,
			String fileid) throws Exception;

	@Interceptors(ApplicationInterceptor.class)
	public abstract Application sendCheckErrorApplication(BugReport bugReport)
			throws Exception;

	@Interceptors(ApplicationInterceptor.class)
	public abstract Application[] sendVerifyMissionApplication(Mission mission, List<SystemFunction> dependentFunctions)
			throws Exception;

	@Interceptors(ApplicationInterceptor.class)
	public abstract Application sendRejectTaskApplication(Task task,
			String reason) throws Exception;

	@Interceptors(ApplicationInterceptor.class)
	public abstract Application sendChangeScheduleApplication(
			system.po.System system, String duration, String comment)
			throws Exception;

	@Interceptors(ApplicationInterceptor.class)
	public abstract Application sendShutdownSystemApplication(
			system.po.System system, String duration, String comment)
			throws Exception;

	@Interceptors(ApplicationInterceptor.class)
	public abstract Application[] sendCreateGuestApplication(String guestName,
			String guestDescription, String businessDescription,
			List<SystemFunction> dependentFunctions, Date expireDate)
			throws Exception;

	public abstract boolean acceptApplication(Application application,
			Object[] args) throws Exception;

	public abstract boolean rejectApplication(Application application,
			Object[] args);

	public abstract boolean acceptAbandonUnitTaskApplication(
			Application application, Object[] args);

	public abstract boolean rejectAbandonUnitTaskApplication(
			Application application, Object[] args);

	public abstract boolean acceptTaskTransferApplication(
			Application application, Object[] args);

	public abstract boolean rejectTaskTransferApplication(
			Application application, Object[] args);

	public abstract boolean acceptChangeScheduleApplication(
			Application application, Object[] args);

	public abstract boolean rejectChangeScheduleApplication(
			Application application, Object[] args);

	public abstract boolean acceptShutdownSystemApplication(
			Application application, Object[] args);

	public abstract boolean rejectShutdownSystemApplication(
			Application application, Object[] args);

	public abstract boolean acceptCreateGuestApplication(
			Application application, Object[] args);

	public abstract boolean rejectCreateGuestApplication(
			Application application, Object[] args);

}