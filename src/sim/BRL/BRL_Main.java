package sim.BRL;

import java.util.Map.Entry;

import javaslam.filter.UnscentedTransformation;
import javaslam.prob.Gaussian;
import javaslam.slam.KalmanSLAMFilter;
import javaslam.slam.LinearizedSLAMFilter;
import action.GreedyActionSelection;
import action.SoftMaxActionSelection;
import agent.BRL.NG_QValue;
import cern.colt.Arrays;
import cern.jet.stat.Gamma;
import cern.jet.stat.Probability;
import env.Environment;
import flanagan.integration.IntegralFunction;
import flanagan.integration.Integration;
import proc.dualprocess.*;

public class BRL_Main {
    public static void main(String[] args) {
        simulation();
//        goNoGoSimulation();
//        hickSimulation();
    }

    private static void hickSimulation() {

        int numberOfChoices = 8;

        int numberOfSimulations = 300;

        Environment environment = new Environment();
        HickTask.init(numberOfChoices);
        environment.run(new HickRunStep(numberOfChoices, 400), numberOfSimulations, true,
                "HicksTask_"+ numberOfChoices + "_" + numberOfChoices);
    }


    public static void goNoGoSimulation(){
        int numberOfSimulations = 3000;
        Environment environment = new Environment();

        GoNoGoTask.init();
        environment.run(new goNoGoRunStep(150, 200, 150), numberOfSimulations, true,
                "Pessiglione_100_100_200_");

    }

    private static void simulation() {
        int numberOfSimulations = 500;
        Environment environment = new Environment();

        CyclicLeverMagazineChainTask.init();
        CyclicLeverMagazineTask.init();
        CyclicLeverChainTaskSingleOutcome.init();

//        environment.run(new DevaluationInSimpleTask_RunStep(240, 100), numberOfSimulations, true,
//                "240_predevaluation_");



//		environment.getLogger().logActionMeanAndSEMtoFile("40_predevaluation_Simple_", 30, 40);
//		environment.getLogger().logActionMeanAndSEMtoFile("40_predevaluation_Simple", 40, 50);
//		
//		environment = new Environment();
//		environment.run(new DevaluationInSimpleTask_RunStep(240, 200), numberOfSimulations, true,
//		"240_predevaluation_");
//		environment.getLogger().logActionMeanAndSEMtoFile("240_predevaluation_Simple_", 230, 240);
//		environment.getLogger().logActionMeanAndSEMtoFile("240_predevaluation_Simple_", 240, 250);
//		
//		environment = new Environment();
		environment.run(new DevaluationInComplexTask_RunStep(240, 200), numberOfSimulations, true,
				"40_predevaluation_");
//        environment.run(new DevaluationInModifiedConcurrentTask_RunStep(240, 200), numberOfSimulations, false,
//                "40_predevaluation_");
//////		environment.getLogger().logActionMeanAndSEMtoFile("240_predevaluation_Complex_", 230, 240);
//		environment.getLogger().logActionMeanAndSEMtoFile("240_predevaluation_Complex", 240, 250);

    }

}