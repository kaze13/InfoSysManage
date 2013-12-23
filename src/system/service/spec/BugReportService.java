package system.service.spec;

import java.util.List;
import java.util.Map;

import system.po.BugReport;

public interface BugReportService {

	public void save(BugReport t) throws Exception;

	public void save(List<BugReport> list) throws Exception;

	public void delete(String id) throws Exception;

	public void update(BugReport t) throws Exception;

	public BugReport get(String id) throws Exception;

	public List<BugReport> findAllByCondition(Map<String, Object> sqlWhereMap)
			throws Exception;


	public abstract void bugFixed(BugReport bugReport, String comment)
			throws Exception;

}