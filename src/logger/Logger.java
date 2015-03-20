package logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;

import agent.Action;
import agent.SeqRL.SeqAgent;
import agent.State;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import env.Cue;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import proc.BRLException;
import reward.Reward;
import agent.Agent;

public class Logger implements Serializable {

    private static final long serialVersionUID = -7120815700421844691L;

    private static final String TEX_OUTPUT = "D:\\Science\\Research\\Dual Process and Action Sequences\\articles\\data";

    private boolean isEnable = true;

    private LogData averageData;

    private Agent agent;

    private int numberOfSimulationSteps;

    private int lastLoggedTrial;

    private Map<Integer, Table<State, Action, Integer>> totalNumberOfTakingAction;

    private Table<Integer, State, Integer> totalNumberOfVisitOfState;

    private Table<Integer, State, Double> deliberationTime;
    private int currentSimulationStep;

    public Logger(int numberOfSimulationSteps) {
        super();
        this.numberOfSimulationSteps = numberOfSimulationSteps;
        setEnable(false);
        lastLoggedTrial = -1;
        averageData = new LogData(this);
        totalNumberOfTakingAction = new HashMap<Integer, Table<State, Action, Integer>>();
        totalNumberOfVisitOfState = HashBasedTable.create();
        deliberationTime = HashBasedTable.<Integer, State, Double>create();
        this.currentSimulationStep = 0;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public void startLogging(Agent agent) {
        this.agent = agent;
        this.currentSimulationStep++;
    }

    public int getCurrentSimulationStep() {
        return currentSimulationStep;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public void logStateActionReward(Action action, Reward reward,
                                     Cue[] cue, int simulationStep, boolean logByTrial) {

        int t = agent.getCurrentTrial();
//        State s = agent.getCurrentState();
        State s = agent.resolvePrimitiveState(cue);

//        Action a = agent.getCurrentAction();
        Action a = action;

        if (totalNumberOfVisitOfState.get(t, s) == null)
            totalNumberOfVisitOfState.put(t, s, 0);

        totalNumberOfVisitOfState.put(t, s, totalNumberOfVisitOfState.get(t, s) + 1);


        if (totalNumberOfTakingAction.get(t) == null)
            totalNumberOfTakingAction.put(t, HashBasedTable.<State, Action, Integer>create());

        if (totalNumberOfTakingAction.get(t).get(s, a) == null)
            totalNumberOfTakingAction.get(t).put(s, a, 0);

        totalNumberOfTakingAction.get(t).put(s, a, totalNumberOfTakingAction.get(t).get(s, a) + 1);

        if (deliberationTime.get(t, s) == null)
            deliberationTime.put(t, s, 0D);

        deliberationTime.put(t, s, deliberationTime.get(t, s) + agent.getTimeSpentForDecisionMaking());
    }

    public void logTrial() {
        lastLoggedTrial = agent.getCurrentTrial();
        averageData.addData(1D / numberOfSimulationSteps,
                lastLoggedTrial);
    }

    public void logStateActionSeries(Table<State, Action, XYSeries> serie, String yLabel) {
        XYSeriesCollection dataSet = new XYSeriesCollection();

        Collection<XYSeries> series = serie.values();
        for (XYSeries xySeries : series) {
            dataSet.addSeries(xySeries);
        }

        dataSet.addSeries(averageData.nonExploratoryAvgReward);

        drawASet(dataSet, yLabel);
    }


    public void logState(Map<State, XYSeries> serie, String yLabel) {
        XYSeriesCollection dataSet = new XYSeriesCollection();

        for (XYSeries xySeries : serie.values()) {
            dataSet.addSeries(xySeries);
        }

        drawASet(dataSet, yLabel);

    }


    private void drawASet(XYSeriesCollection dataSet, String yLabel) {
        // dataSet.addSeries(averageData.nonExploratoryAvgReward);
        dataSet.addSeries(averageData.costOfGoalBasedDecisionMaking);
        // dataSet.addSeries(averageData.delta);
        JFreeChart chart = ChartFactory.createXYLineChart("", "Time", yLabel,
                dataSet, PlotOrientation.VERTICAL, // data
                true, // include legend
                true, true);
        ChartPanel cp = new ChartPanel(chart);

        ApplicationFrame af = new ApplicationFrame("Test");
        af.setContentPane(cp);
        af.pack();
        RefineryUtilities.centerFrameOnScreen(af);
        af.setVisible(true);
    }

    public void logHabitualQValues() {
        if (averageData.habitualQValue != null)
            logStateActionSeries(averageData.habitualQValue, "habitualQ");

    }

    public void logGoalDirectedQValues() {
        if (averageData.goalDirectedQValue != null)
            logStateActionSeries(averageData.goalDirectedQValue, "goalDirectedQ");

    }

    public void logDelta() {
        XYSeriesCollection dataSet = new XYSeriesCollection();
        dataSet.addSeries(averageData.delta);
        drawASet(dataSet, "value");
    }

    public void logVIP() {
        if (averageData.VPI != null)
            logStateActionSeries(averageData.VPI, "VPI");

    }

    public void logV() {
        if (averageData.V != null)
            logStateActionSeries(averageData.V, "V");

    }

    public void logVar() {
        if (averageData.var != null)
            logStateActionSeries(averageData.var, "Var");
    }

    public void logActions() {
        if (averageData.actionCount != null)
            logStateActionSeries(averageData.actionCount, "ActionCount");
    }

    public void logAvailableActions() {
        for (State s : agent.getStates()) {
            for (Action a : agent.getActions()) {
                if (agent.getQValue(s, a) != null) {
                    System.out.println("state: " + s + " action: " + a + " delay:" + agent.getDelayOfTransition(s, a)
                            + " value of state: " + agent.getRewardOfState(s) + "  " + " value: " + ((SeqAgent) agent).getStateActionValue(s, a));
                }
            }
        }
    }


    public void logDeliberationTimes() {
        if (averageData.deliberationTime != null)
            logState(averageData.deliberationTime, "DeliberationTimes");
    }

//    public void logActionsWithSSE() {
//        XYSeries[][] SSEseries = new XYSeries[agent.getNumberOfStates()][agent
//                .getNumberOfActions()];
//
//        for (int i = 0; i < logStep.size() - 1; i++) {
//            LogData data = logStep.get(i);
//            for (int s = 0; s < agent.getNumberOfStates(); s++)
//                for (int a = 0; a < agent.getNumberOfActions(); a++) {
//                    if (a != ActionType.get(ActionNames.terminal).ordinal()) {
//                        if (data.actionCount[s][a] != null) {
//                            XYSeries ser = data.actionCount[s][a];
//                            int itemCount = ser.getItemCount();
//                            System.out.println(itemCount);
//                            for (int j = 0; j < itemCount; j++) {
//                                if (SSEseries[s][a] == null) {
//                                    SSEseries[s][a] = new XYSeries(agent
//                                            .addState(s)
//                                            + "_"
//                                            + agent.getActions().get(a)
//                                            + ".SSEactionCount");
//                                }
//                                Number x = ser.getX(j);
//                                int index = SSEseries[s][a].indexOf(x);
//                                double preV = 0;
//                                if (index >= 0) {
//                                    preV = SSEseries[s][a].getY(index)
//                                            .doubleValue();
//                                } else {
//                                    SSEseries[s][a].add(x,
//                                            averageData.actionCount[s][a].getY(
//                                                    j).doubleValue());
//                                }
//                                // double value = Math
//                                // .pow(
//                                // (ser.getY(j).doubleValue() -
//                                // averageData.actionCount[s][a]
//                                // .getY(j).doubleValue()),
//                                // 2)
//                                // / totalSimulationStep;
//                                // SSEseries[s][a].addOrUpdate(x, preV
//                                // + new Double(+value));
//                            }
//
//                        }
//                    }
//                }
//        }
//        logStateActionSeries(SSEseries, "ActionCountSSE");
//    }

    public void LogStateValues() {
        if (averageData.stateValue != null) {
            XYSeriesCollection dataSet = new XYSeriesCollection();

            for (int s = 0; s < agent.getNumberOfStates(); s++)
                if (averageData.stateValue.get(s) != null) {
                    dataSet.addSeries(averageData.stateValue.get(s));
                }

            // dataSet.addSeries(tonicDA);
            // dataSet.addSeries(delta);
            JFreeChart chart = ChartFactory.createXYLineChart("", "Time",
                    "Value", dataSet, PlotOrientation.VERTICAL, // data
                    true, // include legend
                    true, true);
            ChartPanel cp = new ChartPanel(chart);
            ApplicationFrame af = new ApplicationFrame("Test");
            af.setContentPane(cp);
            af.pack();
            RefineryUtilities.centerFrameOnScreen(af);
            af.setVisible(true);

        }
    }

    public void endLogging() {

        Agent agent = getAgent();

        for (int t = 0; t < agent.getCurrentTrial(); t++)
            for (State s : getAgent().getStates()) {
//                if (averageData.deliberationTime.get(s) == null)
//                    averageData.deliberationTime.put(s, new XYSeries(s + "_" + ".deliberationTime"));
//                if (totalNumberOfVisitOfState.get(t, s) != null)
//                    averageData.deliberationTime.get(s)
//                            .add(
//                                    t,
//                                    ((double) totalTimeOfDeliberation.get(t, s))
//                                            / totalNumberOfVisitOfState.get(t, s));

                for (Action a : agent.getActions()) {
//                    if (agent.getQValue(s, a) != null) {
//                        if (averageData.actionCount.get(s, i) == null)
//                            averageData.actionCount.put(s, i, new XYSeries(s
//                                    + "_"
//                                    + i
//                                    + ".actionCount"));
                    if (totalNumberOfVisitOfState.get(t, s) != null) {

//                        System.out.println("total visits: " + s + " " + totalNumberOfVisitOfState.get(t, s));
                        if (totalNumberOfTakingAction.get(t).get(s, a) != null) {
                            if (averageData.actionCount.get(s, a) == null) {
                                averageData.actionCount.put(s, a, new XYSeries(s
                                        + "_"
                                        + a
                                        + ".actionCount"));
                            }

                            if (averageData.deliberationTime.get(s) == null) {
                                averageData.deliberationTime.put(s, new XYSeries(s + ".deliberationTime"));
                            }

//                            int itemCount = averageData.deliberationTime.get(s).getItemCount();
//
//                            Number u = itemCount > 0 ? averageData.deliberationTime.get(s).getY(itemCount - 1) : 0;
//
//                            u = u.doubleValue() * 0.95 + deliberationTime.get(t, s)
//                                    / totalNumberOfVisitOfState.get(t, s) * 0.05;
//
//
//                            averageData.deliberationTime.get(s).add(t, u);

                            averageData.deliberationTime.get(s).add(t, deliberationTime.get(t, s)
                                    / totalNumberOfVisitOfState.get(t, s));

//                            System.out.println("total actions: " + s + " " + a + " " + totalNumberOfTakingAction.get(t).get(s, a));
                            double v = 0;
                            v = totalNumberOfTakingAction.get(t).get(s, a);

//                            double u =  averageData.actionCount.get(s, a).getY(t-1).doubleValue() * 0.95 +
//                                    0.05 * v / totalNumberOfVisitOfState.get(t, s);


//                            double u =  t > 1 ? averageData.actionCount.get(s, a).getY(t-1).doubleValue() * 0.95:0 +
//                                    0.05 * v / totalNumberOfVisitOfState.get(t, s);
//
//                            int itemCount = averageData.actionCount.get(s, a).getItemCount();

//                            Number u = itemCount > 0 ? averageData.actionCount.get(s, a).getY(itemCount - 1) : 0;

//                            u = u.doubleValue() * 0.9 + v / totalNumberOfVisitOfState.get(t, s) * 0.1;

                            averageData.actionCount.get(s, a)
                                    .add(t, v / totalNumberOfVisitOfState.get(t, s));

//                            averageData.actionCount.get(s, a)
//                                    .add(t, u);

                        }
                    }
                }
            }
//            }
    }


    public void logCumulativeAction(int[] logPoint) {

        Arrays.sort(logPoint);

        Agent agent = getAgent();

        int currentIndex = 0;

        HashMap<Integer, Table<State, Action, Double>> actionCount = new HashMap<Integer, Table<State, Action, Double>>();

        for (int t = 0; t < agent.getCurrentTrial(); t++)
            for (State s : getAgent().getStates()) {
                for (Action a : agent.getActions()) {
                    if (agent.getQValue(s, a) != null) {
                        if (totalNumberOfVisitOfState.get(t, s) != null) {

                            if (averageData.actionCount.get(s, a) == null) {
                                averageData.actionCount.put(s, a, new XYSeries(s
                                        + "_"
                                        + a
                                        + ".actionCount"));
                            }

                            double v = 0;
                            if (totalNumberOfTakingAction.get(t).get(s, a) != null && totalNumberOfTakingAction.get(t).get(s, a) != null)
                                v = totalNumberOfTakingAction.get(t).get(s, a);

//                            double u = averageData.actionCount.get(s, a).getY(t - 1).doubleValue() * 0.95 +
//                                    0.05 * v / totalNumberOfVisitOfState.get(t, s);

                            averageData.actionCount.get(s, a)
                                    .add(t, v / totalNumberOfVisitOfState.get(t, s));
//                            averageData.actionCount.get(s, a)
//                                    .add(t, u);
                        }
                    }
                }
            }
    }

//	public void logActionMeanAndSEMtoFile(String prefix, int fromTrai,
//			int toTrial) {
//		Agent agent = getAgent();
//		double[][][] TempStateAction = new double[agent.getNumberOfStates()][agent
//				.getNumberOfActions()][this.numberOfSimulationSteps];
//
//		double[][] TempState = new double[this.numberOfSimulationSteps][agent
//				.getNumberOfStates()];
//
//		for (int aindex = 0; aindex < numberOfSimulationSteps; aindex++) {
//			for (int t = fromTrai; t < toTrial; t++)
//				for (int s = 0; s < agent.getNumberOfStates(); s++) {
//					TempState[aindex][s] += totalNumberOfVisitOfStateForEachAgent[aindex][t][s];
//					for (int i = 0; i < agent.getNumberOfActions(); i++) {
//						if (agent.getQValue(s, i) != null) {
//							TempStateAction[s][i][aindex] += totalNumberOfTakingActionForEachAgent[aindex][t][s][i];
//						}
//					}
//				}
//		}
//		for (int aindex = 0; aindex < numberOfSimulationSteps; aindex++)
//			for (int s = 0; s < agent.getNumberOfStates(); s++) {
//				for (int i = 0; i < agent.getNumberOfActions(); i++) {
//					if (agent.getQValue(s, i) != null) {
//						TempStateAction[s][i][aindex] = TempStateAction[s][i][aindex]
//								/ TempState[aindex][s];
//					}
//				}
//			}
//
//		double means[][] = new double[agent.getNumberOfStates()][agent
//				.getNumberOfActions()];
//		double ems[][] = new double[agent.getNumberOfStates()][agent
//				.getNumberOfActions()];
//
//		for (int aindex = 0; aindex < numberOfSimulationSteps; aindex++)
//			for (int s = 0; s < agent.getNumberOfStates(); s++) {
//				for (int i = 0; i < agent.getNumberOfActions(); i++) {
//					if (agent.getQValue(s, i) != null) {
//						means[s][i] = Descriptive.mean(new DoubleArrayList(
//								TempStateAction[s][i]));
//						ems[s][i] = Descriptive.standardError(
//								TempStateAction[s][i].length, Descriptive
//										.sampleVariance(new DoubleArrayList(
//												TempStateAction[s][i]),
//												means[s][i]));
//					}
//				}
//			}
//		try {
//			PrintWriter writer = new PrintWriter(TEX_OUTPUT + "/" + prefix
//					+ "_" + ".ActionMeanAndSEMOver" + "_" + fromTrai + "_"
//					+ toTrial);
//			for (int s = 0; s < agent.getNumberOfStates(); s++) {
//				for (int i = 0; i < agent.getNumberOfActions(); i++) {
//					if (agent.getQValue(s, i) != null) {
//						writer.write("State : " + Cue.values()[s].getName()
//								+ " PrimitiveAction : " + agent.getActions()[i].getName()
//								+ " Mean : " + means[s][i] + " S.E.M : "
//								+ ems[s][i] + "\n");
//
//					}
//				}
//			}
//			writer.close();
//		} catch (FileNotFoundException e1) {
//			throw new BRLException(e1);
//		}
//
//	}

    public static Logger loadLogger(String fileName) {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(new FileInputStream(fileName));
            Object obj = in.readObject();
            while (!(obj instanceof Logger))
                obj = in.readObject();
            return (Logger) obj;
        } catch (Exception e) {
            throw new BRLException(e);
        }
    }

    private void logToFile(String prefix, Table<Integer, String, String> data, XYSeries values, int timeOffset) {

        try {
            PrintWriter writer = new PrintWriter(TEX_OUTPUT + "/" + prefix
                    + "_" + values.getKey());

            data.put(-1, values.getKey().toString(), values.getKey().toString());
            for (int j = 0; j < values.getItemCount(); j++) {
//                if (values.getX(j).intValue() % 50 == 0) {
                    if (values.getY(j) != null) {
                        data.put(values.getX(j).intValue(), values.getKey().toString(), values.getY(j) + "");
                        writer.write((values.getX(j).doubleValue() + timeOffset) + ", "
                                + values.getY(j) + "\n");
                    }
//                }
            }
            writer.close();
        } catch (FileNotFoundException e1) {
            throw new BRLException(e1);
        }
    }

//    private void logToFile(String prefix, XYSeries values, int timeOffset) {
//
//        try {
//            PrintWriter writer = new PrintWriter(TEX_OUTPUT + "/" + prefix
//                    + "_" + values.getKey());
//            for (int j = 0; j < values.getItemCount(); j++) {
//                writer.write((values.getX(j).doubleValue() + timeOffset) + ", "
//                        + values.getY(j) + "\n");
//            }
//            writer.close();
//        } catch (FileNotFoundException e1) {
//            throw new BRLException(e1);
//        }
//    }


    public void logToFile(String prefix, State state, Action action,
                          int timeOffset, Table<Integer, String, String> data) {
        if (averageData.habitualQValue != null
                && averageData.habitualQValue.get(state, action) != null) {
            logToFile(prefix, data, averageData.habitualQValue.get(state, action), timeOffset);
        }
        if (averageData.goalDirectedQValue != null
                && averageData.goalDirectedQValue.get(state, action) != null) {
            logToFile(prefix, data, averageData.goalDirectedQValue.get(state, action), timeOffset);
        }
        if (averageData.var != null && averageData.var.get(state, action) != null) {
            logToFile(prefix, data, averageData.var.get(state, action), timeOffset);
        }
        if (averageData.V != null && averageData.V.get(state, action) != null) {
            logToFile(prefix, data, averageData.V.get(state, action), timeOffset);
        }
        if (averageData.VPI != null && averageData.VPI.get(state, action) != null) {
            logToFile(prefix, data, averageData.VPI.get(state, action), timeOffset);
        }
        if (averageData.actionCount.get(state, action) != null) {
            logToFile(prefix, data, averageData.actionCount.get(state, action), timeOffset);
        }

//		try {
//			XYSeries values = averageData.habitualQValue[Cue.InitialState
//					.ordinal()][ActionType.pressLever1.ordinal()];
//			XYSeries values2 = averageData.habitualQValue[Cue.InitialState
//					.ordinal()][ActionType.enterMagazine.ordinal()];
//			PrintWriter writer = new PrintWriter(TEX_OUTPUT + "/" + prefix
//					+ "_" + "diff_press_lever_and_magazine");
//			for (int j = 0; j < values.getItemCount(); j++) {
//				writer.write((values.getX(j).doubleValue() + timeOffset)
//						+ ", "
//						+ (values.getY(j).doubleValue() - values2.getY(j)
//								.doubleValue()) + "\n");
//			}
//			writer.close();
//		} catch (FileNotFoundException e1) {
//			throw new BRLException(e1);
//		}
//
//		if (averageData.habitualQValue[Cue.InitialState.ordinal()][ActionType.pressLever2
//				.ordinal()] != null) {
//			try {
//
//				XYSeries values = averageData.habitualQValue[Cue.InitialState
//						.ordinal()][ActionType.pressLever1.ordinal()];
//				XYSeries values2 = averageData.habitualQValue[Cue.InitialState
//						.ordinal()][ActionType.pressLever2.ordinal()];
//				PrintWriter writer = new PrintWriter(TEX_OUTPUT + "/" + prefix
//						+ "_" + "diff_press_lever_and_lever2");
//				for (int j = 0; j < values.getItemCount(); j++) {
//					writer.write((values.getX(j).doubleValue() + timeOffset)
//							+ ", "
//							+ (values.getY(j).doubleValue() - values2.getY(j)
//									.doubleValue()) + "\n");
//				}
//				writer.close();
//			} catch (FileNotFoundException e1) {
//				throw new BRLException(e1);
//			}
//		}
    }

    public void logToFile(String prefix, int timeOffset) {

        Table<Integer, String, String> data = HashBasedTable.create();

        if (averageData.costOfGoalBasedDecisionMaking != null) {
            logToFile(prefix, data, averageData.costOfGoalBasedDecisionMaking,
                    timeOffset);
        }

        if (averageData.nonExploratoryAvgReward != null) {
            logToFile(prefix, data, averageData.nonExploratoryAvgReward,
                    timeOffset);
        }

        for (State s : agent.getStates()) {
            if (averageData.deliberationTime != null && averageData.deliberationTime.get(s) != null)
                logToFile(prefix, data, averageData.deliberationTime.get(s), timeOffset);
            for (Action a : agent.getActions()) {
                logToFile(prefix, s, a, timeOffset, data);
            }
        }

        for (State s : agent.getStates()) {
            for (Action a : agent.getActions()) {
                for (Action pa : agent.getActions()) {
                    if (averageData.C != null && averageData.C.get(s) != null
                            && averageData.C.get(s).get(a, pa) != null) {
                        logToFile(prefix, data, averageData.C.get(s).get(a, pa), timeOffset);
                    }
                }
            }
        }

        try {
            PrintWriter writer = new PrintWriter(TEX_OUTPUT + "/" + prefix + "-all-");
            Integer[] values = data.rowKeySet().toArray(new Integer[0]);
            Arrays.sort(values);

            String out;
            String[] sss = data.row(-1).keySet().toArray(new String[0]);
            Arrays.sort(sss);

            for (Integer value : values) {
                Map<String, String> dd = data.row(value);
                out = value + "";
                for (String s : sss) {
                    out = out + ", " + dd.get(s);
                }
                out += "\n";
                writer.write(out);
            }
            writer.close();
        } catch (
                FileNotFoundException e1
                ) {
            throw new BRLException(e1);
        }
    }


    public void logActionsToFile(String prefix) {
        for (State s : agent.getStates()) {
            logActionsToFile(prefix, s);
        }
    }

    private void logActionsToFile(String prefix, State s) {

        try {
            for (Action a : agent.getActions()) {
                if (agent.getQValue(s, a) != null) {
                    PrintWriter writer = new PrintWriter(TEX_OUTPUT + "/"
                            + prefix + "_" + s + "_"
                            + a + ".ActionTics");
                    PrintWriter writer2 = new PrintWriter(TEX_OUTPUT + "/"
                            + prefix + "_" + s + "_"
                            + a + ".ActionCount");
                    writer.write("set xtics (");
                    writer.write(")");
                    writer.close();
                    writer2.close();
                }

            }
        } catch (FileNotFoundException e1) {
            throw new BRLException(e1);
        }
    }

    public Agent getAgent() {
        return agent;
    }

    public void timeSpentForDecisionMaking() {
        XYSeriesCollection dataSet = new XYSeriesCollection();
        dataSet.addSeries(averageData.nonExploratoryAvgReward);
        drawASet(dataSet, "value");
    }
}
