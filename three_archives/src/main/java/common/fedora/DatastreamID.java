package common.fedora;

/**
 * 
 * The {@code DatastreamID} enum represents the different options available for
 * datastream identification in teh fedora object. These options will be used
 * during creation and retrieval of the datastreams of the digital object Each
 * digital object can only have one of each type of datastream
 * 
 * @author mthnox003
 *
 */
public enum DatastreamID {
	DC("DUBLIN CORE"), IMG("IMAGE"), IMG_LOW_RES("IMAGE"), VID("VIDEO"), AUD(
			"AUDIO"), PDF("PDF");

	/**
	 * The {@link String} instance representing the description of the
	 * Datastream ID
	 */
	private String description;

	/**
	 * Constructor
	 * 
	 * @param type
	 *            {@link String} instance representing the description of the
	 *            datastream ID
	 */
	private DatastreamID(String type) {
		this.description = type;
	}

	/**
	 * Gets the datastream ID description
	 * 
	 * @return {@link String} instance representing the description of the
	 *         datastream ID
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * This method parses the description into a datastream ID object
	 * 
	 * @param type
	 *            {@link String} instance representing the description of the
	 *            datastream
	 * @return {@link DatastreamID} which corresponds to the description
	 *         specified
	 */
	public static DatastreamID parseDescription(String type) {
		for (DatastreamID id : DatastreamID.values()) {
			if (type.equalsIgnoreCase(id.getDescription())) {
				return id;
			}
		}
		return null;
	}

	/**
	 * This method takes in any format and parses it to the necessary
	 * datastreamID. This is since multimedia types may have many formats but
	 * still fall under the same datastreamID
	 * 
	 * @param format
	 *            {@link String} representation of the datastream format to
	 *            parse
	 * @return {@link DatastreamID} instance mapping to the specified format
	 * @return {@link null} if the entered format does not map to any datastream
	 *         ID
	 */
	public static DatastreamID parseMediaType(String format) {
		if (format != null) {
			String type = format.toLowerCase();
			if (type.contains("image") || type.contains("img")
					|| type.contains("jpeg") || type.contains("jpg")
					|| type.contains("png") || type.contains("gif")) {
				return DatastreamID.IMG;
			} else if (type.contains("vid") || type.contains("3gp")
					|| type.contains("mp4") || type.contains("mpeg4")) {
				return DatastreamID.VID;

			} else if (type.contains("mp3") || type.contains("aud")
					|| type.contains("flac")) {
				return DatastreamID.AUD;
			} else if (type.contains("pdf") || type.contains("txt")) {
				return DatastreamID.PDF;
			}
		}

		return null;
	}
}
