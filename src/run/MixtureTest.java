package run;

import java.util.Random;

import cern.jet.stat.Gamma;

public class MixtureTest {
	public static void main(String[] args) {

		BetaParam p[] = new BetaParam[] { new BetaParam(10, .20),};
//				new BetaParam(.1, .4), new BetaParam(.1, .4),
//				new BetaParam(.1, .1) };
		double r[] = { 0.0, 0.0, 0.0, 0.1 };
		double df = 0.98;

		for (int i = 0; i < 3000; i++) {
			if (new Random().nextBoolean())
				r = new double[]{ Math.random()*0.8, 0.0, 0.0, 0.1 + Math.random() * 0.2 };
			else
				r = new double[]{ Math.random()*0.8, 0.0, 0.0, 0.1 + Math.random() * 0.2 };
			for (int j = 0; j < p.length - 1; j++) {

				double mu = (p[j].alpha + r[j] + df * p[j + 1].mu())
						/ (p[j].alpha + p[j].beta + 1);
				double v = 1
						/ ((p[j].alpha + p[j].beta + 1) * (p[j].alpha
								+ p[j].beta + 2))
						* (p[j].alpha * p[j].alpha + p[j].alpha + r[j] * r[j]
								+ r[j] * (2 * p[j].alpha + 1) + df * df
								* p[j + 1].moment(2) + df * p[j + 1].mu()
								* (2 * r[j] + 2 * p[j].alpha + 1));

				if (Double.isNaN(p[j + 1].moment(2) )){
					System.out.println(p[j+1] + " Error");
				}
				p[j].alpha = mu * ((1 - mu) / v - 1);
				p[j].beta = (1 - mu) * ((1 - mu) / v - 1);

			}

			p[p.length - 1].alpha += r[p.length - 1];
			p[p.length - 1].beta += (1 - r[p.length - 1]);
		}

		int i = 0;
		System.out.println(p[i]);
	}
}

class BetaParam {
	double alpha;
	double beta;

	public double mu() {
		return alpha / (alpha + beta);
	}

	public double moment(int k) {
		return Gamma.beta(alpha + k, beta) / Gamma.beta(alpha, beta);
	}

	public BetaParam(double alpha, double beta) {
		super();
		this.alpha = alpha;
		this.beta = beta;
	}
	
	public double var() {
		return alpha  * beta / ((alpha + beta + 1) * (alpha + beta) *(alpha + beta)) ;
	}

	@Override
	public String toString() {
		return "Alpha : " + alpha + "\n"+
		"Beta : " + beta + "\n" +
		"Mu : " + mu()+ "\n" +
		"Var : " + var();

	}

}
