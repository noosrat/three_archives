package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
	private static TreeMap<String, TreeMap<String, Set<FedoraDigitalObject>>> categorisedFedoraDigitalObjects;

	public static void initialise(String prefix) throws FedoraException, SolrServerException {
		System.out.println("In browse cosntructor");
		filterFedoraObjectsForSpecificArchive(prefix);
		setUpBrowsingCategoriesAndValues(fedoraDigitalObjectsForArchive);
	}

	public static void setFedoraDigitalObjects(Set<FedoraDigitalObject> fedoraDigitalObjects) {
		Browse.fedoraDigitalObjects = fedoraDigitalObjects;
	}

	public static Set<FedoraDigitalObject> getFilteredDigitalObjects() {
		return filteredDigitalObjects;
	}

	private static void setFilteredDigitalObjects(Set<FedoraDigitalObject> filteredDigitalObjects) {
		Browse.filteredDigitalObjects = filteredDigitalObjects;
	}

	public static TreeMap<String, TreeSet<String>> getBrowsingCategories() {
		System.out.println("browsing categories " + browsingCategories);
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

	public static TreeMap<String, TreeMap<String, Set<FedoraDigitalObject>>> getCategorisedFedoraDigitalObjects() {
		return categorisedFedoraDigitalObjects;
	}

	private static void setCategorisedFedoraDigitalObjects(
			TreeMap<String, TreeMap<String, Set<FedoraDigitalObject>>> categorisedFedoraDigitalObjects) {
		Browse.categorisedFedoraDigitalObjects = categorisedFedoraDigitalObjects;
	}

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
		groupFedoraObjectsInCategoriesAndSubCategories();
	}

	private static void filterFedoraObjectsForSpecificArchive(String multiMediaPrefix) {
		System.out.println("PREFIX " + multiMediaPrefix);
		Set<FedoraDigitalObject> filteredFedoraDigitalObjects = new HashSet<FedoraDigitalObject>(fedoraDigitalObjects);
		for (Iterator<FedoraDigitalObject> iterator = filteredFedoraDigitalObjects.iterator(); iterator.hasNext();) {
			FedoraDigitalObject element = iterator.next();
			if (!(element.getPid().contains(multiMediaPrefix))) {
				iterator.remove(); // remove any object who does not have the
									// prefix for this archive
				System.out.println("removing object with pid " + element.getPid());
			}
		}

		setFedoraDigitalObjectsForArchive(filteredFedoraDigitalObjects);
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
						values.add(dc.get(DublinCore.TITLE.name()).substring(0, 1));
					}
					break;
				case YEAR:
					values.add(dc.get(DublinCore.DATE.name()));
					break;
				case EVENT:
					values.add(dc.get("EVENT"));
					break;
				case FORMAT:
					String f = dc.get(DublinCore.FORMAT.name());
					DatastreamID result = DatastreamID.parseMediaType(f);
					if (result != null) {
						values.add(result.name());
					}
					break;
				case TYPE:
					values.add(dc.get(DublinCore.TYPE.name()));
					break;
				case LOCATION:
					break;
				case CREATOR:
					values.add(dc.get(DublinCore.CREATOR.name()));
					break;
				case SOURCE:
					values.add(dc.get(DublinCore.SOURCE.name()));
					break;
				case CONTRIBUTOR:
					values.add(dc.get(DublinCore.CONTRIBUTOR.name()));
					break;
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
		System.out.println("SEARCHING AND BROWSING CATEGORIES " + searchAndBrowseCategoriesAndValues.toString());
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
		System.out.println("Filtering fedora digital objects");
		Set<FedoraDigitalObject> filteredObjects = new HashSet<FedoraDigitalObject>(objectsToFilter);

		System.out.println("FILTERING WITH " + filterCategory + " with value " + filterValue);
		for (Iterator<FedoraDigitalObject> iterator = filteredObjects.iterator(); iterator.hasNext();) {

			FedoraDigitalObject digitalObject = iterator.next();
			DublinCoreDatastream dc = (DublinCoreDatastream) digitalObject.getDatastreams().get(DatastreamID.DC.name());
			switch (SearchAndBrowseCategory.valueOf(filterCategory.toUpperCase())) {
			// omit description and search all
			case TITLE:
				String title = dc.getDublinCoreMetadata().get(DublinCore.TITLE.name());
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
				if (date != null && !date.contains(filterValue)) {
					iterator.remove();
				} else if (date == null) {
					iterator.remove();
				}
				break;
			case EVENT:
				String dublinCoreEvent = dc.getDublinCoreMetadata().get("EVENT");
				if (dublinCoreEvent != null && !dublinCoreEvent.isEmpty()) {
					if (!(dublinCoreEvent.contains(filterValue))) {
						iterator.remove();
					}
				} else if (dublinCoreEvent == null) {
					iterator.remove();
				}
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
			case CONTRIBUTOR:
				String dublinCorecontributor = dc.getDublinCoreMetadata().get(DublinCore.CONTRIBUTOR.name());
				System.out.println("CONTRIBUTOR " + dublinCorecontributor);
				if (dublinCorecontributor != null && !dublinCorecontributor.isEmpty()) {
					if (!(dublinCorecontributor.contains(filterValue))) {
						iterator.remove();
					}
				} else if (dublinCorecontributor == null) {
					iterator.remove();
				}
				break;
			case SOURCE:
				String dublinCoreSource = dc.getDublinCoreMetadata().get(DublinCore.SOURCE.name());
				System.out.println("SOURCE " + dublinCoreSource);
				if (dublinCoreSource != null && !dublinCoreSource.isEmpty()) {
					if (!(dublinCoreSource.contains(filterValue))) {
						iterator.remove();
					}
				} else if (dublinCoreSource == null) {
					iterator.remove();
				}
				break;

			case CREATOR:
				String dublinCoreCreator = dc.getDublinCoreMetadata().get(DublinCore.CREATOR.name());
				System.out.println("CREATOR  " + dublinCoreCreator);

				if (dublinCoreCreator != null && !dublinCoreCreator.isEmpty()) {
					if (!(dublinCoreCreator.contains(filterValue))) {
						iterator.remove();
					}
				} else if (dublinCoreCreator == null) {
					iterator.remove();
				}
				break;
			case FORMAT:
				String f = dc.getDublinCoreMetadata().get(DublinCore.FORMAT.name());
				if (f != null && !f.isEmpty()) {
					if (DatastreamID.parseMediaType(f) != DatastreamID.parseDescription(filterValue.toLowerCase())) {
						iterator.remove();

					}
				} else if (f == null) {
					iterator.remove();
				}
				break;
			case TYPE:
				String dublinCoreType = dc.getDublinCoreMetadata().get(DublinCore.TYPE.name());
				if (dublinCoreType != null && !dublinCoreType.isEmpty()) {
					if (!dublinCoreType.toLowerCase().contains(filterValue.toLowerCase())) {
						iterator.remove();

					}
				} else if (dublinCoreType == null) {
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

	private static void groupFedoraObjectsInCategoriesAndSubCategories() {
		System.out.println("IN GROUP FEDORA OBJECTS");
		TreeMap<String, TreeMap<String, Set<FedoraDigitalObject>>> groupedObjects = new TreeMap<String, TreeMap<String, Set<FedoraDigitalObject>>>();

		for (String category : getBrowsingCategories().keySet()) {
			groupedObjects.put(category, new TreeMap<String, Set<FedoraDigitalObject>>());
		}
		Set<FedoraDigitalObject> dig = new HashSet<FedoraDigitalObject>(fedoraDigitalObjectsForArchive);

		for (FedoraDigitalObject fedoraDigitalObject : dig) {
			DublinCoreDatastream dc = (DublinCoreDatastream) fedoraDigitalObject.getDatastreams()
					.get(DatastreamID.DC.name());
			HashMap<String, String> dcMetadata = dc.getDublinCoreMetadata();
			for (String category : groupedObjects.keySet()) {
				for (String subCategory : getBrowsingCategories().get(category)) {
					String tempSub = subCategory;
					if (category.equalsIgnoreCase("FORMAT")) {
						tempSub = DatastreamID.valueOf(subCategory).getDescription();
					}

					if (groupedObjects.get(category).get(tempSub) == null) {
						groupedObjects.get(category).put(tempSub, new HashSet<FedoraDigitalObject>());
					}
					String tempCat = category;
					if (category.equalsIgnoreCase(SearchAndBrowseCategory.YEAR.name())) {
						tempCat = "DATE";
					}

					if (dcMetadata.get(tempCat) != null
							&& dcMetadata.get(tempCat).toLowerCase().contains(tempSub.toLowerCase())) {

						groupedObjects.get(category).get(tempSub).add(fedoraDigitalObject);

					}
				}
			}
		}
		setCategorisedFedoraDigitalObjects(groupedObjects);

	}

	public static LinkedHashSet<FedoraDigitalObject> sortResults(String value, Set<FedoraDigitalObject> fedoraObjects) {
		ArrayList<FedoraDigitalObject> objectList = new ArrayList<FedoraDigitalObject>(fedoraObjects);
		switch (SearchAndBrowseCategory.valueOf(value)) {
		case TITLE:
			System.out.println("TITLE");
			Collections.sort(objectList, new FedoraDigitalObjectTitleComparator());
			break;
		case YEAR:
			System.out.println("YEAR");
			Collections.sort(objectList, new FedoraDigitalObjectDateComparator());
			break;

		}
		for (FedoraDigitalObject f : objectList) {
			DublinCoreDatastream d = (DublinCoreDatastream) f.getDatastreams().get("DC");
			System.out.println(f.getPid() + " " + d.getDublinCoreMetadata().get("DATE") + " "
					+ d.getDublinCoreMetadata().get("TITLE"));
		}
		LinkedHashSet<FedoraDigitalObject> result = new LinkedHashSet<FedoraDigitalObject>(objectList);
		return result;

	}

}
