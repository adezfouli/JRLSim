package sim.BRL;

import action.BayesianActionSelection;
import action.SoftMaxActionSelection;
import agent.Agent;
import agent.BRL.BRL_Constants;
import agent.kalman.QKalmanAgent;
import env.RunStep;
import proc.dualprocess.GoNoGoTask;
import reward.BRL_RewardValues;
import reward.RewardValue;

public class goNoGoRunStep  extends RunStep {

    private int learningTrial;
    private int extinctionTrial;
    private int reversalTrial;

    public goNoGoRunStep(int learningTrial, int reversalTrial, int extinctionTrial) {
        this.learningTrial = learningTrial;
        this.extinctionTrial = extinctionTrial;
        this.reversalTrial = reversalTrial;
    }

    @Override
    public void run(int time) {
        RewardValue rewardValue = new BRL_RewardValues();

        rewardValue.setR1(1);
        rewardValue.setR2(0);
        rewardValue.setR3(0);
        rewardValue.setR4(1);

        Agent agent = new QKalmanAgent(environment, new BayesianActionSelection(new SoftMaxActionSelection(1)), new BRL_Constants());
        GoNoGoTask procedureOne = new GoNoGoTask(agent,
                rewardValue, learningTrial);
        environment.singleRun(agent, procedureOne, time);

        rewardValue.setR1(0);
        rewardValue.setR2(1);
        rewardValue.setR3(1);
        rewardValue.setR4(0);

        GoNoGoTask procedureTwo = new GoNoGoTask(agent,
                rewardValue, reversalTrial);
        procedureTwo.setCurrentState(procedureOne.getCurrentState());
        environment.singleRun(agent, procedureTwo, time);

        rewardValue.setR1(0);
        rewardValue.setR2(1);
        rewardValue.setR3(0);
        rewardValue.setR4(1);

        GoNoGoTask procedureThree = new GoNoGoTask(agent,
                rewardValue, extinctionTrial);
        procedureThree.setCurrentState(procedureTwo.getCurrentState());
        environment.singleRun(agent, procedureThree, time);
    }
}
