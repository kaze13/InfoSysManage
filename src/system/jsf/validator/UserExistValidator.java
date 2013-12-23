package system.jsf.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import system.interceptor.TransactionInterceptor;
import system.po.User;
import system.service.UserServiceImpl;

@Named("userExistValidator")
public class UserExistValidator implements Validator {

	@Inject
	private UserServiceImpl userService;

	@Override
	@Interceptors(TransactionInterceptor.class)
	public void validate(FacesContext arg0, UIComponent arg1, Object value)
			throws ValidatorException {
		if (value == null)
			return;
		String user = null;
		if (value instanceof User) {
			user = ((User) value).getName();
		} else {
			user = (String) value;
		}
		User validate = null;
		try {
			validate = userService.get(user);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (validate == null) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Validation fail.",
					"User doesn't exsit.");
			throw new ValidatorException(message);
		}

	}

}
