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

public class Extinction extends Procedure {

	public Extinction(Agent agent, RewardValue rewardValue, int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	@Override
	public boolean isPossible(State currState, PrimitiveAction action) {
		if (currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.pressTaking))
			return true;
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		if (action.getActionType() == ActionType.get(ActionNames.pressTaking))
			newTrial();;
		if (currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.pressTaking)){
			delay = getITI();
			return rewardValue.zero();
		}
		throw new BRLException("Illigal state action");
	}

	@Override
	public Cue[] getCue() {
		if (currentState == State.getState(0))
			return new Cue[]{Cue.get(CueNames.TakingEnabled)};
		throw new BRLException("Illigal state action");
	}

}
