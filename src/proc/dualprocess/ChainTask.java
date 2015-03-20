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

public class ChainTask extends Procedure {

	private static final long serialVersionUID = 4548585919539775884L;
	private int numberOfSteps;

	public ChainTask(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials, int numberOfSteps) {
		super(agent, rewardValue, maxNumberOfTrials);
		setITI(1);
		this.numberOfSteps = numberOfSteps;
	}

	@Override
	public Cue[] getCue() {
		if (currentState == State.getState(0))
			return new Cue[] { Cue.get(CueNames.InitialState) };
		else if (currentState == State.getState(1))
			return new Cue[] { Cue.get(CueNames.step0) };
		else if (currentState == State.getState(2))
			return new Cue[] { Cue.get(CueNames.step1) };
		else if (currentState == State.getState(3))
			return new Cue[] { Cue.get(CueNames.step2) };
		else if (currentState == State.getState(4))
			return new Cue[] { Cue.get(CueNames.step3) };
		else if (currentState == State.getState(5))
			return new Cue[] { Cue.get(CueNames.step4) };
		throw new BRLException("Illegal State");
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (action.getActionType() == ActionType.get(ActionNames.pressLever))
			return true;
		return false;
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		newTrial();
		delay = getITI();
		if (currentState.stateNumber < numberOfSteps) {
			currentState = State.getState(currentState.stateNumber + 1);
			return rewardValue.zero();
		} else if (currentState.stateNumber == numberOfSteps) {
			currentState = State.getState(0);
			return rewardValue.zero();
		} else {
			throw new BRLException("Illigal state action");
		}
	}
}
