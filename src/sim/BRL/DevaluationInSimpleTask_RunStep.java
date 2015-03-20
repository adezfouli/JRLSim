package sim.BRL;

import action.*;
import env.Cue;

import env.CueNames;
import env.RunStep;
import proc.dualprocess.CyclicLeverMagazineTask;
import reward.BRL_RewardValues;
import reward.RewardValue;
import agent.Agent;
import agent.BRL.BRL_Constants;
import agent.kalman.QKalmanAgent;

public class DevaluationInSimpleTask_RunStep extends RunStep {

	private int learningTrial;
	private int extinctionTrial;

	public DevaluationInSimpleTask_RunStep(int learningTrial, int extinctionTrial) {
		this.learningTrial = learningTrial;
		this.extinctionTrial = extinctionTrial;
	}

	@Override
	public void run(int time) {
		RewardValue rewardValue = new BRL_RewardValues();
		
		rewardValue.setR1(1);
		rewardValue.setR2(1);
		
        Agent agent = new QKalmanAgent(environment, new BayesianActionSelection(new SoftMaxActionSelection(3)), new BRL_Constants());
		CyclicLeverMagazineTask procedureOne = new CyclicLeverMagazineTask(agent,
				rewardValue, learningTrial);
		environment.singleRun(agent, procedureOne, time);

		rewardValue.setR1(0);
		rewardValue.setR2(0);
//
		agent.setExperiencedRewardDuringTransition(-1,
				Cue.get(CueNames.FoodADelivered), ActionType.get(ActionNames.enterMagazine));


		CyclicLeverMagazineTask procedure = new CyclicLeverMagazineTask(agent,
				rewardValue, extinctionTrial);
		procedure.setCurrentState(procedureOne.getCurrentState());
		environment.singleRun(agent, procedure, time);
	}
}