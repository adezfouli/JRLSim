package agent;

public class AgentInternalConstants {

    protected double averageRewardLR;
    protected double maxTonicDA;
    protected double maxDS;
    protected double df;
    protected double learningRate;
    protected double rewardBasalLevelCoefficient;
    protected double timeOfOneCalculation;
    protected double timeOfDeliberation ;
    public double Cv = 0;
    public double Cu = 0;

    public double getAvgRewardLR() {
	return averageRewardLR;
    }

    public void setAverageRewardLR(double averageRewardLR) {
	this.averageRewardLR = averageRewardLR;
    }

    public double getMaxTonicDA() {
	return maxTonicDA;
    }

    public void setMaxTonicDA(double maxTonicDA) {
	this.maxTonicDA = maxTonicDA;
    }

    public double getMaxDS() {
	return maxDS;
    }

    public void setMaxDS(double maxDS) {
	this.maxDS = maxDS;
    }

    public double getDf() {
	return df;
    }

    public void setDf(double df) {
	this.df = df;
    }

    public double getLR() {
	return learningRate;
    }

    public void setLearningRate(double learningRate) {
	this.learningRate = learningRate;
    }

    public double rewardBasalLevelCoefficient() {
	return rewardBasalLevelCoefficient;
    }

    public double getCv() {
	return Cv;
    }

    public void setCv(double cv) {
	Cv = cv;
    }

    public double getCu() {
	return Cu;
    }

    public void setCu(double cu) {
	Cu = cu;
    }
    
    public double getTimeOfOneCalculation() {
		return timeOfOneCalculation;
	}

	public double getTimeOfDeliberation() {
		return timeOfDeliberation;
	}

	public void setTimeOfDeliberation(double timeOfDeliberation) {
		this.timeOfDeliberation = timeOfDeliberation;
	}

    public double getDelayLR() {
        return 0.1;
    }
}
