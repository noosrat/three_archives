package search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
	private static TreeMap<String, TreeSet<String>> browsingCategories = new TreeMap<String, TreeSet<String>>();
	private static TreeMap<String, TreeSet<String>> filteredBrowsingCategories = new TreeMap<String, TreeSet<String>>();

	public static void initialise() throws FedoraException, SolrServerException {
		System.out.println("In browse cosntructor");
		fedoraDigitalObjects = SearchController.getSearch().findFedoraDigitalObjects("*");
		System.out.println("Processed fedora digital objects, found " + fedoraDigitalObjects.size() + " objects");
		setUpBrowsingCategoriesAndValues(fedoraDigitalObjects);
		SearchController.buildAutocompleteJSONFile(fedoraDigitalObjects);
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

	public static TreeMap<String, TreeSet<String>> getBrowsingCategories() {
		return browsingCategories;
	}

	private static void setBrowsingCategories(TreeMap<String, TreeSet<String>> browsingCategories) {
		Browse.browsingCategories = browsingCategories;
		setFilteredBrowsingCategories(browsingCategories);
	}

	/*
	 * populate these values dynamically from the CuREENT set of data being
	 * browsed?? this makes sure that only necessary things and things that will
	 * result in actual data will be populated
	 */

	public static TreeMap<String, TreeSet<String>> getFilteredBrowsingCategories() {
		return filteredBrowsingCategories;
	}

	public static void setFilteredBrowsingCategories(TreeMap<String, TreeSet<String>> filteredBrowsingCategories) {
		Browse.filteredBrowsingCategories = filteredBrowsingCategories;
	}

	private static void setUpBrowsingCategoriesAndValues(Set<FedoraDigitalObject> fedoraDigitalObjects) {
		System.out.println("Setting up browsing categories and values ");
		TreeMap<String, TreeSet<String>> searchAndBrowseCategoriesAndValues = new TreeMap<String, TreeSet<String>>();

		/*
		 * here we only want to populat the keys for now ..just initiliasing it
		 */

		for (SearchAndBrowseCategory cat : SearchAndBrowseCategory.values()) {
			searchAndBrowseCategoriesAndValues.put(cat.name(), new TreeSet<String>());
		}

		for (SearchAndBrowseCategory cat : SearchAndBrowseCategory.values()) {
			Set<String> values = new HashSet<String>();

			for (FedoraDigitalObject digitalObject : fedoraDigitalObjects) {

				HashMap<String, String> dc = ((DublinCoreDatastream) digitalObject.getDatastreams()
						.get(DatastreamID.DC.name())).getDublinCoreMetadata();
				switch (cat) {
				case TITLE:
					if (dc.get(DublinCore.TITLE.name()) != null) {
						values.add(dc.get(DublinCore.TITLE.name()).substring(0, 1));//just getting the first character of the title
					}
					break;
				case YEAR:
					values.add(dc.get(DublinCore.DATE.name()));
					break;
				case EVENT:
					values.add(dc.get("EVENT"));
					break;// search description
				case EXHIBITION:
					break; // this will be a database query
				case MEDIA_TYPE:
					String f = dc.get(DublinCore.FORMAT.name());
					String t = dc.get(DublinCore.TYPE.name());

					String format = "";
					if (f != null && !f.isEmpty()) {
						format += f.toLowerCase();
					}
					if (t != null && !t.isEmpty()) {
						format += " " + t.toLowerCase();
					}
					DatastreamID result = DatastreamID.parseMediaType(format);

					if (result != null) {
						values.add(result.name());
					}
					break;
				case LOCATION:
					break; // search dublin core coverage
				case CREATOR:
					values.add(dc.get(DublinCore.CREATOR.name()));
					break; // search DC
				case SUBJECT:
					values.add(dc.get(DublinCore.SUBJECT.name()));
					break;// search DC
				case COLLECTION:
					values.add(dc.get("COLLECTION"));
					break;
				}
			}
			values.remove(null);
			searchAndBrowseCategoriesAndValues.put(cat.name(), new TreeSet<String>(values));

		}
		System.out.println("BROWSING CATEGORIES " + searchAndBrowseCategoriesAndValues);
		removeEmptyCategories(searchAndBrowseCategoriesAndValues);
		setBrowsingCategories(searchAndBrowseCategoriesAndValues);
	}

	private static void removeEmptyCategories(TreeMap<String, TreeSet<String>> searchAndBrowseCategoriesAndValues) {
		for (Iterator it = searchAndBrowseCategoriesAndValues.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, TreeSet<String>> entry = (Entry<String, TreeSet<String>>) it.next();

			if (entry.getValue().size() == 0) {
				it.remove();
			}

		}
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
				String title = dc.getDublinCoreMetadata().get(DublinCore.TITLE.name());
				if (title != null && !title.trim().isEmpty()) {
					if (!title.startsWith(filterValue)) {
						System.out.println("removing object with PID " + digitalObject.getPid() + "and title " + title);
						iterator.remove();
					}
				}
				break;
			case YEAR:
				String date = dc.getDublinCoreMetadata().get(DublinCore.DATE.name());
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
				String dublinCoreEvent = dc.getDublinCoreMetadata().get("EVENT");
				if (dublinCoreEvent != null && !dublinCoreEvent.isEmpty()) {
					if (!(dublinCoreEvent.contains(filterValue))) {
						System.out.println("removing object with PID " + digitalObject.getPid()
								+ "and collection value " + dublinCoreEvent);
						iterator.remove();
					}
				}
				break;
			case EXHIBITION: // should we look in the db for this...search
								// by
								// exhibition 1 then behave accordingly
				System.out.println("removing object with PID " + digitalObject.getPid());
				break;
			case COLLECTION:
				String dublinCoreCollection = dc.getDublinCoreMetadata().get("COLLECTION");
				if (dublinCoreCollection != null && !dublinCoreCollection.isEmpty()) {
					if (!(dublinCoreCollection.contains(filterValue))) {
						System.out.println("removing object with PID " + digitalObject.getPid()
								+ "and collection value " + dublinCoreCollection);
						iterator.remove();
					}
				}
				break;
			case MEDIA_TYPE: // should we look in the datastream type and
								// then
				// in the format of the actual Dc metadata
				/*
				 * for now just look in the dublin core record for the
				 * datastream...and then get the format..i.e. image..video...
				 */
				String f = dc.getDublinCoreMetadata().get(DublinCore.FORMAT.name());
				String t = dc.getDublinCoreMetadata().get(DublinCore.TYPE.name());

				String format = "";
				if (f != null && !f.isEmpty()) {
					format += f.toLowerCase();
				}

				if (t != null && !t.isEmpty()) {
					format += " " + t.toLowerCase();
				}
				System.out.println("********************************************Media type and format found " + format);
				String media = filterValue.toLowerCase();
				// check for JPG, JPEG, GIF, PNG
				if (format != null && !format.isEmpty()) {

					if (DatastreamID.parseMediaType(format) == DatastreamID.parseDescription(media)) {
						iterator.remove();

					}
				}
			}

		}
		// setUpBrowsingCategoriesAndValues(filteredObjects); only if filtering
		// on filtered
		TreeMap<String, TreeSet<String>> filteredCategories = new TreeMap<String, TreeSet<String>>(
				getBrowsingCategories());
		filteredCategories.remove(filterCategory);
		System.out.println("Filtered categories " + filteredCategories);
		setFilteredBrowsingCategories(filteredCategories);
		System.out.println("Successfully filtered from " + objectsToFilter.size() + " to " + filteredObjects.size());
		setFilteredDigitalObjects(filteredObjects);
	}

}
