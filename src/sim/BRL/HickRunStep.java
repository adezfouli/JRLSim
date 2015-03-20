package sim.BRL;

import action.BayesianActionSelection;
import action.SoftMaxActionSelection;
import agent.Agent;
import agent.BRL.BRL_Constants;
import agent.kalman.QKalmanAgent;
import env.RunStep;
import proc.dualprocess.GoNoGoTask;
import proc.dualprocess.HickTask;
import reward.BRL_RewardValues;
import reward.RewardValue;


public class HickRunStep extends RunStep{

    private int learningTrial;
    private int numberOfChoices;

    public HickRunStep(int numberOfChoices, int learningTrial) {
        this.learningTrial = learningTrial;
        this.numberOfChoices = numberOfChoices;
    }

    @Override
    public void run(int time) {
        RewardValue rewardValue = new BRL_RewardValues();

        rewardValue.setR1(1);

        Agent agent = new QKalmanAgent(environment, new BayesianActionSelection(new SoftMaxActionSelection(4),1), new BRL_Constants());
        HickTask procedureOne = new HickTask(numberOfChoices, agent,
                rewardValue, learningTrial);
        environment.singleRun(agent, procedureOne, time);
    }
}