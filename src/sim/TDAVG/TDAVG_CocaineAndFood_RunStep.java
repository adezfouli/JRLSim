package sim.TDAVG;

import proc.addiction.CocaineTaking;
import proc.addiction.TwoConcurrentTask;
import proc.addiction.FoodTaking;
import action.GreedyActionSelection;
import action.SoftMaxActionSelection;
import agent.Agent;
import agent.TD.TDAgent;
import agent.TDAVG.TDAVGAgent;
import env.Environment;
import env.RunStep;

public class TDAVG_CocaineAndFood_RunStep extends RunStep {

	private int cocaineTakingNo;
	private int foodTakingNo;

	public TDAVG_CocaineAndFood_RunStep(int cocaineTakingNo, int foodTakingNo) {
		this.cocaineTakingNo = cocaineTakingNo;
		this.foodTakingNo = foodTakingNo;
	}

	@Override
	public void run(int time) {
		Agent agent = new TDAVGAgent(environment, new SoftMaxActionSelection(0.1), new TDAVG_Constants());
		environment.singleRun(agent, new CocaineTaking(agent,
				new TDAVG_RewardVaules(), cocaineTakingNo), time);
		agent.resetAverageReward();
		environment.singleRun(agent, new FoodTaking(agent,
				new TDAVG_RewardVaules(), foodTakingNo), time);
	}
}
