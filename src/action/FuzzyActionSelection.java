package action;

import agent.Action;
import agent.fuzzy.FuzzyKalmanAgent;

import java.util.Map;

public class FuzzyActionSelection extends GreedyActionSelection{

	private static final long serialVersionUID = 3497413421757667508L;

	public FuzzyActionSelection(double epsilon) {
		super(epsilon);
	}

	@Override
	public Action selectAction(Map<Action, Double> values) {
		return super.selectAction(((FuzzyKalmanAgent)getAgent()).getActionValues());
	}

}
