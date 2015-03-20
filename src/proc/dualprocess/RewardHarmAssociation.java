package proc.dualprocess;

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

public abstract class RewardHarmAssociation extends Procedure {

	public RewardHarmAssociation(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	@Override
	public Cue[] getCue() {
		if (currentState == State.getState(0))
			return new Cue[] { Cue.get(CueNames.InitialState) };
		else if (currentState == State.getState(1))
			return new Cue[] { Cue.get(CueNames.FoodDelivered) };
		else if (currentState == State.getState(2))
			return new Cue[] { Cue.get(CueNames.NoReward) };
		else if (currentState == State.getState(3))
			return new Cue[] { Cue.get(CueNames.FoodObtained) };
		else if (currentState == State.getState(4))
			return new Cue[] { Cue.get(CueNames.ShockReceived)};
		throw new BRLException("Illegial State");
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.pressLever)) {
			return true;
		} else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
			return true;
		} else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever))) {
			return true;
		} else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
			return true;
		} else if (currentState == State.getState(3) && action.getActionType() == ActionType.get(ActionNames.other)) {
			return true;
		} else if ((currentState == State.getState(2) || currentState == State
				.getState(4))
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			return true;
		}
		return false;
	}

	public Reward nextState(PrimitiveAction action, Reward reward, Reward zeroReward, Reward harm) {
		delay = getISI();
		if (currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.pressLever)) {
			currentState = State.getState(1);
			return rewardValue.zero();
		} else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
			currentState = State.getState(2);
			return rewardValue.zero();
		} else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever))) {
			currentState = State.getState(2);
			return zeroReward;
		} else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
			currentState = State.getState(3);
			return reward;
		} else if (currentState == State.getState(3) && action.getActionType() == ActionType.get(ActionNames.other)) {
			currentState = State.getState(4);
			return harm;
		} else if ((currentState == State.getState(2) || currentState == State
				.getState(4))
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			currentState = State.getState(0);
			newTrial();
			delay = getITI();
			return rewardValue.zero();
		} else {
			throw new BRLException("Illigal state action");
		}
	}
}
