package common.fedora;

import java.util.HashMap;

/**
 * The {@code DublinCoreDatastream} is a child of {@link Datastream} containing
 * additional information pertaining to the dublin core metadata fields for the
 * specific Fedora Digital object. Every fedora digital object has at least a
 * {@link DublinCoreDatastream}
 * 
 * @author mthnox003
 *
 */

public class DublinCoreDatastream extends Datastream {

	/**
	 * The {@link HashMap<T,E>} instance representing the dublin core metadata
	 * fields of the digital object. The key value is defined by the
	 * {@link DublinCore} enum name value and the value of the HashMap is
	 * defined by the actual value of the dublin core field as obtained from the
	 * dublin core metadata for the digital object
	 * 
	 * In addition to the standard dublin core metadata fields as indicated in
	 * {@link DublinCore}, the HashMap houses values for
	 * "COLLECTION","ANNOTATIONS","EVENT" which are all derived from the
	 * {@link DublinCore.DESCRIPTION} as well as fields for "COVERAGE" and
	 * "LOCATION" which are derived from the {@link DublinCore.COVERAGE} field.
	 * 
	 */
	private HashMap<String, String> dublinCoreMetadata;

	/**
	 * Constructor for the DublinCoreDatastream
	 * 
	 * @param pid
	 *            {@link String} instance of the persistent identifier matching
	 *            that of the {@link FedoraDigitalObject} that the
	 *            DublinCoreDatastream belongs to
	 */
	public DublinCoreDatastream(String pid) {
		super(pid, DatastreamID.DC);
	}

	/**
	 * Constructor for the DublinCoreDatastream
	 * 
	 * @param datastream
	 *            {@link Datastream} instance representing the datastream
	 */

	public DublinCoreDatastream(Datastream datastream) {
		super(datastream);
	}

	/**
	 * Constructor for the DublinCoreDatastream
	 * 
	 * @param pid
	 *            {@link String} instance of the persistent identifier matching
	 *            that of the {@link FedoraDigitalObject} that the
	 *            DublinCoreDatastream belongs to
	 * @param metadata
	 *            {@link HashMap} instance containing the metadata fields to map
	 *            into the datastream metadata elements
	 */

	public DublinCoreDatastream(String pid, HashMap<String, String> metadata) {
		this(pid);
		this.dublinCoreMetadata = metadata;

	}

	/**
	 * Sets the dublinCoreMetadata collection of fields for the datastream
	 * 
	 * @param dublinCoreMetadata
	 *            {@link HashMap} instance containing the values of the metadata
	 *            fields for the object
	 */
	public void setDublinCoreMetadata(HashMap<String, String> dublinCoreMetadata) {
		this.dublinCoreMetadata = dublinCoreMetadata;
	}

	/**
	 * Gets the dublinCoreMetadata collection of fields for the datastream
	 * 
	 * @return {@link HashMap} instance of the dublinCoreMetadata fields for the
	 *         object
	 */
	public HashMap<String, String> getDublinCoreMetadata() {
		return dublinCoreMetadata;
	}

	/**
	 * @return {@link String} representation of the object
	 */
	@Override
	public String toString() {
		String result = "";
		for (String dc : dublinCoreMetadata.keySet()) {
			result += dc + ": " + dublinCoreMetadata.get(dc) + "\n";
		}
		return super.toString() + "Dublin core fields \n " + result;
	}

}
