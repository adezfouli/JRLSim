package agent.TD;

import agent.Agent;
import agent.QValue;
import reward.CocaineReward;
import reward.Reward;

public class TD_QValue extends QValue {

	private static final long serialVersionUID = 6517914525555771144L;

	protected double mu;

	public TD_QValue(QValue value) {
		super(value.getAgent());
		mu = value.Emu();

	}

	public TD_QValue(Agent agent, double mu2) {
		super(agent);
		this.mu = mu2;
	}

	@Override
	public double Emu() {
		return mu;
	}

    public void setEmu(double mu){
        this.mu = mu;
    }

	@Override
	public QValue copy() {
		return new TD_QValue(this);
	}

	@Override
	public double update(Reward r, QValue q, double df, double delay) {
		q = agent.getQValue(agent.getCurrentState(), agent.getMaxValuedAction(agent.getCurrentState()));
		Agent agent = this.agent;
		double delta = 0;
		if (r instanceof CocaineReward) {
            double Ds = ((CocaineReward)r).getDS();
			delta = Math.max((r.getMagnitude()+ df
					* q.Emu())
					- mu + Ds, Ds);
		} else {
			delta = r.getMagnitude() + Math.pow(df,delay) * q.Emu() - mu;
		}
		double learningRate = agent.getLr();
		double sensedValue = delta - df * q.Emu() + mu;
		mu += learningRate * delta;
		return sensedValue;
	}
}
