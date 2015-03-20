package sim.TDAVG;

import
        env.RunStep;
import proc.addiction.CocaineTaking;
import proc.addiction.DelayedDiscountingTask;
import proc.addiction.FoodTaking;
import sim.TD.TD_Constants;
import sim.TD.TD_RewardVaules;
import action.GreedyActionSelection;
import action.SoftMaxActionSelection;
import agent.Agent;
import agent.TD.TDAgent;
import agent.TDAVG.TDAVGAgent;

public class TDAVG_DDT_RunStep extends RunStep {
	private int DDTtrailNumber;
	private int takingTrial;

	public TDAVG_DDT_RunStep(int takingTrial, int DDTtrailNumber) {
		this.DDTtrailNumber = DDTtrailNumber;
		this.takingTrial = takingTrial;
	}

	@Override
	public void run(int time) {
		Agent agent = new TDAVGAgent(environment, new GreedyActionSelection(
				0.2), new TDAVG_Constants());
		environment.singleRun(agent, new CocaineTaking(agent,
				new TDAVG_RewardVaules(), takingTrial), time);
		agent.resetAverageReward();
//		agent.setDeviation(2);
		environment.singleRun(agent, new DelayedDiscountingTask(agent,
				new TDAVG_RewardVaules(), DDTtrailNumber,7), time);
	}
}
