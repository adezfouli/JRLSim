package action;

import agent.Action;
import agent.QValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GreedyActionSelection extends ValueBasedActionSelection {

    private static final long serialVersionUID = 999993168036764291L;

    private double epsilon;

    public GreedyActionSelection(double epsilon) {
        super();
        this.epsilon = epsilon;
    }

    @Override
    public Action selectAction(Map<Action, Double> values) {
        double bestValue = Double.NEGATIVE_INFINITY;
        ArrayList<Action> maxSet = new ArrayList<Action>();
        ArrayList<Action> possibleSet = new ArrayList<Action>();
        for (Action action : values.keySet()) {
            possibleSet.add(action);
            if (values.get(action) > bestValue) {
                bestValue = values.get(action);
                maxSet.clear();
                maxSet.add(action);
            } else {
                if (values.get(action) == bestValue) {
                    maxSet.add(action);
                }
            }
        }

        Action selectedAction = null;
        if (Math.random() > epsilon || possibleSet.size() == 1) {
            Action action = maxSet.get((int) (maxSet.size() * Math.random()));
            action.setExploratory(false);
            selectedAction = action;
        } else {
            int index = (int) (possibleSet.size() * Math
                    .random());
            Action action = possibleSet.get(index);
            if (maxSet.contains(action))
                action.setExploratory(false);
            else
                action.setExploratory(true);

            selectedAction = action;
        }

//        if (possibleSet.size() == 1)
//            selectedAction.setLatency(0.1);
//        else
            selectedAction.setLatency(0.4);

        return selectedAction;
    }

    public Map<Action, Double> getProbabilityOfSelectingAction(Map<Action, QValue> values) {

        double bestValue = -Double.MAX_VALUE;
        ArrayList<Action> maxSet = new ArrayList<Action>();
        ArrayList<Action> possibleSet = new ArrayList<Action>();
        for (Action action : values.keySet()) {
            if (values.get(action) != null) {
                possibleSet.add(action);
                if (values.get(action).Emu() > bestValue) {
                    bestValue = values.get(action).Emu();
                    maxSet.clear();
                    maxSet.add(action);
                } else if (values.get(action).Emu() == bestValue) {
                    maxSet.add(action);
                }
            }
        }
        Map<Action, Double> probs = new HashMap<Action, Double>();
        double x = 1D / possibleSet.size() * epsilon;
        double y = 1D / maxSet.size() * (1 - epsilon);
        for (Action action : possibleSet) {
            probs.put(action, x);
        }

        for (Action action : maxSet) {
            probs.put(action, probs.get(action) + y);
        }
        return probs;
    }
}