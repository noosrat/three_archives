package common.fedora;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.http.MediaType;

/**
 * {@code Datatream} is a class representing a Datastream for the digital
 * objects present within the Fedora digital object repository. This class maps
 * to the fields and values present within the Fedora digital object repository.
 * These fields are defined as in the Fedora Digital object repository.
 * 
 * In detail definitions and explanations of each of these fields can be found
 * {@link https://wiki.duraspace.org/display/FEDORA38/Fedora+Digital+Object+Model#FedoraDigitalObjectModel-Datastreamsdata}
 */
public class Datastream {
	/**
	 * The {@link String} instance representing the unique persistent identifier
	 * for the digital object. This corresponds with the {link
	 * FedoraDigitalObject} that the Datastream belongs to.
	 */
	private String pid;
	/**
	 * The {@link DatastreamID} instance representing the datastream ID for the
	 * specific datastream
	 * 
	 */
	private DatastreamID datastreamID;
	/**
	 * The {@link String} instance representing the datastream label
	 */
	private String label;
	/**
	 * The {@link Date} instance representing the date that the datastream was
	 * created on
	 */
	private Date creation;
	/**
	 * The {@link String} instance representing the version of the datastream.
	 * The Fedora digital object repository allows for each datastream to have
	 * multiple versions
	 */
	private String versionID;
	/**
	 * The {@link State} instance representing the state of the datastream.
	 */
	private State state;
	/**
	 * The {@link MediaType} instance representing the media type of the
	 * datastream.
	 */
	private MediaType mediaType;
	/**
	 * The {@link String} instance representing the format URI of the datastream
	 */
	private String formatURI;
	/**
	 * The {@link String} instance representing the control group of the
	 * datastream
	 */
	private String controlGroup;
	/**
	 * The {@link Integer} instance representing the size in bytes of the
	 * datastream
	 */
	private Integer size;
	/**
	 * The {@link boolean} instance indicating whether the datastream can have
	 * many versions or whether changes just override the existing version
	 */
	private boolean versionable;
	/**
	 * The {@link String} instance representing the URL to the actual content of
	 * the datastream within the Fedora digital object repository
	 */
	private String content;
	/**
	 * The {@link ArrayList} instance holding a collection of the different
	 * versions of the datastream. This will only be populated if the datastream
	 * is versionable and has many versions.
	 */
	private ArrayList<Datastream> versionHistory;
	private String location;

	/**
	 * The parameterless constructor for a Datastream
	 */
	public Datastream() {

	}

	/**
	 * A parameterised constructor for a Datastream.
	 * 
	 * @param pid
	 *            {@link String} representing the persistence identifier for the
	 *            digital object that the Datastream belongs to.  This should be of the structure x:x where x represents any sequence of any alphanumeric character
	 * @param datastreamIdentifier
	 *            instance of {@link DatastreamID} which identifies the type of
	 *            datastream
	 */
	public Datastream(String pid, DatastreamID datastreamIdentifier) {
		this.pid = pid;
		this.datastreamID = datastreamIdentifier;
	}

	/**
	 * A fully parameterised constructor for a Datastream.
	 * 
	 * @param pid
	 *            a {@link String} instance representing the persistence
	 *            identifier for the object
	 * @param datastreamIdentifier
	 *            {@link DatastreamID} representing the datastream identifier
	 * @param label
	 *            a {@link String} instance representing the datastream label
	 * @param creation
	 *            a {@link Date} instance representing the date that the
	 *            datastream was created on
	 * @param versionID
	 *            a {@link String} instance representing the version of the
	 *            datastream.
	 * @param state
	 *            {@link State} instance representing the state of the
	 *            datastream.
	 * @param mediaType
	 *            {@link MediaType} instance representing the type of media of
	 *            the datastream.
	 * @param formatURI
	 *            {@link String} instance representing the format URI of the
	 *            datastream.
	 * @param controlGroup
	 *            {@link String} instance representing the control group of the
	 *            datastream.
	 * @param size
	 *            {@link Integer} instance representing the size of the
	 *            datastream.
	 * @param versionable
	 *            {@link boolean} instance indicating whether the datastream is
	 *            versionable or not
	 * @param location
	 *            {@link String} instance representing the location of the
	 *            datastream.
	 * @param content
	 *            {@link String} instance URL representing the path of the
	 *            datastream content
	 * @param versionHistory
	 *            {@link ArrayList} collection holding different versions of the
	 *            datastreams
	 */
	public Datastream(String pid, DatastreamID datastreamIdentifier, String label, Date creation, String versionID,
			State state, MediaType mediaType, String formatURI, String controlGroup, Integer size, boolean versionable,
			String location, String content, ArrayList<Datastream> versionHistory) {
		super();
		this.pid = pid;
		this.datastreamID = datastreamIdentifier;
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

	/**
	 * A parameterised constructor for a Datastream
	 * 
	 * @param datastream
	 *            an instance of {@link Datastream} representing the entire
	 *            Datastream
	 */
	public Datastream(Datastream datastream) {
		super();
		this.pid = datastream.getPid();
		this.datastreamID = datastream.getDatastreamID();
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

	/**
	 * Gets the persistent identifier of the datastream
	 * 
	 * @return the persistent identifier (pid) for the object
	 * 
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * Sets the persistent identifier of the datastream
	 * 
	 * @param pid
	 *            {@link String} representing the persistent identifier for the
	 *            object
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}

	/**
	 * Gets the datastreamID of the datastream
	 * 
	 * @return {@link DatastreamID} instance representing the datastream ID of
	 *         the datastream
	 */
	public DatastreamID getDatastreamID() {
		return datastreamID;
	}

	/**
	 * Sets the datastreamID of the datastream
	 * 
	 * @param datastreamIdentifier
	 *            instance representing the datastream ID of the datastream
	 */
	public void setDatastreamID(DatastreamID datastreamIdentifier) {
		this.datastreamID = datastreamIdentifier;
	}

	/**
	 * Gets the label of the datastream
	 * 
	 * @return {@link String} instance representing the datastream label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label of the datastream
	 * 
	 * @param label
	 *            {@link String} instance representing the datastream label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gets the creation date of the datastream
	 * 
	 * @return {@link Date} instance representing the creation date of the
	 *         datastream
	 */
	public Date getCreation() {
		return creation;
	}

	/**
	 * Sets the creation date of the datastream
	 * 
	 * @param creation
	 *            {@link Date} instance representing the creation date of the
	 *            datastream
	 */
	public void setCreation(Date creation) {
		this.creation = creation;
	}

	/**
	 * Gets the versionID of the datastream
	 * 
	 * @return {@link String} instance representing the version ID of the
	 *         datastream
	 */
	public String getVersionID() {
		return versionID;
	}

	/**
	 * Sets the versionID of the datastream
	 * 
	 * @param versionID
	 *            {@link String} instance representing the version ID of the
	 *            datastream
	 */
	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}

	/**
	 * Gets the datastream state
	 * 
	 * @return {@link State} instance representing the Datastream state
	 */
	public State getState() {
		return state;
	}

	/**
	 * Sets the datastream state
	 * 
	 * @param state
	 *            {@link State} instance representing the Datastream state
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Gets the media type of the datastream
	 * 
	 * @return {@link MediaType} instance representing the media type of the
	 *         datastream
	 */
	public MediaType getMediaType() {
		return mediaType;
	}

	/**
	 * Sets the media type of the datastream
	 * 
	 * @param mediaType
	 *            {@link MediaType} instance representing the media type of the
	 *            datastream
	 */
	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	/**
	 * Gets the format URI of the datastream
	 * 
	 * @return {@link String} representing the format URI of the datastream
	 */
	public String getFormatURI() {
		return formatURI;
	}

	/**
	 * Sets the format URI of the datastream
	 * 
	 * @param formatURI
	 *            {@link String} representing the format URI of the datastream
	 */
	public void setFormatURI(String formatURI) {
		this.formatURI = formatURI;
	}

	/**
	 * Gets the control group of the datastream
	 * 
	 * @return {@link String} representing the control group of the datastream
	 */
	public String getControlGroup() {
		return controlGroup;
	}

	/**
	 * Sets the control group of the datastream
	 * 
	 * @param controlGroup
	 *            {@link String} representing the control group of the
	 *            datastream
	 */
	public void setControlGroup(String controlGroup) {
		this.controlGroup = controlGroup;
	}

	/**
	 * Gets the size of the datastream
	 * 
	 * @return {@link Integer} instance representing the size of the datastream
	 */

	public Integer getSize() {
		return size;
	}

	/**
	 * Sets the size of the datastream
	 * 
	 * @param size
	 *            {@link Integer} instance representing the size of the
	 *            datastream
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * Gets whether the datastream is versionable or not
	 * 
	 * @return {@link boolean} instance indicating whether the datastream is
	 *         versionable or not
	 */
	public boolean isVersionable() {
		return versionable;
	}

	/**
	 * Sets whether the datastream is versionable or not
	 * 
	 * @param versionable
	 *            {@link boolean} instance indicating whether the datastream is
	 *            versionable or not
	 */
	public void setVersionable(boolean versionable) {
		this.versionable = versionable;
	}

	/**
	 * Gets the location of the datastream
	 * 
	 * @return {@link String} instance representing the location of the
	 *         datastream
	 */

	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location of the datastream
	 * 
	 * @param location
	 *            {@link String} instance representing the location of the
	 *            datastream
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Gets the access url of the datastream
	 * 
	 * @return {@link String} instance representing the access location of the
	 *         datastream content
	 */

	public String getContent() {
		return "http://personalhistories.cs.uct.ac.za:8089/fedora/objects/" + getPid() + "/datastreams/"
				+ getDatastreamID() + "/content";
		// return "http://localhost:8080/fedora/objects/" + getPid() +
		// "/datastreams/" + getDatastreamID() + "/content";
	}

	/**
	 * Gets the version history of the datastream
	 * 
	 * @return {@link ArrayList} holding the different versions of the
	 *         datastream
	 */
	public ArrayList<Datastream> getVersionHistory() {
		return versionHistory;
	}

	/**
	 * Sets the version history of the datastream
	 * 
	 * @param versionHistory
	 *            {@link ArrayList} holding the different versions of the
	 *            datastream
	 */
	public void setVersionHistory(ArrayList<Datastream> versionHistory) {
		this.versionHistory = versionHistory;
	}

	/**
	 * Hashcode implementation for the datastream object calculated using the
	 * datastreamID and pid
	 * 
	 * @return {@link int} representing the hash of the object.
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datastreamID == null) ? 0 : datastreamID.hashCode());
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		return result;
	}

	/**
	 * Equals implementation for the datastream. Uniqueness is identified by a
	 * combination of the datastreams pid and datastreamID
	 * 
	 * @return {@link boolean} indicating whether two objects are equivalent or
	 *         not
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Datastream))
			return false;
		Datastream other = (Datastream) obj;
		if (datastreamID != other.datastreamID)
			return false;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		return true;
	}

	/**
	 * return {@link String} representation of the datastream
	 */
	@Override
	public String toString() {
		return "Datastream [pid=" + pid + ", datastreamIdentifier=" + datastreamID + ", label=" + label + ", creation="
				+ creation + ", versionID=" + versionID + ", state=" + state + ", mediaType=" + mediaType
				+ ", formatURI=" + formatURI + ", controlGroup=" + controlGroup + ", size=" + size + ", versionable="
				+ versionable + ", location=" + location + ", content=" + content + ", versionHistory=" + versionHistory
				+ "]";
	}

}
