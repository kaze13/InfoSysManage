package system.controler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.primefaces.model.UploadedFile;

import system.interceptor.TransactionInterceptor;
import system.manager.spec.SessionManager;
import system.po.Role;
import system.po.UploadFile;
import system.po.User;
import system.service.ApplicationServiceImpl;
import system.service.RoleServiceImpl;
import system.service.UploadFileServiceImpl;
import system.service.UserServiceImpl;
import system.service.spec.UserService;
import system.util.PropertyManager;

@Named
@SessionScoped
public class NewEmployeesFlowControler implements Serializable {

	/**
	 *
	 */
	@Inject
	private ApplicationServiceImpl applicationService;
	@Inject
	private UploadFileServiceImpl uploadFileService;
	@Inject
	private UserServiceImpl userService;
	@Inject
	private RoleServiceImpl roleService;
	@Inject
	private SessionManager sessionManager;
	private User userProfile = new User();
	private List<User> userProfiles = new ArrayList<User>();
	private String comment;
	private UploadedFile file;
	private List<Role> baseRoleList;

	private List<User> baseUserList;

	@PostConstruct
	public void init() throws Exception
	{
		baseRoleList = roleService.findAllByCondition(null);
	}
	public String reinit() {
		userProfile = new User();

		return null;
	}

	@Interceptors(TransactionInterceptor.class)
	public String getUploadFileId() throws Exception {
		String absolutePath = "";
		if (file != null) {
			Properties property = PropertyManager.getApplicationProperties();
			String path = property.getProperty("uploadpath");
			String newFileName = file.getFileName();
			UploadFile uploadFile = new UploadFile(sessionManager
					.getLoginUser().getName(), path, newFileName);
			uploadFileService.save(uploadFile);

			absolutePath = path + newFileName;
			File newFile = new File(absolutePath);

			FileOutputStream a = new FileOutputStream(newFile);
			a.write(file.getContents());
			a.close();
			return uploadFile.getId();
		} else
			return "none";
	}

	public void submitNewEmployeeApplication() throws Exception {
		baseUserList = userService.findAllByCondition(null);
		if (userProfiles.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No input data", "Check your input"));
			return;
		}
		for (User user : userProfiles) {
			for (User old : baseUserList) {
				if (user.getIdentityNumber().equals(old.getIdentityNumber())) {
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Duplicate identity number", user
											.getRealName()
											+ "-"
											+ user.getIdentityNumber()));
					return;
				}
			}
			for (User self : userProfiles) {
				if (user.getIdentityNumber().equals(self.getIdentityNumber()) && (user != self)) {
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Duplicate identity number", user
											.getRealName()
											+ "-"
											+ user.getIdentityNumber()));
					return;
				}
			}
		}
		applicationService.sendNewEmployeeSettingApplication(userProfiles,
				comment, getUploadFileId());
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Application sent"));
		clear();
	}

	public void clear() {
		userProfile = new User();
		comment = null;
		file = null;
		userProfiles = new ArrayList<User>();
	}

	public User getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(User userProfile) {
		this.userProfile = userProfile;
	}

	public List<User> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(List<User> userProfiles) {
		this.userProfiles = userProfiles;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
	public List<Role> getBaseRoleList() {
		return baseRoleList;
	}
	public void setBaseRoleList(List<Role> baseRoleList) {
		this.baseRoleList = baseRoleList;
	}

}
