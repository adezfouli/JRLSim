package agent.fuzzy.set;

public class LeftOpenSideFuzzySet extends AFuzzySet {

	public double p1;
	public double p2;
	
	
	public LeftOpenSideFuzzySet(double p1, double p2) {
		super();
		this.p1 = p1;
		this.p2 = p2;
	}


	public double getMembership(double x) {
		if (x >= p2)
			return 0;
		else if (x >= p1 && x <= p2)
			return (x - p1) / (p2 - p1);
		else if (x <= p1)
			return 1;
		return 0;
	}

	
}
