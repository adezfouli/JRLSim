package agent.LATDAVG;

import agent.CompoundState;
import reward.CocaineReward;
import reward.Reward;
import agent.Agent;
import agent.QValue;
import agent.TDAVG.TDAVGAgent;
import agent.TDAVG.TDAVG_QValue;

public class LATDAVG_QValue extends TDAVG_QValue {

	private static final long serialVersionUID = -4651690044106511263L;

	private double[] weight;

	private boolean[] meet;

	public LATDAVG_QValue(Agent agent, int size) {
		super(agent, 0);
		weight = new double[size];
		meet = new boolean[size];
		CompoundState agentState = (CompoundState) agent
				.getCurrentState();
		double[] s = agentState.getStateVector();
		for (int i = 0; i < s.length; i++) {
			if (s[i] == 1)
				meet[i] = true;
		}
	}

	@Override
	public double Emu() {
		double mu = 0;

		for (int i = 0; i < weight.length; i++) {
				mu += weight[i];
		}
		return mu;
	}

	public double Emu(CompoundState agentState) {
		double mu = 0;
		double[] stateVector = agentState.getStateVector();
		for (int i = 0; i < stateVector.length; i++) {
				mu += weight[i] * stateVector[i];
		}
		return mu;
	}

	@Override
	public QValue copy() {
		return this;
	}

	@Override
	public double update(Reward r, QValue q, double df, double delay) {
		double Vnext = agent.getQValue(agent.getCurrentState(), agent.getMaxValuedAction(agent.getCurrentState())).Emu();
		double rewardRate = agent.getExpDecreasingAverageReward()
				+ agent.getBasalLevelDeviation();
		double delta;
		CompoundState prevState = (CompoundState) agent
				.getPrevState();
		mu = Emu(prevState);
		if (r instanceof CocaineReward) {
            double Ds = ((CocaineReward)r).getDS();

			delta = Math.max(r.getMagnitude() + Vnext - mu + Ds, Ds) - delay
					* rewardRate;
		} else {
			delta = r.getMagnitude() + Vnext - rewardRate * delay - mu;
		}
		((TDAVGAgent) agent).setDelta(delta);
		double learningRate = agent.getLr();
		double[] stateVector = prevState.getStateVector();
		for (int i = 0; i < stateVector.length; i++) {
			weight[i] += learningRate * delta * stateVector[i];
			if (stateVector[i] != 0) {
				meet[i] = true;
			}
		}
		double sensedValue = delta - (Vnext - rewardRate * delay - mu);
		return sensedValue;
	}

	public boolean isMeet(CompoundState agentState) {
		double[] stateVector = agentState.getStateVector();
		for (int i = 0; i < stateVector.length; i++) {
			if (stateVector[i] == 1 && meet[i] == true)
				return true;
		}
		return false;
	}

	public void meet(CompoundState agentState) {
		double[] s = agentState.getStateVector();
		for (int i = 0; i < s.length; i++) {
			if (s[i] == 1)
				meet[i] = true;
		}
	}
}