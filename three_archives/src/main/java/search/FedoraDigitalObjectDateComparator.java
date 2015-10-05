package search;

import java.util.Comparator;
import java.util.Date;

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
//		Date title1 = Date.parse(dco1.getDublinCoreMetadata().get(DublinCore.DATE.name());
//		Date title2 = dco2.getDublinCoreMetadata().get(DublinCore.DATE.name());
//		
//		if (title1==null &&title2!=null){
//			return -1;
//		}
//		if (title1!=null && title2==null){
//			return 1;
//		}
//		if (title1==null && title2==null){
//			return 0;
//		}
//		
//		return title1.compareTo(title2);
		return 0;
	}

}
