package agent.fuzzy;

import agent.Action;
import agent.State;
import env.CueNames;
import proc.grid.GridWorld;
import reward.Reward;
import Jama.Matrix;
import action.ActionSelection;
import agent.AgentInternalConstants;
import agent.fuzzy.set.AFuzzySet;
import agent.fuzzy.set.ATrapezoidFuzzySet;
import agent.fuzzy.set.LeftOpenSideFuzzySet;
import agent.fuzzy.set.RightOpenSideFuzzySet;
import agent.kalman.KalmanFilterAgent;
import agent.kalman.MuVarQValue;
import env.Cue;
import env.Environment;

import java.util.HashMap;
import java.util.Map;

public class FuzzyKalmanAgent extends KalmanFilterAgent {

	private static final long serialVersionUID = 5295108835776727798L;

	private AFuzzySet[] fuzzySet;

	private GridWorld gridWorld;

	private int numberOfSets;

	private double currentXPos;

	private double currentYPos;

	private double prevXPos;

	private double prevYPos;

	private int stepsToPrey;

	private boolean simple;

	public FuzzyKalmanAgent(Environment environment,
			ActionSelection actionSelection,
			AgentInternalConstants parameterValues, int xdim, boolean simple) {
		super(environment, actionSelection, parameterValues);

		int halfNumberOfSets = numberOfSets / 2;
		double precentOfGrid = 0.1;
		double lengthOfSet = xdim * precentOfGrid;
		double steepRatio = 6;
		this.simple = simple;

		fuzzySet = new AFuzzySet[halfNumberOfSets * 2];

		double p2 = 0;

		fuzzySet[0] = new ATrapezoidFuzzySet(0 - lengthOfSet / steepRatio / 2,
				lengthOfSet / steepRatio / 2, 0 + lengthOfSet - lengthOfSet
						/ steepRatio, 0 + lengthOfSet);
		fuzzySet[halfNumberOfSets] = new ATrapezoidFuzzySet(-lengthOfSet, 0
				- lengthOfSet + lengthOfSet / steepRatio, -lengthOfSet
				/ steepRatio / 2, lengthOfSet / steepRatio / 2);

		for (int i = 1; i < halfNumberOfSets - 1; i++) {
			p2 += lengthOfSet;
			fuzzySet[i] = new ATrapezoidFuzzySet(p2 - lengthOfSet / steepRatio,
					p2, p2 + lengthOfSet - lengthOfSet / steepRatio, p2
							+ lengthOfSet);

			fuzzySet[i + halfNumberOfSets] = new ATrapezoidFuzzySet(-p2
					- lengthOfSet,
					-p2 - lengthOfSet + lengthOfSet / steepRatio, -p2, -p2
							+ lengthOfSet / steepRatio);
		}

		fuzzySet[halfNumberOfSets - 1] = new RightOpenSideFuzzySet(p2
				- lengthOfSet / steepRatio, p2);
		fuzzySet[+halfNumberOfSets + halfNumberOfSets - 1] = new LeftOpenSideFuzzySet(
				-p2, -p2 + lengthOfSet / steepRatio);

	}

	@Override
	public int getNumberOfBasisFuncs() {
		numberOfSets = 4;
		return numberOfSets * numberOfSets * getNumberOfActions();
	}

	protected int getIndex(int x, int y, Action action) {
		int state = x * numberOfSets + y;
		return state * getNumberOfActions() + getOrdinalAction(action);
	}

	@Override
	protected void updateValue(Reward r, double transitionDelay) {
		stepsToPrey++;
		gridWorld = (GridWorld) environment.getProcedure();
		observationNoise = 0.08;

		currentXPos = gridWorld.getXpos();
		currentYPos = gridWorld.getYpos();

		double df = Math.pow(this.df, transitionDelay);

		P = P.plus(P.times(DiffusionNoise));

		Matrix H1 = new Matrix(getNumberOfBasisFuncs(), 1, 0), H2 = new Matrix(
				getNumberOfBasisFuncs(), 1, 0);

		for (int x = 0; x < numberOfSets; x++)
			for (int y = 0; y < numberOfSets; y++) {
				H1.set(getIndex(x, y, getCurrentAction()), 0,
						fuzzySet[x].getMembership(currentXPos)
								* fuzzySet[y].getMembership(currentYPos) * df);
				H2.set(getIndex(x, y, getPrevAction()), 0,
						fuzzySet[x].getMembership(prevXPos)
								* fuzzySet[y].getMembership(prevYPos) * 1);
			}
		Matrix H = H2.minus(H1);

		double delta = r.getMagnitude() - H.transpose().times(Theta).get(0, 0);
		Matrix P_theta = P.times(H);
		double P_r = H.transpose().times(P).times(H).get(0, 0)
				+ observationNoise;

		K = P_theta.times(1 / P_r);

		if (simple)
			Theta = Theta.plus(H.times(0.1).times(delta));
		else
			Theta = Theta.plus(K.times(delta));
		P = P.minus(K.times(K.transpose()).times(P_r));

		for (State s: getStates()) {
			for (Action i: getActions()) {
				if (getQValue(s, i) != null) {
					((MuVarQValue) getQValue(s, i)).setEmu(Theta.get(
							getStateActionIndex(s, i), 0));
					((MuVarQValue) getQValue(s, i)).SetVarMu(P.get(getStateActionIndex(
							s, i), getStateActionIndex(s, i)));
				}
			}
		}

		prevXPos = currentXPos;
		prevYPos = currentYPos;
	}

	@Override
	public State resolveState(Cue[] cue) {
		return addState(Cue.get(CueNames.InitialState));
	}

	public void logQTable() {
		for (int y = 0; y < numberOfSets / 2; y++)
			for (int x = 0; x < numberOfSets / 2; x++)
				for (Action a: getActions()) {
					System.out.println("x : " + x + " y : " + y + " a : "
							+ a + " value : "
							+ Theta.get(getIndex(x, y, a), 0));
				}

		for (int y = numberOfSets / 2; y < numberOfSets; y++)
			for (int x = numberOfSets / 2; x < numberOfSets; x++)
                for (Action a: getActions()) {
					System.out.println("x : " + (x - numberOfSets / 2)
							+ " y : " + (y - numberOfSets / 2) + " a : "
							+ a + " value : "
							+ Theta.get(getIndex(x, y, a), 0));
				}
	}

	public double getCurrentXPos() {
		return currentXPos;
	}

	public double getCurrentYPos() {
		return currentYPos;
	}

	public Map<Action, Double> getActionValues() {
		Map<Action, Double > values = new HashMap<Action, Double>();

		for (Action a: getActions())
			for (int x = 0; x < numberOfSets; x++)
				for (int y = 0; y < numberOfSets; y++) {
					if (values.get(a) == null)
						values.put(a, 0D);
					values.put(a, values.get(a) + fuzzySet[x].getMembership(currentXPos)
							* fuzzySet[y].getMembership(currentYPos)
							* Theta.get(getIndex(x, y, a), 0));
				}
		return values;
	}

	@Override
	public void newTrial() {
		super.newTrial();
	}

	public int getStepsToPrey() {
		int k = stepsToPrey;
		stepsToPrey = 0;
		return k;
	}
}
