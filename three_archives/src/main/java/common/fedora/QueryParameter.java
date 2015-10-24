package common.fedora;

/**
 * 
 * The {@code QueryParameters} are used in order to specify the
 * {@link FedoraGetRequest} queryParameters dependent on what query is being
 * made and what parameters are required.
 *
 * @author mthnox003
 */
public enum QueryParameter {
	TERMS("terms"), QUERY("query"), MAX_RESULTS("maxResults"), RESULT_FORMAT(
			"resultFormat"), AS_OF_DATE_TIME("asOfDateTime"), DOWNLOAD(
			"download"), FORMAT("format"), PROFILES("profiles"), SUBJECT(
			"subject"), PREDICATE("predicate"), VALIDATE_CHECKSUM(
			"validateChecksum");

	/**
	 * The {@link String} instance representing the description of the query
	 * parameter and the accepted format to be used when accessing the Fedora
	 * RESTful API
	 */
	private String description;

	/**
	 * Constructor initialising the QueryParameter object using a description
	 * 
	 * @param description
	 *            {@link String} instance representing the description of the
	 *            State being initialised
	 */
	private QueryParameter(String description) {
		this.description = description;
	}

	/**
	 * Gets the description of the query parameter
	 * 
	 * @return {@link String} instance representing the description of the query
	 *         parameter
	 */
	public String getDescription() {
		return description;
	}

}
