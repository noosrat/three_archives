package configuration;

public enum ArchivalService {
	SEARCH_AND_BROWSE("Browse", "searchandbrowse", "browse"), HISTORY_AND_STATISTICS("History", "historyandstatistics",
			" "), EXHIBITIONS("Exhibitions", "exhibitions", "redirect_exhibitions"), UPLOADS("Uploads", "uploads",
					"redirect_uploads"), MAPS("Maps", "maps", "redirect_maps"), DOWNLOADS("Downloads", "downloads",
							"redirect_downloads"), ANNOTATIONS("Annotations", "annotations", "");

	private String interfaceName;
	private String property;
	private String redirect_url;

	private ArchivalService(String interfaceName, String property, String redirect_url) {
		this.interfaceName = interfaceName;
		this.property = property;
		this.redirect_url = redirect_url;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public String getProperty() {
		return property;
	}

	public String getRedirect_url() {
		return redirect_url;
	}
	
	public static ArchivalService parseServiceProperty(String property){
		if (property!=null){
			for (ArchivalService service: ArchivalService.values()){
				if (service.getProperty().equalsIgnoreCase(property)){
					return service;
				}
			}
		}
		return null;
	}

}
