package proc.addiction;

import action.PrimitiveAction;
import action.ActionNames;
import action.ActionType;
import agent.Agent;
import env.Cue;
import env.State;
import proc.BRLException;
import proc.Procedure;
import reward.Reward;
import reward.RewardValue;

public class CocaineTakingWithStimuli extends Procedure {

	private static final long serialVersionUID = 1473200691771909746L;
	
	private Cue[] stimuli;

	public CocaineTakingWithStimuli(Agent agent, RewardValue rewardValue, int maxNumberOfTrials, Cue[] stimuli) {
		super(agent, rewardValue, maxNumberOfTrials);
		this.stimuli = stimuli;
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
			return rewardValue.cocaineTaking();
		}
		throw new BRLException("Illigal state action");
	}

	@Override
	public Cue[] getCue() {
		if (currentState == State.getState(0))
			return stimuli;
		throw new BRLException("Illigal state action");
	}

}
