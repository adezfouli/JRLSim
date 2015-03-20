package sim.BRL;

import action.*;
import env.Cue;

import env.CueNames;
import env.RunStep;
import proc.dualprocess.CyclicLeverMagazineChainTask;
import reward.BRL_RewardValues;
import reward.RewardValue;
import agent.Agent;
import agent.BRL.BRL_Constants;
import agent.kalman.QKalmanAgent;

public class DevaluationInComplexTask_RunStep extends RunStep {

	private int learningTrial;
	private int extinctionTrial;

	public DevaluationInComplexTask_RunStep(int learningTrial,
			int extinctionTrial) {
		this.learningTrial = learningTrial;
		this.extinctionTrial = extinctionTrial;
	}

	@Override
	public void run(int time) {
		RewardValue rewardValue = new BRL_RewardValues();

		rewardValue.setR1(1);
		rewardValue.setR2(1);

		Agent agent = new QKalmanAgent(environment, new BayesianActionSelection(new SoftMaxActionSelection(3)), new BRL_Constants());
		CyclicLeverMagazineChainTask procedureOne = new CyclicLeverMagazineChainTask(
				agent, rewardValue, learningTrial);
		environment.singleRun(agent, procedureOne, time);

		rewardValue.setR1(0.0);
		rewardValue.setR2(0.0);

		agent.setExperiencedRewardDuringTransition(-1, Cue.get(CueNames.FoodADelivered),
				ActionType.get(ActionNames.enterMagazine));

		CyclicLeverMagazineChainTask procedure = new CyclicLeverMagazineChainTask(
				agent, rewardValue, extinctionTrial);
		procedure.setCurrentState(procedureOne.getCurrentState());
		environment.singleRun(agent, procedure, time);
	}
}