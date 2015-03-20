package agent;

import action.ActionSelection;
import action.ActionType;
import env.Cue;
import env.Environment;

import java.util.HashMap;
import java.util.Map;

public abstract class QAgent extends Agent {

    private QValue[][] q;

    /**
     * Initializes internal variables.
     *
     * @param environment     The environment that the agent is going to be simulated in.
     * @param actionSelection The component that the agent uses for action selection.
     * @param parameterValues Values of the agent's internal parameters.
     */
    public QAgent(Environment environment, ActionSelection actionSelection, AgentInternalConstants parameterValues) {
        super(environment, actionSelection, parameterValues);

        q = new QValue[getNumberOfStates()][getNumberOfActions()];
    }

    /**
     * Initializes internal variables.
     *
     * @param environment     The environment that the agent is going to be simulated in.
     * @param actionSelection The component that the agent uses for action selection.
     * @param parameterValues Values of the agent's internal parameters.
     */
    public QAgent(Environment environment, ActionSelection actionSelection,
                  Cue[] states, ActionType[] actionTypes, AgentInternalConstants parameterValues) {
        super(environment, actionSelection, parameterValues);

        for (ActionType actionType : actionTypes) {
            addAction(actionType);
        }

        for (Cue state : states) {
            addState(state);
        }

        q = new QValue[getNumberOfStates()][getNumberOfActions()];
    }

    public int getOrdinalState(State state){

        return state.ordinal();
    }

    public int getOrdinalAction(Action action){

        return action.ordinal();
    }


    /**
     * This function is supposed to return initial value for Q-values.
     *
     * @return initial value of a Q value.
     */
    public abstract QValue getInitQ();

    public QValue getQValue(State state, Action action) {
        return q[getOrdinalState(state)][getOrdinalAction(action)];
    }

    @Override
    public Map<Action, QValue> actionQValues(State state) {

        Map<Action, QValue> values = new HashMap<Action, QValue>();
        for (Action action : getActions()) {
            values.put(action, getQValue(state, action));
        }
        return values;
    }

    protected void initStateActionValues(State state) {
        for (Action action : getActions()) {
            if (getEnvironment().isPossible(action)
                    && getQValue(state, action) == null) {
                q[getOrdinalState(state)][getOrdinalAction(action)] = getInitQ();
            }
        }
    }
}
