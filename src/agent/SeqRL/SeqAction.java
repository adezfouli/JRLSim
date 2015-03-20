package agent.SeqRL;

import agent.Action;
import action.PrimitiveAction;
import agent.State;
import proc.BRLException;

import java.util.ArrayList;

public class SeqAction extends Action {

    private ArrayList<PrimitiveAction> primitiveActions;
    protected int currentIndex;
    private AuxiliaryState auxiliaryState;
    private Action ppAction;
    private Action preAction;


    public SeqAction() {
        super();
        this.primitiveActions = new ArrayList<PrimitiveAction>();
    }

    static public SeqAction getSeqAction(Action action1, Action action2) {

        SeqAction action = new SeqAction();
        action.ppAction = action1;
        action.preAction = action2;

        if (action1 instanceof PrimitiveAction)
            action.primitiveActions.add((PrimitiveAction) action1);
        else if (action1 instanceof SeqAction)
            action.primitiveActions.addAll(((SeqAction) action1).getPrimitiveActions());
        if (action2 instanceof PrimitiveAction)
            action.primitiveActions.add((PrimitiveAction) action2);
        else if (action2 instanceof SeqAction)
            action.primitiveActions.addAll(((SeqAction) action2).getPrimitiveActions());
        else
            throw new BRLException("Invalid action chunk");

        return action;

    }

    public static SeqAction getCyclicAction(Action action) {
        return new CyclicAction(action);
    }


    @Override
    public boolean isTerminated(State state) {
        return currentIndex >= primitiveActions.size();
    }

    @Override
    public PrimitiveAction getPrimitiveAction() {
        if (currentIndex == 0)
            primitiveActions.get(currentIndex).setLatency(getLatency());
        else
            primitiveActions.get(currentIndex).setLatency(0.1);
        return primitiveActions.get(currentIndex++);
    }

    public ArrayList<PrimitiveAction> getPrimitiveActions() {
        return primitiveActions;
    }

    @Override
    public void reset() {
        currentIndex = 0;
    }

    public void setAuxiliaryState(AuxiliaryState auxiliaryState) {
        this.auxiliaryState = auxiliaryState;
    }

    @Override
    public String toString() {
        String name = "SEQ-";

        for (PrimitiveAction primitiveAction : primitiveActions) {
            name += "-" + primitiveAction.toString();
        }
        return name;
    }

    public boolean equalsToSeq(SeqAction seq) {

        if (seq.getPrimitiveActions().size() != primitiveActions.size())
            return false;

        for (int i = 0; i < primitiveActions.size(); i++) {
              if (!primitiveActions.get(i).equals(seq.getPrimitiveActions().get(i)))
                  return false;
        }
        return true;
    }

    public AuxiliaryState getAuxiliaryState() {
        return auxiliaryState;
    }

    public Action getPpAction() {
        return ppAction;
    }

    public Action getPreAction() {
        return preAction;
    }
}
