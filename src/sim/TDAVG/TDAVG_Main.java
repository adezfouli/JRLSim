package sim.TDAVG;

import sim.LATDAVG.LATDAVG_BlockingWithNoBlock_RunStep;
import sim.LATDAVG.LATDAVG_Blocking_RunStep;
import env.Environment;

public class TDAVG_Main {
	private static boolean logToFile;
	private static int totalTimeStep;

	public static void main(String[] args) {
//		new Environment().run(new TDAVG_CocaineTakingShock_RunStep(4000), 100, false,
//				"", 0);
//		logToFile = true;
//		totalTimeStep = 1000;
//		impulsiveChoice();
//		decreasedDelta();
//		blockingEffect();
//		compulsiveDrugSeeking();
	}


	
	public static void decreasedDelta(){
		String prefix = "_cocaineAndFood_";
		new Environment().run(new TDAVG_CocaineAndFood_RunStep(0, 2000), 1, logToFile,
				0+ prefix);
		new Environment().run(new TDAVG_CocaineAndFood_RunStep(1000, 2000), 1, logToFile,
				1000+ prefix);
		new Environment().run(new TDAVG_CocaineAndFood_RunStep(2000, 2000), 1, logToFile,
				2000+ prefix);
	}
	
	public static void impulsiveChoice() {

		String prefix = "_trial_taking_";
		new Environment().run(new TDAVG_DDT_RunStep(1, 1000), totalTimeStep, logToFile,
				0 + prefix);
		new Environment().run(new TDAVG_DDT_RunStep(100, 1000), totalTimeStep, logToFile,
				100 + prefix);
		new Environment().run(new TDAVG_DDT_RunStep(500, 1000), totalTimeStep, logToFile,
				500 + prefix);
		new Environment().run(new TDAVG_DDT_RunStep(2000, 1000), totalTimeStep, logToFile,
				2000 + prefix);
	}
	
	public static void compulsiveDrugSeeking(){
		new Environment().run(new TDAVG_CocaineSeekingShock_RunStep(2500), totalTimeStep, logToFile,
				"");
		new Environment().run(new TDAVG_FoodSeekingShock_RunStep(2500), totalTimeStep, logToFile,
				"");
	}
	
	public static void blockingEffect(){
		new Environment().run(new LATDAVG_Blocking_RunStep(200, 1500, 200, 1000), totalTimeStep, logToFile, "");
		new Environment().run(new LATDAVG_BlockingWithNoBlock_RunStep(1700, 200, 1000), totalTimeStep, logToFile, "");
	}
}
 