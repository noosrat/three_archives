package common.fedora;

public class FedoraCredentials {
	private static final String url = "http://localhost:8080/fedora";
	private static final String username = "fedoraAdmin";
	private static final String password ="fedoraAdmin";

	public static String getUrl() {
		return url;
	}

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}


}
