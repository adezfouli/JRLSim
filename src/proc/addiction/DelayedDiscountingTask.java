package proc.addiction;

import action.ActionNames;
import action.ActionType;
import env.CueNames;
import proc.BRLException;
import proc.Procedure;
import reward.Reward;
import reward.RewardValue;
import action.PrimitiveAction;
import agent.Agent;
import env.Cue;
import env.State;

public class DelayedDiscountingTask extends Procedure {

	private static final long serialVersionUID = -8028085047688656963L;
	private double delayBeforeBigReward;

	public DelayedDiscountingTask(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials, double delayBeforeBigReward) {
		super(agent, rewardValue, maxNumberOfTrials);
		this.delayBeforeBigReward = delayBeforeBigReward;
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (currentState == State.getState(0)
				&& (action.getActionType() == ActionType.get(ActionNames.pressLever1) || action
						.getActionType() == ActionType.get(ActionNames.pressLever2)))
			return true;
		else if (currentState == State.getState(1)
				&& action.getActionType() == ActionType.get(ActionNames.terminal))
			return true;
		else if (currentState == State.getState(2)
				&& action.getActionType() == ActionType.get(ActionNames.terminal))
			return true;
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		if (action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
			currentState = State.getState(1);
			delay = delayBeforeBigReward;
			return rewardValue.zero();
		}
		if (action.getActionType() == ActionType.get(ActionNames.pressLever2)) {
			currentState = State.getState(2);
			delay = getISI();
			return rewardValue.zero();
		}
		if (currentState == State.getState(1)
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			currentState = State.getState(0);
			delay = getITI();
			newTrial();
			return rewardValue.getBigReward();
		}

		if (currentState == State.getState(2)
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			currentState = State.getState(0);
			delay = getITI();
			newTrial();
			return rewardValue.getLittleReward();
		} else
			throw new BRLException("Illigal state action");
	}

	@Override
	public Cue[] getCue() {
		if (currentState == State.getState(0))
			return new Cue[] { Cue.get(CueNames.SeveralLeversEnabled) };
		if (currentState == State.getState(1))
			return new Cue[] { Cue.get(CueNames.wait) };
		if (currentState == State.getState(2))
			return new Cue[] { Cue.get(CueNames.littleFoodAvailable)};
		throw new BRLException("illegal state to infer cue");
	}
	
	@Override
	public Cue getAlwaysVisitingCue() {
		return Cue.get(CueNames.SeveralLeversEnabled);
	}
}
