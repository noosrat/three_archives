package common.fedora;

public enum DatastreamID {
	DC("dublin core"),IMG("image"),IMG_PUB("image"),VID("video"),AUD("audio"), PDF("pdf");
	
	private String description;
	
	private DatastreamID(String type){
		this.description = type;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	
	public static DatastreamID parseDescription(String type){
		for (DatastreamID id: DatastreamID.values()){
			if (type.equalsIgnoreCase(id.getDescription())){
				return id;
			}
		}
		return null;
	}
}
