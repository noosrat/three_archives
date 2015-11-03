package configuration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.junit.Test;

public class PropertiesConfigurationTest {
	@Test
	public void testGeneratePropertiesFile() {

		HashMap<String, String> details = new HashMap<String, String>();
		details.put("archive.name", "Test Archive");
		details.put("archive.landingpage.image", "/images/test.jpg");
		details.put("archive.multimedia.prefix", "tst");
		details.put("service.searchandbrowse", "true");
		details.put("service.historyandstatistics", "false");
		details.put("service.exhibitions", "false");
		details.put("service.uploads", "false");
		details.put("serivce.maps", "true");
		details.put("service.downloads", "true");
		details.put("service.annotations", "true");

		HashMap<String, String> actualResult = new HashMap<String, String>();
		try {
			PropertiesConfiguration.generatePropertiesFile(details);
		} catch (Exception ex) {
			System.out.println(ex);
			throw new AssertionError("Test failed because of exception");
		}
		// we now need to check whether the file was created and need to read
		// it's properties
		File file = new File(
				"src/main/resources/configuration/testArchive.properties");
		if (!file.exists()) {
			throw new AssertionError("Test failed.  File does not exist");
		}

		// read file contents and see if correspond with input
		Properties p = new Properties();

		try {
			InputStream in = new FileInputStream(file);
			p.load(in);
		} catch (IOException ex) {
			System.out.println(ex);
			throw new AssertionError("Test failed.  File not read in ");
		}

		for (String s : p.stringPropertyNames()) {
			actualResult.put(s, p.getProperty(s));
		}

		assertEquals(details.size(), actualResult.size());
		assertEquals(details, actualResult);
		File f = new File(
				"src/main/resources/configuration/testArchive.properties");
		f.delete();
	}

}
