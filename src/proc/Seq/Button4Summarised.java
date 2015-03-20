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

public class Button4Summarised extends Procedure {

    private State twoNextState;
    private boolean randomTrial;

    public Button4Summarised(Agent agent, RewardValue rewardValue, int maxNumberOfTrials, double ISI, boolean randomTrial) {
        super(agent, rewardValue, maxNumberOfTrials);
        currentState = State.getState(0);
        this.randomTrial = randomTrial;
        setISI(ISI);
    }

    @Override
    public Cue[] getCue() {

        if (currentState == State.getState(0))
            return new Cue[]{Cue.get(CueNames.step0)};
        else if (currentState == State.getState(1))
            return new Cue[]{Cue.get(CueNames.step1)};
        else if (currentState == State.getState(2))
            return new Cue[]{Cue.get(CueNames.step2)};
        else if (currentState == State.getState(3))
            return new Cue[]{Cue.get(CueNames.step3)};
        else if (currentState == State.getState(4))
            return new Cue[]{Cue.get(CueNames.NoReward)};
        throw new BRLException("Illegal State");
    }

    @Override
    public boolean isPossible(State currentState, PrimitiveAction action) {

        if ((
                currentState == State.getState(0) ||
                        currentState == State.getState(1) ||
                        currentState == State.getState(2) ||
                        currentState == State.getState(3)) &&
                (action.getActionType() == ActionType.get(ActionNames.Button0) ||
                        action.getActionType() == ActionType.get(ActionNames.Button1) ||
                        action.getActionType() == ActionType.get(ActionNames.Button2) ||
                        action.getActionType() == ActionType.get(ActionNames.Button3)


                ))
            return true;
        if ((
                currentState == State.getState(4))
                && action.getActionType() == ActionType.get(ActionNames.terminal))
            return true;

        return false;
    }

    public Reward nextState(PrimitiveAction action) {

        Reward reward = null;
        delay = action.getLatency() + getISI();
        newTrial();

        if ((
                currentState == State.getState(0) ||
                        currentState == State.getState(1) ||
                        currentState == State.getState(2) ||
                        currentState == State.getState(3)))
            reward = rewardValue.zero();

        if (currentState == State.getState(4))
            reward = rewardValue.foodEating();

        if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.Button0)) {
            currentState = State.getState(1);
        } else if (currentState == State.getState(1)
                && action.getActionType() == ActionType.get(ActionNames.Button1)) {
            currentState = State.getState(2);
        } else if (currentState == State.getState(2)
                && action.getActionType() == ActionType.get(ActionNames.Button2)) {
            currentState = State.getState(3);
        } else if (currentState == State.getState(3)
                && action.getActionType() == ActionType.get(ActionNames.Button3)) {
            currentState = State.getState(4);
        } else if (currentState == State.getState(4)
                && action.getActionType() == ActionType.get(ActionNames.terminal)) {
            currentState = State.getState(0);
        }
        else
            currentState = State.getState(0);
        return reward;
    }

}
