package system.interceptor;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import system.po.Notification;

public class NotificationInterceptor {

	@AroundInvoke
	public Object showGlobalMessage(InvocationContext ctx) {

		FacesContext context = FacesContext.getCurrentInstance();
		Object returnVal = null;
		try {
			returnVal = ctx.proceed();
			if (returnVal instanceof Notification) {
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Notification sent",
						((Notification) returnVal).getTitle()));
			} else if (returnVal instanceof Notification[]) {
				for (Notification n : (Notification[]) returnVal) {
					context.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Notification sent", n.getTitle()));
				}
			} else {
				throw new Exception(
						"unknown return value from notification sending method");
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Error in sending notification",
							((Notification) returnVal).getTitle()));
		}

		return returnVal;
	}
}
