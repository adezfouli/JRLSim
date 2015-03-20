package action;

import agent.Action;
import agent.QValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SoftMaxActionSelection extends ValueBasedActionSelection {

    private static final long serialVersionUID = -1986151763168390135L;

    private double temperature;

    public SoftMaxActionSelection(double temperature) {
        super();
        this.temperature = temperature;
    }

    protected Action selectAction(Map<Action, Double> values) {

        double bestValue = Double.NEGATIVE_INFINITY;
        TreeMap<Double, Action> actionProbSet = new TreeMap<Double, Action>();
        ArrayList<Action> maxSet = new ArrayList<Action>();
        double totalSum = 0;
        for (Action action : values.keySet()) {
            if (values.get(action) != null) {

                double d = Math.exp(values.get(action) * temperature);

                actionProbSet.put(totalSum, action);
                totalSum += d;
                if (values.get(action) > bestValue) {
                    bestValue = values.get(action);
                    maxSet.clear();
                    maxSet.add(action);
                } else if (values.get(action) == bestValue) {
                    maxSet.add(action);
                }
            }
        }

        Action action = null;
        action = actionProbSet.lowerEntry(Math.random() * totalSum).getValue();

        if (maxSet.contains(action)) {
            action.setExploratory(false);
        } else {
            action.setExploratory(true);
        }

//        if (actionProbSet.size() == 1)
//            action.setLatency(0.1);
//        else
        action.setLatency(0.4);

        return action;
    }

    @Override
    public Map<Action, Double> getProbabilityOfSelectingAction(Map<Action, QValue> values) {
        Map<Action, Double> probs = new HashMap<Action, Double>();

        double totalSum = 0;
        for (Action action : values.keySet()) {
            if (values.get(action) != null) {
                double d = Math.exp(values.get(action).Emu() * temperature);
                totalSum += d;
            }
        }
        for (Action action : values.keySet()) {
            if (values.get(action) != null) {
                double d = Math.exp(values.get(action).Emu() * temperature);
                probs.put(action, d / totalSum);
            }
        }
        return probs;
    }
}
