package proc.Seq;


import action.ActionNames;
import action.ActionType;
import action.PrimitiveAction;
import agent.Agent;
import env.Cue;
import env.CueNames;
import env.State;
import proc.BRLException;
import proc.Procedure;
import reward.Reward;
import reward.RewardValue;

import javax.xml.ws.Action;

public class Button4Task extends Procedure {

    private boolean randomTrial;

    public Button4Task(Agent agent, RewardValue rewardValue, int maxNumberOfTrials, double ISI, boolean randomTrial) {
        super(agent, rewardValue, maxNumberOfTrials);
        currentState = State.getState(1);
        this.randomTrial = randomTrial;
        setISI(ISI);
    }

    @Override
    public Cue[] getCue() {

        if (currentState == State.getState(1))
            return new Cue[]{Cue.get(CueNames.step0)};
        else if (currentState == State.getState(2))
            return new Cue[]{Cue.get(CueNames.step1)};
        else if (currentState == State.getState(3))
            return new Cue[]{Cue.get(CueNames.step2)};
        else if (currentState == State.getState(4))
            return new Cue[]{Cue.get(CueNames.step3)};
        else if (currentState == State.getState(5))
            return new Cue[]{Cue.get(CueNames.reward0)};
        else if (currentState == State.getState(6))
            return new Cue[]{Cue.get(CueNames.reward1)};
        else if (currentState == State.getState(7))
            return new Cue[]{Cue.get(CueNames.reward2)};
        else if (currentState == State.getState(8))
            return new Cue[]{Cue.get(CueNames.reward3)};
        else if (currentState == State.getState(9))
            return new Cue[]{Cue.get(CueNames.NoReward)};
        throw new BRLException("Illegal State");
    }

    @Override
    public boolean isPossible(State currentState, PrimitiveAction action) {

        if ((
                currentState == State.getState(1) ||
                        currentState == State.getState(2) ||
                        currentState == State.getState(3) ||
                        currentState == State.getState(4)) &&
                (action.getActionType() == ActionType.get(ActionNames.Button0) ||
                        action.getActionType() == ActionType.get(ActionNames.Button1) ||
                        action.getActionType() == ActionType.get(ActionNames.Button2) ||
                        action.getActionType() == ActionType.get(ActionNames.Button3)


                ))
            return true;
        if ((
                currentState == State.getState(5) ||
                        currentState == State.getState(6) ||
                        currentState == State.getState(7) ||
                        currentState == State.getState(8)
        )
                && action.getActionType() == ActionType.get(ActionNames.rewardReceived))
            return true;


        if (currentState == State.getState(9) && action.getActionType() == ActionType.get(ActionNames.terminal))
            return true;

        return false;
    }

    public Reward nextState(PrimitiveAction action) {

        Reward reward = null;
        delay = action.getLatency() + getISI();
        newTrial();

        if ((
                currentState == State.getState(1) ||
                        currentState == State.getState(2) ||
                        currentState == State.getState(3) ||
                        currentState == State.getState(4)) ||
                        currentState == State.getState(9)
                )
            reward = rewardValue.zero();

        if ((
                currentState == State.getState(5) ||
                        currentState == State.getState(6) ||
                        currentState == State.getState(7) ||
                        currentState == State.getState(8)))
            reward = rewardValue.foodEating();

        if (currentState == State.getState(1)
                && action.getActionType() == ActionType.get(ActionNames.Button0)) {
            currentState = State.getState(5);
        } else if (currentState == State.getState(2)
                && action.getActionType() == ActionType.get(ActionNames.Button1)) {
            currentState = State.getState(6);
        } else if (currentState == State.getState(3)
                && action.getActionType() == ActionType.get(ActionNames.Button2)) {
            currentState = State.getState(7);
        } else if (currentState == State.getState(4)
                && action.getActionType() == ActionType.get(ActionNames.Button3)) {
            currentState = State.getState(8);
        } else if (randomTrial) {
            currentState = State.getState((int) (4 * Math.random()) + 1);
        } else if (currentState == State.getState(5)) {
            currentState = State.getState(2);
        } else if (currentState == State.getState(6)) {
            currentState = State.getState(3);
        } else if (currentState == State.getState(7)) {
            currentState = State.getState(4);
        } else if (currentState == State.getState(8)) {
            currentState = State.getState(1);
        } else if (currentState == State.getState(9) && action.getActionType() == ActionType.get(ActionNames.terminal))
            currentState = State.getState(1);

        else
            currentState = State.getState(9);

        return reward;
    }
}
