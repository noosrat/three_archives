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

import common.Service;
import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import search.Browse;
import search.SearchAndBrowseCategory;

public class History extends Service {

	private static StringBuilder tagCloudText = new StringBuilder();
	private static HashSet<FedoraDigitalObject> objectsSinceLastVisit = new HashSet<FedoraDigitalObject>();

	public History() {
		super();
	}

	private void retrieveDigitalObjectsAlteredSinceLastVisit(Date lastVisited) {
		System.out.println("IN RETRIEVE DIGITAL OJBECTS ALTERED SINCE LAST VISIT");
		HashSet<FedoraDigitalObject> recentlyAlteredFedoraDigitalObjects = new HashSet<FedoraDigitalObject>(
				Browse.getFedoraDigitalObjectsForArchive());

		for (Iterator<FedoraDigitalObject> it = recentlyAlteredFedoraDigitalObjects.iterator(); it.hasNext();) {
			FedoraDigitalObject obj = it.next();
			System.out.println(
					"Date last visited " + lastVisited + "  date of object last mod " + obj.getDateLastModified());
			if (!lastVisited.before(obj.getDateLastModified())) {
				it.remove();
			}
		}
		setObjectsSinceLastVisit(recentlyAlteredFedoraDigitalObjects);
	}

	private static void setObjectsSinceLastVisit(HashSet<FedoraDigitalObject> fedoraDigitalObjects) {
		objectsSinceLastVisit = fedoraDigitalObjects;

	}

	public HashSet<FedoraDigitalObject> getObjectsSinceLastVisit() {
		return objectsSinceLastVisit;
	}

	public HashMap<String, TreeSet<String>> categoriesRecentlyUpdated(
			HashSet<FedoraDigitalObject> objectsRecentlyModified) {
		// we need to iterate through all the objects that have been updated and
		// pick out all the specific categories that have been updated
		// initialise hashmap
		HashMap<String, TreeSet<String>> updates = new HashMap<String, TreeSet<String>>();
//		for (DublinCore dc: DublinCore.values()){
//			switch(dc){
//			case TITLE:
//			case CREATOR:
//			case PUBLISHER:
//			case CONTRIBUTOR:
//			case DATE:
//			case TYPE:
//			case FORMAT:
//			case SOURCE:
//			case COVERAGE:
//			case SUBJECT:
//				updates.put(dc.name(), new TreeSet<String>());
//			}
//		}
		
		updates.put(SearchAndBrowseCategory.COLLECTION.name(), new TreeSet<String>());
		updates.put(SearchAndBrowseCategory.CREATOR.name(), new TreeSet<String>());
		updates.put(SearchAndBrowseCategory.SUBJECT.name(), new TreeSet<String>());
		updates.put(SearchAndBrowseCategory.EVENT.name(), new TreeSet<String>());
		updates.put(SearchAndBrowseCategory.YEAR.name(), new TreeSet<String>());
		updates.put(SearchAndBrowseCategory.LOCATION.name(), new TreeSet<String>());
		updates.put(SearchAndBrowseCategory.FORMAT.name(), new TreeSet<String>());
		updates.put(SearchAndBrowseCategory.TITLE.name(), new TreeSet<String>());

		for (FedoraDigitalObject object : objectsRecentlyModified) {
			DublinCoreDatastream dcDatastream = (DublinCoreDatastream) object.getDatastreams()
					.get(DatastreamID.DC.name());
			for (String dc : updates.keySet()) {
				String item = dcDatastream.getDublinCoreMetadata().get(dc);
				if (item != null) {
					updates.get(dc).add(item);
				}
			}
		}
		
		return updates;
	}

	/* this value has to remain throughout the session */
	public void retrieveRecentlyUpdateItems(String date) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy");
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

	/*
	 * once we retrieve these then we can indicate whether those categories have
	 * been updated recently/since the last visit
	 */
	private ArrayList<String> retrieveTopThreeBrowsingCategories(Cookie browseCategoryCookie) {
		String[] categories = browseCategoryCookie.getValue().split("##");
		HashMap<String, Integer> categoriesAndValues = new HashMap<String, Integer>();
		for (String category : categories) {
			String[] splitCategoryAndValue = category.split(":");
			categoriesAndValues.put(splitCategoryAndValue[0], Integer.parseInt(splitCategoryAndValue[1]));
		}

		List<Map.Entry<String, Integer>> elements = new ArrayList<Map.Entry<String, Integer>>();
		elements.addAll(categoriesAndValues.entrySet());
		System.out.println("Unsorted list " + categoriesAndValues.toString());
		// now let's sort the map according to the values/ratings
		Collections.sort(elements, new Comparator<Map.Entry<String, Integer>>() {

			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return -1 * (o1.getValue().compareTo(o2.getValue()));
			}

		});

		System.out.println("Sorted List categories and values " + categoriesAndValues.toString());
		System.out.println("Sorted List elements " + elements.toString());

		ArrayList<String> topBrowsedCategories = new ArrayList<String>();

		topBrowsedCategories.add(elements.get(0).getKey());
		topBrowsedCategories.add(elements.get(1).getKey());
		topBrowsedCategories.add(elements.get(2).getKey());

		System.out.println("TOP CAT: " + topBrowsedCategories.toString());
		return topBrowsedCategories;

	}

	public ArrayList<String> favouriteBrowsingCategoryUpdates(HashMap<String, TreeSet<String>> updatedCategories,
			Cookie browseCategoryCookie) {
		System.out.println("Processing favourite category updates with browseCategoryCookie of: "
				+ browseCategoryCookie.getValue());
		System.out.println("Updated categories " + updatedCategories.keySet().toString());
		ArrayList<String> topBrowsingCategories = retrieveTopThreeBrowsingCategories(browseCategoryCookie);
		ArrayList<String> result = new ArrayList<String>();
		for (String category : topBrowsingCategories) {
			if (updatedCategories.keySet().contains(category.toUpperCase()) && !result.contains(category)) {
				result.add(category);
			}
		}
		System.out.println("RESULTING TOP CATEGORY SIZE" + result.size());
		return result;

	}

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

	public void persistTagCloudText(String filename) {
		/*
		 * this will be dependent on the archive..so maybe from controller we
		 * should parse in which file we want to look at we continually write
		 * this...and only when we actually want to make / our tag cloud do we
		 * go through and manually process the file
		 */
		System.out.println("Persisting tag cloud text......");
		/*
		 * this needs to be done at the end of every request...do this when we
		 * are in our servlet just before despatch
		 */

		try {

			File file = new File("../webapps/data/");
			if (!file.exists()) {
				System.out.println("OH NO THE DIRECTORY DOES NOT EXIST....WE MUST CREATE IT");
				file.mkdir();
			}
			FileWriter fileWriter = new FileWriter(filename, true);

			System.out.println("Text to persist: " + tagCloudText.toString() + " to file " + filename);
			fileWriter.write(tagCloudText.toString().toLowerCase());
			fileWriter.flush();
			fileWriter.close();

		} catch (IOException e) {
			System.out.println(e);
		}
		tagCloudText = new StringBuilder("");
	}

	private void processTagCloudWords(HashMap<String, Integer> input, String text) {
		String[] words = text.split(",");
		for (String word : words) {
			if (input.containsKey(word)) {
				input.put(word, input.get(word) + 1);
			} else {
				input.put(word, 1);
			}
		}

	}

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
		System.out.println("Cloud constructed with " + words.size() + " words ");
		return words;
	}
	
	public static Set<FedoraDigitalObject> filterFedoraDigitalObjects(Set<FedoraDigitalObject> objectsToFilter, String filterCategory,
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
			case FORMAT: // should we look in the datastream type and
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

					if (DatastreamID.parseMediaType(format) != DatastreamID.parseDescription(media)) {
						iterator.remove();

					}
				} else {
					iterator.remove();
				}
				break;
			}

		}

	
		return filteredObjects;
	}

}
