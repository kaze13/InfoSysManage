package system.po.spec;

import java.io.Serializable;

public interface Printable extends Cloneable, Serializable{

	public String toFormatString();

	public Printable clone() throws CloneNotSupportedException;

}
