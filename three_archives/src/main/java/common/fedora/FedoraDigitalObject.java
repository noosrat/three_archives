package common.fedora;

import java.util.ArrayList;
import java.util.Date;

public class FedoraDigitalObject {
	private String pid; //^([A-Za-z0-9]|-|\.)+:(([A-Za-z0-9])|-|\.|~|_|(%[0-9A-F]{2}))+$
	private String xmlRepresentation; //getobjectxml
	private ArrayList<String> versionHistory; //getobjecthistory
	private String dateCreated;
	private String dateLastModified;
	private State state;
	private ArrayList<Datastream> datastreams; //getdatastreams..and then populate further
	
	

	//check this before creating	System.out.println(id.matches("^([A-Za-z0-9]|-|\\.)+:(([A-Za-z0-9])|-|\\.|~|_|(%[0-9A-F]{2}))+$"));
	//in fedora there is always a default DC datastream
	
	public FedoraDigitalObject(String pid){
		this.pid = pid;
		this.dateCreated = ""; //unless we throw this at fedora and then it gives us the date?
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getXmlRepresentation() {
		return xmlRepresentation;
	}

	public void setXmlRepresentation(String xmlRepresentation) {
		this.xmlRepresentation = xmlRepresentation;
	}

	public ArrayList<String> getVersionHistory() {
		return versionHistory;
	}

	public void setVersionHistory(ArrayList<String> versionHistory) {
		this.versionHistory = versionHistory;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(String dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public ArrayList<Datastream> getDatastreams() {
		return datastreams;
	}

	public void setDatastreams(ArrayList<Datastream> datastreams) {
		this.datastreams = datastreams;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
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
		FedoraDigitalObject other = (FedoraDigitalObject) obj;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FedoraDigitalObject [pid=" + pid + ", xmlRepresentation="
				+ xmlRepresentation + ", dateCreated=" + dateCreated
				+ ", dateLastModified=" + dateLastModified + "]";
	}
	

}
