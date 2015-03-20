package agent.BRL;

import agent.Agent;

public class G_QValue extends NG_QValue {

    private double lr = 0;

    public G_QValue(Agent agent, double mu, double landa, double alpha,
	    double beta) {
	super(agent, mu, landa, alpha, beta);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public double update(double r) {
	double n = 1;
	mu = (1 - lr) * mu + (lr) * r;
	landa = 1;
	alpha += n / 2;
	beta += (r - mu) * (r - mu) / 2;
	return r;
    }
}
