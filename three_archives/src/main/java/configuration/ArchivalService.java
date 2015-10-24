package configuration;

/**
 * The {@code ArchivalService} enumeration contains all the services available
 * for the archives within the application.
 * 
 * @author mthnox003
 *
 */
public enum ArchivalService {
	SEARCH_AND_BROWSE("Browse", "searchandbrowse", "browse"), HISTORY_AND_STATISTICS(
			"History", "historyandstatistics", "history"), EXHIBITIONS(
			"Exhibitions", "exhibitions", "redirect_exhibitions"), UPLOADS(
			"Uploads", "uploads", "redirect_uploads"), MAPS("Maps", "maps",
			"redirect_maps"), DOWNLOADS("Downloads", "downloads",
			"redirect_downloads"), ANNOTATIONS("Annotations", "annotations", "");

	/**
	 * The {@link String} instance representing the name to appear on the
	 * interface to represent the specific service
	 */
	private String interfaceName;
	/**
	 * The {@link String} property mapping to the name of the property within
	 * the .properties files
	 */
	private String property;
	/**
	 * The {@link String} instance representing the action to forward to the
	 * browser on invoking the specific service
	 */
	private String redirect_url;

	/**
	 * Constructor initialising the ArchivalService
	 * 
	 * @param interfaceName
	 *            (@link String} instance representing the interface name
	 *            representation of the service
	 * @param property
	 *            {@link String} instance representing the property name within
	 *            the .properties files
	 * @param redirect_url
	 *            {@link String} instance representin the URL to be directed to
	 *            on invoking the action
	 */
	private ArchivalService(String interfaceName, String property,
			String redirect_url) {
		this.interfaceName = interfaceName;
		this.property = property;
		this.redirect_url = redirect_url;
	}

	/**
	 * Gets the {@link #interfaceName} of the ArchivalService
	 * 
	 * @return {@link String} representation of the interface name
	 */
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	 * Gets the {@link #property} of the ArchivalService
	 * 
	 * @return {@link String} representation of the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Gets the {@link #redirect_url} of the ArchivalService
	 * 
	 * @return {@link String} representation of the redirect_url
	 */
	public String getRedirect_url() {
		return redirect_url;
	}

	/**
	 * This parses the property into the actual ArchivalService
	 * 
	 * @param property
	 *            {@link String} instance representing the property to be parsed
	 * @return {@link ArchivalService} instance of the parsed property
	 */
	public static ArchivalService parseServiceProperty(String property) {
		if (property != null) {
			for (ArchivalService service : ArchivalService.values()) {
				if (service.getProperty().equalsIgnoreCase(property)) {
					return service;
				}
			}
		}
		return null;
	}

}
