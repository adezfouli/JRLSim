package proc.addiction;

import action.PrimitiveAction;
import reward.Reward;
import reward.RewardValue;
import agent.Agent;

public class FoodRandomInterval extends RandomInterval {

	private static final long serialVersionUID = -2627254184208878463L;

	public FoodRandomInterval(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials, double lambda) {
		super(agent, rewardValue, maxNumberOfTrials, lambda);
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		return nextState(action, rewardValue.foodEating());
	}

}
