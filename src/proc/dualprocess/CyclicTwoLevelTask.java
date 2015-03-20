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

import java.util.Random;

public class CyclicTwoLevelTask extends Procedure {

    private static final long serialVersionUID = 2682403111409156684L;

    public CyclicTwoLevelTask(Agent agent, RewardValue rewardValue,
                              int maxNumberOfTrials, double ITI, double ISI) {
        super(agent, rewardValue, maxNumberOfTrials);
        setISI(ISI);
        setITI(ITI);
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
                && (action.getActionType() == ActionType.get(ActionNames.right) || action
                .getActionType() == ActionType.get(ActionNames.left))) {
            return true;
        } else if (currentState == State.getState(1)
                && action.getActionType() == ActionType.get(ActionNames.right)) {
            return true;
        } else if (currentState == State.getState(2)
                && action.getActionType() == ActionType.get(ActionNames.left)) {
            return true;
        }
        return false;
    }

    public Reward nextState(PrimitiveAction action) {

        delay = action.getLatency() + getISI();
        newTrial();

        if (currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.right)) {
            currentState = State.getState(1);
            return rewardValue.zero();
        } else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.left))) {
            currentState = State.getState(2);
            return rewardValue.zero();
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.right))) {
            currentState = State.getState(0);
            return rewardValue.getR1();
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.left))) {
            currentState = State.getState(0);
            return rewardValue.getR2();
        } else {
            throw new BRLException("Illigal state action");
        }

    }
}
