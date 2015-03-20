package sim.SEQ;

import action.*;
import agent.Agent;
import agent.SeqRL.SeqAgent;
import env.Cue;
import env.CueNames;
import env.RunStep;
import proc.Seq.*;
import reward.BRL_RewardValues;
import reward.RewardValue;

public class Seq_RunStep extends RunStep {
    private int learningTrial;
    private int extinctionTrial;
    private String task;

    public Seq_RunStep(int learningTrial,
                       int extinctionTrial, String task) {
        this.learningTrial = learningTrial;
        this.extinctionTrial = extinctionTrial;
        this.task = task;
    }

    @Override
    public void run(int time) {
        RewardValue rewardValue = new BRL_RewardValues();

        if (task.equals("dev"))
            CyclicA1A2TaskDevaluation(time, rewardValue);
        else if (task.equals("deg"))
            CyclicA1A2TaskDegradation(time, rewardValue);
        else if (task.equals("omm"))
            CyclicA1A2TaskOmission(time, rewardValue);
        else if (task.equals("simple"))
            CyclicA1A2Task(time, rewardValue);
        else if (task.equals("b4s"))
            Cyclic4ButtonTaskSeq(time, rewardValue, false);
        else if (task.equals("b4r"))
            Cyclic4ButtonTaskSeq(time, rewardValue, true);
    }

    private void CyclicA1A2Task(int time, RewardValue rewardValue) {
        rewardValue.setF(1D);

        Agent agent = new SeqAgent(environment, new SoftMaxActionSelection(4),
                new Cue[]{
                        Cue.get(CueNames.FoodDelivered),
                        Cue.get(CueNames.F_EM), Cue.get(CueNames.F_LP)
                },
                new ActionType[]{
                        ActionType.get(ActionNames.pressLever),
                        ActionType.get(ActionNames.enterMagazine)
                },
                new Seq_Constants());

        CyclicA1A2Task procedureOne = new CyclicA1A2Task(
                agent, rewardValue, learningTrial, 1);

        environment.singleRun(agent, procedureOne, time);

    }

    private void CyclicA1A2TaskDevaluation(int time, RewardValue rewardValue) {

        rewardValue.setF(1D);

        Agent agent = new SeqAgent(environment, new SoftMaxActionSelection(4),
                new Cue[]{
                        Cue.get(CueNames.FoodDelivered),
                        Cue.get(CueNames.F_EM), Cue.get(CueNames.F_LP)
                },
                new ActionType[]{
                        ActionType.get(ActionNames.pressLever),
                        ActionType.get(ActionNames.enterMagazine)
                },
                new Seq_Constants());

        CyclicA1A2Task procedureOne = new CyclicA1A2Task(
                agent, rewardValue, learningTrial, 1);

        environment.singleRun(agent, procedureOne, time);

        rewardValue.setF(0D);

        CyclicA1A2Task procedureTwo = new CyclicA1A2Task(
                agent, rewardValue, extinctionTrial, 1);

        agent.setRewardOfState(new Cue[]{Cue.get(CueNames.FoodDelivered)}, -1);

//        procedureTwo.setCurrentState(procedureOne.getCurrentState());

        environment.singleRun(agent, procedureTwo, time);
    }

    private void CyclicA1A2TaskDegradation(int time, RewardValue rewardValue) {

        rewardValue.setF(1D);

        Agent agent = new SeqAgent(environment, new SoftMaxActionSelection(4),
                new Cue[]{
                        Cue.get(CueNames.FoodDelivered),
                        Cue.get(CueNames.F_EM), Cue.get(CueNames.F_LP)
                },
                new ActionType[]{
                        ActionType.get(ActionNames.pressLever),
                        ActionType.get(ActionNames.enterMagazine)
                },
                new Seq_Constants());

        CyclicA1A2Task procedureOne = new CyclicA1A2Task(
                agent, rewardValue, learningTrial, 1);

        environment.singleRun(agent, procedureOne, time);

        CyclicUnpairedRewardA1A2Task procedureTwo = new CyclicUnpairedRewardA1A2Task(
                agent, rewardValue, extinctionTrial, 1);

//        if (procedureOne.getCurrentState() != State.getState(1))    {
//            procedureTwo.setCurrentState(procedureOne.getCurrentState());
//        }
        environment.singleRun(agent, procedureTwo, time);
    }

    private void CyclicA1A2TaskOmission(int time, RewardValue rewardValue) {

        rewardValue.setF(1D);

        Agent agent = new SeqAgent(environment, new SoftMaxActionSelection(4),
                new Cue[]{
                        Cue.get(CueNames.FoodDelivered),
                        Cue.get(CueNames.F_EM), Cue.get(CueNames.F_LP)
                },
                new ActionType[]{
                        ActionType.get(ActionNames.pressLever),
                        ActionType.get(ActionNames.enterMagazine)
                },
                new Seq_Constants());

        CyclicA1A2Task procedureOne = new CyclicA1A2Task(
                agent, rewardValue, learningTrial, 1);

        environment.singleRun(agent, procedureOne, time);

        CyclicOmissionA1A2Task procedureTwo = new CyclicOmissionA1A2Task(
                agent, rewardValue, extinctionTrial, 1);

//        if (procedureOne.getCurrentState() != State.getState(1))    {
//            procedureTwo.setCurrentState(State.getState(2));
//        }

        environment.singleRun(agent, procedureTwo, time);
    }

    private void Cyclic4ButtonTaskSeq(int time, RewardValue rewardValue, boolean random) {

        rewardValue.setF(1D);

        Agent agent = new SeqAgent(environment, new SoftMaxActionSelection(4),
                new Cue[]{
                        Cue.get(CueNames.step0),
                        Cue.get(CueNames.step1),
                        Cue.get(CueNames.step2),
                        Cue.get(CueNames.step3),
                        Cue.get(CueNames.NoReward),

                        Cue.get(CueNames.reward0),
                        Cue.get(CueNames.reward1),
                        Cue.get(CueNames.reward2),
                        Cue.get(CueNames.reward3),

                },
                new ActionType[]{
                        ActionType.get(ActionNames.rewardReceived),
                        ActionType.get(ActionNames.Button0),
                        ActionType.get(ActionNames.Button1),
                        ActionType.get(ActionNames.Button2),
                        ActionType.get(ActionNames.Button3),
                        ActionType.get(ActionNames.terminal),
                },
                new Seq_Constants());

        Button4Task procedureOne = new Button4Task(
                agent, rewardValue, learningTrial, 1, random);

        environment.singleRun(agent, procedureOne, time);
    }

    private void CyclicMazeTask(int time, RewardValue rewardValue) {

        rewardValue.setF(1D);

        Agent agent = new SeqAgent(environment, new GreedyActionSelection(0.1),
                new Cue[]{

                        Cue.get(CueNames.InitLeft),
                        Cue.get(CueNames.InitRight),
                        Cue.get(CueNames.MiddleFood),
                        Cue.get(CueNames.MiddleNull),
                        Cue.get(CueNames.EndFood),
                        Cue.get(CueNames.EndNull),
                },

                new ActionType[]{
                        ActionType.get(ActionNames.left),
                        ActionType.get(ActionNames.right),
                        ActionType.get(ActionNames.returnToMaze),
                        ActionType.get(ActionNames.turnBack),
                        ActionType.get(ActionNames.run),
                },
                new Seq_Constants());

        CyclicMazeTask procedureOne = new CyclicMazeTask(
                agent, rewardValue, learningTrial, 1);

        environment.singleRun(agent, procedureOne, time);

    }
}
