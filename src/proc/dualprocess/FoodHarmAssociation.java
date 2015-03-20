package proc.dualprocess;

import reward.Reward;
import reward.RewardValue;
import action.PrimitiveAction;
import agent.Agent;

public class FoodHarmAssociation extends RewardHarmAssociation {

	private static final long serialVersionUID = -6552628276208287443L;

	public FoodHarmAssociation(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		return nextState(action, rewardValue.foodEating(), rewardValue
				.littleFood(), rewardValue.shockWithoutFreeze());
	}
}
