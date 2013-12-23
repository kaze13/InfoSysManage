package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Downloadable;
import system.po.spec.Printable;

@Entity("rd4_application_result")
public class ApplicationResult implements Printable,Downloadable{

	@Id("id")
	private String id;
	@Column("application_class_id")
	private String applicationClazzId;
	@Column("type")
	private Type type;
	@Column("title")
	private String title;
	@Column("body")
	private String body;
	@Column("time")
	private Long time;
	@Column("file_id")
	private String fileId;
	@Column("data1")
	private String data1;
	@Column("data2")
	private String data2;
	@Column("data3")
	private String data3;
	@Column("statue")
	private StatueType statue;

	public enum Type {
		CREATE_GUEST, NORMAL,
	}

	public enum StatueType {
		UNREAD, READ
	}


	public ApplicationResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApplicationResult(ApplicationResult applicationResult) {
		super();
		this.id = applicationResult.id;
		this.applicationClazzId = applicationResult.applicationClazzId;
		this.type = applicationResult.type;
		this.title = applicationResult.title;
		this.body = applicationResult.body;
		this.time = applicationResult.time;
		this.fileId = applicationResult.fileId;
		this.data1 = applicationResult.data1;
		this.data2 = applicationResult.data2;
		this.data3 = applicationResult.data3;
		this.statue = applicationResult.statue;
	}

	public ApplicationResult(String applicationClazzId, Type type,
			String title, String body, Long time, String fileId, String data1,
			String data2, String data3) {
		super();
		this.id = UUID.randomUUID().toString();
		this.applicationClazzId = applicationClazzId;
		this.type = type;
		this.title = title;
		this.body = body;
		this.time = time;
		this.fileId = fileId;
		this.data1 = data1;
		this.data2 = data2;
		this.data3 = data3;
		this.statue = StatueType.UNREAD;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public String getApplicationClazzId() {
		return applicationClazzId;
	}

	public void setApplicationClazzId(String applicationClazzId) {
		this.applicationClazzId = applicationClazzId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}



	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public String getData3() {
		return data3;
	}

	public void setData3(String data3) {
		this.data3 = data3;
	}

	public StatueType getStatue() {
		return statue;
	}

	public void setStatue(StatueType statue) {
		this.statue = statue;
	}

	@Override
	public ApplicationResult clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new ApplicationResult(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((applicationClazzId == null) ? 0 : applicationClazzId
						.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((data1 == null) ? 0 : data1.hashCode());
		result = prime * result + ((data2 == null) ? 0 : data2.hashCode());
		result = prime * result + ((data3 == null) ? 0 : data3.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((statue == null) ? 0 : statue.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ApplicationResult other = (ApplicationResult) obj;
		if (applicationClazzId == null) {
			if (other.applicationClazzId != null)
				return false;
		} else if (!applicationClazzId.equals(other.applicationClazzId))
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (data1 == null) {
			if (other.data1 != null)
				return false;
		} else if (!data1.equals(other.data1))
			return false;
		if (data2 == null) {
			if (other.data2 != null)
				return false;
		} else if (!data2.equals(other.data2))
			return false;
		if (data3 == null) {
			if (other.data3 != null)
				return false;
		} else if (!data3.equals(other.data3))
			return false;
		if (fileId == null) {
			if (other.fileId != null)
				return false;
		} else if (!fileId.equals(other.fileId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (statue != other.statue)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return null;
	}


}
