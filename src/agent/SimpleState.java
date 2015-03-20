package agent;

import env.Cue;

public class SimpleState extends State {

	private static final long serialVersionUID = -3821472314634438755L;

	private Cue cue;

    public SimpleState(Cue cue, int ordinal){
		super();
		this.cue = cue;
        this.ordinal = ordinal;
	}


    public SimpleState(Cue cue){
		super();
		this.cue = cue;
	}

	public SimpleState(int cueOrdinal){
		cue  = Cue.values()[cueOrdinal];
	}


	@Override
	public String toString() {
		return cue.toString();
	}
	
	public static int getNumberOfStates(){
		return Cue.values().length;
	}

	public Cue getCue() {
		return cue;
	}

	public void setCue(Cue cue) {
		this.cue = cue;
	}
}
