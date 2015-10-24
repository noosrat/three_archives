package history;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.Cookie;

import search.BrowseController;
import search.SearchAndBrowseCategory;

import common.Service;
import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;

/**
 * The {@code History} is a {@link Service} responsible for the tracking of the
 * users' interaction with the system and for managing cookies and the trending
 * searches
 * 
 * @author mthnox003
 *
 */
public class History extends Service {
	/**
	 * The {@link StringBuilder} instance representing the words that build up
	 * the tag cloud
	 */
	private static StringBuilder tagCloudText = new StringBuilder();
	/**
	 * The {@link HashSet} instance representing the FedoraDigitalObjects that
	 * have been modified or added since the users' last visit to the archives
	 */
	private static HashSet<FedoraDigitalObject> objectsSinceLastVisit = new HashSet<FedoraDigitalObject>();

	/**
	 * The method responsible for retrieving the FedoraDigitalObjects that have
	 * modified since the users' last interaction with the system. This is
	 * achieved by interrogating the modification date of the digital objects
	 * present within the archive and comparing it with the user cookie storing
	 * the users' last accessed date
	 * 
	 * @param lastVisited
	 *            {@link Date} instance representing the user's last visit
	 */
	private void retrieveDigitalObjectsAlteredSinceLastVisit(Date lastVisited) {
		System.out
				.println("IN RETRIEVE DIGITAL OJBECTS ALTERED SINCE LAST VISIT");
		HashSet<FedoraDigitalObject> recentlyAlteredFedoraDigitalObjects = new HashSet<FedoraDigitalObject>(
				new BrowseController().getBrowse()
						.getFedoraDigitalObjectsForArchive());

		for (Iterator<FedoraDigitalObject> it = recentlyAlteredFedoraDigitalObjects
				.iterator(); it.hasNext();) {
			FedoraDigitalObject obj = it.next();
			System.out.println("Date last visited " + lastVisited
					+ "  date of object last mod " + obj.getDateLastModified());
			if (!lastVisited.before(obj.getDateLastModified())) {
				it.remove();
			}
		}
		setObjectsSinceLastVisit(recentlyAlteredFedoraDigitalObjects);
	}

	/**
	 * Sets the digitalObjects modified since the user's last interaction
	 * 
	 * @param fedoraDigitalObjects
	 *            the {@link HashSet} instance repersenting the objects recently
	 *            modified
	 */
	private static void setObjectsSinceLastVisit(
			HashSet<FedoraDigitalObject> fedoraDigitalObjects) {
		objectsSinceLastVisit = fedoraDigitalObjects;

	}

	/**
	 * Gets the digitalObjects modified since the user's last interaction
	 * 
	 * @return the {@link HashSet} instance repersenting the objects recently
	 *         modified
	 */
	public HashSet<FedoraDigitalObject> getObjectsSinceLastVisit() {
		return objectsSinceLastVisit;
	}

	/**
	 * This checks which categories (dublin core fields) have been updated or
	 * modified since the last visit. This indicates that object properties
	 * pertaining to the categories have been modified
	 * 
	 * @param objectsRecentlyModified
	 *            the {@link HashSet} instance representing the objects that
	 *            have been modified since the last visit
	 * @return
	 */
	public HashMap<String, TreeSet<String>> categoriesRecentlyUpdated(
			HashSet<FedoraDigitalObject> objectsRecentlyModified) {
		HashMap<String, TreeSet<String>> updates = new HashMap<String, TreeSet<String>>();

		for (SearchAndBrowseCategory category : SearchAndBrowseCategory
				.values()) {
			updates.put(category.name(), new TreeSet<String>());
		}
		updates.remove(SearchAndBrowseCategory.SEARCH_ALL);
		updates.remove(SearchAndBrowseCategory.DESCRIPTION);

		for (FedoraDigitalObject object : objectsRecentlyModified) {
			DublinCoreDatastream dcDatastream = (DublinCoreDatastream) object
					.getDatastreams().get(DatastreamID.DC.name());
			for (String dc : updates.keySet()) {
				String item = dcDatastream.getDublinCoreMetadata().get(dc);
				if (item != null) {
					updates.get(dc).add(item);
				}
			}
		}

		return updates;
	}

	/**
	 * This method parses the date last visited and allows for the retrieval of
	 * the objects modified since that date
	 * 
	 * @param date
	 *            {@link String} instance representing the date last visited
	 * @throws Exception
	 */
	public void retrieveRecentlyUpdateItems(String date) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"EEE MMM dd hh:mm:ss zzz yyyy");
		Date dateLastVisited = new Date();
		try {
			dateLastVisited = formatter.parse(date);
		} catch (ParseException e) {
			System.out.println(e);
			throw new Exception("Unable to parse date ", e);
		}
		System.out.println("PARSED DATE " + dateLastVisited);

		// now that we have parsed the date we can get the updated objects
		retrieveDigitalObjectsAlteredSinceLastVisit(dateLastVisited);
	}

	/**
	 * This method retrieves the users favourite top three browsing categories
	 * 
	 * 
	 * @param browseCategoryCookie
	 *            {@link String} instance representing the user's browsing
	 *            category activity. This {@link String} is of the format
	 *            "Collection:0##Creator:0##Event:0##Exhibition:0##Location:0##Subject:0##Contributor:0##Source:0"
	 *            where the number after the colon indicates how many times the
	 *            user has browsed that category
	 * @return {@link ArrayList} instance indicating the user's top three
	 *         browsing categories
	 */
	/*
	 * once we retrieve these then we can indicate whether those categories have
	 * been updated recently/since the last visit
	 */
	private ArrayList<String> retrieveTopThreeBrowsingCategories(
			Cookie browseCategoryCookie) {
		String[] categories = browseCategoryCookie.getValue().split("##");
		HashMap<String, Integer> categoriesAndValues = new HashMap<String, Integer>();
		for (String category : categories) {
			String[] splitCategoryAndValue = category.split(":");
			categoriesAndValues.put(splitCategoryAndValue[0],
					Integer.parseInt(splitCategoryAndValue[1]));
		}

		List<Map.Entry<String, Integer>> elements = new ArrayList<Map.Entry<String, Integer>>();
		elements.addAll(categoriesAndValues.entrySet());
		System.out.println("Unsorted list " + categoriesAndValues.toString());
		// now let's sort the map according to the values/ratings
		Collections.sort(elements,
				new Comparator<Map.Entry<String, Integer>>() {

					public int compare(Entry<String, Integer> o1,
							Entry<String, Integer> o2) {
						return -1 * (o1.getValue().compareTo(o2.getValue()));
					}

				});

		System.out.println("Sorted List categories and values "
				+ categoriesAndValues.toString());
		System.out.println("Sorted List elements " + elements.toString());

		ArrayList<String> topBrowsedCategories = new ArrayList<String>();

		topBrowsedCategories.add(elements.get(0).getKey());
		topBrowsedCategories.add(elements.get(1).getKey());
		topBrowsedCategories.add(elements.get(2).getKey());

		System.out.println("TOP CAT: " + topBrowsedCategories.toString());
		return topBrowsedCategories;

	}

	/**
	 * This method retrieves the users favourite top three browsing categories
	 * that have been updated. This is done by comparing the updated categories
	 * with those considered to be the user's top three favourite browsing
	 * categories. The application then returns a list of categories that match
	 * both these conditions
	 * 
	 * @param updatedCategories
	 *            {@link HashMap} representing the updated categories
	 * @param browseCategoryCookie
	 *            {@link Cookie} instance representing the category browsing
	 *            cookie as per format
	 *            "Collection:0##Creator:0##Event:0##Exhibition:0##Location:0##Subject:0##Contributor:0##Source:0"
	 * 
	 * @return {@link ArrayList} instance representing the top three browsing
	 *         categories of the user that have been updated.
	 */
	public ArrayList<String> favouriteBrowsingCategoryUpdates(
			HashMap<String, TreeSet<String>> updatedCategories,
			Cookie browseCategoryCookie) {
		System.out
				.println("Processing favourite category updates with browseCategoryCookie of: "
						+ browseCategoryCookie.getValue());
		System.out.println("Updated categories "
				+ updatedCategories.keySet().toString());
		ArrayList<String> topBrowsingCategories = retrieveTopThreeBrowsingCategories(browseCategoryCookie);
		ArrayList<String> result = new ArrayList<String>();
		for (String category : topBrowsingCategories) {
			if (updatedCategories.keySet().contains(category.toUpperCase())
					&& !result.contains(category)) {
				result.add(category);
			}
		}
		System.out.println("RESULTING TOP CATEGORY SIZE" + result.size());
		return result;

	}

	/**
	 * This method adds text to the {@link #tagCloudText} to be appended to the
	 * file after the session. This method uses text from the searching and
	 * browsing activity throughout the archive in order to monitor frequent
	 * searches and offer a trend
	 * 
	 * @param text
	 *            {@link String} instance holding the text to be added to the
	 *            tag cloud
	 * @param tokenize
	 *            {@link boolean} indicating whether the
	 * @param text
	 *            should be tokenized. The input is tokenized if the method is
	 *            invoked from searching using the search box, however, if the
	 *            method is invoked from browsing the archive the text is not
	 *            tokenized. This ensures that album names and category names
	 *            are correctly recorded
	 */
	/* this must be done with every single search and browse */
	public static void addTextToTagCloud(String text, boolean tokenize) {
		if (text == null) {
			return;
		}
		if (tokenize) {
			String[] splitText = text.split(" ");
			// we want to eliminate any non-character text
			for (String singleWord : splitText) {
				String word = singleWord.replaceAll("[^a-zA-Z0-9\\s]", "");
				tagCloudText.append(word).append(",");

			}
		}
		tagCloudText.append(text.replaceAll("[^a-zA-Z0-9\\s]", "")).append(",");

	}

	/**
	 * This method is called at the end of the interaction with the specific
	 * archive after having recorded all the searching and browsing activity.
	 * This method persists the record to a text file stored on the file system
	 * to be used to construct the tag cloud found under the History tab
	 * 
	 * @param filename
	 *            {@link String} instance representing the filename to append
	 *            to. This is since each archive has it's own file containing
	 *            words to construct the tagcloud with
	 */
	public void persistTagCloudText(String filename) {
		System.out.println("Persisting tag cloud text......");
		try {

			File file = new File("../webapps/data/");
			if (!file.exists()) {
				System.out
						.println("OH NO THE DIRECTORY DOES NOT EXIST....WE MUST CREATE IT");
				file.mkdir();
			}
			FileWriter fileWriter = new FileWriter(filename, true);

			System.out.println("Text to persist: " + tagCloudText.toString()
					+ " to file " + filename);
			fileWriter.write(tagCloudText.toString().toLowerCase());
			fileWriter.flush();
			fileWriter.close();

		} catch (IOException e) {
			System.out.println(e);
		}
		tagCloudText = new StringBuilder("");
	}

	/**
	 * This method processes the words from the tagcloud file. The processing
	 * involves storing the word and the number of times it has occurred in the
	 * file. This is achieved by using a map
	 * 
	 * @param input
	 *            {@link HashMap} instance where the key is the word and the
	 *            value is the number of occurrences of the word.
	 * @param text
	 *            {@link String} instance representing a line of text from the
	 *            tagcloud text file
	 */
	private void processTagCloudWords(HashMap<String, Integer> input,
			String text) {
		String[] words = text.split(",");
		for (String word : words) {
			if (input.containsKey(word)) {
				input.put(word, input.get(word) + 1);
			} else {
				input.put(word, 1);
			}
		}

	}

	/**
	 * This method constructs the tagcloud by reading in the tagcloud text file
	 * and processing the words contained accordingly
	 * 
	 * @param filename
	 *            {@link String} instance representing the file to be read
	 * @return {@link HashMap} instance containing all the words and their
	 *         occurences to be used for the tagcloud
	 */
	public HashMap<String, Integer> constructTagCloud(String filename) {
		System.out.println("constructing tag cloud");
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		try {
			BufferedReader input = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = input.readLine()) != null)
				processTagCloudWords(words, line);
			input.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		System.out
				.println("Cloud constructed with " + words.size() + " words ");
		return words;
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
	 * @param filterValue}
	 */
	public static Set<FedoraDigitalObject> filterFedoraDigitalObjects(
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

		return filteredObjects;
	}

}
