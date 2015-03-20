package sim.TD;

import proc.addiction.CocaineSeeking;
import proc.addiction.CocaineSeekingShockTaking;
import proc.addiction.CocaineTaking;
import proc.addiction.FoodSeeking;
import proc.addiction.FoodSeekingShockTaking;
import proc.addiction.FoodTaking;
import action.SoftMaxActionSelection;
import agent.TD.TDAgent;
import env.Environment;
import env.RunStep;



public class FoodSeeking_RunStep extends env.RunStep {

	private int takingTrails;
	private int seekingTrails;
	private int seekingShockTrails;

	public FoodSeeking_RunStep(int takingTrails,
			int seekingTrails, int seekingShockTrails) {
		this.takingTrails = takingTrails;
		this.seekingTrails = seekingTrails;
		this.seekingShockTrails = seekingShockTrails;
	}

	@Override
	public void run(int time) {
		TDAgent agent = new TDAgent(environment,
				new SoftMaxActionSelection(0.1), new TD_Constants());
		environment.singleRun(agent, new FoodTaking(agent,
				new TD_RewardVaules(), takingTrails), time);
		environment.singleRun(agent, new FoodSeeking(agent,
				new TD_RewardVaules(), seekingTrails, 1), time);
		environment.singleRun(agent, new FoodSeekingShockTaking(agent,
				new TD_RewardVaules(), seekingShockTrails, 1), time);
	}

}
