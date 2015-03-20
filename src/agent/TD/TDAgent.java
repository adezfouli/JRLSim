package agent.TD;

import action.ActionSelection;
import action.ActionType;
import agent.*;
import env.Cue;
import env.Environment;

public class TDAgent extends QAgent {


    private static final long serialVersionUID = -3065980564040399691L;

    private double delta;

    public TDAgent(Environment environment, ActionSelection actionSelection, Cue states[], ActionType[] actionTypes, AgentInternalConstants constants) {
        super(environment, actionSelection, states, actionTypes, constants);
    }

    public TDAgent(Environment environment, ActionSelection actionSelection, AgentInternalConstants constants) {
        super(environment, actionSelection, constants);
    }


    @Override
    public QValue getInitQ() {
        return new TD_QValue(this, 0);
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public void setQValue(double value, Cue prev, ActionType actionType) {
        ((TD_QValue) getQValue(resolveState(new Cue[]{prev}), getPrimitiveAction(actionType))).setEmu(value);
    }
}
