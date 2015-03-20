package action;

import agent.Action;
import agent.Agent;
import agent.State;
import proc.BRLException;

import java.util.HashMap;
import java.util.Map;

public class TimeBasedActionSelection extends ActionSelection{

    private ActionType[] actions;
    private ActionSelection actionSelection;

    public TimeBasedActionSelection(ActionType[] actions, ActionSelection actionSelection){
        this.actions = actions;
        this.actionSelection = actionSelection;
    }

    @Override
    public void setAgent(Agent agent) {
        super.setAgent(agent);
        this.actionSelection.setAgent(agent);
    }

    @Override
    public Action selectAction(State state) {
        if (agent.getNumOfActionSelections() > actions.length){
            throw new BRLException("No action is mapped to this time: " + agent.getNumOfActionSelections());
        }
        else return agent.getPrimitiveAction(actions[agent.getNumOfActionSelections()]);
    }

    @Override
    public Map<Action, Double> getProbabilityOfSelectingAction(State state) {
        return actionSelection.getProbabilityOfSelectingAction(state);
    }
}
