package reward;

public class CocaineAndOponentReward extends Reward {
	
	private static final long serialVersionUID = 1121L;

	private double firstReward;
	
	private double secondReward;
	
	
	public CocaineAndOponentReward(double firstReward,
			double secondReward) {
		super(firstReward + secondReward);
		this.firstReward = firstReward;
		this.secondReward = secondReward;
	}


	protected CocaineAndOponentReward(double magnitude) {
		super(magnitude);
	}


	public double getFirstReward() {
		return firstReward;
	}


	public void setFirstReward(double firstReward) {
		this.firstReward = firstReward;
	}


	public double getSecondReward() {
		return secondReward;
	}


	public void setSecondReward(double secondReward) {
		this.secondReward = secondReward;
	}
}
