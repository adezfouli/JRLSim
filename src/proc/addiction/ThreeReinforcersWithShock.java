package proc.addiction;

import action.ActionNames;
import action.ActionType;
import env.CueNames;
import proc.BRLException;
import reward.Reward;
import reward.RewardValue;
import action.PrimitiveAction;
import agent.Agent;
import env.Cue;
import env.State;

public class ThreeReinforcersWithShock extends proc.Procedure {

	private static final long serialVersionUID = 4873218608080167287L;

	public ThreeReinforcersWithShock(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
			return true;
		} else if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever2)) {
			return true;
		} else if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever3)) {
			return true;
		} else if (currentState == State.getState(0 + 10)
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			return true;
		} else if (currentState == State.getState(1 + 10)
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			return true;
		} else if (currentState == State.getState(2 + 10)
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			return true;
		}
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {

		if (currentState == State.getState(0))
			newTrial();
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
			currentState = State.getState(0 + 10);
			delay = getISI();
			return rewardValue.getR1();
		} else if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever2)) {
			currentState = State.getState(1 + 10);
			delay = getISI();
			return rewardValue.getR2();
		} else if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever3)) {
			currentState = State.getState(2 + 10);
			delay = getISI();
			return rewardValue.getR3();
		} else if (currentState == State.getState(0 + 10)
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			currentState = State.getState(0);
			delay = getITI();
			return rewardValue.getR1_Shock();
		} else if (currentState == State.getState(1 + 10)
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			currentState = State.getState(0);
			delay = getITI();
			return rewardValue.getR2_Shock();
		} else if (currentState == State.getState(2 + 10)
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			currentState = State.getState(0);
			delay = getITI();
			return rewardValue.getR3_Shock();
		}
		throw new BRLException("Illegal state action");
	}

	@Override
	public Cue[] getCue() {
		if (currentState == State.getState(0))
			return new Cue[] { Cue.get(CueNames.SeveralLeversEnabled) };
		if (currentState == State.getState(0 + 10))
			return new Cue[] { Cue.get(CueNames.AfterPressL1) };
		if (currentState == State.getState(1 + 10))
			return new Cue[] { Cue.get(CueNames.AfterPressL2) };
		if (currentState == State.getState(2 + 10))
			return new Cue[] { Cue.get(CueNames.AfterPressL3) };
		throw new BRLException("Illegal state action");
	}
	
	@Override
	public Cue getAlwaysVisitingCue() {
		return Cue.get(CueNames.SeveralLeversEnabled);
	}
}
