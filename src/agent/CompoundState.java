package agent;

import env.Cue;

public class CompoundState extends State {

	private static final long serialVersionUID = -4990942975895234496L;

	double[] stateVector;
	
	private Cue[] cues;

	public CompoundState(Cue[] cues) {
		this.cues = cues;
		stateVector = new double[Cue.values().length];
		for (Cue cue : cues) {
			stateVector[cue.ordinal()] = 1;
		}
	}

	@Override
	public int ordinal() {
		return 0;
	}

	@Override
	public String toString() {
		return "MultipleStateVector";
	}

	public double[] getStateVector() {
		return stateVector;
	}

	public Cue[] getCues() {
		return cues;
	}

	public void setCues(Cue[] cues) {
		this.cues = cues;
	}
	
	
}
