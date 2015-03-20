package sim.TDAVG;

import reward.RewardValue;

public class TDAVG_RewardVaules extends RewardValue {

	public TDAVG_RewardVaules() {
		CT = 2;
		CM = 0;
		rewardSTDDeviation = 0.02;
		SHF = -2;
		SHWF = -200;
		F = 20;
		R1 = 10;
		R2 = 2;
		R3 = 3;
		R1_Shock = 0;
		R2_Shock = -9;
		R3_Shock = -4;
		bigReward = 15;
		littleReward = 1;
		Cu = -20;
	}
}
