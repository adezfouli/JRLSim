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

public class GoNoGoTask extends Procedure {

    public static void init() {
        Cue.get(CueNames.InitialState);
        Cue.get(CueNames.leftOne);
        Cue.get(CueNames.rightOne);
        ActionType.get(ActionNames.go);
        ActionType.get(ActionNames.noGo);
        ActionType.get(ActionNames.other);
    }

    public GoNoGoTask(Agent agent, RewardValue rewardValue,
                                   int maxNumberOfTrials) {
        super(agent, rewardValue, maxNumberOfTrials);
        setISI(1);
    }

    @Override
    public Cue[] getCue() {
        if (currentState == State.getState(0))
            return new Cue[]{Cue.get(CueNames.InitialState)};
        else if (currentState == State.getState(1))
            return new Cue[]{Cue.get(CueNames.rightOne)};
        else if (currentState == State.getState(2))
            return new Cue[]{Cue.get(CueNames.leftOne)};
        throw new BRLException("Illegal State");
    }

    @Override
    public boolean isPossible(State currentState, PrimitiveAction action) {
        if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.other)) {
            return true;
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.go))) {
            return true;
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.noGo))) {
            return true;
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.go))) {
            return true;
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.noGo))) {
            return true;
        }
        return false;
    }

    @Override
    public Reward nextState(PrimitiveAction action) {
        // newTrial();
        delay = getISI();
        if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.other)) {
            if (Math.random() > 0.5)
                currentState = State.getState(1);
            else
                currentState = State.getState(2);
            return rewardValue.zero();
        } else if (currentState == State.getState(1)
                && action.getActionType() == ActionType.get(ActionNames.go)) {
            currentState = State.getState(0);
            newTrial();
            return rewardValue.getR1();
        } else if (currentState == State.getState(1)
                && action.getActionType() == ActionType.get(ActionNames.noGo)) {
            currentState = State.getState(0);
            newTrial();
            return rewardValue.getR2();
        } else if (currentState == State.getState(2)
                && action.getActionType() == ActionType.get(ActionNames.go)) {
            currentState = State.getState(0);
            newTrial();
            return rewardValue.getR3();
        } else if (currentState == State.getState(2)
                && action.getActionType() == ActionType.get(ActionNames.noGo)) {
            currentState = State.getState(0);
            newTrial();
            return rewardValue.getR4();
        }
        else {
            throw new BRLException("Illigal state action");
        }
    }
}