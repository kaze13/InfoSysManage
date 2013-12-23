package system.po;

import java.io.Serializable;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;


@Entity("rd4_role")
public class Role implements Printable, Serializable{

	@Id("role_name")
	private String name;
	@Column("description")
	private String description;

	public Role(Role role) {
		super();
		this.name = role.getName();
		this.description = role.getDescription();
	}

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Role(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public Role clone() throws CloneNotSupportedException {
		return new Role(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Role [name=" + name + ", description=" + description + "]";
	}

	@Override
	public String toFormatString() {
		return
		"\n*ROLE*\n" +
		"Name: " + name + "\n" +
		"Description: " + description + "\n";
	}




}
