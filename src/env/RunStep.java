package env;

public abstract class RunStep {
	
	protected Environment environment;
	
	public RunStep(Environment environment) {
		super();
		this.environment = environment;
	}
	
	public RunStep() {
	}
	
	public abstract void run(int time);

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
