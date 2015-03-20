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

public class TwoLevelTask extends Procedure {
	
	private static final long serialVersionUID = -2132004027546305639L;

	public TwoLevelTask(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
		setITI(1);
	}

	@Override
	public Cue[] getCue() {
		if (currentState == State.getState(0))
			return new Cue[] { Cue.get(CueNames.InitialState) };
		else if (currentState == State.getState(1))
			return new Cue[] { Cue.get(CueNames.rightOne) };
		else if (currentState == State.getState(2))
			return new Cue[] { Cue.get(CueNames.rightTwo) };
		else if (currentState == State.getState(3))
			return new Cue[] { Cue.get(CueNames.leftOne) };
		else if (currentState == State.getState(4))
			return new Cue[] { Cue.get(CueNames.leftTwo) };
		throw new BRLException("Illegal State");
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {

		if (currentState == State.getState(0)
				&& (action.getActionType() == ActionType.get(ActionNames.right) || action
						.getActionType() == ActionType.get(ActionNames.left))) {
			return true;
		} else if (currentState == State.getState(1)
				&& action.getActionType() == ActionType.get(ActionNames.right)) {
			return true;
		} else if (currentState == State.getState(3)
				&& action.getActionType() == ActionType.get(ActionNames.left)) {
			return true;
		} else if ((currentState == State.getState(2) || currentState == State
				.getState(4))
				&& action.getActionType() == ActionType.get(ActionNames.terminal)) {
			return true;
		}
		return false;
	}

	public Reward nextState(PrimitiveAction action) {
		
		delay = action.getLatency();
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.right)) {
			currentState = State.getState(1);
			return rewardValue.zero();
		} else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.left))) {
			currentState = State.getState(3);
			return rewardValue.zero();
		} else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.right))) {
			currentState = State.getState(2);
			return rewardValue.getR1();
		} else if ((currentState == State.getState(3) && action.getActionType() == ActionType.get(ActionNames.left))) {
			currentState = State.getState(4);
			return rewardValue.getR2();
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
