package agent.LATDAVG;

import agent.Action;
import agent.*;
import env.Cue;
import env.Environment;
import action.ActionSelection;
import agent.TDAVG.TDAVGAgent;

public class LATDAVGAgent extends TDAVGAgent {

	private static final long serialVersionUID = -3725417893288613870L;

	public LATDAVGAgent(Environment environment,
			ActionSelection actionSelection,
			AgentInternalConstants parameterValues) {
		super(environment, actionSelection, parameterValues);
	}


	@Override
	public QValue getInitQ() {
		return new LATDAVG_QValue(this, Cue.values().length);
	}
	
	@Override
	public State resolveState(Cue[] cue) {
		return new CompoundState(cue);
	}
	
	@Override
	public double getStateActionValue(State state, Action action) {
		return ((LATDAVG_QValue)getQValue(state, action)).Emu((CompoundState) state);
	}	
	
}
