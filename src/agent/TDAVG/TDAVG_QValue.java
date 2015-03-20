package agent.TDAVG;

import reward.CocaineReward;
import reward.Reward;
import agent.Agent;
import agent.QValue;
import agent.TD.TD_QValue;

public class TDAVG_QValue extends TD_QValue {

	private static final long serialVersionUID = 266012890903551433L;

	public TDAVG_QValue(Agent agent, double mu) {
		super(agent, mu);
	}

	@Override
	public double update(Reward r, QValue q, double df, double delay) {
		Agent agent = this.agent;
		return simpleAVG(r, q, delay, agent);
	}

	private double simpleAVG(Reward r, QValue q, double delay, Agent agent) {

		double VNext = agent.getQValue(agent.getCurrentState(), agent.getMaxValuedAction(agent.getCurrentState())).Emu();
		double rewardRate = agent.getNonExploratoryAverageReward()+ agent.getBasalLevelDeviation();

		double delta;
		if (r instanceof CocaineReward) {
            double Ds = ((CocaineReward)r).getDS();
			delta = Math.max(r.getMagnitude() + VNext - mu + Ds, Ds) - delay
					* rewardRate;
		} else {
			delta = r.getMagnitude() + VNext - rewardRate * delay - mu;
		}

		double learningRate = agent.getLr();
		double sensedValue = delta - (VNext - rewardRate * delay - mu);

		mu += learningRate * delta;
		return sensedValue;
	}
}
