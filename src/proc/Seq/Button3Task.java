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

public class Button3Task extends Procedure {

    private State twoNextState;
    private boolean randomTrial;

    public Button3Task(Agent agent, RewardValue rewardValue, int maxNumberOfTrials, double ISI, boolean randomTrial) {
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
            return new Cue[]{Cue.get(CueNames.reward0)};
        else if (currentState == State.getState(5))
            return new Cue[]{Cue.get(CueNames.reward1)};
        else if (currentState == State.getState(6))
            return new Cue[]{Cue.get(CueNames.reward2)};
        throw new BRLException("Illegal State");
    }

    @Override
    public boolean isPossible(State currentState, PrimitiveAction action) {

        if ((
                currentState == State.getState(1) ||
                        currentState == State.getState(2) ||
                        currentState == State.getState(3)) &&
                (action.getActionType() == ActionType.get(ActionNames.Button0) ||
                        action.getActionType() == ActionType.get(ActionNames.Button1) ||
                        action.getActionType() == ActionType.get(ActionNames.Button2)


                ))
            return true;
        if ((
                currentState == State.getState(4) ||
                        currentState == State.getState(5) ||
                        currentState == State.getState(6)
        )
                && action.getActionType() == ActionType.get(ActionNames.rewardReceived))
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
                        currentState == State.getState(3)))
            reward = rewardValue.zero();

        if ((
                currentState == State.getState(4) ||
                        currentState == State.getState(5) ||
                        currentState == State.getState(6)))
            reward = rewardValue.foodEating();

        if (currentState == State.getState(1)
                && action.getActionType() == ActionType.get(ActionNames.Button0)) {
            currentState = State.getState(4);
        } else if (currentState == State.getState(2)
                && action.getActionType() == ActionType.get(ActionNames.Button1)) {
            currentState = State.getState(5);
        } else if (currentState == State.getState(3)
                && action.getActionType() == ActionType.get(ActionNames.Button2)) {
            currentState = State.getState(6);
        } else if (randomTrial) {
            currentState = State.getState((int) (3 * Math.random()) + 1);
        } else if (currentState == State.getState(4)) {
            currentState = State.getState(2);
        } else if (currentState == State.getState(5)) {
            currentState = State.getState(3);
        } else if (currentState == State.getState(6)) {
            currentState = State.getState(1);
        } else currentState = State.getState(1);

        return reward;
    }

}
