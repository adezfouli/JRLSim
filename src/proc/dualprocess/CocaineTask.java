package proc.dualprocess;

import reward.Reward;
import reward.RewardValue;
import action.PrimitiveAction;
import agent.Agent;

public class CocaineTask extends EpisodicLeverMagazineTask {

	private static final long serialVersionUID = -4449538640722682376L;

	public CocaineTask(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		super(agent, rewardValue, maxNumberOfTrials);
	}

	@Override
	public Reward nextState(PrimitiveAction action) {
		return nextState(action, rewardValue.cocaineTaking(), rewardValue
				.littleFood());
	}

}
