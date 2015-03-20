package agent;

import reward.Reward;

public abstract class ProbQValue extends QValue {

	private static final long serialVersionUID = 3990694791888005129L;
	
	public ProbQValue(Agent agent) {
		super(agent);
	}

	public abstract double VarMu();
}
