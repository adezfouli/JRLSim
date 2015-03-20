package proc.addiction;

import java.util.Random;

import action.ActionNames;
import action.ActionType;
import env.CueNames;
import proc.BRLException;
import proc.Procedure;

import reward.Reward;
import reward.RewardValue;
import env.Cue;
import env.State;
import action.PrimitiveAction;
import agent.Agent;

public class ProbSeekingPunishment extends Procedure {
	private int numberOfSeeking;

	protected ProbSeekingPunishment(Agent agent, RewardValue rewardValue,
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
				&& action.getActionType() == ActionType.get(ActionNames.other))
			return true;
		if (currentState.stateNumber == numberOfSeeking
				&& action.getActionType() == ActionType.get(ActionNames.pressTaking))
			return true;
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		if (currentState == State.getState(0))
			newTrial();;
		if (currentState.stateNumber < numberOfSeeking
				&& action.getActionType() == ActionType.get(ActionNames.pressSeeking)) {
			if (new Random().nextBoolean()) {
				currentState = State.getState(currentState.stateNumber + 1);
				return rewardValue.zero();
			} else {
				return rewardValue.shockWithoutFreeze();
			}
		}

		if (currentState.stateNumber < numberOfSeeking
				&& action.getActionType() == ActionType.get(ActionNames.other)) {
			return rewardValue.zero();
		}

		else if (currentState.stateNumber == numberOfSeeking
				&& action.getActionType() == ActionType.get(ActionNames.pressTaking)) {
			currentState = State.getState(0);
			return rewardValue.cocaineTaking();

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

	@Override
	public double getDelay() {
		if (currentState == State.getState(0))
			return getITI();
		return getISI();
	}

}
