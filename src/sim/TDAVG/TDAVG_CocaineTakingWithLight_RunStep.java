package sim.TDAVG;

import proc.addiction.CocaineTaking;
import proc.addiction.CocaineTakingWithStimuli;
import action.GreedyActionSelection;
import agent.Agent;
import agent.LATDAVG.LATDAVGAgent;
import agent.TDAVG.TDAVGAgent;
import env.RunStep;

public class TDAVG_CocaineTakingWithLight_RunStep extends RunStep {

	private int trailNumber;

	public TDAVG_CocaineTakingWithLight_RunStep (int trailNumber) {
		this.trailNumber = trailNumber;
	}

	@Override
	public void run(int time) {
		Agent agent = new LATDAVGAgent(environment, new GreedyActionSelection(1),
				new TDAVG_Constants());
		environment.singleRun(agent, new CocaineTaking(agent,
				new TDAVG_RewardVaules(), 800), time);
//		environment.singleRun(agent, new CocaineTakingWithLight(agent,
//				new TDAVG_RewardVaules(), 1000), time);
	}
}
