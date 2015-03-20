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

public class TwoConcurrentTask extends Procedure {

	private static final long serialVersionUID = 5307983432454335327L;

	public TwoConcurrentTask(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
		setITI(1);
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
            if (action.getActionType() == ActionType.get(ActionNames.pressLever1)
				|| action.getActionType() == ActionType.get(ActionNames.pressLever2))
			return true;
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		newTrial();
		delay = getITI();
		if (action.getActionType() == ActionType.get(ActionNames.pressLever1))
			return rewardValue.getR1();
		if (action.getActionType() == ActionType.get(ActionNames.pressLever2))
			return rewardValue.getR2();
//		if (action.getActionType() == ActionType.get(ActionNames.pressLever3)
//			return rewardValue.getR2();
		else
			throw new BRLException("Illigal state action");
	}

	@Override
	public Cue[] getCue() {
		return new Cue[] { Cue.get(CueNames.InitialState) };
	}

	@Override
	public Cue getAlwaysVisitingCue() {
		return Cue.get(CueNames.InitialState);
	}
}
