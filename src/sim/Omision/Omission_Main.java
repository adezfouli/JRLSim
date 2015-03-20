package sim.Omision;

import env.Environment;
import proc.dualprocess.*;
import proc.omision.SimpleRewardTask;
import sim.BRL.DevaluationInComplexTask_RunStep;
import sim.BRL.HickRunStep;
import sim.BRL.goNoGoRunStep;

public class Omission_Main {
    public static void main(String[] args) {
        simulation();

    }


    private static void simulation() {
        int numberOfSimulations = 10;
        Environment environment = new Environment();

        SimpleRewardTask.init();

		environment.run(new SimpleRewardTask_RunStep(240, 200), numberOfSimulations, true,
				"cd");

    }

}