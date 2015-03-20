package proc.addiction;

import action.ActionNames;
import action.ActionType;
import action.PrimitiveAction;
import cern.jet.random.engine.DRand;
import env.CueNames;
import proc.BRLException;
import proc.Procedure;
import cern.jet.random.Exponential;
import env.Cue;
import env.State;
import reward.Reward;
import reward.RewardValue;
import agent.Agent;

public abstract class RandomInterval extends Procedure implements FreeOperant{

	private static final long serialVersionUID = -2856747791875010157L;

	protected double nextBait;
	protected double currentTime;
	protected Exponential generator;
	protected double lambda;

	public RandomInterval(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials, double lambda) {
		super(agent, rewardValue, maxNumberOfTrials);
		generator = new Exponential(lambda, new DRand());
		nextBait = generator.nextDouble();
		this.lambda = lambda;
		setITI(100);
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public Cue[] getCue() {
		if (currentState == State.getState(0))
			return new Cue[] { Cue.get(CueNames.SeekingEnabled) };
		if (currentState == State.getState(1))
			return new Cue[] { Cue.get(CueNames.TakingEnabled) };
		throw new BRLException("Illigal state action");
	}

	public Reward nextState(PrimitiveAction action, Reward reward) {
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressSeeking)) {
			delay = action.getLatency();
			if (currentTime + action.getLatency() > nextBait) {
				nextBait = generator.nextDouble();
				currentState = State.getState(1);
				currentTime = 0;
			} else {
				currentTime += action.getLatency();
			}
			return rewardValue.zero();
		} else if (currentState != State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressTaking)) {
			delay = Math.max(getITI(), action.getLatency());
			currentState = State.getState(0);
			newTrial();
			return reward;
		} else
			throw new BRLException("Illigal state action");
	}

	@Override
	public boolean isPossible(State currentState, PrimitiveAction action) {
		if (currentState == State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressSeeking))
			return true;
		if (currentState != State.getState(0)
				&& action.getActionType() == ActionType.get(ActionNames.pressTaking))
			return true;
		return false;
	}

}