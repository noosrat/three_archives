package uploads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.solr.client.solrj.SolrServerException;
import org.json.simple.JSONArray;

import common.fedora.DatastreamID;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
import search.SearchController;

public class AutoCompleteUtility {
	private HashMap<String, String> archives;
	private Set<FedoraDigitalObject> fedoraDigitalObjects;

	public static void refreshAutocompleFile(HashMap<String, String> archives) throws Exception {
		AutoCompleteUtility utility = new AutoCompleteUtility(archives);
		try {
			utility.retrieveAllFedoraDigitalObjects();
			utility.buildAllAutocompleteJSONFiles();
		} catch (Exception ex) {
			System.out.println(ex);
			throw new Exception("An error seems to have occurred, please report to IT",ex);

		}
	}

	/*
	 * this will get called whenever an upload has been completed....after the
	 * upload and after the re-index We will regenerate the autocomplete files
	 * for all the archives
	 */
	private AutoCompleteUtility(HashMap<String, String> archives) {
		// TODO Auto-generated constructor stub
		this.archives = archives;// this is the name of the archive as well as
									// the media prefix string....

	}

	private void retrieveAllFedoraDigitalObjects() throws SolrServerException, FedoraException {
		try {
			fedoraDigitalObjects = SearchController.getSearch().findFedoraDigitalObjects("*");
		} catch (FedoraException e) {
			System.out.println(e);
			throw e;
		} catch (SolrServerException e) {
			System.out.println(e);
			throw e;
		}
		System.out.println("Retrieved objects : " + fedoraDigitalObjects.size());
	}

	// we have all the objects and all the archives onw we can build the files
	private void buildAllAutocompleteJSONFiles() throws IOException {
		// need to alter the values of the hashmap to make sure there are no
		// speces etc
		for (String archive : archives.keySet()) {
			Set<FedoraDigitalObject> objects = filterFedoraObjectsForSpecificArchive(archives.get(archive));
			String fileName = archive.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "");
			buildAutocompleteJSONFile(fileName, objects);
		}

	}

	private Set<FedoraDigitalObject> filterFedoraObjectsForSpecificArchive(String multiMediaPrefix) {
		Set<FedoraDigitalObject> filteredFedoraDigitalObjects = new HashSet<FedoraDigitalObject>(fedoraDigitalObjects);
		for (Iterator<FedoraDigitalObject> iterator = filteredFedoraDigitalObjects.iterator(); iterator.hasNext();) {
			FedoraDigitalObject element = iterator.next();
			if (!(element.getPid().contains(multiMediaPrefix))) {
				iterator.remove();
				System.out.println("removing object with pid " + element.getPid());
			}
		}
		return filteredFedoraDigitalObjects;
	}

	/*
	 * the below needs to moved to occur whenever there is an upload to the
	 * database...the data will then be retrieved from the db from the dofields
	 * table..
	 */
	private void buildAutocompleteJSONFile(String archive, Set<FedoraDigitalObject> filteredObjects)
			throws IOException {
		JSONArray list = new JSONArray();
		Set<String> values = autocompleteValues(filteredObjects);
		for (String s : values) {
			list.add(s);
		}
		try {

			String fileName = "../webapps/data/" + archive + ".json";
			System.out.println("We are making the autocomplere file for " + fileName);
			File dir = new File(fileName);
			if (!dir.exists()) {
				System.out.println("OH NO THE DIRECTORY DOES NOT EXIST....WE MUST CREATE IT");
				dir.mkdir();
			}
			FileWriter file = new FileWriter(fileName);
			file.write(list.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			System.out.println(e);
			throw e;
		}
	}

	private TreeSet<String> autocompleteValues(Set<FedoraDigitalObject> filteredFedoraDigitalObjects) {
		TreeSet<String> values = new TreeSet<String>();

		for (FedoraDigitalObject fedoraDigitalObject : filteredFedoraDigitalObjects) {
			DublinCoreDatastream dublinCoreDatastream = (DublinCoreDatastream) fedoraDigitalObject.getDatastreams()
					.get(DatastreamID.DC.name());

			for (String dublinCoreFieldValue : dublinCoreDatastream.getDublinCoreMetadata().values()) {
				String[] splitPercentage = dublinCoreFieldValue.split("%");
				values.addAll(Arrays.asList(splitPercentage));
				for (String string : splitPercentage) {
					// now we split by spaces to get the individual tokents
					String[] spaceSplit = string.split(" ");
					values.addAll(Arrays.asList(spaceSplit));
				}
			}
		}

		return values;

	}


}
