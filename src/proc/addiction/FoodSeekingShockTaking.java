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

public class FoodSeekingShockTaking extends Procedure {
	private static final long serialVersionUID = 792665396461829985L;
	private int numberOfSeeking;

	public FoodSeekingShockTaking(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials, int numberOfSeeking) {
		super(agent, rewardValue, maxNumberOfTrials);
		this.numberOfSeeking = numberOfSeeking;
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (currentState.stateNumber < numberOfSeeking
				&& action.getActionType() == ActionType.get(ActionNames.pressSeeking))
			return true;
		if (currentState.stateNumber < numberOfSeeking
				&& action.getActionType() == ActionType.get(ActionNames.freeze))
			return true;
		if (currentState.stateNumber == numberOfSeeking
				&& action.getActionType() == ActionType.get(ActionNames.pressTaking))
			return true;
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		if (currentState == State.getState(0))
			newTrial();
		if (currentState.stateNumber < numberOfSeeking
				&& action.getActionType() == ActionType.get(ActionNames.pressSeeking)) {
			currentState = State.getState(currentState.stateNumber + 1);
			return rewardValue.shockWithoutFreeze();
		} else if (currentState.stateNumber < numberOfSeeking
				&& action.getActionType() == ActionType.get(ActionNames.freeze)) {
			return rewardValue.shockWithFreeze();

		} else if (currentState.stateNumber == numberOfSeeking
				&& action.getActionType() == ActionType.get(ActionNames.pressTaking)) {
			currentState = State.getState(0);
			return rewardValue.foodEating();
		} else
			throw new BRLException("Illigal state action");

	}

	@Override
	public Cue[] getCue() {
		if (currentState.stateNumber < numberOfSeeking)
			return new Cue[] { Cue.get(CueNames.SeekingEnabled) };
		if (currentState.stateNumber == numberOfSeeking)
			return new Cue[] { Cue.get(CueNames.TakingEnabled) };
		else
			throw new BRLException("Illigal state action");
	}

	public double getDelay() {
		if (currentState == State.getState(0))
			return getITI();
		return getISI();
	}
	
	@Override
	public Cue getAlwaysVisitingCue() {
		return Cue.get(CueNames.SeekingEnabled);
	}
}
