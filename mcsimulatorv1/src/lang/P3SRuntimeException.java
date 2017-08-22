package lang;

public class P3SRuntimeException extends RuntimeException {

	public P3SRuntimeException(String message) {
        super(message);
    }

	public P3SRuntimeException(String message, Exception e) {
        super(message, e);
    }

}
