package agent.BRL;

import agent.AgentInternalConstants;

public class BRL_Constants extends AgentInternalConstants {

	public BRL_Constants() {
		maxTonicDA = 3;// etha
		averageRewardLR = 0.005; // sigma
		maxDS = 10;// mu: maximum of the signal
		df = 0.95;
		learningRate = 0.05; // period of the signal
		rewardBasalLevelCoefficient = 1; // period of the signal
		Cv = 0.0;
		Cu = 0.0;
		timeOfOneCalculation = 0.08;
	}
}
