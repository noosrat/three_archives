package history;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.http.Cookie;

import common.Service;
import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import search.Browse;

public class History extends Service {

	public History() {
		super();
	}

	public ArrayList<FedoraDigitalObject> retrieveDigitalObjectsAlteredSinceLastVisit(Date lastVisited) {
		// ArrayList<FedoraDigitalObject> fedoraDigitalObjects = new
		// ArrayList<FedoraDigitalObject>(Browse.getFedoraDigitalObjects());
		// System.out.println("UNSORTED LIST ");
		// for (FedoraDigitalObject f : fedoraDigitalObjects) {
		// System.out.println(f.getPid() + " date modified " +
		// f.getDateLastModified());
		// }
		// Collections.sort(fedoraDigitalObjects, new
		// FedoraDigitalObjectDateComparator());
		// System.out.println("SORTED LIST ");
		// for (FedoraDigitalObject f : fedoraDigitalObjects) {
		// System.out.println(f.getPid() + " date modified " +
		// f.getDateLastModified());
		// }
		// don't actually need to sort...just remove those which are older than
		// the speciffied date
		// we now need to compare what is in our collection with the date we
		// have receieved from the cookie
		System.out.println("IN RETRIEVE DIGITAL OJBECTS ALTERED SINCE LAST VISIT");
		ArrayList<FedoraDigitalObject> recentlyAlteredFedoraDigitalObjects = new ArrayList<FedoraDigitalObject>(
				Browse.getFedoraDigitalObjects());

		for (Iterator<FedoraDigitalObject> it = recentlyAlteredFedoraDigitalObjects.iterator(); it.hasNext();) {
			FedoraDigitalObject obj = it.next();
			System.out.println(
					"Date last visited " + lastVisited + "  date of object last mod " + obj.getDateLastModified());
			if (!lastVisited.before(obj.getDateLastModified())) {
				it.remove();
			}
		}

		return recentlyAlteredFedoraDigitalObjects;
	}

	public HashMap<String, TreeSet<String>> itemsUpdated(ArrayList<FedoraDigitalObject> objectsRecentlyModified) {
		// we need to iterate through all the objects that have been updated and
		// pick out all the specific categories that have been updated
		// initialise hashmap
		HashMap<String, TreeSet<String>> updates = new HashMap<String, TreeSet<String>>();
		updates.put(DublinCore.CONTRIBUTOR.name(), new TreeSet<String>());
		updates.put(DublinCore.CREATOR.name(), new TreeSet<String>());
		updates.put(DublinCore.SUBJECT.name(), new TreeSet<String>());
		updates.put(DublinCore.DATE.name(), new TreeSet<String>());
		updates.put(DublinCore.SOURCE.name(), new TreeSet<String>());
		updates.put("COLLECTION", new TreeSet<String>());
		updates.put("EVENT", new TreeSet<String>());
		updates.put("LOCATION", new TreeSet<String>());

		System.out.println(
				"ABOUT TO ITERATING THROUGH THE OBJECTS WHICH HAVE BEEN MODIFIED " + objectsRecentlyModified.size());
		for (FedoraDigitalObject object : objectsRecentlyModified) {
			DublinCoreDatastream dcDatastream = (DublinCoreDatastream) object.getDatastreams()
					.get(DatastreamID.DC.name());
			System.out.println("Dublin Core datastream of object " + dcDatastream.toString());

			for (String dc : updates.keySet()) {
				System.out.println("dc key " + dc + " current occupants " + updates.get(dc));
				String item = dcDatastream.getDublinCoreMetadata().get(dc);
				if (item != null) {
					updates.get(dc).add(item);
				}
			}
			// for (String dc: dcDatastream.getDublinCoreMetadata().keySet()){
			// System.out.println("Dublinc core " + dc);
			// if (updates.get(dc)!=null){
			// System.out.println("current value sitting in updates" +
			// updates.get(dc));
			// updates.get(dc).add(dcDatastream.getDublinCoreMetadata().get(dc));
			// }
			// //we are adding the values within our updated objects into a list
			// to be presented in the front
			// //we do this by first getting the list we are dealing with from
			// our map, then we append to this list
			// }

		}

		System.out.println(updates.toString());

		// then after this we need to indicate in which categories our items
		// fall under

		return updates;
	}
	
	private static void updateCategoryCookie(Cookie cookie){
		
	}
	
	

}
