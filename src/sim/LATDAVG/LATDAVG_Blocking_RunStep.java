package sim.LATDAVG;

import env.CueNames;
import proc.addiction.BlockingTest;
import proc.addiction.TwoConcurrentTask;
import proc.addiction.CocaineTaking;
import proc.addiction.CocaineTakingWithStimuli;
import proc.addiction.FoodSeekingShockTaking;
import proc.addiction.FoodTaking;
import proc.addiction.ThreeReinforcersWithShock;
import sim.TD.CocaineAndFood_RunStep;
import sim.TDAVG.TDAVG_Constants;
import sim.TDAVG.TDAVG_RewardVaules;
import action.GreedyActionSelection;
import action.SoftMaxActionSelection;
import agent.Agent;
import agent.LATDAVG.LATDAVGAgent;
import agent.TDAVG.TDAVGAgent;
import env.Cue;
import env.RunStep;
import flanagan.circuits.TwoWireLine;

public class LATDAVG_Blocking_RunStep extends RunStep {

	private int singleCueTrial;
	private int twoCueTrial;
	private int testTrial;
	private int freeTaking;

	public LATDAVG_Blocking_RunStep(int freeTaking, int singleCueTrial,
			int twoCueTrial, int testTrial) {
		this.freeTaking = freeTaking;
		this.singleCueTrial = singleCueTrial;
		this.twoCueTrial = twoCueTrial;
		this.testTrial = testTrial;
	}

	@Override
	public void run(int time) {
		LATDAVGAgent agent = new LATDAVGAgent(environment,
				new GreedyActionSelection(0.2), new TDAVG_Constants());
		environment.singleRun(agent, new CocaineTaking(agent,
				new TDAVG_RewardVaules(), freeTaking), time);
		agent.resetAverageReward();
		environment.singleRun(agent, new CocaineTakingWithStimuli(agent,
				new TDAVG_RewardVaules(), singleCueTrial,
				new Cue[] { Cue.get(CueNames.tone) }), time);
		environment.singleRun(agent, new CocaineTakingWithStimuli(agent,
				new TDAVG_RewardVaules(), twoCueTrial, new Cue[] { Cue.get(CueNames.light),
						Cue.get(CueNames.tone) }), time);
		environment.singleRun(agent, new BlockingTest(agent,
				new TDAVG_RewardVaules(), testTrial), time);
	}
}
