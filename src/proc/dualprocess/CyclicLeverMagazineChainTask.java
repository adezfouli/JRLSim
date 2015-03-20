package proc.dualprocess;

import action.PrimitiveAction;
import action.ActionNames;
import action.ActionType;
import agent.Agent;
import env.Cue;
import env.CueNames;
import env.State;
import proc.BRLException;
import proc.Procedure;
import reward.Reward;
import reward.RewardValue;

public class CyclicLeverMagazineChainTask extends Procedure {

    private static final long serialVersionUID = 6211937409993118633L;

    public static void init() {
        Cue.get(CueNames.InitialState);
        Cue.get(CueNames.FoodADelivered);
        Cue.get(CueNames.FoodBDelivered);
        ActionType.get(ActionNames.pressLever1);
        ActionType.get(ActionNames.pressLever2);
        ActionType.get(ActionNames.enterMagazine);
    }

    public CyclicLeverMagazineChainTask(Agent agent, RewardValue rewardValue,
                                        int totalTrials) {
        super(agent, rewardValue, totalTrials);
    }

    @Override
    public Cue[] getCue() {
        if (currentState == State.getState(0))
            return new Cue[]{Cue.get(CueNames.InitialState)};
        else if (currentState == State.getState(1))
            return new Cue[]{Cue.get(CueNames.FoodADelivered)};
        else if (currentState == State.getState(2))
            return new Cue[]{Cue.get(CueNames.FoodBDelivered)};
        throw new BRLException("Illegal State");
    }

    @Override
    public boolean isPossible(State currentState, PrimitiveAction action) {
        if (currentState == State.getState(0)
                && (action.getActionType() == ActionType.get(ActionNames.pressLever1)
                || action.getActionType() == ActionType.get(ActionNames.enterMagazine) || action
                .getActionType() == ActionType.get(ActionNames.pressLever2))) {
            return true;
        } else if (currentState == State.getState(1)
                && (action.getActionType() == ActionType.get(ActionNames.pressLever1)
                || action.getActionType() == ActionType.get(ActionNames.enterMagazine) || action
                .getActionType() == ActionType.get(ActionNames.pressLever2))) {
            return true;
        } else if (currentState == State.getState(2)
                && (action.getActionType() == ActionType.get(ActionNames.pressLever1)
                || action.getActionType() == ActionType.get(ActionNames.enterMagazine) || action
                .getActionType() == ActionType.get(ActionNames.pressLever2))) {
            return true;
        }

        return false;
    }

    @Override
    public Reward nextState(PrimitiveAction action) {

        newTrial();
        delay = getISI() + action.getLatency();

        if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            currentState = State.getState(1);
            return rewardValue.zero();
        } else if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.pressLever2)) {
            currentState = State.getState(2);
            return rewardValue.zero();
        } else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(0);
            return rewardValue.zero();
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever1))) {
            currentState = State.getState(0);
            return rewardValue.zero();
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever2))) {
            currentState = State.getState(0);
            return rewardValue.zero();
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(0);
            return rewardValue.getR1();
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.pressLever1))) {
            currentState = State.getState(0);
            return rewardValue.zero();
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.pressLever2))) {
            currentState = State.getState(0);
            return rewardValue.zero();
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(0);
            return rewardValue.getR2();
        } else {
            throw new BRLException("Illigal state action");
        }
    }
}
