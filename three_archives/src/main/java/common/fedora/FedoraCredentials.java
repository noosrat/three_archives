package common.fedora;

public class FedoraCredentials {
	private static final String url = "http://localhost:8080/fedora";
	private static final String username = "fedoraAdmin";
	private static final String password ="fedoraAdmin";

	public static String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}


}
