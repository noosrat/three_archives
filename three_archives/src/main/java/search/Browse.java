package search;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.solr.client.solrj.SolrServerException;

import common.Service;
import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;

public class Browse extends Service {

	private static Set<FedoraDigitalObject> fedoraDigitalObjects = new HashSet<FedoraDigitalObject>();
	private static Set<FedoraDigitalObject> filteredDigitalObjects = new HashSet<FedoraDigitalObject>();
	private static TreeMap<String, Set<String>> browsingCategories = new TreeMap<String, Set<String>>();
	private static TreeMap<String, Set<String>> filteredBrowsingCategories = new TreeMap<String, Set<String>>();

	public static void initialise() throws FedoraException, SolrServerException {
		System.out.println("In browse cosntructor");
		fedoraDigitalObjects = SearchController.getSearch().findFedoraDigitalObjects("*");
		System.out.println("Processed fedora digital objects, found " + fedoraDigitalObjects.size() + " objects");
		setUpBrowsingCategoriesAndValues(fedoraDigitalObjects);
	}

	public static Set<FedoraDigitalObject> getFedoraDigitalObjects() {
		return fedoraDigitalObjects;
	}

	public static Set<FedoraDigitalObject> getFilteredDigitalObjects() {
		return filteredDigitalObjects;
	}

	private static void setFilteredDigitalObjects(Set<FedoraDigitalObject> filteredDigitalObjects) {
		Browse.filteredDigitalObjects = filteredDigitalObjects;
	}

	public static TreeMap<String, Set<String>> getBrowsingCategories() {
		return browsingCategories;
	}

	private static void setBrowsingCategories(TreeMap<String, Set<String>> browsingCategories) {
		Browse.browsingCategories = browsingCategories;
		setFilteredBrowsingCategories(browsingCategories);
	}

	/*
	 * populate these values dynamically from the CuREENT set of data being
	 * browsed?? this makes sure that only necessary things and things that will
	 * result in actual data will be populated
	 */

	public static TreeMap<String, Set<String>> getFilteredBrowsingCategories() {
		return filteredBrowsingCategories;
	}

	public static void setFilteredBrowsingCategories(TreeMap<String, Set<String>> filteredBrowsingCategories) {
		Browse.filteredBrowsingCategories = filteredBrowsingCategories;
	}

	private static void setUpBrowsingCategoriesAndValues(Set<FedoraDigitalObject> fedoraDigitalObjects) {
		System.out.println("Setting up browsing categories and values ");
		TreeMap<String, Set<String>> searchAndBrowseCategoriesAndValues = new TreeMap<String, Set<String>>();
		
		/*here we only want to populat the keys for now*/
		
		for (SearchAndBrowseCategory cat : SearchAndBrowseCategory.values()) {
			Set<String> values = new TreeSet<String>();
			switch (cat) {
			case TITLE:
				values.add("M");
				values.add("S");
				break; // search in dublin core titile
			case YEAR:
				values.add("1981");
				values.add("1990");
				values.add("2012");
				values.add("2013");
				break;// search in dublin core date
			case EVENT:
				break;// search description
			case EXHIBITION:
				break; // this will be a data type search
			case MEDIA_TYPE:
				for (DatastreamID dsID : DatastreamID.values()) {
					values.add(dsID.getDescription());
				}
				break; // this just filters according to the media types of
						// the
						// datastream
			case LOCATION:
				break; // search dublin core ds
			case ACADEMIC_PAPER:
				break;// search description nad maybe title
			case CREATOR:
				break; // search DC
			case SUBJECT:
				break;// search DC
			}

			searchAndBrowseCategoriesAndValues.put(cat.name(), values);

		}
		System.out.println("BROWSING CATEGORIES " + searchAndBrowseCategoriesAndValues);
		setBrowsingCategories(searchAndBrowseCategoriesAndValues);

		// for (SearchAndBrowseCategory cat : SearchAndBrowseCategory.values())
		// {
		// Set<String> values = new TreeSet<String>();
		// switch (cat) {
		// case TITLE:
		// values.add("M");
		// values.add("S");
		// break; // search in dublin core titile
		// case YEAR:
		// values.add("1981");
		// values.add("1990");
		// values.add("2012");
		// values.add("2013");
		// break;// search in dublin core date
		// case EVENT:
		// break;// search description
		// case EXHIBITION:
		// break; // this will be a data type search
		// case MEDIA_TYPE:
		// for (DatastreamID dsID : DatastreamID.values()) {
		// values.add(dsID.getDescription());
		// }
		// break; // this just filters according to the media types of
		// // the
		// // datastream
		// case LOCATION:
		// break; // search dublin core ds
		// case ACADEMIC_PAPER:
		// break;// search description nad maybe title
		// case CREATOR:
		// break; // search DC
		// case SUBJECT:
		// break;// search DC
		// }
		//
		// searchAndBrowseCategoriesAndValues.put(cat.name(), values);
		//
		// }
		// System.out.println("BROWSING CATEGORIES " +
		// searchAndBrowseCategoriesAndValues);
		// setBrowsingCategories(searchAndBrowseCategoriesAndValues);

	}

	public static void filterFedoraDigitalObjects(Set<FedoraDigitalObject> objectsToFilter, String filterCategory,
			String filterValue) {

		Set<FedoraDigitalObject> filteredObjects = new HashSet<FedoraDigitalObject>(objectsToFilter);
		System.out.println("About to filter objects" + filteredObjects.toString() + " by : " + filterCategory
				+ " value=" + filterValue);

		for (Iterator<FedoraDigitalObject> iterator = filteredObjects.iterator(); iterator.hasNext();) {
			FedoraDigitalObject digitalObject = iterator.next();
			DublinCoreDatastream dc = (DublinCoreDatastream) digitalObject.getDatastreams().get(DatastreamID.DC.name());
			switch (SearchAndBrowseCategory.valueOf(filterCategory.toUpperCase())) {
			case TITLE:
				String title = dc.getDublinCoreMetadata().get(DublinCore.TITLE);
				if (title != null && !title.trim().isEmpty()) {
					if (!title.startsWith(filterValue)) {
						System.out.println("removing object with PID " + digitalObject.getPid() + "and title " + title);
						iterator.remove();
					}
				}
				break;
			case YEAR:
				String date = dc.getDublinCoreMetadata().get(DublinCore.DATE);
				if (!(date != null && date.contains(filterValue))) {
					System.out.println("removing object with PID " + digitalObject.getPid() + "and date " + date);
					iterator.remove();
				}
				break;
			// if (date != null) {
			// Integer year = Integer.parseInt(date.substring(0, 4));
			// if (!year.equals(Integer.parseInt(filterValue))) {
			// iterator.remove();
			// }
			// }
			// break;
			case EVENT:
				System.out.println("removing object with PID " + digitalObject.getPid());
				break;
			case EXHIBITION: // should we look in the db for this...search
								// by
								// exhibition 1 then behave accordingly
				System.out.println("removing object with PID " + digitalObject.getPid());
				break;
			case MEDIA_TYPE: // should we look in the datastream type and
								// then
				// in the format of the actual Dc metadata
				/*
				 * for now just look in the dublin core record for the
				 * datastream...and then get the format..i.e. image..video...
				 */
				String f = dc.getDublinCoreMetadata().get(DublinCore.FORMAT);
				String t = dc.getDublinCoreMetadata().get(DublinCore.TYPE);

				String format = "";
				if (f != null && !f.isEmpty()) {
					format += f.toLowerCase();
				}

				if (t != null && !t.isEmpty()) {
					format += " " + t.toLowerCase();
				}
				// concatenated
				// the
				// two
				// so
				// we
				// only
				// have
				// to
				// search
				// one
				// filed..reducing
				// if
				// statements
				System.out.println("********************************************Media type and format found " + format);
				String media = filterValue.toLowerCase();
				// check for JPG, JPEG, GIF, PNG
				if (format != null && !format.isEmpty()) {
					switch (DatastreamID.parseDescription(media)) {
					case IMG:
					case IMG_PUB:
						if (!(format.contains(media) || format.contains("jpg") || format.contains("jpeg")
								|| format.contains("gif") || format.contains("png"))) {
							System.out.println("removing object with PID " + digitalObject.getPid());
							iterator.remove();
						}
						break;
					case VID:
						if (!(format.contains(media) || format.contains("3gp") || format.contains("mp4")
								|| format.contains("mpeg4") || format.contains("vid"))) {
							System.out.println("removing object with PID " + digitalObject.getPid());
							iterator.remove();
						}
						break;
					case AUD:
						if (!(format.contains(media) || format.contains("mp3") || format.contains("aud")
								|| format.contains("flac"))) {
							System.out.println("removing object with PID " + digitalObject.getPid());
							iterator.remove();
						}
						break;
					}

				}
			}

		}
		setUpBrowsingCategoriesAndValues(filteredObjects);
		TreeMap<String, Set<String>> filteredCategories = new TreeMap<String, Set<String>>(getBrowsingCategories());
		filteredCategories.remove(filterCategory);
		System.out.println("Filtered categories " + filteredCategories);
		setFilteredBrowsingCategories(filteredCategories);
		System.out.println("Successfully filtered from " + objectsToFilter.size() + " to " + filteredObjects.size());
		setFilteredDigitalObjects(filteredObjects);
	}

}
