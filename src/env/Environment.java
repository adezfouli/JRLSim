package env;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import agent.Action;
import action.PrimitiveAction;
import agent.SeqRL.AuxiliaryState;
import logger.Logger;
import org.apache.poi.xssf.model.Table;
import proc.Procedure;
import reward.Reward;
import agent.Agent;

public class Environment implements Serializable {

    private Procedure procedure;

    private Logger logger;

    private Reward r;

    public Procedure getProcedure() {
        return procedure;
    }

    public String getName() {
        return getProcedure().getClass().getSimpleName() + "-" + "trials="
                + getProcedure().getNumberOfTrials() + "-";
    }

    public void singleRun(Agent agent, Procedure procedure, int simulationStep) {
        this.procedure = procedure;
        agent.reset();
        logger.startLogging(agent);

        do {
            PrimitiveAction action = agent.learnAndSelectAction(r, procedure.getCue(),
                    procedure.getDelay());
            logger.logStateActionReward(action, r, procedure.getCue(), simulationStep, true);
            r = procedure.nextState(action);
        } while (!procedure.isFinished() || agent.getCurrentState() instanceof AuxiliaryState);
    }

    public ArrayList<Map<Action, Double>> runFixAgent(Agent agent, Procedure procedure) {

        ArrayList<Map<Action, Double>> probs = new ArrayList<Map<Action, Double>>();
        this.procedure = procedure;
        logger = new Logger(1);
        logger.setEnable(false);
        logger.setAgent(agent);
        agent.setEnvironment(this);
        agent.reset();
        do {
            PrimitiveAction action = agent.learnAndSelectAction(r, procedure.getCue(),
                    procedure.getDelay());
            r = procedure.nextState(action);
            probs.add(agent.selectActionProbs());
        } while (!procedure.isFinished());

        return probs;
    }


    public void run(RunStep runStep, int numberOfSimulations,
                    boolean logToFile, String prefix) {
        runStep.setEnvironment(this);
        logger = new Logger(numberOfSimulations);
        logger.setEnable(false);
        for (int t = 0; t < numberOfSimulations; t++) {
            System.out.println("Simulation step " + t);
            runStep.run(t);
        }
        logger.endLogging();
        drawChart();

        if (logToFile) {
            logAllToFile(prefix + runStep.getClass().getName());
        }
    }

    public boolean isPossible(Action action) {
        return procedure.isPossible((PrimitiveAction) action);
    }

    public void drawChart() {
        // logger.drawActionChart();
        // // logger.logDelta();
        // // logger.LogStateValues();
        logger.logHabitualQValues();
//		logger.logVar();
//		logger.logVIP();
        logger.logActions();
//        logger.logDeliberationTimes();
//		logger.logGoalDirectedQValues();
//        logger.logAvailableActions();
//        logger.timeSpentForDecisionMaking();
        // logger.logActionsWithSSE();
        // logger.drawActionChart();
        // logger.logV();
        // logger.drawRewardChart(10);
    }

    public void logAllToFile(String prefix) {
        logger.logToFile(prefix, 0);
    }

    public void logActionsToFile(String prefix) {
        logger.logActionsToFile(prefix);
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}