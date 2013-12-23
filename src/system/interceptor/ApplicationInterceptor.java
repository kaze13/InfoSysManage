package system.interceptor;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import system.po.Application;

public class ApplicationInterceptor {

	@AroundInvoke
	public Object showGlobalMessage(InvocationContext ctx) {

		FacesContext context = FacesContext.getCurrentInstance();
		Object returnVal = null;
		try {
			returnVal = ctx.proceed();
			if (returnVal instanceof Application) {
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Application sent",
						((Application) returnVal).getTitle()));
			} else if (returnVal instanceof Application[]) {
				for (Application n : (Application[]) returnVal) {
					if(n == null)
						continue;
					context.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Application sent", n.getTitle()));
				}
			} else {
				throw new Exception(
						"unknown return value from application sending method");
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"Error in sending application",
							((Application) returnVal).getTitle()));
		}

		return returnVal;
	}
}
