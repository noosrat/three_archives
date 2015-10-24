package common.fedora;

/**
 * The {@code DublinCore} enum contains values mapping to the Dublin Core fields
 * as per the Dublin Core metadata standards. These fields will be used to
 * retrieve and correctly map data contained within the
 * {@link DublinCoreDatastream}
 *
 * @author mthnox003
 *
 */
public enum DublinCore {
	PID("pid"), LABEL("label"), STATE("state"), OWNER_ID("ownerId"), C_DATE(
			"cDate"), M_DATE("mDate"), DCM_DATE("dcmDate"), TITLE("title"), CREATOR(
			"creator"), SUBJECT("subject"), DESCRIPTION("description"), PUBLISHER(
			"publisher"), CONTRIBUTOR("contributor"), DATE("date"), TYPE("type"), FORMAT(
			"format"), IDENTIFIER("identifier"), SOURCE("source"), LANGUAGE(
			"language"), RELATION("relation"), COVERAGE("coverage"), RIGHTS(
			"rights");

	/**
	 * The {@link String} instance representing the description of the Dublin
	 * Core metadata field
	 */
	private String description;

	/**
	 * Constructor
	 * 
	 * @param type
	 *            {@link String} instance representing the description of the
	 *            Dublin Core metadata field
	 */
	private DublinCore(String description) {
		this.description = description;
	}

	/**
	 * Gets the description of the dublin core metadata field
	 * 
	 * @return {@link String} instance representing the description fo the
	 *         dublin core metadata field
	 */

	public String getDescription() {
		return description;
	}

	/**
	 * This method parses the description into a DublinCore metadata field
	 * 
	 * @param type
	 *            {@link String} instance of the description of the metadata
	 *            field
	 * @return {@link DublinCore} instance that the description maps to
	 * @return null if there is no match
	 */
	public static DublinCore parseDescription(String type) {
		for (DublinCore id : DublinCore.values()) {
			if (type.equalsIgnoreCase(id.getDescription())) {
				return id;
			}
		}
		return null;
	}

}
