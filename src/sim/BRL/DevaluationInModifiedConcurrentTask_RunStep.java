package sim.BRL;

import action.ActionNames;
import action.ActionType;
import action.BayesianActionSelection;
import action.SoftMaxActionSelection;
import agent.Agent;
import agent.BRL.BRL_Constants;
import agent.kalman.QKalmanAgent;
import env.Cue;
import env.CueNames;
import env.RunStep;
import proc.dualprocess.CyclicLeverChainTaskSingleOutcome;
import reward.BRL_RewardValues;
import reward.RewardValue;

public class DevaluationInModifiedConcurrentTask_RunStep extends RunStep {

	private int learningTrial;
	private int extinctionTrial;

	public DevaluationInModifiedConcurrentTask_RunStep(int learningTrial,
			int extinctionTrial) {
		this.learningTrial = learningTrial;
		this.extinctionTrial = extinctionTrial;
	}

	@Override
	public void run(int time) {
		RewardValue rewardValue = new BRL_RewardValues();

		rewardValue.setR1(1);
		rewardValue.setR2(1);

		Agent agent = new QKalmanAgent(environment, new BayesianActionSelection(new SoftMaxActionSelection(1)), new BRL_Constants());
		CyclicLeverChainTaskSingleOutcome procedureOne = new CyclicLeverChainTaskSingleOutcome(
				agent, rewardValue, learningTrial);
		environment.singleRun(agent, procedureOne, time);

		rewardValue.setR1(0.0);
		rewardValue.setR2(0.0);

		agent.setExperiencedRewardDuringTransition(-1, Cue.get(CueNames.FoodADelivered),
				ActionType.get(ActionNames.enterMagazine));

		CyclicLeverChainTaskSingleOutcome procedure = new CyclicLeverChainTaskSingleOutcome(
				agent, rewardValue, extinctionTrial);
		procedure.setCurrentState(procedureOne.getCurrentState());
		environment.singleRun(agent, procedure, time);
	}
}
