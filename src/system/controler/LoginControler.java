package system.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FlowEvent;

import system.po.MainAuthority;
import system.po.User;
import system.service.MainAuthorityServiceImpl;
import system.service.UserServiceImpl;
import system.service.spec.MainAuthorityService;

@Named
@SessionScoped
public class LoginControler implements Serializable {

	@Inject
	private UserServiceImpl userService;
	@Inject
	private MainAuthorityServiceImpl mainAuthorityService;
	private String username;
	private String password;
	private String originalURL;

	private String newComerRealName;
	private String newComerIdentityNumber;
	private String newPassword;
	private String newUserName;
	private User newComerData;

	@PostConstruct
	public void init() {
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		originalURL = (String) externalContext.getRequestMap().get(
				RequestDispatcher.FORWARD_REQUEST_URI);

		if (originalURL == null) {
			originalURL = externalContext.getRequestContextPath()
					+ "/index.jsf";
		} else {
			String originalQuery = (String) externalContext.getRequestMap()
					.get(RequestDispatcher.FORWARD_QUERY_STRING);

			if (originalQuery != null) {
				originalURL += "?" + originalQuery;
			}
		}
	}

	public void login() throws Exception {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext
				.getRequest();

		try {

			User user = userService.get(username);
			if (user.getExpireTime() != 0) {
				if (user.getExpireTime() < System.currentTimeMillis()) {
					context.addMessage(null,
							new FacesMessage("Account expired at "
									+ new Date(user.getExpireTime()),"Please contact the administrator"));
					return;
				}
			}
			request.login(username, password);
			externalContext.getSessionMap().put("user", user);
			externalContext.redirect(originalURL);
		} catch (ServletException e) {
			// Handle unknown username/password in request.login().
			context.addMessage(null, new FacesMessage("Unknown login"));
		}
	}

	public void logout() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		externalContext.invalidateSession();
		externalContext.redirect(externalContext.getRequestContextPath()
				+ "/login.jsf");
	}

	public void initWizard() {
		newComerRealName = null;
		newComerIdentityNumber = null;
		newPassword = null;
		newUserName = null;
	}

	public String onFlowProcess(FlowEvent event) throws Exception {
		if (event.getNewStep().equals("createAccount")) {
			Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
			sqlWhereMap.put("real_name", newComerRealName);
			sqlWhereMap.put("identity_number", newComerIdentityNumber);
			List<User> newComer = userService.findAllByCondition(sqlWhereMap);
			if (newComer.size() < 1) {
				FacesContext.getCurrentInstance().addMessage(
						"checkIdentityMsg",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Employee does not exist", "Check your input"));
				return "checkIdentity";
			}
			if (newComer.size() > 1) {
				FacesContext.getCurrentInstance().addMessage(
						"checkIdentityMsg",
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"More than one data", ""));
				return "checkIdentity";
			}
			if (newComer.size() == 1) {
				if (!newComer.get(0).getName().contains("_new_comer_")) {
					FacesContext.getCurrentInstance().addMessage(
							"checkIdentityMsg",
							new FacesMessage(FacesMessage.SEVERITY_FATAL,
									"Not a new employee", "Check your input"));
					return "checkIdentity";
				}
				newComerData = newComer.get(0);
				return event.getNewStep();
			}
		}
		return event.getNewStep();
	}

	public void confirmNewAccount() throws Exception {
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("user_name", newUserName);
		List<User> result = userService.findAllByCondition(sqlWhereMap);
		if (result.size() > 0) {
			FacesContext.getCurrentInstance().addMessage(
					"accountConfirmMsg",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"User name already exist",
							"Change another user name"));
			return;
		}

		try {
			User delete = newComerData.clone();
			newComerData.setPassword(newPassword);
			newComerData.setName(newUserName);
			userService.save(newComerData);
			MainAuthority newAuthority = new MainAuthority(
					newComerData.getName(), "user");
			mainAuthorityService.save(newAuthority);
			userService.delete(delete.getName());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Exception happen", e.getMessage()));
			e.printStackTrace();
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				"growl",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Account created",
						"Now you can login"));
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOriginalURL() {
		return originalURL;
	}

	public void setOriginalURL(String originalURL) {
		this.originalURL = originalURL;
	}

	public String getNewComerRealName() {
		return newComerRealName;
	}

	public void setNewComerRealName(String newComerRealName) {
		this.newComerRealName = newComerRealName;
	}

	public String getNewComerIdentityNumber() {
		return newComerIdentityNumber;
	}

	public void setNewComerIdentityNumber(String newComerIdentityNumber) {
		this.newComerIdentityNumber = newComerIdentityNumber;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewUserName() {
		return newUserName;
	}

	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}

}
