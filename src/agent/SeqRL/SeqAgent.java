package agent.SeqRL;

import action.*;
import agent.*;
import agent.TDAVG.TDAVG_QValue;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import env.Cue;
import env.Environment;
import reward.Reward;
import sim.SEQ.Seq_Constants;

import java.util.HashMap;
import java.util.Map;


public class SeqAgent extends Agent {

    private Table<State, Action, QValue> q;

    private HashMap<State, Table<Action, Action, Double>> adv;
    private HashMap<State, Table<Action, Action, SeqAction>> chunkIndex;

    private Action ppAction;
    private State ppState;

    private double seqLR;

    /**
     * Initializes internal variables.
     *
     * @param environment     The environment that the agent is going to be simulated in.
     * @param actionSelection The component that the agent uses for action selection.
     * @param states          initial state of the agent
     * @param actions         primitives actions of the agent
     * @param parameterValues Values of the agent's internal parameters.
     */
    public SeqAgent(Environment environment, ActionSelection actionSelection, Cue[] states, ActionType[] actions, AgentInternalConstants parameterValues) {
        super(environment, actionSelection, parameterValues);


        for (ActionType action : actions) {
            addAction(action);
        }

        for (Cue state : states) {
            addState(state);
        }

        q = HashBasedTable.create();
        adv = new HashMap<State, Table<Action, Action, Double>>();
        chunkIndex = new HashMap<State, Table<Action, Action, SeqAction>>();
        seqLR = ((Seq_Constants)parameterValues).getSeqLR();

    }


    protected void initStateActionValues(State state) {
        for (Action action : getActions()) {
            if (action instanceof PrimitiveAction && getEnvironment().isPossible(action)
                    && getQValue(state, action) == null && !(state instanceof AuxiliaryState)) {
                if (getQValue(state, action) == null && actionTakings.get(state, action) == null) {
                    q.put(state, action, getInitQ());
                }
            }
        }
    }

    @Override
    protected void updateValue(Reward r, double transitionDelay) {

        super.updateValue(r, transitionDelay);

        if (getActionTakingProbability(prevState, prevAction) != 0)
            chunkProcessing(r, transitionDelay);

        ppAction = getPrevAction();
        ppState = getPrevState();

    }

    private void chunkProcessing(Reward r, double transitionDelay) {
        if (ppAction != null && ppState != null) {

            State ppState2 = ppState;

            if (ppAction instanceof SeqAction) {
                ppState2 = ((SeqAction) ppAction).getAuxiliaryState().getPpState();
            }


            if (adv.get(ppState2) == null)
                adv.put(ppState2, HashBasedTable.<Action, Action, Double>create());

            Double oldAdv = adv.get(ppState2).get(ppAction, prevAction);

            oldAdv = oldAdv == null ? -2 : oldAdv;

            double v = nextActionProbability(ppState, ppAction, prevAction) / getActionTakingProbability(prevState, prevAction);

            double delta = v * computeActorError(r.getMagnitude(), transitionDelay);


            if (Double.isInfinite(delta)){
                return;
            }

            double improvement = oldAdv * (1 - seqLR) + delta * seqLR;

            adv.get(ppState2).put(ppAction, prevAction, improvement);

            for (Action action : getActions()) {
                if (getQValue(prevState, action) == null) {
                    if (!(prevState instanceof AuxiliaryState)) {
                        adv.get(ppState2).put(ppAction, action, Double.NEGATIVE_INFINITY);
                    }
                }
            }

            if (currentState != prevState && ppState2 != prevState && ppAction != prevAction) {
//                if (!(ppAction instanceof PrimitiveAction)
//                        || ((PrimitiveAction) ppAction).getActionType() != ActionType.get(ActionNames.rewardReceived)
//                        )
                if (-improvement < getNonExploratoryAverageReward() * 0.3) {
                    SeqAction seqAction = null;
                    if (ppState2 == prevState && ppState2 == currentState && ppAction == prevAction) {
                        seqAction = SeqAction.getCyclicAction(ppAction);
                    } else
                        seqAction = SeqAction.getSeqAction(ppAction, prevAction);

                    if (chunkIndex.get(ppState2) == null) {
                        chunkIndex.put(ppState2, HashBasedTable.<Action, Action, SeqAction>create());
                    }

                    if (!containsSeq(ppState2, seqAction) && chunkIndex.get(ppState2).get(ppAction, prevAction) == null) {
                        addAction(seqAction);

                        seqAction.setAuxiliaryState(getState(ppState2, seqAction));
                        chunkIndex.get(ppState2).put(ppAction, prevAction, seqAction);

                        actionTakings.put(ppState2, seqAction, 1);
                        T.get(ppState2).put(seqAction, seqAction.getAuxiliaryState(), 1D);
                        actionTakings.put(seqAction.getAuxiliaryState(), seqAction, 1);
                        T.put(seqAction.getAuxiliaryState(), HashBasedTable.<Action, State, Double>create());
                        T.get(seqAction.getAuxiliaryState()).put(seqAction, currentState, 1D);

                        double re = sR.get(prevState);
                        if (ppAction instanceof SeqAction)
                            re += sR.get(((SeqAction) ppAction).getAuxiliaryState());
                        if (prevAction instanceof SeqAction)
                            re += sR.get(((SeqAction) prevAction).getAuxiliaryState());

                        sR.put(seqAction.getAuxiliaryState(), re);

                        double del = D.get(prevState, prevAction);
                        if (ppAction instanceof SeqAction)
                            del += D.get(((SeqAction) ppAction).getAuxiliaryState(), ppAction);
                        if (prevAction instanceof SeqAction)
                            del += D.get(((SeqAction) prevAction).getAuxiliaryState(), prevAction);

                        D.put(seqAction.getAuxiliaryState(), seqAction, del - 0.3);
                        D.put(ppState2, seqAction, D.get(ppState2, ppAction));

                        q.put(ppState2, seqAction, getInitQ(getQValue(ppState2, ppAction).Emu() + 0.3 * getNonExploratoryAverageReward()));
                        q.put(seqAction.getAuxiliaryState(), seqAction, getInitQ(getQValue(prevState, prevAction).Emu()));

                        q.remove(ppState2, ppAction);

                        System.out.println("trial: " + getCurrentTrial() + " chunk added: " + ppState2 + " " + ppAction + "<> " + prevAction + " seq: " + seqAction + " improvement " + -improvement);
                    }
                } else {
                    if (chunkIndex.get(ppState2) != null && -improvement > getNonExploratoryAverageReward() * 0.6) {
                        SeqAction action = chunkIndex.get(ppState2).get(ppAction, prevAction);
                        if (action != null) {
                            removeChunk(ppState2, action);
                        }
                    }
                }
            }
        }
    }

    private void removeChunk(State ppState2, SeqAction action) {
        q.put(ppState2, action.getPpAction(), new TDAVG_QValue(this, getStateActionValue(ppState2, action)));
        q.remove(ppState2, action);
        chunkIndex.get(ppState2).remove(action.getPpAction(), action.getPreAction());
//        q.put(ppState2,action.getPpAction(), new TDAVG_QValue(this, 0));
//        System.out.println(valueOfStateAction(ppState2, action.getPpAction()));
//        q.put(ppState2, action.getPpAction(), new TDAVG_QValue(this, valueOfStateAction(ppState2, action.getPpAction())));
        System.out.println("trial: " + getCurrentTrial() + " chunk removed: " + ppState2 + " " + action.getPpAction() + "<> " + action.getPreAction());
    }


    public AuxiliaryState getState(State state, SeqAction action) {
        AuxiliaryState auxState = new AuxiliaryState(state, "Aux: " + state + " " + action);
        return (AuxiliaryState) addState(auxState);
    }

    boolean containsSeq(State state, SeqAction seq) {
        for (Action a : getActions()) {
            if (getQValue(state, a) != null)
                if (a instanceof SeqAction) {
                    if ((seq).equalsToSeq((SeqAction) a))
                        return true;
                }
        }
        return false;
    }

    @Override
    public QValue getQValue(State state, Action action) {
        return q.get(state, action);
    }


    protected double computeActorError(double r, double delay) {
        double VNext = getStateActionValue(getCurrentState(), getMaxValuedAction(getCurrentState()));
        double VPrev = getStateActionValue(getPrevState(), getMaxValuedAction(getPrevState()));
        double rewardRate = getNonExploratoryAverageReward() + getBasalLevelDeviation();


        return r + VNext - rewardRate * delay - VPrev;
    }

    public Map<Action, Double> actionValues(State s) {
        Map<Action, Double> values = new HashMap<Action, Double>();

        for (Action a : getActions()) {
            if (getQValue(s, a) != null)
                values.put(a, getStateActionValue(s, a));
//                values.put(a, getQValue(state, a).Emu());
        }
        return values;
    }

    public double getStateActionValue(State state, Action a) {
        return treeBasedValueOfStateAction(state, a, 3);
    }

    @Override
    public Action selectAction(State currentState) {

        if (currentState instanceof AuxiliaryState) {
            if (currentAction != null && !currentAction.isTerminated(null)) {
                return currentAction;
            }
        }

        if (currentAction != null && currentAction.isTerminated(null))
            currentAction.reset();

        Action action = super.selectAction(currentState);

//        if (chunkIndex.get(currentState) != null)
//        for (Action a: getActions()){
//            if (chunkIndex.get(currentState).get(action, a) != null){
//                action = chunkIndex.get(currentState).get(action, a);
//            }
//        }
//
        if (action instanceof SeqAction){
            SeqAction seqAction = (SeqAction) action;
            if (adv.get(currentState) != null)
                if (-adv.get(currentState).get(seqAction.getPpAction(), seqAction.getPreAction()) > 0.5 * getNonExploratoryAverageReward()){
                    removeChunk(currentState, seqAction);
                    return selectAction(currentState);
                }

        }

        return action;
    }

    @Override
    public PrimitiveAction learnAndSelectAction(Reward r, Cue[] cue, double transitionDelay) {
        return super.learnAndSelectAction(r, cue, transitionDelay);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public State resolveState(Cue[] cue) {
        if (prevAction != null && !prevAction.isTerminated(null) && prevAction instanceof SeqAction)
            return ((SeqAction) prevAction).getAuxiliaryState();
        return super.resolveState(cue);

    }

    public QValue getInitQ() {
        return new TDAVG_QValue(this, 0);
    }

    public QValue getInitQ(double mean) {
        return new TDAVG_QValue(this, mean);
    }

    public double nextActionProbability(State s, Action a, Action nextAction) {
        double prob = 0;
        for (State state : getStates()) {
            prob += getTransitionProbability(s, a, state) * getActionTakingProbability(state, nextAction);
        }
        return prob;
    }

    public Double getC(State s, Action a, Action prevA) {
        if (adv.get(s) == null)
            return -2D;
        else
            return adv.get(s).get(a, prevA);
    }
}
