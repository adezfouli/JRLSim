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

public class EpisodicLeverMagazineChainTask extends Procedure {

	private static final long serialVersionUID = -8312186790412305801L;

	public EpisodicLeverMagazineChainTask(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
		setITI(300);
	}

	@Override
	public Cue[] getCue() {
		if (currentState == State.getState(0))
			return new Cue[] { Cue.get(CueNames.InitialState) };
		else if (currentState == State.getState(1))
			return new Cue[] { Cue.get(CueNames.FoodADelivered) };
		else if (currentState == State.getState(2))
			return new Cue[] { Cue.get(CueNames.FoodBDelivered) };
		else if (currentState == State.getState(3))
			return new Cue[] { Cue.get(CueNames.NoReward) };
		else if (currentState == State.getState(4))
			return new Cue[] { Cue.get(CueNames.FoodAObtained) };
		else if (currentState == State.getState(5))
			return new Cue[] { Cue.get(CueNames.FoodBObtained) };
		throw new BRLException("Illegal State");
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (currentState == State.getState(0)
				&& (action.getActionType() == ActionType.get(ActionNames.pressLever)
						|| action.getActionType() == ActionType.get(ActionNames.enterMagazine) || action
						.getActionType() == ActionType.get(ActionNames.pullChain))) {
			return true;
		} else if (currentState == State.getState(1)
				&& (action.getActionType() == ActionType.get(ActionNames.pressLever)
						|| action.getActionType() == ActionType.get(ActionNames.enterMagazine) || action
						.getActionType() == ActionType.get(ActionNames.pullChain))) {
			return true;
		} else if (currentState == State.getState(2)
				&& (action.getActionType() == ActionType.get(ActionNames.pressLever)
						|| action.getActionType() == ActionType.get(ActionNames.enterMagazine) || action
						.getActionType() == ActionType.get(ActionNames.pullChain))) {
			return true;
		} else if (currentState == State.getState(3)
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			return true;
		}
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		delay = getISI();
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressLever)) {
			currentState = State.getState(1);
			return rewardValue.zero();
		} else if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pullChain)) {
			currentState = State.getState(2);
			return rewardValue.zero();
		} else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
			currentState = State.getState(3);
			return rewardValue.zero();
		} else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever))) {
			currentState = State.getState(3);
			return rewardValue.zero();
		} else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pullChain))) {
			currentState = State.getState(3);
			return rewardValue.zero();
		} else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
			currentState = State.getState(3);
			return rewardValue.getR1();
		} else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.pressLever))) {
			currentState = State.getState(3);
			return rewardValue.zero();
		} else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.pullChain))) {
			currentState = State.getState(3);
			return rewardValue.zero();
		} else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
			currentState = State.getState(3);
			return rewardValue.getR2();
		} else if (currentState == State.getState(3)
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
