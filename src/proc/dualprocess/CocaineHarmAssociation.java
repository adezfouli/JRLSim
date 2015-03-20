package proc.dualprocess;

import action.PrimitiveAction;
import reward.Reward;
import reward.RewardValue;
import agent.Agent;

public class CocaineHarmAssociation extends RewardHarmAssociation {

	private static final long serialVersionUID = 662897649458704482L;

	public CocaineHarmAssociation(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		return nextState(action, rewardValue.cocaineTaking(), rewardValue
				.littleFood(), rewardValue.shockWithoutFreeze());
	}
}
