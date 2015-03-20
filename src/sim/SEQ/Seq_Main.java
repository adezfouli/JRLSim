package sim.SEQ;

import env.Environment;
import proc.omision.SimpleRewardTask;

public class Seq_Main {


    public static void main(String[] args) {
        int numberOfSimulations = 1;
//        simple(numberOfSimulations);
//        devaluationLongTerm(numberOfSimulations);
//        devaluationShortTerm(numberOfSimulations);
//        degradationShortTerm(numberOfSimulations);
//        degradationLongTerm(numberOfSimulations);
//        omissionLongTerm(numberOfSimulations);
//        omissionShortTerm(numberOfSimulations);
//        Button4Seq(numberOfSimulations);
        Button4Random(numberOfSimulations);

        System.out.println("Finished!");
    }

    public static void simple(int numberOfSimulations) {

        Environment environment = new Environment();
        environment.run(new Seq_RunStep(10000, 0, "simple"), numberOfSimulations, true,
                "simple");

    }

    public static void devaluationLongTerm(int numberOfSimulations) {

        Environment environment = new Environment();
        environment.run(new Seq_RunStep(9000, 1000, "dev"), numberOfSimulations, true,
                "devaluation_long_term");

    }

    private static void devaluationShortTerm(int numberOfSimulations) {

        Environment environment = new Environment();
        environment.run(new Seq_RunStep(3000, 1000, "dev"), numberOfSimulations, true,
                "devaluation_short_term");
    }

    private static void degradationLongTerm(int numberOfSimulations) {

        Environment environment = new Environment();
        environment.run(new Seq_RunStep(9000, 3000, "deg"), numberOfSimulations, true,
                "degradation_long_term");
    }

    private static void degradationShortTerm(int numberOfSimulations) {

        Environment environment = new Environment();
        environment.run(new Seq_RunStep(3000, 1000, "deg"), numberOfSimulations, true,
                "degradation_short_term");
    }

    private static void omissionLongTerm(int numberOfSimulations) {

        Environment environment = new Environment();
        environment.run(new Seq_RunStep(9000, 1000, "omm"), numberOfSimulations, true,
                "omission_long_term");
    }

    private static void omissionShortTerm(int numberOfSimulations) {

        Environment environment = new Environment();
        environment.run(new Seq_RunStep(3000, 1000, "omm"), numberOfSimulations, true,
                "omission_short_term");
    }


    private static void Button4Seq(int numberOfSimulations) {

        Environment environment = new Environment();
		environment.run(new Seq_RunStep(90000, 1000, "b4s"), numberOfSimulations, true,
				"Button4-seq");
    }

    private static void Button4Random(int numberOfSimulations) {

        Environment environment = new Environment();
		environment.run(new Seq_RunStep(90000, 1000, "b4r"), numberOfSimulations, true,
				"Button4-random");
    }


}