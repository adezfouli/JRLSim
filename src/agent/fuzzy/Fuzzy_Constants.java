package agent.fuzzy;

import agent.AgentInternalConstants;

public class Fuzzy_Constants extends AgentInternalConstants {
	public Fuzzy_Constants() {
		maxTonicDA = 3;// etha
		averageRewardLR = 0.02; // sigma
		maxDS = 10;// mu: maximum of the signal
		df = 0.9;
		learningRate = 0.1; // period of the signal
		rewardBasalLevelCoefficient = 1; // period of the signal
		Cv = 0.0;
		Cu = 0.0;
		timeOfOneCalculation = 0.08;
	}

}
