package common.fedora;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.http.MediaType;


public class Datastream {
	private String pid;
	private String datastreamIdentifier;
	private String label;
	private Date creation;
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
	
	public Datastream(String pid, String datastreamIdentifier){
		this.pid = pid;
		this.datastreamIdentifier = datastreamIdentifier;
	}


	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}



	public String getDatastreamIdentifier() {
		return datastreamIdentifier;
	}

	public void setDatastreamIdentifier(String datastreamIdentifier) {
		this.datastreamIdentifier = datastreamIdentifier;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
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
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Datastream other = (Datastream) obj;
		if (datastreamIdentifier == null) {
			if (other.datastreamIdentifier != null)
				return false;
		} else if (!datastreamIdentifier.equals(other.datastreamIdentifier))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Datastream [datastreamIdentifier=" + datastreamIdentifier
				+ ", label=" + label + ", accessURL=" + content + "]";
	}
	
	
	
	
	
	
	
	

	
}
