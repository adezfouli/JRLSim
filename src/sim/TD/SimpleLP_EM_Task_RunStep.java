package sim.TD;

import action.ActionNames;
import action.ActionType;
import action.SoftMaxActionSelection;
import agent.Agent;
import agent.TD.TDAgent;
import env.Cue;
import env.CueNames;
import env.RunStep;
import proc.dualprocess.FoodTask;
import reward.BRL_RewardValues;
import reward.RewardValue;

public class SimpleLP_EM_Task_RunStep extends RunStep {

    private int learningTrial;
    private int extinctionTrial;

    public SimpleLP_EM_Task_RunStep(int learningTrial,
                                    int extinctionTrial) {
        this.learningTrial = learningTrial;
        this.extinctionTrial = extinctionTrial;
    }

    @Override
    public void run(int time) {

        RewardValue rewardValue = new BRL_RewardValues();

        Agent agent = new TDAgent(environment, new SoftMaxActionSelection(10),
                new Cue[]{
                        Cue.get(CueNames.InitialState),
                        Cue.get(CueNames.FoodDelivered),
                        Cue.get(CueNames.FoodObtained),
                        Cue.get(CueNames.LittleFoodObtained),
                        Cue.get(CueNames.NoReward),
                }, new ActionType[]{
                        ActionType.get(ActionNames.pressLever),
                        ActionType.get(ActionNames.enterMagazine),
                        ActionType.get(ActionNames.terminal)
                },
                new TD_Constants());

        FoodTask procedureOne = new FoodTask(agent, rewardValue, learningTrial);

        environment.singleRun(agent, procedureOne, time);

        FoodTask procedureTwo = new FoodTask(agent, rewardValue, learningTrial);

        ((TDAgent)agent).setQValue(-1, Cue.get(CueNames.FoodDelivered), ActionType.get(ActionNames.enterMagazine));

        rewardValue.setR1(0);

        environment.singleRun(agent, procedureTwo, time);

    }
}
