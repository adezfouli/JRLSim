package agent.SeqRL;


import agent.State;

public class AuxiliaryState extends State {

    private  String name;

    private State ppState;

    public AuxiliaryState(State ppState, String name){
        this.ppState = ppState;
        this.name = name;
    }

    public State getPpState() {
        return ppState;
    }

    @Override
    public String toString() {
        return "Auxiliary State " + name;
    }
}
