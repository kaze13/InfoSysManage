package system.po;

import java.util.UUID;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_main_authority")
public class MainAuthority implements Printable{

	@Id("id")
	private String id;
	@Column("user_name")
	private String userName;
	@Column("authority_name")
	private String authorityName;

	public MainAuthority(String userName, String authorityName) {
		super();
		this.id = UUID.randomUUID().toString();
		this.userName = userName;
		this.authorityName = authorityName;
	}
	public MainAuthority() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MainAuthority(MainAuthority mainAuthority) {
		this.id = mainAuthority.getId();
		this.userName = mainAuthority.getUserName();
		this.authorityName = mainAuthority.getAuthorityName();
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAuthorityName() {
		return authorityName;
	}
	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}


	@Override
	public MainAuthority clone() throws CloneNotSupportedException {
		return new MainAuthority(this);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authorityName == null) ? 0 : authorityName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		MainAuthority other = (MainAuthority) obj;
		if (authorityName == null) {
			if (other.authorityName != null)
				return false;
		} else if (!authorityName.equals(other.authorityName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MainAuthority [id=" + id + ", userName=" + userName
				+ ", authorityName=" + authorityName + "]";
	}
	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return
		"\n*MAIN AUTHORITY*\n" +
		"User name: " + userName + "\n" +
		"Authority name: " + authorityName + "\n";
	}


}
