package sim.fuzzy;

import sim.BRL.DevaluationInSimpleTask_RunStep;
import env.Environment;

public class FMain {
	public static void main(String[] args) {
		Environment environment = new Environment();

		boolean logToFile = true;
		
		environment.run(new Fuzzy_RunStep(5, false), 50, logToFile, "FKTD_5_");
		environment.run(new Fuzzy_RunStep(10, false), 50, logToFile, "FKTD_10_");
		environment.run(new Fuzzy_RunStep(20, false), 50, logToFile, "FKTD_20_");

		environment.run(new Fuzzy_RunStep(5, true), 50, logToFile, "FTD_5_");
		environment.run(new Fuzzy_RunStep(10, true), 50, logToFile, "FTD_10_");
		environment.run(new Fuzzy_RunStep(20, true), 50, logToFile, "FTD_20_");
	}
}
