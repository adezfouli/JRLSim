package sim.LATDAVG;

import env.Environment;

public class LATDAVG_Main {
	public static void main(String[] args) {
		new Environment().run(
				new LATDAVG_Blocking_RunStep(100, 500, 100, 500), 100, false,
				"");
		new Environment().run(new LATDAVG_BlockingWithNoBlock_RunStep(600,
				100, 500), 100, false, "");
	}
}
