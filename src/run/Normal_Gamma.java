package run;

import java.util.Random;

import cern.jet.random.Normal;

public class Normal_Gamma {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NG p[] = new NG[] { 
				new NG(10, 2,3,4),
				new NG(10, 2,3,4),
				new NG(10, 2,3,4),
		};
		double r[] = { 1, 1, -1};
		double df = 0.98;

		for (int i = 0; i < 100; i++) {
//			if (new Random().nextBoolean())
//				r = new double[] { Math.random() * 0.8, 0.0, 0.0,
//						0.1 + Math.random() * 0.2 };
//			else
//				r = new double[] { Math.random() * 0.8, 0.0, 0.0,
//						0.1 + Math.random() * 0.2 };
			for (int j = 0; j < p.length - 1; j++) {
				double M1 = r[j] + df * p[j+1].mu();
				double M2 = r[j]*r[j]+2*df*p[j+1].mu() + df*df*p[j+1].moment2();	
				p[j].update(M1, M2, 1);
			}
			p[p.length-1].update(r[p.length-1], r[p.length-1]*r[p.length-1], 1);
		}

		int i = 0;
		System.out.println(p[i]);
	}

}

class NG {
	public double landa;
	public double mu;
	public double alpha;
	public double beta;

	public NG(double landa, double mu, double alpha, double beta) {
		super();
		this.landa = landa;
		this.mu = mu;
		this.alpha = alpha;
		this.beta = beta;
	}

	public double mu() {
		return mu;
	}

	public double moment2() {
		return (landa + 1) / landa * beta / (alpha - 1) + mu * mu;
	}

	public  void update(double M1, double M2,
			double n) {
		mu = (landa * mu + n * M1) / (landa + n);
		landa += n;
		alpha += n / 2;
		beta += (n / 2) * (M2 - M1 * M1) + n * landa * (M1 - mu)
				* (M1 - mu) / (2 * (landa + n));
	}
	
	@Override
	public String toString() {
		return "mean : " + mu + "\n"+
		"var : " + beta / landa / (alpha - 1);
	}
}