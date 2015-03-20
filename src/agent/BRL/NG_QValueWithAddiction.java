package agent.BRL;

import reward.CocaineReward;
import reward.Reward;
import agent.Agent;
import agent.QValue;

public class NG_QValueWithAddiction extends NG_QValue {

    private static final long serialVersionUID = -4707065506177970302L;

    public NG_QValueWithAddiction(Agent agent, double mu, double landa,
	    double alpha, double beta) {

	super(agent, mu, landa, alpha, beta);
    }

    @Override
    public double update(Reward reward, QValue q, double df, double delay) {
	double r = getDrugInducedReward(reward, q, df, delay);
	return super.update(r, q, df, delay);
    }
}
