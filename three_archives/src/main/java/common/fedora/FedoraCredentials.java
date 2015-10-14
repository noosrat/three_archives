package common.fedora;

/**
 * The {@code FedoraCredentials} class is for the storage and management of
 * credentials to be used to globally to communicate with the Fedora digital
 * object repository.
 */

public class FedoraCredentials {
	/**
	 * The {@link String} representing the URL where the Fedora digital
	 * repository instance is running
	 */
	private static final String URL = "http://localhost:8089/fedora";
	/**
	 * The username used to access the administrative elements of the Fedora
	 * digital repository
	 */
	private static final String USERNAME = "fedoraAdmin";
	/**
	 * The password for the {@code USERNAME} used to access the administrative
	 * elements of the Fedora digital repository
	 */
	private static final String PASSWORD = "3Arch";

	/**
	 * Allows for the retrieval of the {@link String} representing the URL where
	 * the Fedora digital repository instance is running
	 * 
	 * @return {@link String} representing the {@code URL}
	 */
	public static String getUrl() {
		return URL;
	}

	/**
	 * Allows for the retrieval of the Username used to log into the Fedora
	 * repository
	 * 
	 * @return String representing the {@code USERNAME}
	 */

	public static String getUsername() {
		return USERNAME;
	}

	/**
	 * This allows for the retrieval of the Password used to log into the Fedora
	 * repository
	 * 
	 * @return String representing the {@code PASSWORD}
	 */
	public static String getPassword() {
		return PASSWORD;
	}

}
