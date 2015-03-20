package proc.addiction;


import action.PrimitiveAction;
import action.ActionNames;
import action.ActionType;
import agent.Agent;
import env.Cue;
import env.CueNames;
import env.State;

import proc.BRLException;
import proc.Procedure;
import reward.Reward;
import reward.RewardValue;

public class BlockingTest extends Procedure {


	private static final long serialVersionUID = 8408279039373000338L;

	public BlockingTest(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever1))
			return true;
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever2))
			return true;
		if (currentState == State.getState(1)
				&& action.getActionType() == ActionType.get(ActionNames.terminal))
			return true;
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		if (currentState == State.getState(0))
			newTrial();
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever1)){
			delay = getISI();
			return rewardValue.zero();
		}
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever2)){
			currentState = State.getState(1);
			delay = getISI();
			return rewardValue.zero();
		}
		if (currentState == State.getState(1)
				&& action.getActionType() == ActionType.get(ActionNames.terminal)){
			currentState = State.getState(0);
			delay = getISI();
			return rewardValue.zero();
		}
		throw new BRLException("Illigal state action");
	}

	@Override
	public Cue[] getCue() {
		if (currentState == State.getState(0))
			return new Cue[]{Cue.get(CueNames.SeveralLeversEnabled)};
		if (currentState == State.getState(1))
			return new Cue[]{Cue.get(CueNames.light)};
		else
			throw new BRLException("Illigal state action");
	}
	
	@Override
	public Cue getAlwaysVisitingCue() {
		return Cue.get(CueNames.SeveralLeversEnabled);
	}
	
	@Override
	public double getISI() {
		return 0.0001;
	}
}
