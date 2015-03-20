package sim.Omision;

import action.ActionNames;
import action.ActionType;
import action.SoftMaxActionSelection;
import agent.Agent;
import agent.TD.TDAgent;
import agent.TD.TD_Constants;
import env.Cue;
import env.CueNames;
import env.RunStep;
import proc.omision.SimpleRewardTask;
import reward.RewardValue;
import reward.SimpleRewardValue;

public class SimpleRewardTask_RunStep extends RunStep {

	private int learningTrial;
	private int extinctionTrial;

	public SimpleRewardTask_RunStep(int learningTrial,
			int extinctionTrial) {
		this.learningTrial = learningTrial;
		this.extinctionTrial = extinctionTrial;
	}

	@Override
	public void run(int time) {
		RewardValue rewardValue = new SimpleRewardValue();

		rewardValue.setR1(0.9);
		rewardValue.setR2(0);

		Agent agent = new TDAgent(environment, new SoftMaxActionSelection(2),
                new Cue[]{Cue.get(CueNames.InitialState)}, new ActionType[]{ActionType.get(ActionNames.left), ActionType.get(ActionNames.right)}, new TD_Constants());

		SimpleRewardTask procedureOne = new SimpleRewardTask(
				agent, rewardValue, learningTrial);

		environment.singleRun(agent, procedureOne, time);

		rewardValue.setR1(0.9);
		rewardValue.setR2(1);

        SimpleRewardTask procedureTwo = new SimpleRewardTask(
                agent, rewardValue, extinctionTrial);
        procedureTwo.setCurrentState(procedureOne.getCurrentState());

        environment.singleRun(agent, procedureTwo, time);
	}
}