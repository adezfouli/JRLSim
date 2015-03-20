package proc;

public class BRLException extends RuntimeException {

	public BRLException(Throwable e) {
		super(e);
	}
	
	public BRLException(String message) {
		super(message);
	}
}
