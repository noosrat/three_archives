package common.fedora;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * The {@code FedoraDigitalObject} class maps to the digital objects contained
 * within the Fedora digital object repository. Additional information on the
 * nature of the objects and fields being mapped can be found at {@link } This
 * class will be used as a front-end representation of objects represented in
 * the digital object repository
 * 
 * @author mthnox003
 *
 */
public class FedoraDigitalObject {
	/**
	 * The {@link String} instance representing the unique persistent identifier
	 * for the digital object.
	 */
	private String pid;
	/**
	 * The {@link InputStream} instance representing the entire XML
	 * representation of the digital object. This is mapped using the
	 * {@link FedoraGetRequest.getObjectXML} method.
	 */
	private InputStream xmlRepresentation;

	/**
	 * The {@link List<String>} instance representing the different version of
	 * the fedora digital object as retrieved using the
	 * {@link FedoraGetRequest.getObjectHistory} method. This is a list of
	 * component creation and modification dates
	 */
	private List<String> versionHistory;
	/**
	 * The {@link Date} instance representing the date on which the digital
	 * object was created
	 */
	private Date dateCreated;
	/**
	 * The {@link Date} instance representing the date on which the digital
	 * object was last modified. Date format yyyy-MM-dd HH:mm:ss.SSS
	 */
	private Date dateLastModified;

	/**
	 * The {@link State} instance representing the state of the fedora digital
	 * object. Date format yyyy-MM-dd HH:mm:ss.SSS
	 */
	private State state;
	/**
	 * The {@link HashMap} instance representing the datasreams contained within
	 * the object. The Key value is indicated by the {@link DatastreamID} name
	 * attribute and the value is the actual {@link Datastream}
	 */
	private HashMap<String, Datastream> datastreams;

	/**
	 * Constructor for the digital object using the persistent id (pid) for the
	 * object. All FedoraDigtialObjects at least have a persistent identifier
	 * and it is unique
	 * 
	 * @param pid
	 *            {@link String} instance representing the pid of the object
	 */
	public FedoraDigitalObject(String pid) {
		this.pid = pid;
	}

	/**
	 * Gets the pid of the object
	 * 
	 * @return {@link String} instance representing the pid of the object
	 */

	public String getPid() {
		return pid;
	}

	/**
	 * 
	 * Sets the pid of the object
	 * 
	 * @param pid
	 *            {@link String} instance representing the pid of the object
	 * 
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}

	/**
	 * Gets the XML representation of the fedora digital object
	 * 
	 * @return {@link InputStream} instance representing the XML form of the
	 *         digital object
	 */
	public InputStream getXmlRepresentation() {
		return xmlRepresentation;
	}

	/**
	 * Sets the XML representation of the fedora digital object
	 * 
	 * @param xmlRepresentation
	 *            {@link InputStream} instance representing the XML form of the
	 *            digital object
	 */
	public void setXmlRepresentation(InputStream xmlRepresentation) {
		this.xmlRepresentation = xmlRepresentation;
	}

	/**
	 * Gets the version history of the digital object. This is a list of
	 * component creation and modification dates
	 * 
	 * @return {@link List} the versionHistory of the object
	 */
	public List<String> getVersionHistory() {
		return versionHistory;
	}

	/**
	 * Sets the version history of the digital object. This is a list of
	 * component creation and modification dates
	 * 
	 * @param versionHistory
	 *            {@link List} representing the version history for the object
	 */
	public void setVersionHistory(List<String> versionHistory) {
		this.versionHistory = versionHistory;
	}

	/**
	 * Gets the date that the digital object was created on of the format i.e.
	 * 2014-10-09T08:54:26.980Z
	 * 
	 * @return {@link Date} instance representing the date the object was
	 *         created on
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * Sets the date that the digital object was created on
	 * 
	 * @param dateCreated
	 *            {@link Date} instance representing the date the object was
	 *            created on
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * Gets the date that the digital object was last modified on on of the
	 * format i.e. 2014-10-09T08:54:26.980Z
	 * 
	 * @return {@link Date} instance representing the date the object was last
	 *         modified on
	 */
	public Date getDateLastModified() {
		return dateLastModified;
	}

	/**
	 * Sets the date that the digital object was last modified on
	 * 
	 * @param dateLastModified
	 *            {@link Date} instance representing the date the object was
	 *            last modified on
	 */
	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	/**
	 * Gets the state of the digital object
	 * 
	 * @return {@link State} instance representing the state of the digital
	 *         object
	 */
	public State getState() {
		return state;
	}

	/**
	 * Sets the state of the digital object
	 * 
	 * @param state
	 *            {@link State} instance representing the state of the digital
	 *            object
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Gets the list of Datastreams contained within the digital object
	 * 
	 * @return {@link HashMap} instance holding the datastreams of the digital
	 *         object
	 */
	public HashMap<String, Datastream> getDatastreams() {
		return datastreams;
	}

	/**
	 * Sets the list of Datastreams contained within the digital object
	 * 
	 * @param datastreams
	 *            {@link HashMap} instance holding the datastreams of the
	 *            digital object
	 */
	public void setDatastreams(HashMap<String, Datastream> datastreams) {
		this.datastreams = datastreams;
	}

	/**
	 * Hashcode for the object calculated on the {@link #pid}
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		return result;
	}

	/**
	 * Equals for the object calculated on the {@link #pid}
	 */

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

	/**
	 * @return {@link String} representation of the object
	 */
	@Override
	public String toString() {
		return "FedoraDigitalObject [pid=" + pid + ", xmlRepresentation=" + xmlRepresentation + ", versionHistory="
				+ versionHistory + ", dateCreated=" + dateCreated + ", dateLastModified=" + dateLastModified
				+ ", state=" + state + ", datastreams=" + datastreams + "]";
	}

}
