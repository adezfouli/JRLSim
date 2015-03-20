package sim.TDAVG;

import proc.addiction.CocaineTaking;
import proc.addiction.FoodTaking;
import action.GreedyActionSelection;
import agent.Agent;
import agent.TD.TDAgent;
import agent.TDAVG.TDAVGAgent;
import env.RunStep;

public class TDAVG_FoodTaking_RunStep extends RunStep {

	private int trailNumber;

	public TDAVG_FoodTaking_RunStep(int trailNumber) {
		this.trailNumber = trailNumber;
	}

	@Override
	public void run(int time) {
		Agent agent = new TDAVGAgent(environment, new GreedyActionSelection(1),
				new TDAVG_Constants());
		environment.singleRun(agent, new FoodTaking(agent,
				new TDAVG_RewardVaules(), trailNumber), time);
	}

}
