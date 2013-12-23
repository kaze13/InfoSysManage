package system.po;

import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;


@Entity("rd4_authority")
public class Authority implements Printable{

	@Id("authority_name")
	private String authorityName;


	public Authority(Authority authority) {
		super();
		this.authorityName = authority.getAuthorityName();
	}



	public Authority(String authorityName) {
		super();
		this.authorityName = authorityName;
	}



	public Authority() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authorityName == null) ? 0 : authorityName.hashCode());
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
		Authority other = (Authority) obj;
		if (authorityName == null) {
			if (other.authorityName != null)
				return false;
		} else if (!authorityName.equals(other.authorityName))
			return false;
		return true;
	}

	@Override
	public Authority clone() throws CloneNotSupportedException {
		return new Authority(this);
	}

	@Override
	public String toString() {
		return "Authority [authorityName=" + authorityName + "]";
	}

	@Override
	public String toFormatString() {
		return
		"\n*AUTHORITY*\n" +
		"Authority name: " + authorityName + "\n";
	}


}
