package system.interceptor;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;


public class MyPhaseListener implements PhaseListener{

	private Logger logger = Logger.getLogger("mytimer");;
	private long start;
	private long end;


	@Override
	public void afterPhase(PhaseEvent arg0) {
		end = System.currentTimeMillis();
		String info = arg0.getPhaseId().toString() + " time:" + String.valueOf(end - start);
		logger.log(Level.INFO,info);
	}

	@Override
	public void beforePhase(PhaseEvent arg0) {
		Logger logger = Logger.getLogger("mytimer");

		start = System.currentTimeMillis();
	}

	@Override
	public PhaseId getPhaseId() {
		// TODO Auto-generated method stub
		return PhaseId.ANY_PHASE;
	}


}
