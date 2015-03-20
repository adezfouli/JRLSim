package sim.TD;

import env.Environment;
import sim.SEQ.Seq_RunStep;
import umontreal.iro.lecuyer.simevents.Sim;

public class TD_Main {

    public static void main(String[] args) {
        simple(500);
    }

    public static void simple(int numberOfSimulations) {

        Environment environment = new Environment();
        environment.run(new SimpleLP_EM_Task_RunStep(500, 0), numberOfSimulations, false,
                "simple");

    }

}
