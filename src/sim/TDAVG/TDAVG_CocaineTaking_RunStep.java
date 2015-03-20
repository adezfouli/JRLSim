package sim.TDAVG;

import proc.addiction.CocaineTaking;
import proc.addiction.FoodTaking;
import action.GreedyActionSelection;
import agent.Agent;
import agent.TDAVG.TDAVGAgent;
import env.RunStep;

public class TDAVG_CocaineTaking_RunStep extends RunStep {

	private int trailNumber;

	public TDAVG_CocaineTaking_RunStep(int trailNumber) {
		this.trailNumber = trailNumber;
	}

	@Override
	public void run(int time) {
		Agent agent = new TDAVGAgent(environment, new GreedyActionSelection(0),
				new TDAVG_Constants());
		environment.singleRun(agent, new CocaineTaking(agent,
				new TDAVG_RewardVaules(), trailNumber), time);
//		environment.singleRun(agent, new FoodTaking(agent,
//				new TDAVG_RewardVaules(), 6000), time);
	}
}
