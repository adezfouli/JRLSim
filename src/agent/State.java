package agent;

import java.io.Serializable;

public abstract class State implements Serializable {

	private static final long serialVersionUID = -7961058278815146435L;
	// States related to addiction

	boolean isTerminal;
	
    protected int ordinal;

	protected State(){
		isTerminal = false;
	}
	
	protected State(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}

    public int ordinal() {
        return ordinal;
    }


	@Override
	public abstract String toString();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;

        State that = (State) o;

        if (ordinal != that.ordinal) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ordinal;
    }
}
