package agent.BRL;

import agent.Action;
import agent.QAgent;
import env.Environment;
import action.ActionSelection;
import agent.AgentInternalConstants;
import agent.State;

import java.util.Map;

public abstract class BayesianAgent extends QAgent {

    private static final long serialVersionUID = 4671110592987416783L;

    public BayesianAgent(Environment environment,
                         ActionSelection actionSelection,
                         AgentInternalConstants parameterValues) {
        super(environment, actionSelection, parameterValues);
    }

    public abstract Map<Action, Double> calculateValueOfPerfectInformation(State state);

}