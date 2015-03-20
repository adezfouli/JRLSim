package sim.TDAVG;

import proc.addiction.ThreeReinforcersWithShock;
import action.GreedyActionSelection;
import action.SoftMaxActionSelection;
import agent.LATDAVG.LATDAVGAgent;
import agent.TDAVG.TDAVGAgent;
import env.RunStep;

public class TDAVG_ThreeReinforcers_RunStep extends RunStep {

	private int trailNumber;

	public TDAVG_ThreeReinforcers_RunStep(int trailNumber) {
		this.trailNumber = trailNumber;
	}

	@Override
	public void run(int time) {
		TDAVGAgent agent = new TDAVGAgent(environment,
				new GreedyActionSelection(0.2), new TDAVG_Constants());
		environment.singleRun(agent, new ThreeReinforcersWithShock(agent,
				new TDAVG_RewardVaules(), trailNumber), time);
	}
}
