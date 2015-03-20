package action;

import agent.Action;
import agent.State;

import java.io.Serializable;

public class PrimitiveAction extends Action implements Serializable{

	private static final long serialVersionUID = 3765113884036156500L;

	private ActionType actionType;

    public PrimitiveAction(ActionType actionType, int ordinal) {
		super(ordinal);
		this.actionType = actionType;
        this.ordinal = ordinal;
		isExploratory = false;

	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionTypeType) {
		this.actionType = actionTypeType;
	}

    @Override
    public boolean isTerminated(State state) {
        return true;
    }

    @Override
    public PrimitiveAction getPrimitiveAction() {
        return this;
    }

    @Override
    public String toString() {
        return actionType.toString();
    }
}
