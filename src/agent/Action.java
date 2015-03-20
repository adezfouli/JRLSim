package agent;


import action.PrimitiveAction;

public abstract class Action {
    private double cost;
    protected boolean isExploratory;
    private double latency;
    protected int ordinal;

    public abstract boolean isTerminated(State state);

    public abstract PrimitiveAction getPrimitiveAction();

    public Action(int ordinal) {
        this();
        this.ordinal = ordinal;
    }

    public Action() {
        isExploratory = false;
    }


    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isExploratory() {
        return isExploratory;
    }

    public void setExploratory(boolean isExploratory) {
        this.isExploratory = isExploratory;
    }

    public double getLatency() {
        return latency;
    }

    public void setLatency(double latency) {
        this.latency = latency;
    }

    public final int ordinal() {
        return ordinal;
    }

    public void reset(){}

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action)) return false;

        Action action = (Action) o;

        if (ordinal != action.ordinal) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ordinal;
    }
}
