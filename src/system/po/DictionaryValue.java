package system.po;

import system.annotation.Column;
import system.annotation.Entity;
import system.annotation.Id;
import system.po.spec.Printable;

@Entity("rd4_dictionary")
public class DictionaryValue implements Printable{

	@Id("dickey")
	private String dickey;
	@Column("value")
	private String value;

	public DictionaryValue(DictionaryValue dictionaryValue)
	{
		this.dickey = dictionaryValue.getDickey();
		this.value = dictionaryValue.getValue();
	}

	public DictionaryValue(String dickey, String value) {
		super();
		this.dickey = dickey;
		this.value = value;
	}

	public DictionaryValue() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDickey() {
		return dickey;
	}

	public String getValue() {
		return value;
	}

	public void setDickey(String dickey) {
		this.dickey = dickey;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public DictionaryValue clone() throws CloneNotSupportedException {
		return new DictionaryValue(this);
	}

	@Override
	public String toFormatString() {
		// TODO Auto-generated method stub
		return null;
	}


}
