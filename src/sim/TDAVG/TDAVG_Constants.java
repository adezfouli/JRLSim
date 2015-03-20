package sim.TDAVG;

import agent.AgentInternalConstants;

public class TDAVG_Constants extends AgentInternalConstants {
	public TDAVG_Constants() {
		maxTonicDA = 10;// etha
		averageRewardLR = 0.005; // sigma
		maxDS = 10;// Ds: maximum of the signal
		df = 0.02;
		learningRate = 0.2; // period of the signal
		rewardBasalLevelCoefficient = 0.1; // mu
		Cv = 60;
		Cu = 6;

	
/*		maxTonicDA = 10;// etha
		averageRewardLR = 0.0002; // sigma
		maxDS = 0.000002;// Ds: maximum of the signal
		df = 0.02;
		lr = 0.2; // period of the signal
		rewardBasalLevelCoefficient = 0.1; // mu
		Cv = 60;
		Cu = 6;
*/	
	
	}
}
