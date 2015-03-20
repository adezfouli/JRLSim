package sim.fuzzy;

import proc.grid.GridWorld;
import reward.RewardValue;
import action.FuzzyActionSelection;
import agent.Agent;
import agent.BRL.BRL_Constants;
import agent.fuzzy.FuzzyKalmanAgent;
import env.RunStep;

public class Fuzzy_RunStep extends RunStep {

	private int xdim;

	private boolean simple;
	
	public Fuzzy_RunStep(int xdim, boolean simple) {
		this.xdim = xdim;
		this.simple = simple;
	}



	@Override
	public void run(int time) {
		RewardValue rewardValue = new Fuzzy_RewardValue();

		Agent agent = new FuzzyKalmanAgent(environment, new FuzzyActionSelection(0.1), new BRL_Constants(), xdim, simple);
		environment.singleRun(agent, new GridWorld(agent, rewardValue, 100, xdim, xdim), time);
		((FuzzyKalmanAgent)agent).logQTable();
	}

}
