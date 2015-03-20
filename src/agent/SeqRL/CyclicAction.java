package agent.SeqRL;

import action.PrimitiveAction;
import agent.Action;
import agent.State;
import proc.BRLException;

import java.util.ArrayList;

public class CyclicAction extends SeqAction {

    public CyclicAction(Action action) {
        if (action instanceof PrimitiveAction)
            getPrimitiveActions().add((PrimitiveAction)action);
        else if (action instanceof SeqAction && ! (action instanceof CyclicAction) )
            getPrimitiveActions().addAll(((SeqAction)action).getPrimitiveActions());
        else
            throw new BRLException("Invalid action cycling");
    }

    @Override
    public boolean isTerminated(State state) {
        return false;
    }

    @Override
    public PrimitiveAction getPrimitiveAction() {
        if (currentIndex >= getPrimitiveActions().size())
            currentIndex = 0;
        return super.getPrimitiveAction();

    }

    @Override
    public String toString() {
        return "Cycle "  + super.toString();
    }

    @Override
    public boolean equalsToSeq(SeqAction seq) {
        if (!(seq instanceof CyclicAction))
            return false;
        else
            return super.equalsToSeq(seq);

    }
}
