package common.fedora;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.http.MediaType;


public class Datastream {
	private String pid;
	private DatastreamId datastreamIdentifier;
	private String label;
	private String creation;
	private String versionID;
	private State state;
	private MediaType mediaType;
	private String formatURI;
	private String controlGroup;
	private Integer size;
	private boolean versionable;
	//look at location type: internal versus not..
	private String location;
	private String content;
	private ArrayList<Datastream> versionHistory;
	
	public Datastream(){
		
	}
	
	
	public Datastream(String pid, DatastreamId datastreamIdentifier){
		this.pid = pid;
		this.datastreamIdentifier = datastreamIdentifier;
	}

	

	public Datastream(String pid, DatastreamId datastreamIdentifier,
			String label, String creation, String versionID, State state,
			MediaType mediaType, String formatURI, String controlGroup,
			Integer size, boolean versionable, String location, String content,
			ArrayList<Datastream> versionHistory) {
		super();
		this.pid = pid;
		this.datastreamIdentifier = datastreamIdentifier;
		this.label = label;
		this.creation = creation;
		this.versionID = versionID;
		this.state = state;
		this.mediaType = mediaType;
		this.formatURI = formatURI;
		this.controlGroup = controlGroup;
		this.size = size;
		this.versionable = versionable;
		this.location = location;
		this.content = content;
		this.versionHistory = versionHistory;
	}

	public Datastream(Datastream datastream){
		super();
		this.pid = datastream.getPid();
		this.datastreamIdentifier = datastream.getDatastreamIdentifier();
		this.label = datastream.getLabel();
		this.creation = datastream.getCreation();
		this.versionID = datastream.getVersionID();
		this.state = datastream.getState();
		this.mediaType = datastream.getMediaType();
		this.formatURI = datastream.getFormatURI();
		this.controlGroup = datastream.getControlGroup();
		this.size = datastream.getSize();
		this.versionable = datastream.isVersionable();
		this.location = datastream.getLocation();
		this.content = datastream.getContent();
		this.versionHistory = datastream.getVersionHistory();
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}



	public DatastreamId getDatastreamIdentifier() {
		return datastreamIdentifier;
	}

	public void setDatastreamIdentifier(DatastreamId datastreamIdentifier) {
		this.datastreamIdentifier = datastreamIdentifier;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCreation() {
		return creation;
	}

	public void setCreation(String creation) {
		this.creation = creation;
	}

	public String getVersionID() {
		return versionID;
	}

	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public String getFormatURI() {
		return formatURI;
	}

	public void setFormatURI(String formatURI) {
		this.formatURI = formatURI;
	}

	public String getControlGroup() {
		return controlGroup;
	}

	public void setControlGroup(String controlGroup) {
		this.controlGroup = controlGroup;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public boolean isVersionable() {
		return versionable;
	}

	public void setVersionable(boolean versionable) {
		this.versionable = versionable;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContent() {
		return "http://localhost:8080/fedora/objects/" + getPid() + "/datastreams/" + getDatastreamIdentifier() + "/content";
	}


	public ArrayList<Datastream> getVersionHistory() {
		return versionHistory;
	}

	public void setVersionHistory(ArrayList<Datastream> versionHistory) {
		this.versionHistory = versionHistory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((datastreamIdentifier == null) ? 0 : datastreamIdentifier
						.hashCode());
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Datastream))
			return false;
		Datastream other = (Datastream) obj;
		if (datastreamIdentifier != other.datastreamIdentifier)
			return false;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Datastream [pid=" + pid + ", datastreamIdentifier="
				+ datastreamIdentifier + ", label=" + label + ", creation="
				+ creation + ", versionID=" + versionID + ", state=" + state
				+ ", mediaType=" + mediaType + ", formatURI=" + formatURI
				+ ", controlGroup=" + controlGroup + ", size=" + size
				+ ", versionable=" + versionable + ", location=" + location
				+ ", content=" + content + ", versionHistory=" + versionHistory
				+ "]";
	}
	
	

	
	
	
	
	
	
	
	

	
}
