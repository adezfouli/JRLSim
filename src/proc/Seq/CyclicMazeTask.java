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

public class CyclicMazeTask extends Procedure {

    public CyclicMazeTask(Agent agent, RewardValue rewardValue,
                          int maxNumberOfTrials, double ISI) {
        super(agent, rewardValue, maxNumberOfTrials);
        setISI(ISI);
    }

    @Override
    public Cue[] getCue() {
        if (currentState == State.getState(0))
            return new Cue[]{Cue.get(CueNames.InitLeft)};
        else if (currentState == State.getState(1))
            return new Cue[]{Cue.get(CueNames.InitRight)};
        if (currentState == State.getState(2))
            return new Cue[]{Cue.get(CueNames.MiddleFood)};
        else if (currentState == State.getState(3))
            return new Cue[]{Cue.get(CueNames.MiddleNull)};
        if (currentState == State.getState(4))
            return new Cue[]{Cue.get(CueNames.EndFood)};
        if (currentState == State.getState(5))
            return new Cue[]{Cue.get(CueNames.EndNull)};
        throw new BRLException("Illegal State");
    }

    @Override
    public boolean isPossible(State currentState, PrimitiveAction action) {

        if (currentState == State.getState(0)
                && (action.getActionType() == ActionType.get(ActionNames.left))) {
            return true;
        } else if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.right)) {
            return true;
        } else if (currentState == State.getState(1)
                && action.getActionType() == ActionType.get(ActionNames.left)) {
            return true;
        } else if (currentState == State.getState(1)
                && action.getActionType() == ActionType.get(ActionNames.right)) {
            return true;
        } else if (currentState == State.getState(2)
                && action.getActionType() == ActionType.get(ActionNames.run)) {
            return true;
        } else if (currentState == State.getState(2)
                && action.getActionType() == ActionType.get(ActionNames.turnBack)) {
            return true;
        } else if (currentState == State.getState(3)
                && action.getActionType() == ActionType.get(ActionNames.run)) {
            return true;
        } else if (currentState == State.getState(3)
                && action.getActionType() == ActionType.get(ActionNames.turnBack)) {
            return true;
        } else if (currentState == State.getState(4)
                && action.getActionType() == ActionType.get(ActionNames.returnToMaze)) {
            return true;
        } else if (currentState == State.getState(5)
                && action.getActionType() == ActionType.get(ActionNames.returnToMaze)) {
            return true;
        }
        return false;
    }

    public Reward nextState(PrimitiveAction action) {

        delay = action.getLatency() + getISI();
        newTrial();

        if (currentState == State.getState(0)
                && (action.getActionType() == ActionType.get(ActionNames.left))) {
            currentState = State.getState(2);
            return rewardValue.zero();
        } else if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.right)) {
            currentState = State.getState(3);
            return rewardValue.zero();
        } else if (currentState == State.getState(1)
                && action.getActionType() == ActionType.get(ActionNames.left)) {
            currentState = State.getState(3);
            return rewardValue.zero();
        } else if (currentState == State.getState(1)
                && action.getActionType() == ActionType.get(ActionNames.right)) {
            currentState = State.getState(2);
            return rewardValue.zero();
        } else if (currentState == State.getState(2)
                && action.getActionType() == ActionType.get(ActionNames.run)) {
            currentState = State.getState(4);
            return rewardValue.zero();
        } else if (currentState == State.getState(2)
                && action.getActionType() == ActionType.get(ActionNames.turnBack)) {
            currentState = State.getState(3);
            return rewardValue.zero();
        } else if (currentState == State.getState(3)
                && action.getActionType() == ActionType.get(ActionNames.run)) {
            currentState = State.getState(5);
            return rewardValue.zero();
        } else if (currentState == State.getState(3)
                && action.getActionType() == ActionType.get(ActionNames.turnBack)) {
            currentState = State.getState(2);
            return rewardValue.zero();
        } else if (currentState == State.getState(4)
                && action.getActionType() == ActionType.get(ActionNames.returnToMaze)) {
            if (Math.random() < 0.5)
                currentState = State.getState(0);
            else
                currentState = State.getState(1);
            return rewardValue.foodEating();
        } else if (currentState == State.getState(5)
                && action.getActionType() == ActionType.get(ActionNames.returnToMaze)) {
            if (Math.random() < 0.5)
                currentState = State.getState(0);
            else
                currentState = State.getState(1);
            return rewardValue.zero();

        } else {
            throw new BRLException("Illigal state action");
        }
    }
}
