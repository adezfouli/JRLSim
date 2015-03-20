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

public class SeekingShock extends Procedure {


	private static final long serialVersionUID = 7978094811559396397L;

	protected SeekingShock(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (action.getActionType() == ActionType.get(ActionNames.freeze))
			return true;
		if (action.getActionType() == ActionType.get(ActionNames.pressSeeking))
			return true;
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		newTrial();;
		delay = getITI();
		if (action.getActionType() == ActionType.get(ActionNames.freeze))
			return rewardValue.shockWithFreeze();
		if (action.getActionType() == ActionType.get(ActionNames.pressSeeking))
			return rewardValue.shockWithoutFreeze();

		else
			throw new BRLException("Illigal state action");
	}

	@Override
	public Cue[] getCue() {
		return new Cue[] { Cue.get(CueNames.SeekingEnabled) };
	}
}
