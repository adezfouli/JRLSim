package proc.addiction;

import reward.Reward;
import reward.RewardValue;
import action.PrimitiveAction;
import agent.Agent;

public class CocaineFixedRatio extends FixedRatio {

	private static final long serialVersionUID = 4154325572535933230L;

	public CocaineFixedRatio(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		return nextState(action, rewardValue.cocaineTaking());
	}
}
