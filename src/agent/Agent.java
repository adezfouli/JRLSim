package agent;

import java.io.Serializable;
import java.util.*;

import action.PrimitiveAction;
import action.ActionType;
import action.ActionSelection;
import agent.SeqRL.AuxiliaryState;
import agent.TDAVG.TDAVG_QValue;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import proc.BRLException;
import reward.Reward;
import env.Cue;
import env.Environment;

public abstract class Agent implements Serializable {

    private static final long serialVersionUID = -6046624909344557888L;

    /**
     * Previous state of the agent
     */
    protected State prevState;
    /**
     * Last action that the agent has taken
     */
    protected Action prevAction;

    /**
     * The action selected to be executed
     */
    protected Action currentAction;

    /**
     * Agent that the agent learns from and does actions in
     */
    protected transient Environment environment;

    /**
     * PrimitiveAction selection component of the agent.
     */
    private ActionSelection actionSelection;

    protected State currentState;

    /**
     * Controls if the agent learns or not. It is by default true
     */
    protected boolean learningEnable;


    /**
     * Net total reward that the agent has gained. It includes costs such as
     * vigor costs.
     */
    protected double totalReward;

    /**
     * Total number of times that the agent has received reinforcer
     */
    protected int rewardCount;

    /**
     * Exponentially decreasing average reward
     */
    protected double expDecreasingAverageReward;

    /**
     * Exponentially decreasing average reward rate in non-exploratory actions
     */
    protected double nonExploratoryAverageReward;

    /**
     * Coefficient that determines portion of average reward that contributes to
     * reward basal level.
     */
    protected double rewardBasalLevelCoefficient;

    /**
     * Rate of updating the average reward
     */
    private double avgRewardLR;


    /**
     * Discounting factor in exponential discounting algorithm
     */
    protected double df;

    /**
     * Learning rate for updating Q values.
     */
    protected double lr;

    /**
     * Real time elapsed so far.
     */
    protected double elapsedTime;

    protected double delayLR;

    /**
     * T[i][j][k] is total number of transitions from state i with action j to
     * state k.
     */
    protected Map<State, Table<Action, State, Double>> T;

    /**
     * R[i][j] is exponentially decreasing average reward (with rate
     * lr)that the agent gains by taking action j in state i.
     */
    protected Table<State, Action, Double> R;


    /**
     * sR[i] is exponentially decreasing average reward (with rate
     * lr)that the agent gains by entering state i
     */
    protected Map<State, Double> sR;

    /**
     * D[i][j] is the average time for elapsed after taking action i in state j.
     */
    protected Table<State, Action, Double> D;


    /**
     * actionTakings[i][j] represents total number that the action i
     * has been taking in state j.
     */
    protected Table<State, Action, Integer> actionTakings;

    /**
     * Value of states at current time.
     */
    protected Map<State, Double> V;

    /**
     * Stores the time spent for decision-making in a trial
     */
    protected double timeSpentForDecisionMaking;

    public double costOfValueIteration;

    public double timeOfOneCalculation;

    public double deliberationTime;

    /**
     * This fiend stores cost of the last values iteration.
     */
    private int currentCostOfValueIteration;

    /**
     * Indicates if current trial up to now contains an exploratory action.
     */
    private boolean exploratoryTrial;

    private int currentTrial, numOfActionSelections;

//    private boolean[][] visited;

    /**
     * this variable represents deviation of reward basal level from its normal
     * value
     */
    private double basalLevelDeviation;

    private HashMap<State, Integer> stateVisits;

    private ArrayList<Action> actions;

    private ArrayList<State> states;

    /**
     * These variables are related to action sequence implementation
     */
    double temporalReward, temporalDelay;


    /**
     * Initializes internal variables.
     *
     * @param environment     The environment that the agent is going to be simulated in.
     * @param actionSelection The component that the agent uses for action selection.
     * @param parameterValues Values of the agent's internal parameters.
     */

    private int currentActionIndex = 0;
    private double timeOfMakingDecision = 0;

    public Agent(Environment environment, ActionSelection actionSelection,
                 AgentInternalConstants parameterValues) {

        super();
        this.environment = environment;
        this.actionSelection = actionSelection;
        actionSelection.setAgent(this);
        learningEnable = true;

        T = new HashMap<State, Table<Action, State, Double>>();
        R = HashBasedTable.create();
        D = HashBasedTable.create();
        actionTakings = HashBasedTable.create();
        stateVisits = new HashMap<State, Integer>();
        V = new HashMap<State, Double>();
        actions = new ArrayList<Action>();
        states = new ArrayList<State>();
        sR = new HashMap<State, Double>();

        // indicated whether the action selected is exploratory
        exploratoryTrial = false;
        currentTrial = 0;
        numOfActionSelections = 0;
        nonExploratoryAverageReward = 0.01;
        elapsedTime = 1;
        expDecreasingAverageReward = 0;

        // initializing the internal varibles of the model
        rewardBasalLevelCoefficient = parameterValues
                .rewardBasalLevelCoefficient();
        df = parameterValues.getDf();
        timeOfOneCalculation = parameterValues.getTimeOfOneCalculation();
        deliberationTime = parameterValues.getTimeOfDeliberation();
        lr = parameterValues.getLR();
        avgRewardLR = parameterValues.getAvgRewardLR();
        delayLR = parameterValues.getDelayLR();

        // these are variables for implementation of the action sequences.
        temporalReward = 0;
        temporalDelay = 0;

    }

    /**
     * Learns from the reward and selects the next action.
     *
     * @param r               The reward that the agent gains from its previous action.
     * @param cue             Cues that the agent observe now in its current state.
     * @param transitionDelay The time has been taking for transition from previous state to
     *                        current state.
     * @return selected action
     */
    public PrimitiveAction learnAndSelectAction(Reward r, Cue[] cue,
                                                double transitionDelay) {

        if (prevState != null) {
            temporalDelay += transitionDelay;
            temporalReward += r.getMagnitude();
            r.setMagnitude(temporalReward);
            transitionDelay = temporalDelay;
        }

        numOfActionSelections++;

        // Identifies current state from observed cues.
        currentState = resolveState(cue);

        if (prevState instanceof AuxiliaryState && currentState instanceof AuxiliaryState)
            learningEnable = false;
        else
            learningEnable = true;


        // init values
        initStateActionValues(currentState);


        // if it's not the first time that the agent learns and if learning is
        // enable.
        if (prevState != null && learningEnable) {

            // updating internal variables

            State s = prevState;
            Action a = prevAction;


            if (stateVisits.get(s) == null)
                stateVisits.put(s, 0);

            stateVisits.put(s, stateVisits.get(s) + 1);

            if (actionTakings.get(s, a) == null)
                actionTakings.put(s, a, 0);

            actionTakings.put(s, a, actionTakings.get(s, a) + 1);

            if (T.get(s) == null) {
                T.put(s, HashBasedTable.<Action, State, Double>create());
                T.get(s).put(a, currentState, 0D);
            }

            if (T.get(s).get(a, currentState) == null)
                T.get(s).put(a, currentState, 0D);

            T.get(s).put(a, currentState, T.get(s).get(a, currentState) + 1);

            if (R.get(s, a) == null)
                R.put(s, a, 0D);

            R.put(s, a, (1 - lr) * R.get(s, a) + lr * r.getMagnitude());


            if (sR.get(s) == null)
                sR.put(s, 0D);
            sR.put(s, (1 - lr) * sR.get(s) + lr * r.getMagnitude());


            if (D.get(s, a) == null)
                D.put(s, a, 1.4D);

            D.put(s, a, (1 - delayLR) * D.get(s, a) + delayLR * transitionDelay);


            rewardCount += 1;

            updateValue(r, transitionDelay);

            // updating internal variables
            totalReward += r.getMagnitude();

            expDecreasingAverageReward = Math.pow((1 - avgRewardLR),
                    transitionDelay)
                    * expDecreasingAverageReward + (r.getMagnitude()) * (avgRewardLR);

            if (!exploratoryTrial) {
                double currentMax = getMaxValue(currentState);
                double prevMax = getMaxValue(prevState);
                if (!Double.isInfinite(currentMax) && !Double.isInfinite(prevMax))
                    nonExploratoryAverageReward = Math.pow((1 - avgRewardLR),
                            1)
                            * nonExploratoryAverageReward + ((r.getMagnitude() + currentMax - prevMax) / transitionDelay
                    ) * (avgRewardLR);
            }
        }

        if (!(prevState instanceof AuxiliaryState) || !(currentState instanceof AuxiliaryState)) {
            temporalDelay = 0;
            temporalReward = 0;
        }

        // selects next action
        currentAction = selectAction(currentState);

        prevAction = currentAction;
        prevState = currentState;
        elapsedTime += transitionDelay;

        exploratoryTrial = currentAction.isExploratory();

        PrimitiveAction primitiveAction = currentAction.getPrimitiveAction();
        timeOfMakingDecision = primitiveAction.getLatency();

        return primitiveAction;
    }

    protected abstract void initStateActionValues(State state);

    protected void updateValue(Reward r, double transitionDelay) {
        getQValue(prevState, prevAction)
                .update(r, getQValue(currentState, currentAction), df, transitionDelay);
    }


    /**
     * Register a primitive action in the actions list
     *
     * @param actionType The name of the action.
     * @return the created action
     */
    public PrimitiveAction addAction(ActionType actionType) {

        PrimitiveAction action = getPrimitiveAction(actionType);
        actions.add(action);
        return action;

    }

    public PrimitiveAction getPrimitiveAction(ActionType actionType) {
        for (Action action : actions) {
            if (action instanceof PrimitiveAction)
                if (((PrimitiveAction) action).getActionType().equals(actionType))
                    return (PrimitiveAction) action;
        }
        currentActionIndex++;
        return new PrimitiveAction(actionType, currentActionIndex - 1);
    }

    /**
     * Register an action in the action list
     *
     * @param action The name of the action.
     * @return the created action
     */
    public Action addAction(Action action) {
        currentActionIndex++;
        action.setOrdinal(currentActionIndex - 1);
        actions.add(action);
        return action;
    }

    public void removeAction(Action action) {
        actions.remove(action);
    }

    public State addState(Cue cue) {

        for (State value : states) {
            if (value instanceof SimpleState)
                if (((SimpleState) value).getCue() == cue)
                    return value;
        }
        SimpleState state = new SimpleState(cue);
        return addState(state);

    }

    public State addState(State state) {
        state.ordinal = states.size();
        states.add(state);
        return state;
    }


    public double treeBasedValueOfStateAction(State state, Action action, int depthOfSearch) {
        currentCostOfValueIteration = 0;
        double valueIteration = fwdTreeValueIteration(state, action, 1, depthOfSearch);
        costOfValueIteration = (1 - lr) * costOfValueIteration
                + lr * getCurrentTimeOfValueIteration();
        return valueIteration;
    }

    protected double valueIteration(State state, Action action,
                                    int currentDepth, int depthOfSearch) {

        if (currentDepth > depthOfSearch)
            if (getQValue(state, action) != null)
                return getQValue(state, action).Emu();
            else
                return 0;
        else
            currentDepth++;


        if (R.get(state, action) == null)
            R.put(state, action, 0D);

        double value = R.get(state, action);
        for (State nextSate : getStates()) {
            if (getTransitionProbability(state, action, nextSate) != 0) {
                value += getTransitionProbability(state, action, nextSate)
                        * getDiscountedValue(state, action, nextSate,
                        QValueIteration(nextSate, currentDepth, depthOfSearch));

                currentCostOfValueIteration++;
                timeSpentForDecisionMaking++;
            }
        }
        return value;
    }


    protected double fwdTreeValueIteration(State state, Action action,
                                           int currentDepth, int depthOfSearch) {

        if (getQValue(state, action) == null)
            return Double.NEGATIVE_INFINITY;

        if (currentDepth > depthOfSearch) {
            if (getQValue(state, action) != null)
                return getQValue(state, action).Emu();
            else
                return Double.NEGATIVE_INFINITY;
        } else
            currentDepth++;


        if (sR.get(state) == null)
            sR.put(state, 0D);

        double value = sR.get(state);

        for (State nextState : getStates()) {
            if (getTransitionProbability(state, action, nextState) != 0) {

                value += getTransitionProbability(state, action, nextState)
                        * getDiscountedValue(state, action, nextState,
                        QValueIteration(nextState, currentDepth, depthOfSearch));

                currentCostOfValueIteration++;
                timeSpentForDecisionMaking++;
            }
        }
        return value;
    }


    public double getDiscountedValue(State state, Action action, State nextState, double nextStateValue) {
//        System.out.println("state: "  + state + action + " " + D.get(state, action));
        return nextStateValue - D.get(state, action) * getNonExploratoryAverageReward();
    }


    public double QValueIteration(State state, int currentDepth,
                                  int depthOfSearch) {
        ArrayList<Double> value = new ArrayList<Double>();
        for (Action action : getActions()) {
            if (actionTakings.get(state, action) != null
                    && actionTakings.get(state, action) != 0)
                value.add(fwdTreeValueIteration(state, action,
                        currentDepth, depthOfSearch));
            else
                value.add(Double.NEGATIVE_INFINITY);
        }
        double findMax = findMax(value);
//        if (Double.isInfinite(findMax))
//            findMax = 0;
        return findMax;
    }

    public Double getRewardOfState(State state) {
        return sR.get(state);
    }


    public Double getDelayOfTransition(State state, Action action) {
        return D.get(state, action);
    }

//    public double SARSAValueIteration(State state, int currentDepth,
//                                      int depthOfSearch) {
////		Double[] probs = actionSelection.getProbabilityOfSelectingAction(state);
//        Double[] probs = new Double[getNumberOfActions()];
//        // if (lastSelectedActionInTheState[state.ordinal()] == null)
//        // return 0;
//        probs[lastSelectedActionInTheState[state.ordinal()]] = 1D;
//        double value = 0;
//        for (ActionType action : getActions()) {
//            if (probs[action.ordinal()] != null) {
//                value += probs[action.ordinal()]
//                        * valueIteration(state, action, currentDepth,
//                        depthOfSearch);
//            }
//        }
//        return value;
//        // return valueIteration(state, ActionType.values()[
//        // lastSelectedActionInTheState[state.ordinal()]], currentDepth,
//        // depthOfSearch);
//
//    }

    public double findMax(double[] values) {

        double maxValue = Double.NEGATIVE_INFINITY;
        for (double value : values) {
            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }

    public double findMax(Collection<Double> values) {

        double maxValue = Double.NEGATIVE_INFINITY;
        for (double value : values) {
            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }


    public double getCurrentTimeOfValueIteration() {
        return currentCostOfValueIteration * getTimeOfOneCalculation();
    }


    public Action getMaxValuedAction(State state) {
        double maxValue = Double.NEGATIVE_INFINITY;
        Action index = null;
        for (Action action : getActions()) {
            if (hasStateActionValue(state, action)) {
                double value = getStateActionValue(state, action);
                if (value > maxValue) {
                    maxValue = value;
                    index = action;
                }
            }
        }
        return index;
    }

    public double getMaxValue(State state) {
        return getStateActionValue(state, getMaxValuedAction(state));
    }


    public boolean hasStateActionValue(State state, Action action) {
        return getQValue(state, action) != null;
    }

    public double getStateActionValue(State state, Action action) {
        return getQValue(state, action).Emu();
    }

    public State resolveState(Cue[] cue) {
        for (State value : states) {
            if (value instanceof SimpleState)
                if (((SimpleState) value).getCue() == cue[0])
                    return value;
        }
        throw new BRLException("No state associated with this cue: " + Arrays.toString(cue));
    }

    public State resolvePrimitiveState(Cue[] cue) {
        for (State value : states) {
            if (value instanceof SimpleState)
                if (((SimpleState) value).getCue() == cue[0])
                    return value;
        }
        throw new BRLException("No state associated with this cue: " + Arrays.toString(cue));
    }


    /**
     * This function calculates point estimation of transition probability from
     * s1 with taking action a to s2.
     *
     * @param s1     source state
     * @param action the action has been taking in state s1
     * @param s2     destination state
     * @return returns the probability of transition
     */
    public double getTransitionProbability(State s1, Action action,
                                           State s2) {
        if (actionTakings.get(s1, action) == null
                || T.get(s1).get(action, s2) == null
                || actionTakings.get(s1, action) == 0)
            return 0;
        return T.get(s1).get(action, s2)
                / ((double) actionTakings.get(s1, action));
    }

    public double getActionTakingProbability(State s, Action action) {
        if (actionTakings.get(s, action) == null
                || stateVisits.get(s) == null)
            return 0D;

        return ((double) actionTakings.get(s, action)) / ((double) stateVisits.get(s));
    }

    public void setTransitionProbability(Cue s1, ActionType actionType,
                                         Cue s2, double prob) {

        T.get(s1).put(getPrimitiveAction(actionType), resolveState(new Cue[]{s2}), prob);
        actionTakings.put(resolveState(new Cue[]{s1}), getPrimitiveAction(actionType), 1);
    }

    public void setRewardOfState(Cue[] cue, double value) {
        State state = addState(cue[0]);
        sR.put(state, value);
    }

    public void resetQValues() {
        for (State state : getStates()) {
            for (Action action : getActions()) {
                if (getQValue(state, action) != null)
                    ((TDAVG_QValue) getQValue(state, action)).setEmu(0);
            }
        }
    }

    public int getNumberOfActions() {
        return getActions().size();
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public double getAverageRewardRate() {
        return totalReward / elapsedTime;
    }


    /**
     * Procedure calls this function when new trial starts.
     */
    public void newTrial() {
        environment.getLogger().logTrial();
//        exploratoryTrial = false;
        timeSpentForDecisionMaking = 0;
        currentTrial++;
    }


    public int getNumberOfStates() {
        return states.size();
    }

    public Map<Action, QValue> actionQValues(State state) {
        Map<Action, QValue> values = new HashMap<Action, QValue>();

        for (Action a : getActions()) {
            values.put(a, getQValue(state, a));
        }
        return values;
    }


    public Map<Action, Double> actionValues(State state) {
        Map<Action, Double> values = new HashMap<Action, Double>();

        for (Action a : getActions()) {
            if (getQValue(state, a) != null)
                values.put(a, getQValue(state, a).Emu());
        }
        return values;
    }


    abstract public QValue getQValue(State state, Action action);

    public State getCurrentState() {
        return currentState;
    }


    public void reset() {
//		prevAction = null;
        if (currentAction != null)
            currentAction.reset();

        if (prevAction != null)
            prevAction.reset();

        prevState = null;
        prevAction = null;
        currentAction = null;
        exploratoryTrial = false;
        timeSpentForDecisionMaking = 0;
//		currentTrial++;
//		numOfActionSelections++;
    }

    public State getState(int s) {
        return states.get(s);
    }

    public Action getAction(int a) {
        return actions.get(a);
    }


    public double getTotalReward() {
        return totalReward;
    }

    public double getBasalLevelDeviation() {
        return basalLevelDeviation;
    }

    public double getBasalRewardLevel() {
        return getBasalLevelDeviation() + getNonExploratoryAverageReward();
    }

    public void setTotalReward(double totalReward) {
        this.totalReward = totalReward;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setDf(double df) {
        this.df = df;
    }

    public double getDf() {
        return df;
    }

    public Action selectAction(State currentState) {
        return actionSelection.selectAction(currentState);
    }

    public Map<Action, Double> selectActionProbs() {
        return actionSelection.getProbabilityOfSelectingAction(currentState);
    }

    public double getExpDecreasingAverageReward() {
        return expDecreasingAverageReward;
    }

    public double getLr() {
        return lr;
    }

    public void setLr(double lr) {
        this.lr = lr;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    public void decTrial() {
        currentTrial--;
    }

    public Map<State, Double> getV() {
        return V;
    }

    public ActionSelection getActionSelection() {
        return actionSelection;
    }

    public void setActionSelection(ActionSelection actionSelection) {
        this.actionSelection = actionSelection;
        actionSelection.setAgent(this);
    }

    public void setExperiencedRewardDuringTransition(double value, Cue prev,
                                                     ActionType actionType) {
        R.put(resolveState(new Cue[]{prev}), getPrimitiveAction(actionType), value);
    }



    public double getTimeSpentForDecisionMaking() {
//        return timeSpentForDecisionMaking * getTimeOfOneCalculation();
        return timeOfMakingDecision;
    }

    public double getNonExploratoryAverageReward() {
        return nonExploratoryAverageReward;
    }

    public int getCurrentTrial() {
        return currentTrial;
    }

    public int getNumOfActionSelections() {
        return numOfActionSelections;
    }

    public State getPrevState() {
        return prevState;
    }

    public Action getPrevAction() {
        return prevAction;
    }

    public Action getCurrentAction() {
        return currentAction;
    }

    public double costOfGoalBasedDecisionMaking() {
        return nonExploratoryAverageReward * 0.3;
    }

    public void resetAverageReward() {
        expDecreasingAverageReward = 0;
        nonExploratoryAverageReward = 0;
    }


    public double getTimeOfOneCalculation() {
        return timeOfOneCalculation;
    }

    public boolean isExploratoryTrial() {
        return exploratoryTrial;
    }

    public void setTimeOfOneCalculation(double timeOfOneCalculation) {
        this.timeOfOneCalculation = timeOfOneCalculation;
    }

    public double getTimeOfHabitualDecisionMaking() {
        return 0;
    }
}