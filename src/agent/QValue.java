package agent;

import java.io.Serializable;

import reward.Reward;

public abstract class QValue implements Serializable{

	protected Agent agent;
	public abstract double Emu();
	public abstract QValue copy() ;
	
	/**
	 * updates Q value.
	 * @param r the reward that the Q value should be updated accordingly.
	 * @param q next state in the Markov chain
	 * @param df discounting factor
	 * @param delay transition delay
	 * @return hedonic value experienced by the agent.
	 */
	public abstract double update(Reward r, QValue q, double df, double delay);
	public QValue(Agent agent) {
		super();
		this.agent = agent;
	}
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	
}