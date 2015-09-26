package common.fedora;

import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

public class DublinCoreDatastream extends Datastream {

	// we have the dublin core metadata fields within this...maybe it should
	// just be an array list? or map...we need to parse the content

	private HashMap<String, String> dublinCoreMetadata; // these are usually
														// populated by the dC
														// datstreams but when
														// doing coverage and
														// description we
														// introduce new fields
	private TreeSet<String> digitalObjectTags; // this is a derived value and
												// can be derived from all teh
												// metadata fields..it is just a
												// breakdown of all the search
												// tags within object appearing
												// as one word

	public DublinCoreDatastream(String pid) {
		super(pid, DatastreamID.DC);
	}

	public DublinCoreDatastream(Datastream datastream) {
		super(datastream);
	}

	public DublinCoreDatastream(String pid, HashMap<String, String> metadata) {
		this(pid);
		this.dublinCoreMetadata = metadata;

	}

	public void setDublinCoreMetadata(HashMap<String, String> dublinCoreMetadata) {
		this.dublinCoreMetadata = dublinCoreMetadata;
	}

	public HashMap<String, String> getDublinCoreMetadata() {
		return dublinCoreMetadata;
	}

	private void setDigitalObjectTags() {
		// go through all the metadat elements and just split them by a space
		if (dublinCoreMetadata != null) {
			for (String elements : getDublinCoreMetadata().values()) {
				digitalObjectTags.addAll(Arrays.asList(elements.split(" ")));
			}
		}

	}

	public TreeSet<String> getDigitalObjectTags() {
		setDigitalObjectTags();
		// derive this vlaue from the metadata fields
		return digitalObjectTags;
	}

	@Override
	public String toString() {
		String result = "";
		for (String dc : dublinCoreMetadata.keySet()) {
			result += dc + ": " + dublinCoreMetadata.get(dc) + "\n";
		}
		return super.toString() + "Dublin core fields \n " + result;
	}

}
