package search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import common.Service;
import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.State;

public class SearchTest {

	private Search instance;

	/**
	 * To test: findFedoraDigitalObjects
	 */

	@Before
	public void setUp() throws Exception {
		instance = new Search();
	}

	private Set<FedoraDigitalObject> getMixedFedoraDigitalObjects() {
		Set<FedoraDigitalObject> objects = new HashSet<FedoraDigitalObject>();
		for (int i = 0; i < 5; i++) {
			objects.add(new FedoraDigitalObject("pref:" + i));
		}
		return objects;
	}

	@Test
	public void testFindFedoraDigitalObjects() {
		FedoraCommunicator fc = Mockito.mock(FedoraCommunicator.class);

		try {
			Field communicator = Service.class.getDeclaredField("communicator");
			communicator.setAccessible(true);
			communicator.set(instance, fc);
			Set<FedoraDigitalObject> objects = getMixedFedoraDigitalObjects();
			when(fc.findFedoraDigitalObjects("test", "search")).thenReturn(
					objects);
			Set<FedoraDigitalObject> actualObjects = instance
					.findFedoraDigitalObjects("test");
			assertEquals(objects.size(), actualObjects.size());
			assertEquals(objects, actualObjects);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AssertionError("Test failed");
		}
	}

}
