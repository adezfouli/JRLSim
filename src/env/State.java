package env;

import java.util.HashMap;

public class State implements Comparable{
	
	/**
	 * A hash map for memorizing different states, each time a non-existing state is requested 
	 * the system allocated it in this map,and from time to end it is gotten from here. 
	 */
	private static HashMap<Integer, State> states = new HashMap<Integer, State>();
	
	public int stateNumber;

	public static State endState = new State(-1);
	
	private State(int stateNumber) {
		super();
		this.stateNumber = stateNumber;
	}

//	public int getStateNumber() {
//		return stateNumber;
//	}

	public static State getState(int stateNumber) {
		State state = states.get(stateNumber);
		if (state == null){
			state = new State(stateNumber);
			states.put(stateNumber, state);
		}
		return state;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + stateNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final State other = (State) obj;
		if (stateNumber != other.stateNumber)
			return false;
		return true;
	}
	
	public String toString() {
		return ""+ stateNumber;
	}

	public int compareTo(Object state2) {
		if (stateNumber < ((State)state2).stateNumber)
			return -1;
		if (stateNumber > ((State)state2).stateNumber)
			return 1;
		return 0;
	}
}
