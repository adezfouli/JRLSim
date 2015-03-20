package proc.dualprocess;

import action.PrimitiveAction;
import agent.Agent;
import reward.Reward;
import reward.RewardValue;


public class RewardDevalutationTask extends EpisodicLeverMagazineTask {

	private static final long serialVersionUID = -5271491899421992012L;

	public RewardDevalutationTask(Agent agent, RewardValue rewardValue, int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		return nextState(action, rewardValue.devaluedFood(), rewardValue.littleFood());
	}
}
