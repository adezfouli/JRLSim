package sim.TD;

import agent.AgentInternalConstants;

public class TD_Constants extends AgentInternalConstants {
	public TD_Constants() {
		maxTonicDA = 3;// etha
		averageRewardLR = 0.004; // sigma
		maxDS = 6;// Ds: maximum of the signal
		df = 0.98;
		learningRate = 0.02; // period of the signal
		rewardBasalLevelCoefficient=0.1; //mu 
		Cv = 0.1;
		Cu = 0.1;
	}
}
