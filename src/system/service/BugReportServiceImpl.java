package system.service;

import java.io.Serializable;
import javax.interceptor.Interceptors;

import system.interceptor.TimerInterceptor;
import system.po.BugReport;
import system.service.spec.BugReportService;

@Interceptors(TimerInterceptor.class)
public class BugReportServiceImpl extends AbstractDataAccessService<BugReport>
		implements Serializable, BugReportService {



	public BugReportServiceImpl() {
		super(BugReport.class);
	}

	/* (non-Javadoc)
	 * @see system.service.BugReportService#bugFixed(system.po.BugReport, java.lang.String)
	 */
	@Override
	public void bugFixed(BugReport bugReport, String comment) throws Exception
	{
		bugReport.setFixReport(comment);
		bugReport.setIsFixed(1);
		this.update(bugReport);

	}
}
