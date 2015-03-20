package reward;

import java.lang.reflect.Field;

import cern.jet.random.engine.DRand;
import proc.BRLException;
import proc.Procedure;
import cern.jet.random.Normal;

public class RewardValue {

	/**
	 * Cocaine taking reward
	 */
	public double CT = 1.8;
	public double CM = 3;
	/**
	 * Variance of normal variables
	 */
	public double rewardSTDDeviation = 0.0000001;
	/**
	 * Shock punishment with freeze
	 */
	public double SHF = -0.3;
	/**
	 * Shock punishment without freeze
	 */
	public double SHWF = -12;
	/**
	 * Food eating reward
	 */
	public double F = 2.43;

	/**
	 * Devalued food
	 */
	public double DF = -2;

	/**
	 * Little food
	 */
	public double LF = 1;

	public double Cu = 1;

	public double R1, R2, R3, R4, bigReward, littleReward;

	public double R1_Shock, R2_Shock, R3_Shock;

	public double punishment;

	private Procedure procedure;

	public RewardValue() {
		super();
	}

	public Procedure getProcedure() {
		return procedure;
	}

	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}

	public RewardValue(Procedure procedure) {
		this.procedure = procedure;
	}

	public CocaineReward cocaineTaking() {
		return new CocaineReward(new Normal(CT, rewardSTDDeviation,
				new DRand()).nextDouble());
	}

	public Cost shockWithFreeze() {
		return new Cost(new Normal(SHF, rewardSTDDeviation, new DRand())
				.nextDouble());
	}

	public Cost shockWithoutFreeze() {
		return new Cost(new Normal(SHWF, rewardSTDDeviation, new DRand())
				.nextDouble());
	}

	public FoodReward foodEating() {
//        return new FoodReward(new Normal(F, rewardSTDDeviation,
//                new RandomJava()).nextDouble());
        return new FoodReward(F);
	}

	public WaitReinforcer zero() {
//		return new WaitReinforcer(new Normal(0, rewardSTDDeviation,
//				new RandomJava()).nextDouble());
        return new WaitReinforcer(0);
	}

	public Reward devaluedFood() {
		return new FoodReward(new Normal(DF, rewardSTDDeviation,
				new DRand()).nextDouble());
	}

	public Reward littleFood() {
		return new FoodReward(new Normal(LF, rewardSTDDeviation,
				new DRand()).nextDouble());
	}

	public static String getLoggerMessage() {
		String msg = "";
		Field[] f = RewardValue.class.getFields();
		for (int i = 0; i < f.length; i++) {
			try {
				msg += f[i].getName() + "=" + f[i].getDouble(new RewardValue())
						+ "-";
			} catch (Exception e) {
				throw new BRLException(e);
			}
		}
		return msg;
	}

	public Reward getR1() {
//		return new FoodReward(new Normal(R1, rewardSTDDeviation,
//				new RandomJava()).nextDouble());
        return new FoodReward(R1);
	}

	public Reward getR2() {
//        return new FoodReward(new Normal(R2, rewardSTDDeviation,
//                new RandomJava()).nextDouble());
        return new FoodReward(R2);
	}

	public Reward getR3() {
		return new FoodReward(new Normal(R3, rewardSTDDeviation,
				new DRand()).nextDouble());
	}

    public Reward getR4() {
        return new FoodReward(new Normal(R4, rewardSTDDeviation,
                new DRand()).nextDouble());
    }

	public Reward getR1_Shock() {
		return new Cost(new Normal(R1_Shock, rewardSTDDeviation,
				new DRand()).nextDouble());
	}

	public Reward getR2_Shock() {
		return new Cost(new Normal(R2_Shock, rewardSTDDeviation,
				new DRand()).nextDouble());
	}

	public Reward getR3_Shock() {
		return new Cost(new Normal(R3_Shock, rewardSTDDeviation,
				new DRand()).nextDouble());
	}

	public FoodReward getBigReward() {
		return new FoodReward(new Normal(bigReward, rewardSTDDeviation,
				new DRand()).nextDouble());
	}

	public FoodReward getLittleReward() {
		return new FoodReward(new Normal(littleReward, rewardSTDDeviation,
				new DRand()).nextDouble());
	}

	public Cost getCu() {
		return new Cost(new Normal(Cu, rewardSTDDeviation, new DRand())
				.nextDouble());
	}

	public void setR1(double r1) {
		R1 = r1;
	}

	public void setR2(double r2) {
		R2 = r2;
	}

    public void setR3(double r) {
        R3 = r;
    }
    public void setR4(double r) {
        R4 = r;
    }
	public void setF(double f) {
		F = f;
	}

	public Reward punishment() {
		return new Cost(new Normal(punishment, rewardSTDDeviation,
				new DRand()).nextDouble());
	}
}