package proc.dualprocess;

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

public abstract class EpisodicLeverMagazineTask extends Procedure {

	private static final long serialVersionUID = 1352667185328778837L;

	public EpisodicLeverMagazineTask(Agent agent, RewardValue rewardValue, int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
		setITI(300);
        setISI(1);
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
			return new Cue[] { Cue.get(CueNames.LittleFoodObtained) };
		throw new BRLException("Illegal State");
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever)) {
			return true;
		} else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
			return true;
		} else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever))) {
			return true;
		} else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
			return true;
		} else if ((currentState == State.getState(2)
				|| currentState == State.getState(3) || currentState == State
				.getState(4))
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			return true;
		}
		return false;
	}

	public Reward nextState(PrimitiveAction action, Reward reward, Reward zeroReward) {
		delay = getISI();
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever)) {
			currentState = State.getState(1);
			return zeroReward;
		} else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
			currentState = State.getState(2);
			return zeroReward;
		} else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever))) {
			currentState = State.getState(4);
			return zeroReward;
		} else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
			currentState = State.getState(3);
			return reward;
		} else if ((currentState == State.getState(2)
				|| currentState == State.getState(3) || currentState == State
				.getState(4))
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			currentState = State.getState(0);
			newTrial();
			delay = getITI();
			return zeroReward;
		} else {
			throw new BRLException("Illigal state action");
		}
	}
}
