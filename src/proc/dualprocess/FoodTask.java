package proc.dualprocess;

import reward.Reward;
import reward.RewardValue;
import action.PrimitiveAction;
import agent.Agent;

public class FoodTask extends EpisodicLeverMagazineTask {

	private static final long serialVersionUID = -8779191727418587216L;

	public FoodTask(Agent agent, RewardValue rewardValue, int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		return nextState(action, rewardValue.getR1(), rewardValue.zero());
	}
}
