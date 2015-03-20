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

public class FoodSeeking extends Procedure{
	
	private static final long serialVersionUID = 7608975536503024957L;
	private int numberOfSeeking;
	
	public FoodSeeking(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials, int numberOfSeeking) {
		super(agent, rewardValue, maxNumberOfTrials);
		this.numberOfSeeking = numberOfSeeking;
		setITI(300);
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (currentState.stateNumber < numberOfSeeking && action.getActionType() == ActionType.get(ActionNames.pressSeeking))
			return true;
		if (currentState.stateNumber == numberOfSeeking && action.getActionType() == ActionType.get(ActionNames.pressTaking))
			return true;
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		if (action.getActionType() == ActionType.get(ActionNames.pressTaking))
			newTrial();;
		if (currentState.stateNumber < numberOfSeeking && action.getActionType() == ActionType.get(ActionNames.pressSeeking))
		{
			delay = getISI();
			currentState =  State.getState(currentState.stateNumber+1);
			return rewardValue.zero();
		}
		else if (currentState.stateNumber == numberOfSeeking && action.getActionType() == ActionType.get(ActionNames.pressTaking)){
			delay = getITI();
			currentState =  State.getState(0);
			return rewardValue.foodEating();
		}
		else
			throw new BRLException("Illigal state action");	
	}
	
	@Override
	public Cue[] getCue() {
		if (currentState.stateNumber < numberOfSeeking)
			return new Cue[] {Cue.get(CueNames.SeekingEnabled)};
		if (currentState.stateNumber == numberOfSeeking)
			return new Cue[] {Cue.get(CueNames.FoodAvailable)};
		throw new BRLException("Illigal state action");	
	}
}
