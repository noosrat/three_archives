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

/**
 * The {@code Browse} Service is responsible for housing the
 * FedoraDigitalObjects being browsed and displayed via the interface. The
 * {@link Service} performs filtering and categorisation to facilitate a richer
 * browsing experience
 * 
 * @author mthnox003
 *
 */
public class Browse extends Service {

	/**
	 * The {@link Set} instance representing the digital objects for contained
	 * within the repository
	 */
	private Set<FedoraDigitalObject> fedoraDigitalObjects = new HashSet<FedoraDigitalObject>();
	/**
	 * The {@link Set} instance representing the filtered digital objects
	 */
	private Set<FedoraDigitalObject> filteredDigitalObjects = new HashSet<FedoraDigitalObject>();
	/**
	 * The {@link TreeMap} instance representing the browsingCategories
	 * available
	 */
	private TreeMap<String, TreeSet<String>> browsingCategories = new TreeMap<String, TreeSet<String>>();
	/**
	 * The {@link TreeMap} instance representing the filtered browsingCategories
	 * available
	 */
	private TreeMap<String, TreeSet<String>> filteredBrowsingCategories = new TreeMap<String, TreeSet<String>>();
	/**
	 * The {@link Set} instance representing the digital objects for the
	 * specific archive being explored
	 */

	private Set<FedoraDigitalObject> fedoraDigitalObjectsForArchive = new HashSet<FedoraDigitalObject>();
	/**
	 * The {@link TreeMap} instance representing the FedoraDigitalObjects for
	 * the archive grouped into whichever category they fall under The
	 * {@link String} key is the main category The {@link TreeMap} value
	 * represents the objects and the subcategories belonging to the main
	 * category
	 */
	private TreeMap<String, TreeMap<String, Set<FedoraDigitalObject>>> categorisedFedoraDigitalObjects;

	/**
	 * This method initialises the user's browsing experience by filtering the
	 * digital objects and retaining only those pertaining to the archive being
	 * browsed This method also intiialsed the browsing categories for the
	 * fedora digital objects for the archive being explored
	 * 
	 * @param prefix
	 *            {@link String} instance representing the prefix of the
	 *            {@link FedoraDigitalObject}'s pid. This is unique per archive
	 *            and allows for the efficient retrieval of digital objects
	 *            specifically for a certain archive
	 * @throws FedoraException
	 * @throws SolrServerException
	 */
	public void initialise(String prefix) throws FedoraException,
			SolrServerException {
		System.out.println("In browse cosntructor");
		filterFedoraObjectsForSpecificArchive(prefix);
		setUpBrowsingCategoriesAndValues(fedoraDigitalObjectsForArchive);
	}

	/**
	 * Sets the {@link #fedoraDigitalObjects}
	 * 
	 * @param fedoraDigitalObjects
	 *            {@link Set} instance representing fedoraDigitalObjects
	 */
	public void setFedoraDigitalObjects(
			Set<FedoraDigitalObject> fedoraDigitalObjects) {
		this.fedoraDigitalObjects = fedoraDigitalObjects;
	}

	/**
	 * Gets the {@link #filteredDigitalObjects}
	 * 
	 * @return {@link Set} instance representing the fedora digital objects
	 */
	public Set<FedoraDigitalObject> getFilteredDigitalObjects() {
		return filteredDigitalObjects;
	}

	/**
	 * Sets the {@link #filteredDigitalObjects}
	 * 
	 * @param {@link Set} instance representing the filtered fedora digital
	 *        objects
	 */

	private void setFilteredDigitalObjects(
			Set<FedoraDigitalObject> filteredDigitalObjects) {
		this.filteredDigitalObjects = filteredDigitalObjects;
	}

	/**
	 * Gets the {@link #browsingCategories}
	 * 
	 * @return {@link TreeMap} representing the browsing categories
	 */
	public TreeMap<String, TreeSet<String>> getBrowsingCategories() {
		return browsingCategories;
	}

	/**
	 * Sets the {@link #browsingCategories}
	 * 
	 * @param {@link TreeMap} representing the browsing categories
	 */
	private void setBrowsingCategories(
			TreeMap<String, TreeSet<String>> browsingCategories) {
		this.browsingCategories = browsingCategories;
		setFilteredBrowsingCategories(browsingCategories);
	}

	/**
	 * Gets the digital objects that have been categorised
	 * 
	 * @return {@link TreeMap} representing digital objects that have been
	 *         categorised
	 */
	public TreeMap<String, TreeMap<String, Set<FedoraDigitalObject>>> getCategorisedFedoraDigitalObjects() {
		return categorisedFedoraDigitalObjects;
	}

	/**
	 * Sets the digital objects that have been categorised
	 * 
	 * @param {@link TreeMap} representing digital objects that have been
	 *        categorised
	 */
	private void setCategorisedFedoraDigitalObjects(
			TreeMap<String, TreeMap<String, Set<FedoraDigitalObject>>> categorisedFedoraDigitalObjects) {
		this.categorisedFedoraDigitalObjects = categorisedFedoraDigitalObjects;
	}

	/**
	 * Gets the {@link #fedoraDigitalObjectsForArchive}
	 * 
	 * @return the {@link Set} instance representing the fedora digital objects
	 *         for the specific archive
	 */
	public Set<FedoraDigitalObject> getFedoraDigitalObjectsForArchive() {
		return fedoraDigitalObjectsForArchive;
	}

	/**
	 * Sets the {@link #fedoraDigitalObjectsForArchive}
	 * 
	 * @param fedoraDigitalObjectsForArchive
	 *            {@link Set} instance representing the fedora digital objects
	 *            for the archive
	 */
	public void setFedoraDigitalObjectsForArchive(
			Set<FedoraDigitalObject> fedoraDigitalObjectsForArchive) {
		this.fedoraDigitalObjectsForArchive = fedoraDigitalObjectsForArchive;
	}

	/**
	 * Gets the filtered browsing categories
	 * 
	 * @return {@link TreeMap} instance representing the filtered browsing
	 *         categories
	 */
	public TreeMap<String, TreeSet<String>> getFilteredBrowsingCategories() {
		return filteredBrowsingCategories;
	}

	/**
	 * Gets the filtered browsing categories
	 * 
	 * @param filteredBrowsingCategories
	 *            {@link TreeMap} instance representing the filtered browsing
	 *            categories
	 */

	public void setFilteredBrowsingCategories(
			TreeMap<String, TreeSet<String>> filteredBrowsingCategories) {
		this.filteredBrowsingCategories = filteredBrowsingCategories;
		groupFedoraObjectsInCategoriesAndSubCategories();
	}

	/**
	 * Filters the digital objects from the repository to ensure that only
	 * digital objects for the specific archive are available
	 * 
	 * @param multiMediaPrefix
	 *            {@link String} instance representing the prefix of the
	 *            {@link FedoraDigitalObject}'s pid. This is unique per archive
	 *            and allows for the efficient retrieval of digital objects
	 *            specifically for a certain archive
	 */
	private void filterFedoraObjectsForSpecificArchive(String multiMediaPrefix) {
		System.out.println("PREFIX " + multiMediaPrefix);
		Set<FedoraDigitalObject> filteredFedoraDigitalObjects = new HashSet<FedoraDigitalObject>(
				fedoraDigitalObjects);
		for (Iterator<FedoraDigitalObject> iterator = filteredFedoraDigitalObjects
				.iterator(); iterator.hasNext();) {
			FedoraDigitalObject element = iterator.next();
			if (!(element.getPid().contains(multiMediaPrefix))) {
				iterator.remove(); // remove any object who does not have the
									// prefix for this archive
				System.out.println("removing object with pid "
						+ element.getPid());
			}
		}

		setFedoraDigitalObjectsForArchive(filteredFedoraDigitalObjects);
	}

	/**
	 * Sets the browsing categories and values
	 * 
	 * @param fedoraDigitalObjects
	 *            {@link Set} instance representing the browsing categories and
	 *            values
	 */
	private void setUpBrowsingCategoriesAndValues(
			Set<FedoraDigitalObject> fedoraDigitalObjects) {
		TreeMap<String, TreeSet<String>> searchAndBrowseCategoriesAndValues = new TreeMap<String, TreeSet<String>>();

		for (SearchAndBrowseCategory cat : SearchAndBrowseCategory.values()) {
			searchAndBrowseCategoriesAndValues.put(cat.name(),
					new TreeSet<String>());
		}
		for (SearchAndBrowseCategory cat : SearchAndBrowseCategory.values()) {
			Set<String> values = new HashSet<String>();

			for (FedoraDigitalObject digitalObject : fedoraDigitalObjects) {

				HashMap<String, String> dc = ((DublinCoreDatastream) digitalObject
						.getDatastreams().get(DatastreamID.DC.name()))
						.getDublinCoreMetadata();
				switch (cat) {
				case TITLE:
					if (dc.get(DublinCore.TITLE.name()) != null) {
						values.add(dc.get(DublinCore.TITLE.name()).substring(0,
								1));
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
			searchAndBrowseCategoriesAndValues.put(cat.name(),
					new TreeSet<String>(values));

		}
		System.out.println("SEARCHING AND BROWSING CATEGORIES "
				+ searchAndBrowseCategoriesAndValues.toString());
		removeEmptyCategories(searchAndBrowseCategoriesAndValues);
		setBrowsingCategories(searchAndBrowseCategoriesAndValues);
	}

	/**
	 * This method removes any browsing categories which are empty in order to
	 * prevent them showing on the front-end
	 * 
	 * @param searchAndBrowseCategoriesAndValues
	 */
	private void removeEmptyCategories(
			TreeMap<String, TreeSet<String>> searchAndBrowseCategoriesAndValues) {
		for (Iterator it = searchAndBrowseCategoriesAndValues.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, TreeSet<String>> entry = (Entry<String, TreeSet<String>>) it
					.next();

			if (entry.getValue().size() == 0) {
				it.remove();
			}

		}
	}

	/**
	 * This methods allows for the objects recently updated to be filtered
	 * dependent on category and tag selection by the user
	 * 
	 * @param objectsToFilter
	 *            {@link Set} instance indicating the objects to be filtered
	 * @param filterCategory
	 *            {@link String} instance representing the category being
	 *            filtered by
	 * @param filterValue
	 *            {@link String} instance representing the value being filtered
	 *            by
	 * @return {@link Set} instance of fedora digital objects that have been
	 *         filtered based on the
	 * @param fitlerCategory
	 *            and
	 * @param filterValue
	 */
	public void filterFedoraDigitalObjects(
			Set<FedoraDigitalObject> objectsToFilter, String filterCategory,
			String filterValue) {
		System.out.println("Filtering fedora digital objects");
		Set<FedoraDigitalObject> filteredObjects = new HashSet<FedoraDigitalObject>(
				objectsToFilter);

		System.out.println("FILTERING WITH " + filterCategory + " with value "
				+ filterValue);
		for (Iterator<FedoraDigitalObject> iterator = filteredObjects
				.iterator(); iterator.hasNext();) {

			FedoraDigitalObject digitalObject = iterator.next();
			DublinCoreDatastream dc = (DublinCoreDatastream) digitalObject
					.getDatastreams().get(DatastreamID.DC.name());
			switch (SearchAndBrowseCategory.valueOf(filterCategory
					.toUpperCase())) {
			// omit description and search all
			case TITLE:
				String title = dc.getDublinCoreMetadata().get(
						DublinCore.TITLE.name());
				if (title != null && !title.trim().isEmpty()) {
					if (!title.startsWith(filterValue)) {
						iterator.remove();
					}
				} else if (title == null) {
					iterator.remove();
				}
				break;
			case YEAR:
				String date = dc.getDublinCoreMetadata().get(
						DublinCore.DATE.name());
				if (date != null && !date.contains(filterValue)) {
					iterator.remove();
				} else if (date == null) {
					iterator.remove();
				}
				break;
			case EVENT:
				String dublinCoreEvent = dc.getDublinCoreMetadata()
						.get("EVENT");
				if (dublinCoreEvent != null && !dublinCoreEvent.isEmpty()) {
					if (!(dublinCoreEvent.contains(filterValue))) {
						iterator.remove();
					}
				} else if (dublinCoreEvent == null) {
					iterator.remove();
				}
				break;
			case COLLECTION:
				String dublinCoreCollection = dc.getDublinCoreMetadata().get(
						"COLLECTION");
				System.out.println("COLLECTION " + dublinCoreCollection);

				if (dublinCoreCollection != null
						&& !dublinCoreCollection.isEmpty()) {
					if (!(dublinCoreCollection.contains(filterValue))) {
						iterator.remove();
					}
				} else if (dublinCoreCollection == null) {
					iterator.remove();
				}
				break;
			case SUBJECT:
				String dublinCoreSubject = dc.getDublinCoreMetadata().get(
						DublinCore.SUBJECT.name());
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
				String dublinCorecontributor = dc.getDublinCoreMetadata().get(
						DublinCore.CONTRIBUTOR.name());
				System.out.println("CONTRIBUTOR " + dublinCorecontributor);
				if (dublinCorecontributor != null
						&& !dublinCorecontributor.isEmpty()) {
					if (!(dublinCorecontributor.contains(filterValue))) {
						iterator.remove();
					}
				} else if (dublinCorecontributor == null) {
					iterator.remove();
				}
				break;
			case SOURCE:
				String dublinCoreSource = dc.getDublinCoreMetadata().get(
						DublinCore.SOURCE.name());
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
				String dublinCoreCreator = dc.getDublinCoreMetadata().get(
						DublinCore.CREATOR.name());
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
				String f = dc.getDublinCoreMetadata().get(
						DublinCore.FORMAT.name());
				if (f != null && !f.isEmpty()) {
					if (DatastreamID.parseMediaType(f) != DatastreamID
							.parseDescription(filterValue.toLowerCase())) {
						iterator.remove();

					}
				} else if (f == null) {
					iterator.remove();
				}
				break;
			case TYPE:
				String dublinCoreType = dc.getDublinCoreMetadata().get(
						DublinCore.TYPE.name());
				if (dublinCoreType != null && !dublinCoreType.isEmpty()) {
					if (!dublinCoreType.toLowerCase().contains(
							filterValue.toLowerCase())) {
						iterator.remove();

					}
				} else if (dublinCoreType == null) {
					iterator.remove();
				}
				break;
			}

		}

		TreeMap<String, TreeSet<String>> filteredCategories = new TreeMap<String, TreeSet<String>>(
				getBrowsingCategories());
		filteredCategories.remove(filterCategory);
		System.out.println("Fiiltered objects " + filteredObjects.size());
		setFilteredBrowsingCategories(filteredCategories);
		setFilteredDigitalObjects(filteredObjects);
	}

	/**
	 * This groups the fedora digital objects into their categories and sub
	 * categories
	 */
	private void groupFedoraObjectsInCategoriesAndSubCategories() {
		System.out.println("IN GROUP FEDORA OBJECTS");
		TreeMap<String, TreeMap<String, Set<FedoraDigitalObject>>> groupedObjects = new TreeMap<String, TreeMap<String, Set<FedoraDigitalObject>>>();

		for (String category : getBrowsingCategories().keySet()) {
			groupedObjects.put(category,
					new TreeMap<String, Set<FedoraDigitalObject>>());
		}
		Set<FedoraDigitalObject> dig = new HashSet<FedoraDigitalObject>(
				fedoraDigitalObjectsForArchive);

		for (FedoraDigitalObject fedoraDigitalObject : dig) {
			DublinCoreDatastream dc = (DublinCoreDatastream) fedoraDigitalObject
					.getDatastreams().get(DatastreamID.DC.name());
			HashMap<String, String> dcMetadata = dc.getDublinCoreMetadata();
			for (String category : groupedObjects.keySet()) {
				for (String subCategory : getBrowsingCategories().get(category)) {
					String tempSub = subCategory;
					if (category.equalsIgnoreCase("FORMAT")) {
						tempSub = DatastreamID.valueOf(subCategory)
								.getDescription();
					}

					if (groupedObjects.get(category).get(tempSub) == null) {
						groupedObjects.get(category).put(tempSub,
								new HashSet<FedoraDigitalObject>());
					}
					String tempCat = category;
					if (category.equalsIgnoreCase(SearchAndBrowseCategory.YEAR
							.name())) {
						tempCat = "DATE";
					}

					if (dcMetadata.get(tempCat) != null
							&& dcMetadata.get(tempCat).toLowerCase()
									.contains(tempSub.toLowerCase())) {

						groupedObjects.get(category).get(tempSub)
								.add(fedoraDigitalObject);

					}
				}
			}
		}
		setCategorisedFedoraDigitalObjects(groupedObjects);

	}

	/**
	 * This sorts the fedora digital objects in ascending order dependent on the
	 * field specified
	 * 
	 * @param value
	 *            {@link String} value representing the field to sort by
	 * @param fedoraObjects
	 *            {@link Set} representing the fedora digital objects to be
	 *            sorted
	 * @return {@link LinkeHashSet} containing the sorted fedora digital objects
	 */
	public LinkedHashSet<FedoraDigitalObject> sortResults(String value,
			Set<FedoraDigitalObject> fedoraObjects) {
		ArrayList<FedoraDigitalObject> objectList = new ArrayList<FedoraDigitalObject>(
				fedoraObjects);
		switch (SearchAndBrowseCategory.valueOf(value)) {
		case TITLE:
			System.out.println("TITLE");
			Collections.sort(objectList,
					new FedoraDigitalObjectTitleComparator());
			break;
		case YEAR:
			System.out.println("YEAR");
			Collections.sort(objectList,
					new FedoraDigitalObjectDateComparator());
			break;

		}
		for (FedoraDigitalObject f : objectList) {
			DublinCoreDatastream d = (DublinCoreDatastream) f.getDatastreams()
					.get("DC");
			System.out.println(f.getPid() + " "
					+ d.getDublinCoreMetadata().get("DATE") + " "
					+ d.getDublinCoreMetadata().get("TITLE"));
		}
		LinkedHashSet<FedoraDigitalObject> result = new LinkedHashSet<FedoraDigitalObject>(
				objectList);
		return result;

	}

}
