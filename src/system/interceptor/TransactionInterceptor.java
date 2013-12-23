package system.interceptor;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import system.dao.spec.Transaction;

public class TransactionInterceptor implements Serializable {

	@Inject
	Transaction transaction;

	/**
	 *
	 */
	private static final long serialVersionUID = -8188637607823822567L;

	@AroundInvoke
	public Object keepTransaction(InvocationContext ctx) throws Exception {

		try {
			Logger logger = Logger.getLogger("mytimer");

			long step1 = System.currentTimeMillis();

			//transaction.begin();
			long step2 = System.currentTimeMillis();
			Object returnVal = ctx.proceed();
			long step3 = System.currentTimeMillis();
			;
			transaction.commit();
			long step4 = System.currentTimeMillis();
			;

			String info = "TRANSACTION BEGIN TIME: "
					+ String.valueOf(step2 - step1)
					+ "\nMETHOD EXECUTION TIME: "
					+ String.valueOf(step3 - step2)
					+ "\nTRANSACTION COMMIT TIME: "
					+ String.valueOf(step4 - step3);
		//	logger.log(Level.INFO, info);
			return returnVal;

		} catch (ValidatorException e) {
			throw e;
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"EXCEPTION HAPPEN!!", e.toString()));
			transaction.rollback();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"EXCEPTION HAPPEN!!", e.toString()));
			return null;
		}
	}
}
