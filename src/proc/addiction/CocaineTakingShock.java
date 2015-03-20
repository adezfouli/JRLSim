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

public class CocaineTakingShock extends Procedure {

	private static final long serialVersionUID = -8006780440330440763L;

	public CocaineTakingShock(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (currentState == State.getState(0)){
			if (action.getActionType() == ActionType.get(ActionNames.other) || action.getActionType() == ActionType.get(ActionNames.pressTaking))
				return true;
		}
		if (currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.other))
			return true;
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		if (currentState == State.getState(0))
			newTrial();;
		if (currentState == State.getState(0) 
				&& action.getActionType() == ActionType.get(ActionNames.pressTaking)) {
			currentState = State.getState(1);
			return rewardValue.cocaineTaking();
		} else if (currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.other)){
			currentState = State.getState(0);
			return rewardValue.shockWithoutFreeze();
		}else if (currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.other)){
			currentState = State.getState(0);
			return rewardValue.zero();
		}
		else
			throw new BRLException("Illigal state action");
	}

	@Override
	public Cue[] getCue() {
		if (currentState == State.getState(0)){
			return new Cue[]{Cue.get(CueNames.TakingEnabled)};
		}else if (currentState == State.getState(1)){
			return new Cue[]{Cue.get(CueNames.AfterTaking)};
		}
		else
			throw new BRLException("Illigal state action");
	}

	public double getDelay() {
		if (currentState == State.getState(0))
			return getITI();
		return getISI();
	}
}
