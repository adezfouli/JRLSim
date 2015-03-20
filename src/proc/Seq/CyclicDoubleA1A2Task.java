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

public class CyclicDoubleA1A2Task extends Procedure {


    public CyclicDoubleA1A2Task(Agent agent, RewardValue rewardValue,
                                int maxNumberOfTrials, int ISI) {
        super(agent, rewardValue, maxNumberOfTrials);
        setISI(ISI);
    }

    @Override
    public Cue[] getCue() {
        if (currentState == State.getState(0))
            return new Cue[]{Cue.get(CueNames.FoodADelivered)};
        else if (currentState == State.getState(1))
            return new Cue[]{Cue.get(CueNames.FoodBDelivered)};
        else if (currentState == State.getState(2))
            return new Cue[]{Cue.get(CueNames.AfterPressL1)};
        else if (currentState == State.getState(3))
            return new Cue[]{Cue.get(CueNames.AfterPressL2)};
        else if (currentState == State.getState(4))
            return new Cue[]{Cue.get(CueNames.After_EM)};
        throw new BRLException("Illegal State");
    }

    @Override
    public boolean isPossible(State currentState, PrimitiveAction action) {
        if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            return true;
        } else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.pressLever2))) {
            return true;
        } else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            return true;
        } else if (currentState == State.getState(1)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            return true;
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever2))) {
            return true;
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            return true;
        } else if (currentState == State.getState(2)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            return true;
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.pressLever2))) {
            return true;
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            return true;
        }else if (currentState == State.getState(3)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            return true;
        } else if ((currentState == State.getState(3) && action.getActionType() == ActionType.get(ActionNames.pressLever2))) {
            return true;
        } else if ((currentState == State.getState(3) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            return true;
        } else if (currentState == State.getState(4)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            return true;
        } else if ((currentState == State.getState(4) && action.getActionType() == ActionType.get(ActionNames.pressLever2))) {
            return true;
        } else if ((currentState == State.getState(4) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            return true;
        }
        return false;
    }

    public Reward nextState(PrimitiveAction action) {

        Reward reward = null;
        delay = action.getLatency() + getISI();
        newTrial();


        if (currentState == State.getState(0))
            reward = rewardValue.getR1();
        else if (currentState == State.getState(1))
            reward = rewardValue.getR2();
        else
            reward = rewardValue.zero();


        if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            currentState = State.getState(2);
        } else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.pressLever2))) {
            currentState = State.getState(3);
        } else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(4);
        } else if (currentState == State.getState(1)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            currentState = State.getState(2);
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever2))) {
            currentState = State.getState(3);
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(4);
        } else if (currentState == State.getState(2)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            currentState = State.getState(2);
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.pressLever2))) {
            currentState = State.getState(2);
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(0);
        }else if (currentState == State.getState(3)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            currentState = State.getState(3);
        } else if ((currentState == State.getState(3) && action.getActionType() == ActionType.get(ActionNames.pressLever2))) {
            currentState = State.getState(3);
        } else if ((currentState == State.getState(3) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(1);
        } else if (currentState == State.getState(4)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            currentState = State.getState(2);
        } else if ((currentState == State.getState(4) && action.getActionType() == ActionType.get(ActionNames.pressLever2))) {
            currentState = State.getState(3);
        } else if ((currentState == State.getState(4) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(4);
        }

        return reward;
    }
}

