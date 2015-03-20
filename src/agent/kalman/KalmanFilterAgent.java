package agent.kalman;


import agent.Action;
import agent.State;
import cern.jet.stat.Probability;
import env.Environment;
import Jama.Matrix;
import action.ActionSelection;
import agent.AgentInternalConstants;
import agent.QValue;
import agent.BRL.BayesianAgent;

import java.util.HashMap;
import java.util.Map;

public abstract class KalmanFilterAgent extends BayesianAgent {

	private static final long serialVersionUID = 4777929656775009883L;
	/**
	 * covariance matrix
	 */
	protected Matrix P;
	protected Matrix K;
	protected Matrix Theta;
	protected Matrix DiffusionNoise;
	protected int numberOfBasisFuncs;
	protected double observationNoise;
	protected double initialVariance;

	public KalmanFilterAgent(Environment environment,
			ActionSelection actionSelection,
			AgentInternalConstants parameterValues) {
		super(environment, actionSelection, parameterValues);
		double diffusionValue = 0.000005;
		initialVariance = 1;
		numberOfBasisFuncs = getNumberOfBasisFuncs();
		P = Matrix.identity(numberOfBasisFuncs, numberOfBasisFuncs);
		K = new Matrix(numberOfBasisFuncs, 1);
		Theta = new Matrix(numberOfBasisFuncs, 1);
		DiffusionNoise = Matrix
				.identity(numberOfBasisFuncs, numberOfBasisFuncs).times(diffusionValue);
		observationNoise = 0.001;

	}


	public int getNumberOfBasisFuncs() {
		return getNumberOfActions() * getNumberOfStates();
	}

	protected void toString(double[][] x) {
		System.out.print("[");
		for (int i = 0; i < x.length; i++) {
			System.out.println();
			for (int j = 0; j < x[0].length - 1; j++) {
				System.out.print(x[i][j] + " , ");
			}
			System.out.print(x[x.length - 1][x.length - 1]);
		}
		System.out.println("]");
	}

	protected int getStateActionIndex(State state, Action action) {
		return getStateActionIndex(getOrdinalState(state), getOrdinalAction(action));
	}

	protected int getStateActionIndex(int state, int action) {
		return state * getNumberOfActions() + action;
	}

	protected int getPrevStateActionIndex() {
		return getStateActionIndex(prevState, prevAction);
	}

	@Override
	public QValue getInitQ() {
		return new MuVarQValue(this, 0, initialVariance);
	}


	@Override
	public Map<Action, Double> calculateValueOfPerfectInformation(State state) {
		Map<Action, Double> VPI = new HashMap<Action, Double>();
		MuVarQValue best = null, secBest = null;
		Action best_n = null;

		// calculating best and second best action
		for (Action i : getActions()) {
			if (getQValue(state, i) != null) {
				if (best == null) {
					best = (MuVarQValue) getQValue(state, i);
					best_n = i;
				} else {
					if (getQValue(state, i).Emu() > best.Emu()) {
						secBest = best;
						best = (MuVarQValue) getQValue(state, i);
						best_n = i;
					} else if (secBest == null
							|| getQValue(state, i).Emu() > secBest.Emu())
						secBest = (MuVarQValue) getQValue(state, i);
				}
			}
		}
		if (secBest == null)
			return VPI;

		double vpi = 0;
		for (Action i : getActions()) {
			if (getQValue(state, i) != null) {
				if (i == best_n) {
					double mu2 = secBest.Emu();
					double mu = best.Emu();
					double sigma = Math.sqrt(best.VarMu());
					vpi =
					// q[Cue.InitialState.ordinal()][ActionType.pressLever2.ordinal()].Emu()
					// -
					// q[Cue.InitialState.ordinal()][ActionType.pressLever1.ordinal()].Emu();
					// best.Emu() - secBest.Emu();
					(mu2 - mu)
							* (0.5 + 0.5 * Probability.errorFunction((mu2 - mu)
									/ sigma / Math.sqrt(2)))
							+ sigma
							* sigma
							* Math.exp(-(mu2 - mu) * (mu2 - mu) / 2 / sigma
									/ sigma) / sigma / Math.sqrt(2 * Math.PI);

					VPI.put(i, vpi);
				} else {
					MuVarQValue value = (MuVarQValue) getQValue(state, i);
					double mu2 = best.Emu();
					double mu = value.Emu();
					double sigma = Math.sqrt(value.VarMu());

					vpi =
					// q[Cue.InitialState.ordinal()][ActionType.pressLever2.ordinal()].Emu()
					// -
					// q[Cue.InitialState.ordinal()][ActionType.pressLever1.ordinal()].Emu();
					// best.Emu() - secBest.Emu();
					(mu - mu2)
							* (0.5 - 0.5 * Probability.errorFunction((mu2 - mu)
									/ sigma / Math.sqrt(2)))
							+ sigma
							* sigma
							* Math.exp(-(mu2 - mu) * (mu2 - mu) / 2 / sigma
									/ sigma) / sigma / Math.sqrt(2 * Math.PI);

					VPI.put(i, vpi);
				}
			}
		}
		return VPI;
	}

}
