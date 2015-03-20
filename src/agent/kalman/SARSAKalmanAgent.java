package agent.kalman;

import agent.Action;
import reward.Reward;

import Jama.Matrix;
import action.ActionSelection;
import agent.AgentInternalConstants;
import env.Environment;

public class SARSAKalmanAgent extends KalmanFilterAgent {

	private static final long serialVersionUID = -7402345451620609205L;

	public SARSAKalmanAgent(Environment environment,
			ActionSelection actionSelection,
			AgentInternalConstants parameterValues) {
		super(environment, actionSelection, parameterValues);
	}

	@Override
	protected void updateValue(Reward r, double transitionDelay) {
		observationNoise = 0.08;
		// observationNoise = 0.04;
		// int maxActionIndex = getMaxValuedAction(currentState);
		Action maxActionIndex = currentAction;

		double df = Math.pow(this.df, transitionDelay);
		int currentStateActionIndex = getStateActionIndex(currentState, maxActionIndex);

		P = P.plus(P.times(DiffusionNoise));

		Matrix H1 = new Matrix(getNumberOfBasisFuncs(), 1, 0), H2 = new Matrix(
				getNumberOfBasisFuncs(), 1, 0);
		H1.set(currentStateActionIndex, 0, df);
		H2.set(getPrevStateActionIndex(), 0, 1);
		Matrix H = H2.minus(H1);

		double delta = r.getMagnitude()
				- H.transpose().times(Theta).get(0, 0);
		Matrix P_theta = P.times(H);
		double P_r = H.transpose().times(P).times(H).get(0, 0)
				+ observationNoise;

		K = P_theta.times(1 / P_r);
		Theta = Theta.plus(K.times(delta));
		P = P.minus(K.times(K.transpose()).times(P_r));

		((MuVarQValue) getQValue(currentState, maxActionIndex)).setEmu(Theta
				.get(currentStateActionIndex, 0));
		((MuVarQValue) getQValue(prevState, prevAction))
				.setEmu(Theta.get(getPrevStateActionIndex(), 0));

		((MuVarQValue) getQValue(currentState, maxActionIndex)).SetVarMu(P
				.get(currentStateActionIndex, currentStateActionIndex));
		((MuVarQValue) getQValue(prevState, prevAction)).SetVarMu(P
				.get(getPrevStateActionIndex(),
						getPrevStateActionIndex()));
	}
}
