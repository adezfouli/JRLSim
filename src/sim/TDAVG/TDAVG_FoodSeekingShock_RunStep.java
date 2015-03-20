package sim.TDAVG;

import proc.addiction.TwoConcurrentTask;
import proc.addiction.CocaineSeeking;
import proc.addiction.CocaineSeekingShockTaking;
import proc.addiction.CocaineTaking;
import proc.addiction.FoodSeeking;
import proc.addiction.FoodSeekingShockTaking;
import proc.addiction.FoodTaking;
import proc.addiction.ThreeReinforcersWithShock;
import action.GreedyActionSelection;
import action.SoftMaxActionSelection;
import agent.TD.TDAgent;
import agent.TDAVG.TDAVGAgent;
import env.Environment;
import env.RunStep;

public class TDAVG_FoodSeekingShock_RunStep extends env.RunStep {

	private int seekingShockTrails;

	public TDAVG_FoodSeekingShock_RunStep(int seekingShockTrails) {
		this.seekingShockTrails = seekingShockTrails;
	}

	@Override
	public void run(int time) {
		TDAVGAgent agent = new TDAVGAgent(environment,
				new GreedyActionSelection(0.1), new TDAVG_Constants());
		environment.singleRun(agent, new FoodSeekingShockTaking(agent,
				new TDAVG_RewardVaules(), seekingShockTrails, 1), time);
	}
}
