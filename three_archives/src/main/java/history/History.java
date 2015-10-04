package history;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.servlet.http.Cookie;

import common.Service;
import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import search.Browse;

public class History extends Service {

	private static StringBuilder tagCloudText = new StringBuilder();

	public History() {
		super();
	}

	public ArrayList<FedoraDigitalObject> retrieveDigitalObjectsAlteredSinceLastVisit(Date lastVisited) {
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

	public HashMap<String, TreeSet<String>> categoriesRecentlyUpdated(
			ArrayList<FedoraDigitalObject> objectsRecentlyModified) {
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
	public ArrayList<FedoraDigitalObject> retrieveRecentlyUpdateItems(String date) throws Exception {
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
		ArrayList<FedoraDigitalObject> fedoraDigitalObjects = retrieveDigitalObjectsAlteredSinceLastVisit(
				dateLastVisited);
		return fedoraDigitalObjects;

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
	public static void addTextToTagCloud(String text) {
		System.out.println("Adding text to tag cloud text...wooohooooo");
		String[] splitText = text.split(" ");
		// we want to eliminate any non-character text
		for (String singleWord : splitText) {
			String word = singleWord.replaceAll("[^a-zA-Z0-9\\s]", ""); 
			tagCloudText.append(word).append(",");

		}

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

		/*try {
			FileWriter file = new FileWriter(filename,true);
			System.out.println("Text to persist: " + tagCloudText.toString() + " to file " + filename);
			file.write(tagCloudText.toString().toLowerCase());
			file.flush();
			file.close();


		} catch (IOException e) {
			System.out.println(e);
		}*/
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
		/*try {
			BufferedReader input = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = input.readLine()) != null)
				processTagCloudWords(words, line);
			input.close();
		} catch (IOException e) {
			System.out.println(e);
		}*/
		System.out.println("Cloud constructed with " + words.size() + " words ");
		return words;
	}
}
