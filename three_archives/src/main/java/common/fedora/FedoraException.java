package common.fedora;

/**
 * The {@code FeodraException} will be thrown whenever issues are experienced
 * with connecting to or communicating with the Fedora RESTful API or when the
 * response returned by the RESTful service is anything other than 200
 * 
 * @author mthnox003
 */
public class FedoraException extends Exception {

	/**
	 * SerialVersionUID for the object
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 * 
	 * 
	 */
	public FedoraException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified {@link Throwable} cause.
	 * 
	 * @param cause
	 */
	public FedoraException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with the specified {@link Throwable} cause and
	 * specified detail message.
	 * 
	 * @param message
	 * @param cause
	 */
	public FedoraException(String message, Throwable cause) {
		super(message);
	}

}
