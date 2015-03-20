package action;

import
        agent.Action;
import agent.State;
import agent.QValue;

import java.util.Map;

public abstract class ValueBasedActionSelection extends ActionSelection {

	protected abstract Action selectAction(Map<Action, Double> values);

	public abstract Map<Action, Double> getProbabilityOfSelectingAction(Map<Action, QValue> values);
	
	private Double[] QValueToValue(QValue[] q) {
		Double[] values = new Double[q.length];
		for (int i = 0; i < q.length; i++) {
			if (q[i] == null) {
				values[i] = null;
			} else{
				values[i] = agent.getStateActionValue(agent.getCurrentState(), agent.getAction(i));
			}
		}
		return values;
	}

	public Action selectAction(State currentState) {
		Map<Action, Double> q = agent.actionValues(currentState);
		return selectAction(q);
	}
	
	@Override
	public Map<Action, Double> getProbabilityOfSelectingAction(State state) {
		Map<Action, QValue> q = agent.actionQValues(state);
		return getProbabilityOfSelectingAction(q);
	}
}
