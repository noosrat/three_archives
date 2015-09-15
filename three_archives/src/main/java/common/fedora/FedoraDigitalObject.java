package common.fedora;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class FedoraDigitalObject {
	private String pid; //^([A-Za-z0-9]|-|\.)+:(([A-Za-z0-9])|-|\.|~|_|(%[0-9A-F]{2}))+$
	private InputStream xmlRepresentation; //getobjectxml
	private List<String> versionHistory; //getobjecthistory
	private Date dateCreated;
	private Date dateLastModified;
	private State state;
	private List<Datastream> datastreams; //getdatastreams..and then populate further
	
	

	//check this before creating	System.out.println(id.matches("^([A-Za-z0-9]|-|\\.)+:(([A-Za-z0-9])|-|\\.|~|_|(%[0-9A-F]{2}))+$"));
	//in fedora there is always a default DC datastream
	
	public FedoraDigitalObject(String pid){
		this.pid = pid;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public InputStream getXmlRepresentation() {
		return xmlRepresentation;
	}

	public void setXmlRepresentation(InputStream xmlRepresentation) {
		this.xmlRepresentation = xmlRepresentation;
	}

	public List<String> getVersionHistory() {
		return versionHistory;
	}

	public void setVersionHistory(List<String> versionHistory) {
		this.versionHistory = versionHistory;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public List<Datastream> getDatastreams() {
		return datastreams;
	}

	public void setDatastreams(List<Datastream> datastreams) {
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
				+ xmlRepresentation + ", versionHistory=" + versionHistory
				+ ", dateCreated=" + dateCreated + ", dateLastModified="
				+ dateLastModified + ", state=" + state + ", datastreams="
				+ datastreams + "]";
	}


	

}
