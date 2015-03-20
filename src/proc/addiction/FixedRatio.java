package proc.addiction;

import action.ActionNames;
import action.ActionType;
import action.PrimitiveAction;
import env.CueNames;
import proc.BRLException;
import proc.Procedure;
import reward.Reward;
import reward.RewardValue;
import agent.Agent;
import env.Cue;
import env.State;

public abstract class FixedRatio extends Procedure implements FreeOperant{

	private static final long serialVersionUID = -3956639569733527654L;

	public FixedRatio(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	public Cue[] getCue() {
		if (currentState == State.getState(0))
			return new Cue[] { Cue.get(CueNames.SeekingEnabled )};
		if (currentState == State.getState(1))
			return new Cue[] { Cue.get(CueNames.TakingEnabled) };
		throw new BRLException("Illigal state action");
	}

	public Reward nextState(PrimitiveAction action, Reward reward) {
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressSeeking)) {
			delay = action.getLatency();
			currentState = State.getState(1);
			return rewardValue.zero();
		} else if (currentState != State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressTaking)) {
			delay = getITI();
			currentState = State.getState(0);
			newTrial();
			return reward;
		} else
			throw new BRLException("Illigal state action");
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressSeeking))
			return true;
		if (currentState != State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressTaking))
			return true;
		return false;
	}
}
