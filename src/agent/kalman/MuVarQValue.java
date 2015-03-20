package agent.kalman;

import proc.BRLException;
import reward.Reward;
import agent.Agent;
import agent.ProbQValue;
import agent.QValue;

public class MuVarQValue extends ProbQValue {
	
	private static final long serialVersionUID = 6355373767277491111L;
	
	private double mu;
	
	private double var;
	
	public MuVarQValue(Agent agent, double mu, double var) {
		super(agent);
		this.mu = mu;
		this.var = var;
	}


	public void setEmu(double Emu){
		this.mu = Emu;
	}
	
	public void SetVarMu(double varMu){
		this.var = varMu;
	}
	
	@Override
	public double VarMu() {
		return var;
	}

	@Override
	public double Emu() {
		return mu;
	}

	@Override
	public QValue copy() {
		throw new BRLException("copy is not supported");
	}

	@Override
	public double update(Reward r, QValue q, double df, double delay) {
		throw new BRLException("This Q value is not updatable");
	}

}
 