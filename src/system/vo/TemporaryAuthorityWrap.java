package system.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import system.po.SystemFunction;
import system.po.TemporaryAuthority;

public class TemporaryAuthorityWrap implements Serializable{

	private TemporaryAuthority temporaryAuthority;
	private SystemFunction function;



	public TemporaryAuthorityWrap() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TemporaryAuthorityWrap(TemporaryAuthority temporaryAuthority) {
		super();
		this.temporaryAuthority = temporaryAuthority;
	}
	public TemporaryAuthorityWrap(TemporaryAuthority temporaryAuthority,
			SystemFunction function) {
		super();
		this.temporaryAuthority = temporaryAuthority;
		this.function = function;
	}
	public TemporaryAuthority getTemporaryAuthority() {
		return temporaryAuthority;
	}
	public void setTemporaryAuthority(TemporaryAuthority temporaryAuthority) {
		this.temporaryAuthority = temporaryAuthority;
	}
	public SystemFunction getFunction() {
		return function;
	}
	public void setFunction(SystemFunction function) {
		this.function = function;
	}
	public String getId() {
		return temporaryAuthority.getId();
	}
	public void setId(String id) {
		temporaryAuthority.setId(id);
	}
	public String getUserName() {
		return temporaryAuthority.getUserName();
	}
	public void setUserName(String userName) {
		temporaryAuthority.setUserName(userName);
	}
	public String getSystemFunctionId() {
		return temporaryAuthority.getSystemFunctionId();
	}
	public void setSystemFunctionId(String systemFunctionId) {
		temporaryAuthority.setSystemFunctionId(systemFunctionId);
	}
	public String getDescription() {
		return temporaryAuthority.getDescription();
	}
	public void setDescription(String description) {
		temporaryAuthority.setDescription(description);
	}
	public long getExpireTime() {
		return temporaryAuthority.getExpireTime();
	}
	public void setExpireTime(long expireTime) {
		temporaryAuthority.setExpireTime(expireTime);
	}

	public static List<TemporaryAuthorityWrap> convert(List<TemporaryAuthority> list)
	{
		List<TemporaryAuthorityWrap> results = new ArrayList<TemporaryAuthorityWrap>();
		for(TemporaryAuthority t:list)
		{
			results.add(new TemporaryAuthorityWrap(t));
		}
		return results;
	}
	@Override
	public String toString() {
		return "TemporaryAuthorityWrap [temporaryAuthority="
				+ temporaryAuthority + ", function=" + function + "]";
	}

//


}
