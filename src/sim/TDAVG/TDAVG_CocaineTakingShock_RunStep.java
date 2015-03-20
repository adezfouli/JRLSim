package sim.TDAVG;

import proc.addiction.CocaineSeekingShockTaking;
import proc.addiction.CocaineTakingShock;
import action.GreedyActionSelection;
import agent.TDAVG.TDAVGAgent;
import env.RunStep;

public class TDAVG_CocaineTakingShock_RunStep extends RunStep {

	private int seekingShockTrails;

	public TDAVG_CocaineTakingShock_RunStep(int seekingShockTrails) {
		this.seekingShockTrails = seekingShockTrails;
	}

	@Override
	public void run(int time) {
		TDAVGAgent agent = new TDAVGAgent(environment,
				new GreedyActionSelection(0.2), new TDAVG_Constants());
		environment.singleRun(agent, new CocaineTakingShock(agent,
				new TDAVG_RewardVaules(), seekingShockTrails), time);
	}
}
