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

public class HickTask extends Procedure {
    private int size;

    public static void init(int size) {
        Cue.get(CueNames.InitialState);

        for (int i = 0; i < size; i++) {
            Cue.get(getNameForCue(i + 1));
        }

        for (int i = 0; i < size; i++) {
            ActionType.get(getNameForAction(i + 1));
        }

        ActionType.get(ActionNames.other);
    }

    public static String getNameForCue(int n) {
        return "hick_cue_" + n;
    }

    public static String getNameForAction(int n) {
        return "hick_action_" + n;
    }


    public HickTask(int size, Agent agent, RewardValue rewardValue,
                    int maxNumberOfTrials) {
        super(agent, rewardValue, maxNumberOfTrials);
//        setISI(1);

        // prior knowledge

        for (int i = 0; i < size; i++) {
            agent.setTransitionProbability(Cue.get(CueNames.InitialState), ActionType.get(ActionNames.other), Cue.get(getNameForCue(i + 1)), 1D / size);
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                agent.setTransitionProbability(Cue.get(getNameForCue(i + 1)), ActionType.get(getNameForAction(j + 1)), Cue.get(CueNames.InitialState), 1);
            }
        }


        this.size = size;
    }

    @Override
    public Cue[] getCue() {
        if (currentState == State.getState(0))
            return new Cue[]{Cue.get(CueNames.InitialState)};
        if (currentState.stateNumber > 0 && currentState.stateNumber <= size)
            return new Cue[]{Cue.get(getNameForCue(currentState.stateNumber))};
        throw new BRLException("Illegal State");
    }

    @Override
    public boolean isPossible(State currentState, PrimitiveAction action) {
        if (currentState == State.getState(0)
                && action.getActionType() == ActionType.get(ActionNames.other)) {
            return true;
        } else if (currentState != State.getState(0)
                && action.getActionType() != ActionType.get(ActionNames.other)) {
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
            currentState = State.getState((int) (Math.random() * size) + 1);
            return rewardValue.zero();
        } else if (currentState != State.getState(0) && action.getActionType() == ActionType.get(getNameForAction(currentState.stateNumber))) {
            currentState = State.getState(0);
            newTrial();
            return rewardValue.getR1();
        } else {
            currentState = State.getState(0);
            newTrial();
            return rewardValue.zero();
        }
    }
}
