package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;


@Entity("rd4_upload_file")
public class UploadFile implements Printable{

	@Id("id")
	private String id;
	@Column("uploader_name")
	private String uploaderName;
	@Column("file_path")
	private String filePath;
	@Column("file_name")
	private String fileName;


	public UploadFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UploadFile(UploadFile uploadFile) {
		this.id = uploadFile.getId();
		this.uploaderName = uploadFile.getUploaderName();
		this.filePath = uploadFile.getFilePath();
		this.fileName = uploadFile.getFileName();
	}
	public UploadFile(String uploaderName, String filePath,
			String fileName) {
		super();
		this.id = UUID.randomUUID().toString();
		this.uploaderName = uploaderName;
		this.filePath = filePath;
		this.fileName = fileName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUploaderName() {
		return uploaderName;
	}
	public void setUploaderName(String uploaderName) {
		this.uploaderName = uploaderName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	@Override
	public UploadFile clone() throws CloneNotSupportedException {
		return new UploadFile(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result
				+ ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((uploaderName == null) ? 0 : uploaderName.hashCode());
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
		UploadFile other = (UploadFile) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (uploaderName == null) {
			if (other.uploaderName != null)
				return false;
		} else if (!uploaderName.equals(other.uploaderName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "UploadFile [id=" + id + ", uploaderName=" + uploaderName
				+ ", filePath=" + filePath + ", fileName=" + fileName + "]";
	}

	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return null;
	}


}
