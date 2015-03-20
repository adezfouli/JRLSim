package agent.kalman;

import agent.Action;
import agent.State;
import javaslam.filter.NoisyVectorFunction;

import javaslam.filter.UnscentedTransformation;
import javaslam.prob.Gaussian;
import javaslam.prob.Variable;
import javaslam.util.ListSet;
import reward.Reward;
import Jama.Matrix;
import action.ActionSelection;
import agent.AgentInternalConstants;
import env.Environment;

public class QKalmanAgent extends KalmanFilterAgent {

	private static final long serialVersionUID = -3256639443144774909L;

	private QFunction function;

	public QKalmanAgent(Environment environment,
			ActionSelection actionSelection,
			AgentInternalConstants parameterValues) {
		super(environment, actionSelection, parameterValues);
		function = new QFunction(1);
	}

	@Override
	protected void updateValue(Reward r, double transitionDelay) {

		observationNoise = 0.08;

		P = P.plus(DiffusionNoise);

		ListSet listSet = new ListSet();
		listSet.add(new Variable("Theta", getNumberOfBasisFuncs()));

		Gaussian x = new Gaussian(listSet, Theta, P, true);

		function.setDf(Math.pow(this.df, transitionDelay));

		UnscentedTransformation UT = new UnscentedTransformation(function, x);

		ListSet vars = new ListSet(UT.getOutputVariable());

		double rHat = UT.getDistribution().getMu(vars).get(0, 0);

		double P_r = UT.getDistribution().getSigma(vars, vars).get(0, 0)
				+ observationNoise;

		double delta = r.getMagnitude() - rHat;// - getNonExploratoryAverageReward();

		Matrix P_theta = UT.getDistribution().getSigma(listSet, vars);

		K = P_theta.times(1 / P_r);
		Theta = Theta.plus(K.times(delta));
		P = P.minus(K.times(K.transpose()).times(P_r));

		for (State s: getStates()) {
			for (Action a: getActions()) {
				if (getQValue(s, a) != null) {
					((MuVarQValue) getQValue(s, a)).setEmu(Theta.get(
							getStateActionIndex(s, a), 0));
					((MuVarQValue) getQValue(s, a)).SetVarMu(P.get(getStateActionIndex(
							s, a), getStateActionIndex(s, a)));
				}
			}
		}
	}

	class QFunction implements NoisyVectorFunction {

		double df;

		public void setDf(double df) {
			this.df = df;
		}

		public QFunction(double df) {
			this.df = df;
		}

		public Gaussian getNoiseModel() {
			return null;
		}

		public double[] evaluate(double[] theta) {
			Action maxActionIndex = getMaxValuedAction(currentState);

			int currentStateActionIndex = getStateActionIndex(currentState, maxActionIndex);
			return new double[] { theta[getPrevStateActionIndex()] - df
					* theta[currentStateActionIndex] };
//			return new double[] { theta[getPrevStateActionIndex()] - 1
//					* theta[currentStateActionIndex] };
		}

		public int getInputDim() {
			return getNumberOfBasisFuncs();
		}

		public int getOutputDim() {
			return 1;
		}
	}
}
