package action;

import java.io.Serializable;
import java.util.Map;

import agent.Action;
import agent.Agent;
import agent.State;

public abstract class ActionSelection implements Serializable{
	private static final long serialVersionUID = -13737131228090961L;
	
	protected Agent agent;
	
	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public abstract Action selectAction(State state);
	
	public abstract Map<Action, Double> getProbabilityOfSelectingAction(State state);
}
