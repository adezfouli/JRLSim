package logger;

import java.util.HashMap;
import java.util.Map;

import agent.Action;
import agent.SeqRL.AuxiliaryState;
import agent.SeqRL.SeqAction;
import agent.SeqRL.SeqAgent;
import agent.State;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jfree.data.xy.XYSeries;

import reward.Reward;
import agent.Agent;
import agent.TD.TDAgent;

public class LogData {

    public Table<State, Action, XYSeries> actionCount;

    public Map<State, XYSeries> deliberationTime;

    public Map<Integer, Reward> rewardList;

    public Table<State, Action, XYSeries> habitualQValue;

    public Table<State, Action, XYSeries> goalDirectedQValue;

    public Table<State, Action, XYSeries> var;

    public Table<State, Action, XYSeries> VPI;

    public Table<State, Action, XYSeries> V;

    public HashMap<State, Table<Action, Action, XYSeries>> C;

    public Map<State, XYSeries> stateValue;

    public XYSeries optimalLatency;

    public XYSeries delta;

    public XYSeries nonExploratoryAvgReward;

    public XYSeries costOfGoalBasedDecisionMaking;

    public Logger logger;

    @SuppressWarnings("unchecked")
    public LogData(Logger logger) {

        this.logger = logger;
        actionCount = HashBasedTable.create();
        deliberationTime = new HashMap();
        rewardList = new HashMap();
        habitualQValue = HashBasedTable.create();
        goalDirectedQValue = HashBasedTable.create();
        optimalLatency = new XYSeries(".Optimal_Latency");
        delta = new XYSeries(".delta");
        nonExploratoryAvgReward = new XYSeries(".Non_Explo_Avg_Reward");
        costOfGoalBasedDecisionMaking = new XYSeries(
                "cost_Of_GoalBased_DecisionMaking");
        stateValue = new HashMap();
        var = HashBasedTable.create();
        VPI = HashBasedTable.create();
        V = HashBasedTable.create();
        C = new HashMap<State, Table<Action, Action, XYSeries>>();
    }

    public void addData(double factor, int time) {

        Agent agent = logger.getAgent();

        addTo(costOfGoalBasedDecisionMaking, time, agent
                .costOfGoalBasedDecisionMaking()
                * factor);

        addTo(nonExploratoryAvgReward, time, agent.getBasalRewardLevel() * factor);

        if (agent instanceof TDAgent)
            addTo(delta, time, ((TDAgent) (agent)).getDelta() * factor);

        for (State s : agent.getStates()) {
//			Map<Action, Double> temp_V = new HashMap<Action, Double>();
//			Map<Action, Double> temp_VPI = new HashMap<Action, Double>();
//			if (agent instanceof BayesianAgent) {
//				temp_VPI = ((BayesianAgent) agent)
//						.calculateValueOfPerfectInformation(s);
//				// temp_V = ((BRL_Agent)
//				// agent).getTotalValueOfState(agent.addState(s),temp_VPI);
//				for (Action i : agent.getActions()) {
//					if (agent.getQValue(s, i) != null) {
//						if (VPI.get(s, i) == null)
//							VPI.put(s, i , new XYSeries(s + "_"
//									+ i + ".VPI"));
//						if (V.get(s, i) == null)
//							V.put(s, i, new XYSeries(s + "_"
//									+ i + ".V"));
//						if (!Double.isInfinite(temp_VPI.get(i)))
//							addTo(VPI.get(s, i), time, temp_VPI.get(i) * factor);
//						else
//							throw new BRLException("In calculation of VPI");
//						if (!Double.isInfinite(temp_V.get(i)))
//							addTo(V.get(s, i), time, temp_V.get(i) * factor);
//					}
//				}
//			}
        }
        for (State s : agent.getStates()) {
            if (agent.getV().get(s) != null && agent.getV().get(s) != 0) {
                if (stateValue.get(s) == null)
                    stateValue.put(s, new XYSeries("value of " + s));
                addTo(stateValue.get(s), time, agent.getV().get(s) * factor);
            }
            for (Action a : agent.getActions()) {
                if (agent.getQValue(s, a) != null) {
                    if (habitualQValue.get(s, a) == null)
                        habitualQValue.put(s, a, new XYSeries(s + "_" + a + ".model-free"));

                    addTo(habitualQValue.get(s, a), time, (agent.getQValue(s, a)
                            .Emu())
                            * factor);

//                    if (goalDirectedQValue.get(s, a) == null)
//                        goalDirectedQValue.put(s, a, new XYSeries(s + "_" + a + ".model-based"));
//                    double v = agent
//                            .treeBasedValueOfStateAction(s, a, 3);
//                    if (!Double.isInfinite(v))
//                        addTo(goalDirectedQValue.get(s, a), time, v
//                                * factor);

//					if (agent.getQValue(s, a) instanceof MuVarQValue) {
//						if (var.get(s, a) == null)
//							var.put(s, a, new XYSeries(s + "_" + a + ".VAR"));
//						double varMu = ((MuVarQValue) agent.getQValue(s, a))
//								.VarMu();
//						addTo(var.get(s, a), time, varMu * factor);
//					}
                }

//                if (time % 50 == 0) {
//
//                if (agent instanceof SeqAgent) {
//                    for (Action prevA : agent.getActions()) {
//                        SeqAgent seqAgent = (SeqAgent) agent;
//                        if (!(s instanceof AuxiliaryState) &&
//                                (a instanceof SeqAction)
//                            &&
//                                (prevA instanceof SeqAction)
//                            &&
//                                seqAgent.getC(s, a, prevA) != null
//                            ) {
//                            if (C.get(s) == null) {
//                                C.put(s, HashBasedTable.<Action, Action, XYSeries>create());
//                            }
//                            if (C.get(s).get(a, prevA) == null)
//                                C.get(s).put(a, prevA, new XYSeries(s + "_" + a + "_" + prevA + ".adv"));
//                            addTo(C.get(s).get(a, prevA), time, (-seqAgent.getC(s, a, prevA))
//                                    * factor);
//                        } else if (C.get(s) != null && C.get(s).get(a, prevA) != null) {
//                            addTo(C.get(s).get(a, prevA), time, (Double.POSITIVE_INFINITY)
//                                    * factor);
//                        }
//                    }
//                }
//            }
        }
    }
    }

//	public void addToActions(double factor, int time) {
//		Agent agent = logger.getAgent();
//		int state = agent.getPrevState().ordinal();
//		for (int i = 0; i < agent.getNumberOfActions(); i++) {
//			if (agent.getQValue(state, i) != null) {
//				if (actionCount[state][i] == null)
//					actionCount[state][i] = new XYSeries(agent.addState(state)
//							+ "_" + ActionType.values()[i] + ".actionCount");
//				addTo(actionCount[state][i], time, agent
//						.getRatioOfActionSelection(state, i)
//						* factor);
//			}
//		}
//	}
//
//	public void setActions(PrimitiveAction action, double count, int time) {
//		Agent agent = logger.getAgent();
//		if (actionCount[agent.getCurrentState().ordinal()][action
//				.getActionType().ordinal()] == null)
//			actionCount[agent.getCurrentState().ordinal()][action
//					.getActionType().ordinal()] = new XYSeries(agent
//					.addState(agent.getCurrentState().ordinal())
//					+ "_"
//					+ ActionType.values()[action.getActionType().ordinal()]
//					+ ".actionCount");
//
//		actionCount[agent.getCurrentState().ordinal()][action.getActionType()
//				.ordinal()].add(time, count);
//	}

    public static void addTo(XYSeries series, double x, double value) {
        int index = series.indexOf(new Double(x));
        double preV = 0;
        if (index >= 0)
            preV = series.getY(index).doubleValue();
        // System.out.println(x + " " + index);
        // System.out.println(value);
        series.addOrUpdate((Double)new Double(x), (Double)(preV + new Double(+value)));
    }
}
