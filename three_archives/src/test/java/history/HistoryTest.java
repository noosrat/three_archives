package history;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.Cookie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import search.SearchAndBrowseCategory;

import common.fedora.Datastream;
import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.State;

public class HistoryTest {
	History instance;

	@Before
	public void setUp() throws Exception {
		instance = new History();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCategoriesRecentlyUpdated() {
	}

	private Set<FedoraDigitalObject> getExpectedResult() {
		Set<FedoraDigitalObject> objects = new HashSet<FedoraDigitalObject>();
		for (int x = 0; x < 5; x++) {
			objects.add(expectedResult());
		}
		return objects;
	}

	private ArrayList<String> getPids(
			Set<FedoraDigitalObject> fedoraDigitalObjects) {
		ArrayList<String> result = new ArrayList<String>();
		for (FedoraDigitalObject object : fedoraDigitalObjects) {
			result.add(object.getPid());
		}
		return result;
	}

	private FedoraDigitalObject expectedResult() {
		FedoraDigitalObject fedoraDigitalObject = new FedoraDigitalObject(
				"test:" + (int)(Math.random() *10));
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

	// @Test could not test this since retrieving the objects from the browse
	// controller which cannot be mocked since being instantiated within
	public void testRecentlyUpdatedItems() {
		Set<FedoraDigitalObject> testObjects = new HashSet<FedoraDigitalObject>();
		String date = "Mon Sep 28 14:49:16 SAST 2015";
		// iterate through these and assign different "date last modified"
		FedoraDigitalObject f1 = expectedResult();
		f1.setDateLastModified(new Date(10000000000L));
		FedoraDigitalObject f2 = expectedResult();
		f2.setDateLastModified(new Date(1000000));
		FedoraDigitalObject f3 = expectedResult();
		f3.setDateLastModified(new Date(9999999999999L));
		FedoraDigitalObject f4 = expectedResult();
		f4.setDateLastModified(new Date());

		testObjects.add(f1);
		testObjects.add(f2);
		testObjects.add(f3);
		testObjects.add(f4);
		try {
			instance.retrieveRecentlyUpdateItems(date);
		} catch (Exception ex) {
			throw new AssertionError("Test failed");

		}
		Set<FedoraDigitalObject> result = instance.getObjectsSinceLastVisit();
		assertEquals(2, result.size());
	}

	// @Test could not test this since private browse controller and objects
	// being used from there
	public void testFavouiteBrowsingCategories() {
		Cookie browsingCategoryCookie = new Cookie(
				"test",
				"Collection:5##Creator:0##Event:0##Exhibition:15##Location:0##Subject:11##Contributor:0##Source:9");
		HashMap<String, TreeSet<String>> updatedC = new HashMap<String, TreeSet<String>>();
		updatedC.put(
				"COLLECTION",
				new TreeSet<String>(Arrays.asList("Test Collection 1",
						"Test Collection 2", "Test Collection 3")));
		updatedC.put(
				"CREATOR",
				new TreeSet<String>(Arrays.asList("Test Creator 1",
						"Test Creator 2", "Test Creator 3")));
		updatedC.put(
				"EXHIBITION",
				new TreeSet<String>(Arrays.asList("Test exhibition 1",
						"Test exhibition 2", "Test exhibition 3")));
		updatedC.put(
				"LOCATION",
				new TreeSet<String>(Arrays
						.asList("Test location 1", "Test location 2",
								"Test location 3", "Test location 4")));

		ArrayList<String> expectedResult = new ArrayList<String>(
				Arrays.asList("Exhibition"));
		ArrayList<String> actualResult = instance
				.favouriteBrowsingCategoryUpdates(updatedC,
						browsingCategoryCookie);

		for (int s = 0; s < expectedResult.size(); s++) {
			assertEquals(expectedResult.get(s), actualResult.get(s));
		}
	}

	@Test
	public void testAddTextToTagCloud() {
		StringBuilder expectedResult = new StringBuilder("Test 1 2 three,");
		instance.addTextToTagCloud("Test 1 2 three;", false);
		StringBuilder result = instance.getTagCloudText();
		assertEquals(expectedResult.toString(), result.toString());
	}

	@Test
	public void testAddTextToTagCloudTokenize() {
		StringBuilder expectedResult = new StringBuilder(
				"Test,1,2,three,Test 1 2 three,");
		instance.setTagCloudText(new StringBuilder(""));
		instance.addTextToTagCloud("Test 1 2 three;", true);
		StringBuilder result = instance.getTagCloudText();
		assertEquals(expectedResult.toString(), result.toString());
	}

	// @Test
	public void testPersistTagCloudText() {
		// we will just check for the existence of the file afterwards
		// only way to check this is to persist a line and see if that line has
		// been written
		instance.persistTagCloudText("historyTest");
		StringBuilder tagCloud = instance.getTagCloudText();

		File file = new File("../webapps/data/historyTest");
		if (!file.exists()) {
			throw new AssertionError("test failed.  file does not exist");
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					file.getName()));
			String line;
			String result = "";
			while ((line = reader.readLine()) != null) {
				result += " " + line;
			}
			reader.close();
			assertTrue(result.contains(tagCloud));
		} catch (IOException e) {
			throw new AssertionError("test failed");
		}

	}

	@Test
	public void testConstructTagCloud() {

	}

	private Set<FedoraDigitalObject> prepareTestFedoraDigitalObjects() {
		Set<FedoraDigitalObject> objects = new HashSet<FedoraDigitalObject>();
		FedoraDigitalObject o1 = expectedResult();
		DublinCoreDatastream dc = (DublinCoreDatastream) o1.getDatastreams()
				.get(DatastreamID.DC.name());
		HashMap<String, String> meta = dc.getDublinCoreMetadata();
		meta.put(DublinCore.TITLE.name(), "test ");
		meta.put(DublinCore.DATE.name(), "1990");
		meta.put(DublinCore.CONTRIBUTOR.name(), "Test contributor 3");
		meta.put(DublinCore.FORMAT.name(), "jpg");
		objects.add(o1);
		o1 = expectedResult();
		dc = (DublinCoreDatastream) o1.getDatastreams().get(
				DatastreamID.DC.name());
		meta = dc.getDublinCoreMetadata();
		meta.put(DublinCore.TITLE.name(), "Not test ");
		meta.put(DublinCore.DATE.name(), "1990");
		meta.put(DublinCore.CONTRIBUTOR.name(), "Test contributor 2");
		meta.put(DublinCore.FORMAT.name(), "mpeg");
		objects.add(o1);
		o1 = expectedResult();
		dc = (DublinCoreDatastream) o1.getDatastreams().get(
				DatastreamID.DC.name());
		meta = dc.getDublinCoreMetadata();
		meta.put(DublinCore.TITLE.name(), "test again ");
		meta.put(DublinCore.DATE.name(), "2011");
		meta.put(DublinCore.CONTRIBUTOR.name(), "Test contributor 3");
		meta.put(DublinCore.FORMAT.name(), "jpeg");
		objects.add(o1);
		o1 = expectedResult();
		dc = (DublinCoreDatastream) o1.getDatastreams().get(
				DatastreamID.DC.name());
		meta = dc.getDublinCoreMetadata();
		meta.put(DublinCore.TITLE.name(), "my object ");
		meta.put(DublinCore.DATE.name(), "2010");
		meta.put(DublinCore.CONTRIBUTOR.name(), "Test contributor 1");
		meta.put(DublinCore.FORMAT.name(), "mp4");
		objects.add(o1);
		return objects;
	}

	private Set<FedoraDigitalObject> expectedResultFilterByTitle(
			Set<FedoraDigitalObject> objects) {
		// filter by title beginning with t
		Set<FedoraDigitalObject> result = new HashSet<FedoraDigitalObject>(
				objects);
		for (Iterator<FedoraDigitalObject> it = result.iterator(); it.hasNext();) {
			FedoraDigitalObject obj = it.next();
			DublinCoreDatastream dc = (DublinCoreDatastream) obj
					.getDatastreams().get(DatastreamID.DC.name());
			if (dc.getDublinCoreMetadata().get(DublinCore.TITLE.name()) != null
					&& !dc.getDublinCoreMetadata().get(DublinCore.TITLE.name())
							.startsWith("t")) {
				it.remove();
			}
		}

		return result;
	}

	private Set<FedoraDigitalObject> expectedResultFilterByDate(
			Set<FedoraDigitalObject> objects) {
		// 1990
		Set<FedoraDigitalObject> result = new HashSet<FedoraDigitalObject>(
				objects);
		for (Iterator<FedoraDigitalObject> it = result.iterator(); it
				.hasNext();) {
			FedoraDigitalObject obj = it.next();
			DublinCoreDatastream dc = (DublinCoreDatastream) obj
					.getDatastreams().get(DatastreamID.DC.name());
			if (dc.getDublinCoreMetadata().get(DublinCore.DATE.name()) != null
					&& !dc.getDublinCoreMetadata().get(DublinCore.DATE.name())
							.contains("1990")) {
				it.remove();
			}
		}

		return result;
	}

	private Set<FedoraDigitalObject> expectedResultFilterByContributor(
			Set<FedoraDigitalObject> objects) {
		// Test contributor 3
		Set<FedoraDigitalObject> result = new HashSet<FedoraDigitalObject>(
				objects);
		for (Iterator<FedoraDigitalObject> it = result.iterator(); it
				.hasNext();) {
			FedoraDigitalObject obj = it.next();
			DublinCoreDatastream dc = (DublinCoreDatastream) obj
					.getDatastreams().get(DatastreamID.DC.name());
			if (dc.getDublinCoreMetadata().get(DublinCore.CONTRIBUTOR.name()) != null
					&& !dc.getDublinCoreMetadata()
							.get(DublinCore.CONTRIBUTOR.name())
							.contains("Test contributor 3")) {
				it.remove();
			}
		}
		return result;
	}

	private Set<FedoraDigitalObject> expectedResultFilterByFormat(
			Set<FedoraDigitalObject> objects) {
		// jpg
		Set<FedoraDigitalObject> result = new HashSet<FedoraDigitalObject>(
				objects);
		for (Iterator<FedoraDigitalObject> it = result.iterator(); it
				.hasNext();) {
			FedoraDigitalObject obj = it.next();
			DublinCoreDatastream dc = (DublinCoreDatastream) obj
					.getDatastreams().get(DatastreamID.DC.name());
			if (dc.getDublinCoreMetadata().get(DublinCore.FORMAT.name()) != null
					&& !dc.getDublinCoreMetadata().get(DublinCore.FORMAT.name()).contains("jpg") && !dc.getDublinCoreMetadata().get(DublinCore.FORMAT.name()).contains("jpeg")){
				it.remove();
			}
		}
		return result;
	}

	@Test
	public void testFilterFedoraDigitalObjectsByTitle() {
		Set<FedoraDigitalObject> objects = prepareTestFedoraDigitalObjects();
		Set<FedoraDigitalObject> expected = expectedResultFilterByTitle(objects);
		Set<FedoraDigitalObject> actualResult = instance
				.filterFedoraDigitalObjects(objects, SearchAndBrowseCategory.TITLE.name(),
						"test");

		assertEquals(expected.size(), actualResult.size());
		assertEquals(expected, actualResult);

	}

	@Test
	public void testFilterFedoraDigitalObjectsByDate() {
		Set<FedoraDigitalObject> objects = prepareTestFedoraDigitalObjects();
		Set<FedoraDigitalObject> expected = expectedResultFilterByDate(objects);
		Set<FedoraDigitalObject> actualResult = instance
				.filterFedoraDigitalObjects(objects, SearchAndBrowseCategory.YEAR.name(),
						"1990");

		assertEquals(expected.size(), actualResult.size());
		assertEquals(expected, actualResult);

	}

	@Test
	public void testFilterFedoraDigitalObjectsByContributor() {
		Set<FedoraDigitalObject> objects = prepareTestFedoraDigitalObjects();
		Set<FedoraDigitalObject> expected = expectedResultFilterByContributor(objects);
		Set<FedoraDigitalObject> actualResult = instance
				.filterFedoraDigitalObjects(objects,
						SearchAndBrowseCategory.CONTRIBUTOR.name(), "Test contributor 3");

		assertEquals(expected.size(), actualResult.size());
		assertEquals(expected, actualResult);

	}

	@Test
	public void testFilterFedoraDigitalObjectsByFormat() {
		Set<FedoraDigitalObject> objects = prepareTestFedoraDigitalObjects();
		Set<FedoraDigitalObject> expected = expectedResultFilterByFormat(objects);
		Set<FedoraDigitalObject> actualResult = instance
				.filterFedoraDigitalObjects(objects, SearchAndBrowseCategory.FORMAT.name(),
						"jpg");

		assertEquals(expected.size(), actualResult.size());
		assertEquals(expected, actualResult);

	}

}
