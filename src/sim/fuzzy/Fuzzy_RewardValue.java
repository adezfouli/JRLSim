package sim.fuzzy;

import reward.RewardValue;

public class Fuzzy_RewardValue extends RewardValue {

	public Fuzzy_RewardValue() {
		CT = 12;
		CM = 0;
		rewardSTDDeviation = 0.05;
		SHF = -2;
		SHWF = -17;
		F = 1;
		DF = 1;
		LF = 0.0;
		Cu = 0;
		punishment = -1;
		bigReward = 50;
	}
}
