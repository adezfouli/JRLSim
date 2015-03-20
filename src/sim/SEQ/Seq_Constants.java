package sim.SEQ;

import agent.AgentInternalConstants;

public class Seq_Constants extends AgentInternalConstants {

    private double seqLR;

    public Seq_Constants() {
		averageRewardLR = 0.002; // sigma
		learningRate = 0.05; // period of the signal
        seqLR = 0.001;
	}

    public double getSeqLR() {
        return seqLR;
    }
}
