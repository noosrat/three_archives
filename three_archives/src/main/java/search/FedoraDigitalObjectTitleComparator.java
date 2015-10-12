package search;

import java.util.Comparator;

import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;

public class FedoraDigitalObjectTitleComparator implements Comparator<FedoraDigitalObject> {


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
		String title1 = dco1.getDublinCoreMetadata().get(DublinCore.TITLE.name());
		String title2 = dco2.getDublinCoreMetadata().get(DublinCore.TITLE.name());
		
		if (title1==null &&title2!=null){
			return -1;
		}
		if (title1!=null && title2==null){
			return 1;
		}
		if (title1==null && title2==null){
			return 0;
		}
		
		return title1.toLowerCase().compareTo(title2.toLowerCase());
	}

}
