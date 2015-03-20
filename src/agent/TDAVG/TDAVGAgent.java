package agent.TDAVG;

import env.Environment;
import action.ActionSelection;
import agent.AgentInternalConstants;
import agent.QValue;
import agent.TD.TDAgent;

public class TDAVGAgent extends TDAgent {

	private static final long serialVersionUID = 1992062118448677889L;

	public TDAVGAgent(Environment environment, ActionSelection actionSelection,
			AgentInternalConstants constants) {
		super(environment, actionSelection, constants);
	}

	@Override
	public QValue getInitQ() {
		return new TDAVG_QValue(this, 0);
	}
}
