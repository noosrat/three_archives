package search;


/**
 * The {@code SearchAndBrowseCategory} enumeration contains all the options
 * available for the search categories and the browsing categories on the
 * frontend.
 * 
 * @author mthnox003
 *
 */
public enum SearchAndBrowseCategory {
	SEARCH_ALL(), COLLECTION("dc.description"), CREATOR("dc.creator"), CONTRIBUTOR(
			"dc.contributor"), SOURCE("dc.source"), EVENT("dc.description"),PUBLISHER("dc.publisher"),

	DESCRIPTION("dc.description"), LOCATION("dc.location"), FORMAT("dc.format"), TYPE(
			"dc.type"), TITLE("dc.title"), SUBJECT("dc.subject"), YEAR(
			"dc.date");

	/**
	 * The {@link String} instance indicating the corresponsponding dublinCore
	 * field as per the Dublin Core Datastream XML representation
	 */
	private String dublinCoreField;

	/**
	 * Constructor Default constructor for the enumeration
	 */
	private SearchAndBrowseCategory() {

	}

	/**
	 * Constructor setting the dublinCoreField
	 * 
	 * @param dublinCoreField
	 *            {@link String} instance representing the dublin core metadata
	 *            field
	 */
	private SearchAndBrowseCategory(String dublinCoreField) {
		this.dublinCoreField = dublinCoreField;
	}

	/**
	 * Gets the dublin core field instance
	 * 
	 * @return {@link String} instance representing the dublin core field
	 */
	public String getDublinCoreField() {
		return dublinCoreField;
	}

}
