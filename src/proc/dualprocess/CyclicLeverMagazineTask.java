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

public class CyclicLeverMagazineTask extends Procedure {

    private static final long serialVersionUID = 6532503110269194634L;

    static {
        Cue.get(CueNames.InitialState);
        Cue.get(CueNames.FoodAvailable);
    }

    static {
        ActionType.get(ActionNames.pressLever1);
        ActionType.get(ActionNames.enterMagazine);
    }

    public static void init() {
        Cue.get(CueNames.InitialState);
        Cue.get(CueNames.FoodAvailable);
        ActionType.get(ActionNames.pressLever1);
        ActionType.get(ActionNames.enterMagazine);
    }

    public CyclicLeverMagazineTask(Agent agent, RewardValue rewardValue,
                                   int maxNumberOfTrials) {
        super(agent, rewardValue, maxNumberOfTrials);
        setISI(1);
    }

    @Override
    public Cue[] getCue() {
        if (currentState == State.getState(0))
            return new Cue[]{Cue.get(CueNames.InitialState)};
        else if (currentState == State.getState(1))
            return new Cue[]{Cue.get(CueNames.FoodADelivered)};
        throw new BRLException("Illegal State");
    }

    @Override
    public boolean isPossible(State currentState, PrimitiveAction action) {
        if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            return true;
        } else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            return true;
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever1))) {
            return true;
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            return true;
        }
        return false;
    }

    public Reward nextState(PrimitiveAction action) {
        return nextState(action, rewardValue.foodEating(), rewardValue.zero());
    }

    public Reward nextState(PrimitiveAction action, Reward reward, Reward zeroReward) {
        delay = action.getLatency() + getISI();
        newTrial();

        if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.pressLever1)) {
            currentState = State.getState(1);
            return zeroReward;
        } else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(0);
            return zeroReward;
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever1))) {
            currentState = State.getState(0);
            return zeroReward;
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(0);
            return reward;
        } else {
            throw new BRLException("Illigal state action");
        }
    }
}
