package sim.TD;

import proc.addiction.CocaineTaking;
import proc.addiction.TwoConcurrentTask;
import action.GreedyActionSelection;
import agent.Agent;
import agent.TD.TDAgent;
import env.Environment;
import env.RunStep;

public class CocaineAndFood_RunStep extends RunStep {

	private int trailNumber;

	public CocaineAndFood_RunStep(int trailNumber) {
		this.trailNumber = trailNumber;
	}

	@Override
	public void run(int time) {
		Agent agent = new TDAgent(environment, new GreedyActionSelection(1),
				new TD_Constants());
		environment.singleRun(agent, new TwoConcurrentTask(agent,
				new TD_RewardVaules(), trailNumber), time);
	}
}
