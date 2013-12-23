package system.manager.spec;

import java.io.IOException;

import system.po.User;


public interface SessionManager {
	public User getLoginUser() throws Exception;
	public boolean hasUserRole(String role);
	public void logoff();
	public void redirect(String url)  throws IOException;
	public void addGlobalMessageFatal(String summary, String detail);
	public void addGlobalMessageWarn(String summary, String detail);
	public void addGlobalMessageInfo(String summary, String detail);
}
