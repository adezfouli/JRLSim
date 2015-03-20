package agent.fuzzy.set;

public class ATrapezoidFuzzySet extends AFuzzySet{

	public double p1;
	public double p2;
	public double p3;
	public double p4;
	
	public ATrapezoidFuzzySet(double p1, double p2, double p3, double p4) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
	}
	
	public double getMembership(double x){
		if (x <= p3 && x >= p2)
			return 1;
		else if (x <= p1)
			return 0;
		else if (x >= p4)
			return 0;
		else if (x >= p1 && x<=p2) 
			return (x - p1) / (p2 - p1);
		else if (x >= p3 && x<=p4) 
			return (x - p3) / (p4 - p3);
		return 0;
	}
}
