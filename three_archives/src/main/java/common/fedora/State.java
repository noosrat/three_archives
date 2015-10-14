package common.fedora;

/**
 * 
 * The {@code State} class indicates the state of the {@link Datastream} and {@link FedoraDigitalObject} as
 * indicated in the Fedora repository
 *
 */
public enum State {
	A("Active"), I("Inactive"), D("Deleted");

	/**
	 * The {@link String} instance representing the description of the State
	 */
	private String description;

	/**
	 * Constructor initialising the State object using a description
	 * 
	 * @param description {@link String} instance representing the description of the State being initialised
	 */
	private State(String description) {
		this.description = description;
	}

	/**
	 * Gets the description of the datastream state
	 * 
	 * @return {@link String} instance representing the description of the
	 *         {@link Datastream} state
	 */

	public String getDescritpion() {
		return description;
	}
}
