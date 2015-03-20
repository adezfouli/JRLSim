package proc;

import java.io.Serializable;

import action.PrimitiveAction;
import reward.Reward;
import reward.RewardValue;

import agent.Agent;
import env.Cue;
import env.State;

public abstract class Procedure implements Serializable {

	private static final long serialVersionUID = 8485166668668045843L;
	
	protected State currentState;
	private int currentTrial;
	protected double delay;
	/**
	 * Inter-trial interval
	 */
	private double ITI;
	/**
	 * Inter step interval
	 */
	private double ISI;
	private int numberOfTrials;
	protected Agent agent;
	protected RewardValue rewardValue;

	protected Procedure(Agent agent, RewardValue rewardValue,
			int maxNumberOfTrials) {
		this(rewardValue, maxNumberOfTrials);
		this.agent = agent;
	}

	
	protected Procedure(RewardValue rewardValue, int numberOfTrials) {
		this.numberOfTrials = numberOfTrials;
		currentState = State.getState(0);
		this.rewardValue = rewardValue;
		this.rewardValue.setProcedure(this);
		currentTrial = 0;
		ITI = 1;
		ISI = 1;
	}

	/**
	 * Updates state of the procedure according to the action has been taken by
	 * the agent. The function is supposed to update delay and currentState
	 * fields.
	 * 
	 * @param action
	 *            action that the agent has been taking
	 * @return reward that the agent gains
	 */
	public abstract Reward nextState(PrimitiveAction action);

	public boolean isPossible(PrimitiveAction action) {
		return isPossible(currentState, action);
	}

	protected void newTrial() {
		currentTrial++;
		agent.newTrial();
	}

	public abstract boolean isPossible(State currentState, PrimitiveAction action);

	/**
	 * Calculates cues that the agent observes being in the current state.
	 * 
	 * @return cues that the agent observes.
	 */
	public abstract Cue[] getCue();

	/**
	 * Delay of the transition delay for last action executed.
	 * 
	 * @return transition delay.
	 */
	public double getDelay() {
		return delay;
	}

	public double getITI() {
		return ITI;
	}

	public double getISI() {
		return ISI;
	}

	public void setITI(double iti) {
		ITI = iti;
	}

	public boolean isFinished() {
		return currentState == null || currentTrial > numberOfTrials;
	}

	public int getCurrentTrial() {
		return currentTrial;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setNumberOfTrials(int maxNumberOfTrials) {
		this.numberOfTrials = maxNumberOfTrials;
	}

	public int getNumberOfTrials() {
		return numberOfTrials;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	public Cue getAlwaysVisitingCue(){
		return null;
	}
	
	public void setISI(double isi) {
		ISI = isi;
	}
	
	public RewardValue getRewardValue() {
		return rewardValue;
	}
	
	public void setCurrentState(State state){
		currentState = state;
	}
}
