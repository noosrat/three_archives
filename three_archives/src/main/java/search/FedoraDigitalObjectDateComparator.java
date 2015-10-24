package search;

import java.util.Comparator;

import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;

/**
 * The {@code FedoraDigitalObjectDateComparator} allows for a collection of
 * FedoraDigitalObjects to be sorted in ascending order by their year. This
 * assumes that the only entry in the DublinCore DATE field is a 4 digit value
 * representing the year.
 * 
 * @author mthnox003
 *
 */

public class FedoraDigitalObjectDateComparator implements
		Comparator<FedoraDigitalObject> {

	public int compare(FedoraDigitalObject o1, FedoraDigitalObject o2) {
		if (o1 == null && o2 != null) {
			return -1;
		}
		if (o1 != null && o2 == null) {
			return 1;
		}
		if (o1 == null && o2 == null) {
			return 0;
		}
		DublinCoreDatastream dco1 = (DublinCoreDatastream) o1.getDatastreams()
				.get(DatastreamID.DC.name());
		DublinCoreDatastream dco2 = (DublinCoreDatastream) o2.getDatastreams()
				.get(DatastreamID.DC.name());
		Integer year1 = null;
		Integer year2 = null;
		try {
			year1 = Integer.parseInt(dco1.getDublinCoreMetadata().get(
					DublinCore.DATE.name()));
			year2 = Integer.parseInt(dco2.getDublinCoreMetadata().get(
					DublinCore.DATE.name()));
		} catch (NumberFormatException ex) {
			System.out
					.println("COULD NOT SORT RESULTS.  INCORRECT DATE FORMAT");
		}

		if (year1 == null || year2 == null) {
			return 0;
		}

		return year1.compareTo(year2);
	}

}
