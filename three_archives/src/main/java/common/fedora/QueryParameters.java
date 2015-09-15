package common.fedora;

public enum QueryParameters {
	TERMS("terms"), QUERY("query"), MAX_RESULTS("maxResults"), RESULT_FORMAT(
			"resultFormat"), AS_OF_DATE_TIME("asOfDateTime"), DOWNLOAD(
			"download"), FORMAT("format"), PROFILES("profiles"), SUBJECT(
			"subject"), PREDICATE("predicate"), VALIDATE_CHECKSUM("validateChecksum");

	private String description;

	private QueryParameters(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	

}
