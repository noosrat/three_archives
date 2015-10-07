package search;

import java.util.Comparator;

import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;

public class FedoraDigitalObjectDateComparator implements Comparator<FedoraDigitalObject> {


	public int compare(FedoraDigitalObject o1, FedoraDigitalObject o2) {
		if (o1==null &&o2!=null){
			return -1;
		}
		if (o1!=null && o2==null){
			return 1;
		}
		if (o1==null && o2==null){
			return 0;
		}

		//we need to extract the dublin core datastream and compare the title of these
		DublinCoreDatastream dco1 = (DublinCoreDatastream)o1.getDatastreams().get(DatastreamID.DC.name());
		DublinCoreDatastream dco2 = (DublinCoreDatastream)o2.getDatastreams().get(DatastreamID.DC.name());
		Integer year1 = Integer.parseInt(dco1.getDublinCoreMetadata().get(DublinCore.DATE.name()));
		Integer year2 = Integer.parseInt(dco2.getDublinCoreMetadata().get(DublinCore.DATE.name()));
		
		
		if (year1==null || year2==null){
			return 0;
		}
		
		return year1.compareTo(year2);
	}

}
