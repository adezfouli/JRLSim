package proc.omision;

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


public class SimpleRewardTask extends Procedure {

    public SimpleRewardTask(Agent agent, RewardValue rewardValue,
                            int maxNumberOfTrials) {
        super(agent, rewardValue, maxNumberOfTrials);
        setITI(100);
        setISI(100);
    }


    public static void init() {
        Cue.get(CueNames.InitialState);
        ActionType.get(ActionNames.left);
        ActionType.get(ActionNames.right);
    }

    @Override
    public Cue[] getCue() {
        if (currentState == State.getState(0))
            return new Cue[]{Cue.get(CueNames.InitialState)};

        throw new BRLException("Illegal State");
    }

    @Override
    public boolean isPossible(State currentState, PrimitiveAction action) {

        if (currentState == State.getState(0)
                && (action.getActionType() == ActionType.get(ActionNames.right) || action
                .getActionType() == ActionType.get(ActionNames.left)))
            return true;
        return false;
    }

    public Reward nextState(PrimitiveAction action) {

        newTrial();
        delay = getITI();
        if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.right)) {
            return rewardValue.getR1();
        } else if (currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.left)) {
            return rewardValue.getR2();
        } else {
            throw new BRLException("Illigal state action");
        }
    }
}
