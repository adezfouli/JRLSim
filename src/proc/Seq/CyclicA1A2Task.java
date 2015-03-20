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

public class CyclicA1A2Task extends Procedure {


    public CyclicA1A2Task(Agent agent, RewardValue rewardValue,
                          int maxNumberOfTrials, int ISI) {
        super(agent, rewardValue, maxNumberOfTrials);
        setISI(ISI);
    }

    @Override
    public Cue[] getCue() {
        if (currentState == State.getState(0))
            return new Cue[]{Cue.get(CueNames.FoodDelivered)};
        else if (currentState == State.getState(1))
            return new Cue[]{Cue.get(CueNames.F_EM)};
        else if (currentState == State.getState(2))
            return new Cue[]{Cue.get(CueNames.F_LP)};
        throw new BRLException("Illegal State");
    }

    @Override
    public boolean isPossible(State currentState, PrimitiveAction action) {
        if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.pressLever)) {
            return true;
        } else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            return true;
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever))) {
            return true;
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            return true;
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.pressLever))) {
            return true;
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            return true;
        }
        return false;
    }

    public Reward nextState(PrimitiveAction action) {
        return nextState(action, rewardValue.foodEating(), rewardValue.zero());
    }

    public Reward nextState(PrimitiveAction action, Reward positiveReward, Reward zeroReward) {

        Reward reward = null;
        delay = action.getLatency() + getISI();
        newTrial();


        if (currentState == State.getState(0))
            reward = positiveReward;
        else
            reward =  zeroReward;


        if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.pressLever)) {
            currentState = State.getState(2);
//            return zeroReward;
        } else if ((currentState == State.getState(0) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(1);
//            return zeroReward;
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.pressLever))) {
            currentState = State.getState(2);
//            return zeroReward;
        } else if ((currentState == State.getState(1) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(1);
//            return zeroReward;
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.pressLever))) {
            currentState = State.getState(2);
//            return zeroReward;
        } else if ((currentState == State.getState(2) && action.getActionType() == ActionType.get(ActionNames.enterMagazine))) {
            currentState = State.getState(0);
//            return reward;
        }
        else{
            throw new BRLException("Illigal state action");
        }

        return reward;
    }
}
