package agent.BRL;

import agent.Action;
import agent.State;
import proc.BRLException;

import action.ActionSelection;
import agent.AgentInternalConstants;
import agent.QValue;

import cern.jet.stat.Gamma;
import env.Environment;
import flanagan.integration.IntegralFunction;
import flanagan.integration.Integration;

import java.util.HashMap;
import java.util.Map;

public class BRL_Agent extends BayesianAgent {

	private static final long serialVersionUID = -2737226196255453329L;

	public BRL_Agent(Environment environment, ActionSelection actionSelection,
			AgentInternalConstants constants) {
		super(environment, actionSelection, constants);
	}

	public QValue getInitQ() {
		return new NG_QValue(this, 0,2, 2, 2);
	}

	/**
	 * Calculates Value of Perfect Information (VPI)
	 * 
	 *
     * @param state
     *            the state in which value of perfect information is calculated
     * @return
	 */
	@Override
	public Map<Action, Double> calculateValueOfPerfectInformation(State state) {
		Map<Action, Double> VPI = new HashMap<Action, Double>();
		NG_QValue best = null, secBest = null;
		Action best_n = null;

		// calculating best and second best action
		for (Action action : getActions()) {
			Action i = action;
			if (getQValue(state, i) != null) {
				if (best == null) {
					best = (NG_QValue) getQValue(state, i);
					best_n = i;
				} else {
					if (getQValue(state, i).Emu() > best.Emu()) {
						secBest = best;
						best = (NG_QValue) getQValue(state,i);
						best_n = i;
					} else if (secBest == null
							|| getQValue(state,i).Emu() > secBest.Emu())
						secBest = (NG_QValue) getQValue(state, i);
				}
			}
		}
		if (secBest == null)
			return VPI;

		double vpi = 0;
		for (Action action : getActions()) {
			Action i = action;
			if (getQValue(state, i) != null) {

				if (i == best_n) {

					vpi = (secBest.Emu() - best.Emu())
							* best.cdf(secBest.Emu())
							- Math.sqrt((double) 1 / 2 / Math.PI / best.landa)
							* GammaRation(best.alpha)
							/ (-best.alpha + 0.5)
							* Math.sqrt(best.beta)
							* Math.pow(1 + best.landa / 2 / best.beta
									* (secBest.Emu() - best.mu)
									* (secBest.Emu() - best.mu),
									-best.alpha + 0.5);

					VPI.put(i, vpi);
				} else {
					NG_QValue value = (NG_QValue) getQValue(state, i);
					vpi = (getQValue(state, i).Emu() - best.Emu())
							* (1 - (value).cdf(best.Emu()))
							- Math.sqrt((double) 1 / 2 / Math.PI
									/ (value).landa)
							* GammaRation((value).alpha)
							/ (-(value).alpha + 0.5)
							* Math.sqrt(value.beta)
							* Math.pow(1 + (value).landa / 2 / value.beta
									* (best.Emu() - (value).mu)
									* (best.Emu() - (value).mu),
									-(value).alpha + 0.5);

					VPI.put(i, vpi);
				}
				if (Double.isNaN(vpi) || Double.isInfinite(vpi)) {
					throw new BRLException("VPI computation exception");
				}
			}
		}

		return VPI;
	}

	/**
	 * Calculates Gamma(k+0.5) / Gamma(k)
	 * 
	 * @param k
	 *            the value of ratio
	 * @return Gamma(k+0.5) / Gamma(k)
	 */
	public static double GammaRation(double k) {
		double value = 1;
		while (k > 1) {
			value *= (k - 0.5) / (k - 1);
			k--;
		}
		return value * Gamma.gamma(k + 0.5) / Gamma.gamma(k);
	}

	private double calculateWithIntegral(NG_QValue[] values, int i, int best_n,
			QValue secBest) {
		return Integration.gaussQuad(new Gain(values, i, best_n, secBest),
				-100, 100, 100000);
	}

	private double calcC(NG_QValue p) {
		double k = p.alpha
				* Gamma.gamma(p.alpha + 0.5)
				* Math.sqrt(p.beta)
				/ ((p.alpha - 0.5) * Gamma.gamma(p.alpha) * Gamma.gamma(0.5)
						* p.alpha * Math.sqrt(2 * p.landa))
				* Math.pow(1 + p.Emu() * p.Emu() / (2 * p.alpha),
						-p.alpha + 0.5);
		if (Double.isNaN(k) || Double.isInfinite(k))
			System.err
					.println("Computation exceeds limits of numerical calculation");
		return k;
	}

	public void setLearningEnable(boolean learningEnable) {
		this.learningEnable = learningEnable;
	}

}

class Gain implements IntegralFunction {

	private NG_QValue[] actionValues;
	private int a;
	private int bestAction;
	private QValue secondBest;

	public Gain(NG_QValue[] actionValues, int a, int bestAction,
			QValue secondBest) {
		super();
		this.actionValues = actionValues;
		this.a = a;
		this.bestAction = bestAction;
		this.secondBest = (QValue) secondBest;
	}

	public double function(double x) {
		if (a == bestAction && x < secondBest.Emu()) {
			return (secondBest.Emu() - x) * actionValues[a].pdfMu(x);
		} else if (a != bestAction && x > actionValues[bestAction].Emu()) {
			return (x - actionValues[bestAction].Emu())
					* actionValues[a].pdfMu(x);
		}
		return 0;
	}
}
