package reward;

import java.io.Serializable;

public abstract class Reward implements Serializable {

	private double magnitude;

	protected Reward(double magnitude) {
		super();
		this.magnitude = magnitude;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + " : " + getMagnitude();
	}
}