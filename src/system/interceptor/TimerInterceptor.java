package system.interceptor;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class TimerInterceptor implements Serializable{


	@AroundInvoke
	public Object timeTracking(InvocationContext ctx) {
		try {
			Logger logger = Logger.getLogger("mytimer");

			long start = System.currentTimeMillis();
			Object returnVal = ctx.proceed();
			long end = System.currentTimeMillis();
			String info = ctx.getTarget() + " " + ctx.getMethod().getName() + " time:" + String.valueOf(end - start);
			if(end - start > 10)
				logger.log(Level.INFO,info);

			return returnVal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
