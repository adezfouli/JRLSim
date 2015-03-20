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

public class FoodTaking extends Procedure {


	private static final long serialVersionUID = -5862364693019687214L;

	public FoodTaking(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
		setITI(1);
		setISI(1);
	}

	@Override
	public boolean isPossible(State currState, PrimitiveAction action) {
		if (currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.pressLever1))
			return true;
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		if (action.getActionType() == ActionType.get(ActionNames.pressLever1))
			newTrial();
		if (currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
			delay = getITI();
			return rewardValue.getR1();
		} else
			throw new BRLException("Illigal state action");

	}

	@Override
	public Cue[] getCue() {
		if (currentState == State.getState(0))
			return new Cue[] {Cue.get(CueNames.InitialState)};
		throw new BRLException("Illigal state action");
	}
}
