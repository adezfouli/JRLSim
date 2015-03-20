package sim.TD;

import proc.addiction.CocaineSeeking;
import proc.addiction.CocaineSeekingShockTaking;
import proc.addiction.CocaineTaking;
import action.ActionSelection;
import action.SoftMaxActionSelection;
import agent.TD.TDAgent;
import env.Environment;
import env.RunStep;

public class CocaineSeeking_RunStep extends RunStep {

	private int takingTrails;
	private int seekingTrails;
	private int seekingShockTrails;
	
	
	public CocaineSeeking_RunStep(int takingTrails, int seekingTrails, int seekingShockTrails) {
		this.takingTrails = takingTrails;
		this.seekingTrails = seekingTrails;
		this.seekingShockTrails = seekingShockTrails;
	}


	@Override
	public void run(int time) {
		TDAgent agent = new TDAgent(environment,
				new SoftMaxActionSelection(0.1), new TD_Constants());
		environment.singleRun(agent, new CocaineTaking(agent,
				new TD_RewardVaules(), takingTrails), time);
		environment.singleRun(agent, new CocaineSeeking(agent,
				new TD_RewardVaules(), seekingTrails, 1), time);
		environment.singleRun(agent, new CocaineSeekingShockTaking(agent,
				new TD_RewardVaules(), seekingShockTrails, 1), time);
	}
}
