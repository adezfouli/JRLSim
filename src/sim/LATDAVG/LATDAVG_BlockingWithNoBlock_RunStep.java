package sim.LATDAVG;

import env.CueNames;
import proc.addiction.BlockingTest;
import proc.addiction.CocaineTaking;
import proc.addiction.CocaineTakingWithStimuli;
import sim.TDAVG.TDAVG_Constants;
import sim.TDAVG.TDAVG_RewardVaules;
import action.GreedyActionSelection;
import agent.Agent;
import agent.LATDAVG.LATDAVGAgent;
import env.Cue;
import env.RunStep;

public class LATDAVG_BlockingWithNoBlock_RunStep extends RunStep {

	private int twoCueTrial;
	private int testTrial;
	private int freeTaking;

	public LATDAVG_BlockingWithNoBlock_RunStep(int freeTaking, int twoCueTrial, int testTrial) {
		this.freeTaking = freeTaking;
		this.twoCueTrial = twoCueTrial;
		this.testTrial = testTrial;
	}

	@Override
	public void run(int time) {
		Agent agent = new LATDAVGAgent(environment, new GreedyActionSelection(
				0.2), new TDAVG_Constants());
		environment.singleRun(agent, new CocaineTaking(agent,
				 new TDAVG_RewardVaules(), freeTaking), time);
		agent.resetAverageReward();
		environment.singleRun(agent, new CocaineTakingWithStimuli(agent,
				new TDAVG_RewardVaules(), twoCueTrial, new Cue[] { Cue.get(CueNames.light),
						Cue.get(CueNames.tone) }), time);
		environment.singleRun(agent, new BlockingTest(agent,
				new TDAVG_RewardVaules(), testTrial), time);
	}

}
