package search;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.fedora.Datastream;
import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.State;

public class BrowseTest {

	Browse instance;

	@Before
	public void setUp() throws Exception {
		instance = new Browse();
	}

	@After
	public void tearDown() throws Exception {
	}

	private HashMap<String, Datastream> getDatastreams(String pid) {

		HashMap<String, Datastream> datastreams = new HashMap<String, Datastream>();
		Datastream img = new Datastream(pid, DatastreamID.IMG);
		int random = (int) Math.random() * 10;
		img.setLocation("documents/images/img" + random + ".jpg");
		datastreams.put(DatastreamID.IMG.name(), new Datastream(pid,
				DatastreamID.IMG));
		HashMap<String, String> meta = new HashMap<String, String>();
		DublinCoreDatastream dc = new DublinCoreDatastream(pid, meta);
		datastreams.put(DatastreamID.DC.name(), dc);
		return datastreams;
	}

	private FedoraDigitalObject singleObject(String pid) {
		FedoraDigitalObject fedoraDigitalObject = new FedoraDigitalObject(pid);
		fedoraDigitalObject.setState(State.A);
		fedoraDigitalObject.setDateCreated(new Date());
		fedoraDigitalObject.setDateLastModified(new Date());
		fedoraDigitalObject.setDatastreams(getDatastreams(fedoraDigitalObject
				.getPid()));
		fedoraDigitalObject.setXmlRepresentation(null);
		fedoraDigitalObject.setVersionHistory(new ArrayList(Arrays.asList(
				new Date().toString(), new Date(1000).toString(), new Date(
						12345678).toString())));
		return fedoraDigitalObject;

	}

	private Set<FedoraDigitalObject> getMixedFedoraDigitalObjects() {
		Set<FedoraDigitalObject> objects = new HashSet<FedoraDigitalObject>();
		for (int i = 0; i < 5; i++) {
			FedoraDigitalObject dig = singleObject("pref1:" + i);
			objects.add(dig);

			DublinCoreDatastream dc = (DublinCoreDatastream) dig
					.getDatastreams().get(DatastreamID.DC.name());
			HashMap<String, String> meta = dc.getDublinCoreMetadata();
			meta.put(DublinCore.TITLE.name(), "test ");
			meta.put(DublinCore.DATE.name(), "1990");
			meta.put(DublinCore.CONTRIBUTOR.name(), "Test contributor 3");
			meta.put(DublinCore.FORMAT.name(), "jpg");

		}

		for (int i = 0; i < 5; i++) {
			FedoraDigitalObject dig = singleObject("pref2:" + i);
			objects.add(dig);

			DublinCoreDatastream dc = (DublinCoreDatastream) dig
					.getDatastreams().get(DatastreamID.DC.name());
			HashMap<String, String> meta = dc.getDublinCoreMetadata();
			meta.put(DublinCore.SOURCE.name(), "testSource");
			meta.put(DublinCore.DATE.name(), "1995");
			meta.put(DublinCore.PUBLISHER.name(), "Test publisher 3");
			meta.put(DublinCore.TYPE.name(), "photograph");

		}

		for (int i = 0; i < 5; i++) {
			FedoraDigitalObject dig = singleObject("pref3:" + i);
			objects.add(dig);

			DublinCoreDatastream dc = (DublinCoreDatastream) dig
					.getDatastreams().get(DatastreamID.DC.name());
			HashMap<String, String> meta = dc.getDublinCoreMetadata();
			meta.put(DublinCore.TITLE.name(), "browse test ");
			meta.put(DublinCore.DATE.name(), "1990");
			meta.put(DublinCore.CONTRIBUTOR.name(), "contributor 3");
			meta.put(DublinCore.FORMAT.name(), "img");
			meta.put(DublinCore.SOURCE.name(), "source");
			meta.put(DublinCore.PUBLISHER.name(), "publisher 3");
			meta.put(DublinCore.TYPE.name(), "newspaper");

		}
		return objects;
	}

	@Test
	public void testInitialise() {
		Set<FedoraDigitalObject> objects = getMixedFedoraDigitalObjects();
		Set<FedoraDigitalObject> expectedResult = new HashSet<FedoraDigitalObject>(
				objects);
		instance.setFedoraDigitalObjects(getMixedFedoraDigitalObjects());
		try {
			instance.initialise("pref2");
			Set<FedoraDigitalObject> actualResult = instance
					.getFedoraDigitalObjectsForArchive();
			// we now need to compare it with the result.
			for (Iterator<FedoraDigitalObject> it = expectedResult.iterator(); it
					.hasNext();) {
				FedoraDigitalObject o = it.next();
				if (!o.getPid().contains("pref2")) {
					it.remove();
				}
			}

			assertEquals(expectedResult.size(), actualResult.size());
			assertEquals(expectedResult, actualResult);

			// we must check the browsing categories and values now
			TreeMap<String, TreeSet<String>> expectedSearchAndBrowsingCategories = new TreeMap<String, TreeSet<String>>();
			// this is dependent on our prefix
			expectedSearchAndBrowsingCategories.put(
					SearchAndBrowseCategory.SOURCE.name(), new TreeSet<String>(
							Arrays.asList("testSource")));
			expectedSearchAndBrowsingCategories.put(
					SearchAndBrowseCategory.YEAR.name(), new TreeSet<String>(
							Arrays.asList("1995")));
			expectedSearchAndBrowsingCategories.put(
					SearchAndBrowseCategory.PUBLISHER.name(),
					new TreeSet<String>(Arrays.asList("Test publisher 3")));
			expectedSearchAndBrowsingCategories.put(
					SearchAndBrowseCategory.TYPE.name(), new TreeSet<String>(
							Arrays.asList("photograph")));

			TreeMap<String, TreeSet<String>> actualSearchAndBrowsingCategories = instance
					.getBrowsingCategories();
			assertEquals(expectedSearchAndBrowsingCategories.size(),
					actualSearchAndBrowsingCategories.size());
			assertEquals(expectedSearchAndBrowsingCategories,
					actualSearchAndBrowsingCategories);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AssertionError("Test failed");
		}

	}

	@Test
	public void testFilterFedoraDigitalObjects() {
		// at the end of the filtering check the filtered objects and filtered
		// categories
		// check filteredDigital objects
		// we will be testing with all the objects we have
		// we will just check filter by date 1990
		Set<FedoraDigitalObject> originalObjects = getMixedFedoraDigitalObjects();
		Set<FedoraDigitalObject> expectedFilteredObjects = new HashSet<FedoraDigitalObject>(
				originalObjects);
		// removing those that do not match our date 1990 filter
		for (Iterator<FedoraDigitalObject> it = expectedFilteredObjects
				.iterator(); it.hasNext();) {
			FedoraDigitalObject obj = it.next();
			DublinCoreDatastream dc = (DublinCoreDatastream) obj
					.getDatastreams().get(DatastreamID.DC.name());
			if (dc.getDublinCoreMetadata().get(DublinCore.DATE.name()) != null
					&& !dc.getDublinCoreMetadata().get(DublinCore.DATE.name())
							.contains("1990")) {
				it.remove();
			}
		}
		instance.setFedoraDigitalObjects(originalObjects);
		try {
			instance.initialise("pre");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AssertionError("Test failed");
		}
		
		instance.filterFedoraDigitalObjects(originalObjects,
				SearchAndBrowseCategory.YEAR.name(), "1990");
		Set<FedoraDigitalObject> actualFilteredDigitalObjects = instance
				.getFilteredDigitalObjects();
		assertEquals(expectedFilteredObjects.size(),
				actualFilteredDigitalObjects.size());
		assertEquals(expectedFilteredObjects, actualFilteredDigitalObjects);
		// now we check the categories
		TreeMap<String, TreeSet<String>> expectedBrowsingcategories = new TreeMap<String, TreeSet<String>>();
		expectedBrowsingcategories.put(SearchAndBrowseCategory.TITLE.name(),
				new TreeSet<String>(Arrays.asList("t", "b")));
		expectedBrowsingcategories.put(SearchAndBrowseCategory.YEAR.name(),
				new TreeSet<String>(Arrays.asList("1990", "1995")));
		expectedBrowsingcategories.put(
				SearchAndBrowseCategory.CONTRIBUTOR.name(),
				new TreeSet<String>(Arrays.asList("Test contributor 3",
						"contributor 3")));
		expectedBrowsingcategories.put(SearchAndBrowseCategory.FORMAT.name(),
				new TreeSet<String>(Arrays.asList("IMG")));
		expectedBrowsingcategories.put(SearchAndBrowseCategory.SOURCE.name(),
				new TreeSet<String>(Arrays.asList("testSource", "source")));
		expectedBrowsingcategories.put(
				SearchAndBrowseCategory.PUBLISHER.name(), new TreeSet<String>(
						Arrays.asList("Test publisher 3", "publisher 3")));
		expectedBrowsingcategories.put(SearchAndBrowseCategory.TYPE.name(),
				new TreeSet<String>(Arrays.asList("newspaper", "photograph")));
		expectedBrowsingcategories.remove(SearchAndBrowseCategory.YEAR.name());
		TreeMap<String, TreeSet<String>> actualBrowsingCategories = instance
				.getFilteredBrowsingCategories();
		assertEquals(expectedBrowsingcategories.size(),
				actualBrowsingCategories.size());
		assertEquals(expectedBrowsingcategories, actualBrowsingCategories);
	}

}
