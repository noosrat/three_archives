package common.fedora;

import java.util.HashMap;

public class DublinCoreDatastream extends Datastream {

	//we have the dublin core metadata fields within this...maybe it should just be an array list? or map...we need to parse the content
	
	private HashMap<String,String> dublinCoreMetadata;

	
	public DublinCoreDatastream(String pid) {
		super(pid, DatastreamID.DC);
	}
	
	public DublinCoreDatastream(Datastream datastream) {
		super(datastream);
	}

	public DublinCoreDatastream(String pid,HashMap<String,String> metadata){
		this(pid);
		this.dublinCoreMetadata = metadata;
		
	}

	public void setDublinCoreMetadata(HashMap<String, String> dublinCoreMetadata) {
		this.dublinCoreMetadata = dublinCoreMetadata;
	}

	public HashMap<String,String> getDublinCoreMetadata(){
		return dublinCoreMetadata;
	}
	
	

	@Override
	public String toString() {
		String result = "";
		for (String dc: dublinCoreMetadata.keySet()){
			result += dc +": " + dublinCoreMetadata.get(DublinCore.valueOf(dc)) + "\n";
		}
		return super.toString() + "Dublin core fields \n " + result;
	}
	
	
	
}
