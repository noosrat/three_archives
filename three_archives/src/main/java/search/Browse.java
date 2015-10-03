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
	private static Set<FedoraDigitalObject> fedoraDigitalObjectsForArchive = new HashSet<FedoraDigitalObject>();

	public static void initialise(String prefix) throws FedoraException, SolrServerException {
		System.out.println("In browse cosntructor");
		fedoraDigitalObjects = SearchController.getSearch().findFedoraDigitalObjects("*");
//		setUpBrowsingCategoriesAndValues(fedoraDigitalObjects);
		filterFedoraObjectsForSpecificArchive(prefix);		
		setUpBrowsingCategoriesAndValues(fedoraDigitalObjectsForArchive);
//		SearchController.buildAutocompleteJSONFile(fedoraDigitalObjects);//this is per archive..to be done at db stage/upload stage
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

	public static Set<FedoraDigitalObject> getFedoraDigitalObjectsForArchive() {
		return fedoraDigitalObjectsForArchive;
	}

	public static void setFedoraDigitalObjectsForArchive(Set<FedoraDigitalObject> fedoraDigitalObjectsForArchive) {
		Browse.fedoraDigitalObjectsForArchive = fedoraDigitalObjectsForArchive;
	}

	public static TreeMap<String, TreeSet<String>> getFilteredBrowsingCategories() {
		return filteredBrowsingCategories;
	}

	public static void setFilteredBrowsingCategories(TreeMap<String, TreeSet<String>> filteredBrowsingCategories) {
		Browse.filteredBrowsingCategories = filteredBrowsingCategories;
	}
	

	private static void filterFedoraObjectsForSpecificArchive(String multiMediaPrefix) {
		Set<FedoraDigitalObject> fedoraDigitalObjects = new HashSet<FedoraDigitalObject>(
				Browse.getFedoraDigitalObjects());
		for (Iterator<FedoraDigitalObject> iterator = fedoraDigitalObjects.iterator(); iterator.hasNext();) {
			FedoraDigitalObject element = iterator.next();
			if (!(element.getPid().contains(multiMediaPrefix))) {
				iterator.remove(); // remove any object who does not have the
									// prefix for this archive
				System.out.println("removing object with pid " + element.getPid());
			}
		}

		setFedoraDigitalObjectsForArchive(fedoraDigitalObjects);
	}


	private static void setUpBrowsingCategoriesAndValues(Set<FedoraDigitalObject> fedoraDigitalObjects) {
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
						values.add(dc.get(DublinCore.TITLE.name()).substring(0, 1));// just
																					// getting
																					// the
																					// first
																					// character
																					// of
																					// the
																					// title
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
					values.add(dc.get(DublinCore.SOURCE.name()));
					values.add(dc.get(DublinCore.CONTRIBUTOR.name()));
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

	private static Set<FedoraDigitalObject> titleObjects = new HashSet<FedoraDigitalObject>();
	private static Set<FedoraDigitalObject> yearObjects = new HashSet<FedoraDigitalObject>();
	private static Set<FedoraDigitalObject> eventObjects = new HashSet<FedoraDigitalObject>();
	private static Set<FedoraDigitalObject> exhibitionObjects = new HashSet<FedoraDigitalObject>();
	private static Set<FedoraDigitalObject> collectionObjects = new HashSet<FedoraDigitalObject>();
	private static Set<FedoraDigitalObject> mediaTypeObjects = new HashSet<FedoraDigitalObject>();

	public static void filterFedoraDigitalObjects(Set<FedoraDigitalObject> objectsToFilter, String filterCategory,
			String filterValue) {
		System.out.println("Filtering fedora digital objects");
		Set<FedoraDigitalObject> filteredObjects = new HashSet<FedoraDigitalObject>(objectsToFilter);

		System.out.println("FILTERING WITH " + filterCategory + " with value " + filterValue);
		for (Iterator<FedoraDigitalObject> iterator = filteredObjects.iterator(); iterator.hasNext();) {

			FedoraDigitalObject digitalObject = iterator.next();
			DublinCoreDatastream dc = (DublinCoreDatastream) digitalObject.getDatastreams().get(DatastreamID.DC.name());
			switch (SearchAndBrowseCategory.valueOf(filterCategory.toUpperCase())) {
			case TITLE:
				String title = dc.getDublinCoreMetadata().get(DublinCore.TITLE.name());
				System.out.println("TITLE " + title);
				if (title != null && !title.trim().isEmpty()) {
					if (!title.startsWith(filterValue)) {
						iterator.remove();
					}
				} else if (title == null) {
					iterator.remove();
				}
				break;
			case YEAR:
				String date = dc.getDublinCoreMetadata().get(DublinCore.DATE.name());
				System.out.println("DATE " + date);
				if (!(date != null && date.contains(filterValue))) {
					iterator.remove();
				} else if (date == null) {
					iterator.remove();
				}
				break;
			case EVENT:
				String dublinCoreEvent = dc.getDublinCoreMetadata().get("EVENT");
				System.out.println("EVENT " + dublinCoreEvent);
				if (dublinCoreEvent != null && !dublinCoreEvent.isEmpty()) {
					if (!(dublinCoreEvent.contains(filterValue))) {
						iterator.remove();
					}
				} else if (dublinCoreEvent == null) {
					iterator.remove();
				}
				break;
			case EXHIBITION: // should we look in the db for this...search
								// by
								// exhibition 1 then behave accordingly
				System.out.println("removing object with PID " + digitalObject.getPid());
				break;
			case COLLECTION:
				String dublinCoreCollection = dc.getDublinCoreMetadata().get("COLLECTION");
				System.out.println("COLLECTION " + dublinCoreCollection);

				if (dublinCoreCollection != null && !dublinCoreCollection.isEmpty()) {
					if (!(dublinCoreCollection.contains(filterValue))) {
						iterator.remove();
					}
				} else if (dublinCoreCollection == null) {
					iterator.remove();
				}
				break;
			case SUBJECT:
				String dublinCoreSubject = dc.getDublinCoreMetadata().get(DublinCore.SUBJECT.name());
				System.out.println("SUBJECT " + dublinCoreSubject);
				if (dublinCoreSubject != null && !dublinCoreSubject.isEmpty()) {
					if (!(dublinCoreSubject.contains(filterValue))) {
						iterator.remove();
					}
				} else if (dublinCoreSubject == null) {
					iterator.remove();
				}
				break;

			case CREATOR:
				String dublinCoreCreator = dc.getDublinCoreMetadata().get(DublinCore.CREATOR.name());
				String dublinCoreContributor = dc.getDublinCoreMetadata().get(DublinCore.CONTRIBUTOR.name());
				String dublinCoreSource = dc.getDublinCoreMetadata().get(DublinCore.SOURCE.name());
				System.out.println("CREATOR  " + dublinCoreCreator + " CONTRIBUTOR " + dublinCoreContributor
						+ " SOURCE " + dublinCoreSource);

				if ((dublinCoreCreator != null && !dublinCoreCreator.isEmpty())
						|| (dublinCoreContributor != null && !dublinCoreContributor.isEmpty())
						|| (dublinCoreSource != null && !dublinCoreSource.isEmpty())) {
					// if ((!(dublinCoreCreator.contains(filterValue)) ||
					// (!(dublinCoreCreator.contains(filterValue)) ||
					// (!(dublinCoreCreator.contains(filterValue))) {
					// iterator.remove();
					// }
					if (!(dublinCoreCreator.contains(filterValue) || dublinCoreCreator.contains(filterValue)
							|| dublinCoreCreator.contains(filterValue))) {
						iterator.remove();
					}
				} else {
					iterator.remove();
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
				String media = filterValue.toLowerCase();
				// check for JPG, JPEG, GIF, PNG
				if (format != null && !format.isEmpty()) {

					if (DatastreamID.parseMediaType(format) == DatastreamID.parseDescription(media)) {
						iterator.remove();

					}
				} else {
					iterator.remove();
				}
				break;
			}

		}

		// setUpBrowsingCategoriesAndValues(filteredObjects); only if filtering
		// on filtered
		TreeMap<String, TreeSet<String>> filteredCategories = new TreeMap<String, TreeSet<String>>(
				getBrowsingCategories());
		filteredCategories.remove(filterCategory);
		System.out.println("Fiiltered objects " + filteredObjects.size());
		setFilteredBrowsingCategories(filteredCategories);
		setFilteredDigitalObjects(filteredObjects);
	}

}
