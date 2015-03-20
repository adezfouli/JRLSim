package proc.addiction;

import reward.Reward;
import reward.RewardValue;
import action.PrimitiveAction;
import agent.Agent;

public class CocaineRandomInterval extends RandomInterval {

    private static final long serialVersionUID = -2627254184208878463L;

    public CocaineRandomInterval(Agent agent, RewardValue rewardValue,
	    int maxNumberOfTrials, double lambda) {
	super(agent, rewardValue, maxNumberOfTrials, lambda);
    }

    @Override
    public Reward nextState(PrimitiveAction action) {
	return nextState(action, rewardValue.cocaineTaking());
    }
}
