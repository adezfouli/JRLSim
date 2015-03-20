package sim.BRL;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import proc.dualprocess.TwoLevelTask;
import reward.BRL_RewardValues;
import reward.RewardValue;
import action.BayesianActionSelection;
import action.GreedyActionSelection;
import agent.Agent;
import agent.BRL.BRL_Agent;
import agent.BRL.BRL_Constants;
import env.RunStep;

public class BRL_SharpDevaluation extends RunStep {

	private int learningTrial;
	private int sharpDevaluationTrial;
	private int returnTrial;
	
	
	
	public BRL_SharpDevaluation(int learningTrial,
			int sharpDevaluationTrial, int returnTrial)  {
		this.learningTrial = learningTrial;
		this.sharpDevaluationTrial = sharpDevaluationTrial;
		this.returnTrial = returnTrial;
	}


	@Override
	public void run(int time) {
		RewardValue rewardValue = new BRL_RewardValues();
		rewardValue.setR1(50);
		rewardValue.setR2(30);
		Agent agent = new BRL_Agent(environment, new BayesianActionSelection(
				new GreedyActionSelection(0.6)), new BRL_Constants());
		environment.singleRun(agent, new TwoLevelTask(agent, rewardValue, learningTrial),
				time);
		rewardValue.setR1(2);
		rewardValue.setR2(30);
		environment.singleRun(agent, new TwoLevelTask(agent, rewardValue, sharpDevaluationTrial),
				time);

		rewardValue.setR1(20);
		rewardValue.setR2(30);
		environment.singleRun(agent, new TwoLevelTask(agent, rewardValue, returnTrial),
				time);
	}
}
