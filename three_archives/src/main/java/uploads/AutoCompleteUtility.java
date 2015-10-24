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
import configuration.PropertiesHandler;
import search.SearchController;

/**
 * The {@code AutoCompleteUtility} is responsible for assembling the
 * autocomplete.json files to be used for each archive. These files are
 * assembled dependent on the contents of the actual archives
 * 
 * @author mthnox003
 *
 */
public class AutoCompleteUtility {
	/**
	 * The {@link HashMap} instance representing the archives available for
	 * exploration within the system
	 */
	private HashMap<String, String> archives;
	/**
	 * The {@link Set} instance of Fedora digital objects
	 */
	private Set<FedoraDigitalObject> fedoraDigitalObjects;
	/**
	 * The {@link HashMap} instance of the archival properties
	 */
	private HashMap<String, PropertiesHandler> archiveProperties;

	/**
	 * Constructor initialising the {@link #archives}
	 */
	public AutoCompleteUtility() {
		archives = new HashMap<String, String>();
	}

	/**
	 * This method allows for the refreshing of all the autcomplete files for
	 * each archive
	 */
	public void refreshAllAutocompleteFiles() {
		// need to find all the files and their archive properties...
		retrieveArchives();
		try {
			refreshFiles(archives);
		} catch (Exception ex) {
			System.out.println("Issue with generating files for autcomplete");
			System.out.println(ex);
		}
	}

	/**
	 * This method loads the archival properties
	 */
	private void loadArchiveProperties() {
		System.out.println("IN LOAD ARCHIVE PROPERTIES");
		archiveProperties = new HashMap<String, PropertiesHandler>();
		ClassLoader classLoader = AutoCompleteUtility.class.getClassLoader();
		File directory = new File(classLoader.getResource("configuration").getFile());
		if (directory != null) {
			for (File file : directory.listFiles()) {
				if (file.getName().contains(".properties") && !file.getName().contains("general")) {
					archiveProperties.put(file.getName(), new PropertiesHandler(file.getAbsolutePath()));
				}
			}
		}
	}

	/**
	 * This allows for the retrieval of all the archives accessible within the
	 * application
	 */
	private void retrieveArchives() {
		if (archiveProperties == null) {
			loadArchiveProperties();
		}
		System.out.println("PROCESSING ARCHIVE PROPERTIES");

		for (String archive : archiveProperties.keySet()) {
			System.out.println("Properties file : " + archive);
			String name = archiveProperties.get(archive).getProperty("archive.name").replaceAll("[^a-zA-Z0-9\\s]", "")
					.replaceAll("\\s+", "");
			System.out.println("Archive name : " + name);
			archives.put(archiveProperties.get(archive).getProperty("archive.name"),
					archiveProperties.get(archive).getProperty("archive.multimedia.prefix"));
		}
		System.out.println("WE FOUND " + archiveProperties.size() + " archives");
		System.out.println(archiveProperties.toString());

	}

	/**
	 * This refreshes the autocomplete files
	 * 
	 * @param archives
	 *            {@link HashMap} instance representing the archives
	 * @throws Exception
	 */
	private static void refreshFiles(HashMap<String, String> archives) throws Exception {
		AutoCompleteUtility utility = new AutoCompleteUtility(archives);
		try {
			utility.retrieveAllFedoraDigitalObjects();
			utility.buildAllAutocompleteJSONFiles();
		} catch (Exception ex) {
			System.out.println(ex);
			throw new Exception("An error seems to have occurred, please report to IT", ex);

		}
	}

	/*
	 * this will get called whenever an upload has been completed....after the
	 * upload and after the re-index We will regenerate the autocomplete files
	 * for all the archives
	 */
	private AutoCompleteUtility(HashMap<String, String> archives) {
		this.archives = archives;// this is the name of the archive as well as
									// the media prefix string....

	}

	/**
	 * This retrieves all the digital objects contained within the archive
	 * 
	 * @throws SolrServerException
	 * @throws FedoraException
	 */
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

	/**
	 * This begins the process of building the autocomplete files once having
	 * obtained all the archives
	 */
	private void buildAllAutocompleteJSONFiles() throws IOException {
		for (String archive : archives.keySet()) {
			Set<FedoraDigitalObject> objects = filterFedoraObjectsForSpecificArchive(archives.get(archive));
			String fileName = archive.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "");
			buildAutocompleteJSONFile(fileName, objects);
		}

	}

	/**
	 * This filters the digital objects from the archive to ensure that those
	 * being used for the generation of the autocomplete files are those
	 * pertaining to the specific archive
	 * 
	 * @param multiMediaPrefix
	 *            {@link String} instance representing the prefix of the PID for
	 *            each of the archives
	 * @return
	 */
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

	/**
	 * This builds the autocomplete json files by extracting the metadata from
	 * the digital objects for the specific archive and appending it to an array
	 * to be written to the json file
	 * 
	 * @param archive
	 *            {@link String} instance representing the archive whose
	 *            autocomplete file is being generated
	 * @param filteredObjects
	 *            {@link Set} instance representing the digital objects for the
	 *            specific archive
	 * @throws IOException
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
			File fileExisting = new File(fileName);

			FileWriter file = new FileWriter(fileExisting);
			file.write(list.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			System.out.println(e);
			throw e;
		}
	}

	/**
	 * This allows for the extraction of the actual words to be used within the
	 * autocomplete files. This is done by interrogating the metadata of the
	 * digital objects contained within teh specific archive
	 * 
	 * @param filteredFedoraDigitalObjects
	 *            {@link Set} instance representing the digital objects
	 *            contained within the archive
	 * @return
	 */
	private TreeSet<String> autocompleteValues(Set<FedoraDigitalObject> filteredFedoraDigitalObjects) {
		TreeSet<String> values = new TreeSet<String>();

		for (FedoraDigitalObject fedoraDigitalObject : filteredFedoraDigitalObjects) {
			DublinCoreDatastream dublinCoreDatastream = (DublinCoreDatastream) fedoraDigitalObject.getDatastreams()
					.get(DatastreamID.DC.name());

			for (String dublinCoreFieldValue : dublinCoreDatastream.getDublinCoreMetadata().values()) {
				String[] splitPercentage = dublinCoreFieldValue.split("%");
				values.addAll(Arrays.asList(splitPercentage));
				for (String string : splitPercentage) {
					// now we split by spaces to get the individual tokens
					String[] spaceSplit = string.split(" ");
					values.addAll(Arrays.asList(spaceSplit));
				}
			}
		}

		return values;

	}

}
