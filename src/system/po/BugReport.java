package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;


@Entity("rd4_bug_report")
public class BugReport implements Printable{

	@Id("id")
	private String id;
	@Column("system_name")
	private String systemName;
	@Column("founder_name")
	private String founderName;
	@Column("report")
	private String report;
	@Column("fix_report")
	private String fixReport;
	@Column("isfixed")
	private int isFixed;
	@Column("file_id")
	private String fileId;


	public BugReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BugReport(BugReport bugReport) {
		this.id = bugReport.getId();
		this.systemName = bugReport.getSystemName();
		this.founderName = bugReport.getFounderName();
		this.report = bugReport.getReport();
		this.fixReport = bugReport.getFixReport();
		this.isFixed = bugReport.getIsFixed();
		this.fileId = bugReport.getFileId();

	}
	public BugReport(String systemName, String founderName,
			String report, String fixReport, String fileId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.systemName = systemName;
		this.founderName = founderName;
		this.report = report;
		this.fixReport = fixReport;
		this.isFixed = 0;
		this.fileId = fileId;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getSystemName() {
		return systemName;
	}


	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}


	public String getFounderName() {
		return founderName;
	}


	public void setFounderName(String founderName) {
		this.founderName = founderName;
	}


	public String getReport() {
		return report;
	}


	public void setReport(String report) {
		this.report = report;
	}


	public String getFixReport() {
		return fixReport;
	}


	public void setFixReport(String fixReport) {
		this.fixReport = fixReport;
	}


	public int getIsFixed() {
		return isFixed;
	}


	public void setIsFixed(int isFixed) {
		this.isFixed = isFixed;
	}


	public String getFileId() {
		return fileId;
	}


	public void setFileId(String fileId) {
		this.fileId = fileId;
	}


	@Override
	public BugReport clone() throws CloneNotSupportedException {
		return new BugReport(this);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result
				+ ((fixReport == null) ? 0 : fixReport.hashCode());
		result = prime * result
				+ ((founderName == null) ? 0 : founderName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + isFixed;
		result = prime * result + ((report == null) ? 0 : report.hashCode());
		result = prime * result
				+ ((systemName == null) ? 0 : systemName.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BugReport other = (BugReport) obj;
		if (fileId == null) {
			if (other.fileId != null)
				return false;
		} else if (!fileId.equals(other.fileId))
			return false;
		if (fixReport == null) {
			if (other.fixReport != null)
				return false;
		} else if (!fixReport.equals(other.fixReport))
			return false;
		if (founderName == null) {
			if (other.founderName != null)
				return false;
		} else if (!founderName.equals(other.founderName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isFixed != other.isFixed)
			return false;
		if (report == null) {
			if (other.report != null)
				return false;
		} else if (!report.equals(other.report))
			return false;
		if (systemName == null) {
			if (other.systemName != null)
				return false;
		} else if (!systemName.equals(other.systemName))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "BugReport [id=" + id + ", systemName=" + systemName
				+ ", founderName=" + founderName + ", report=" + report
				+ ", fixReport=" + fixReport + ", isFixed=" + isFixed
				+ ", fileId=" + fileId + "]";
	}


	@Override
	public String toFormatString() {
		return
		"\n*BUG REPORT\n" +
		"System name: " + systemName + "\n" +
		"Founder name: " + founderName + "\n" +
		"Report: " + report + "\n" +
		"Fix Report: " + fixReport + "\n";
	}



}
