package sim.TDAVG;

import proc.addiction.CocaineSeeking;
import proc.addiction.CocaineSeekingShockTaking;
import proc.addiction.CocaineTaking;
import proc.addiction.FoodSeeking;
import proc.addiction.FoodSeekingShockTaking;
import proc.addiction.FoodTaking;
import action.GreedyActionSelection;
import agent.TDAVG.TDAVGAgent;
import env.RunStep;

public class TDAVG_CocaineSeekingShock_RunStep extends RunStep {

	private int seekingShockTrails;

	public TDAVG_CocaineSeekingShock_RunStep(int seekingShockTrails) {
		this.seekingShockTrails = seekingShockTrails;
	}

	@Override
	public void run(int time) {
		TDAVGAgent agent = new TDAVGAgent(environment,
				new GreedyActionSelection(0.2), new TDAVG_Constants());
		environment.singleRun(agent, new CocaineSeekingShockTaking(agent,
				new TDAVG_RewardVaules(), seekingShockTrails, 1), time);
	}
}
