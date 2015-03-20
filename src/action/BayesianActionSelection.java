package action;

import agent.Action;
import proc.BRLException;
import agent.Agent;
import agent.State;
import agent.BRL.BayesianAgent;

import java.util.HashMap;
import java.util.Map;

public class BayesianActionSelection extends ActionSelection {
	private static final long serialVersionUID = 6901589375754400624L;

	public ValueBasedActionSelection actionSelection;
    private int depthOfSearch;

    public BayesianActionSelection(ValueBasedActionSelection actionSelection) {
		this.actionSelection = actionSelection;
        this.depthOfSearch = 3;
	}

    public BayesianActionSelection(ValueBasedActionSelection actionSelection, int depthOfSearch) {
        this.actionSelection = actionSelection;
        this.depthOfSearch = depthOfSearch;
    }

	@Override
	public void setAgent(Agent agent) {
		super.setAgent(agent);
		actionSelection.setAgent(agent);
	}

	@Override
	public Action selectAction(State state) {
		if (!(agent instanceof BayesianAgent))
			throw new BRLException("Agent should be Bayesian Learner");
		BayesianAgent BRL_agent = (BayesianAgent) agent;
		Map<Action, Double> VPI = BRL_agent.calculateValueOfPerfectInformation(state);
		Map<Action, Double> values = new HashMap<Action, Double>();
		double numberOfCalc = 0;
		double delay = 0;
		for (Action action : agent.getActions()) {
			if (agent.getEnvironment().isPossible(action)) {
				double rRavgTau = BRL_agent.costOfGoalBasedDecisionMaking();
				if (VPI.get(action) > rRavgTau) {
					values.put(action ,  BRL_agent
							.treeBasedValueOfStateAction(state, action, this.depthOfSearch));
					numberOfCalc++;
					delay += agent.getCurrentTimeOfValueIteration();
				} else {
					values.put(action, agent.getQValue(state, action)
							.Emu());
					delay += agent.getTimeOfHabitualDecisionMaking();
				}
			}
		}
		Action action = actionSelection.selectAction(values);
		action.setLatency(delay);
		return action;
	}

	@Override
	public Map<Action, Double> getProbabilityOfSelectingAction(State state) {
		return actionSelection.getProbabilityOfSelectingAction(state);
	}
}