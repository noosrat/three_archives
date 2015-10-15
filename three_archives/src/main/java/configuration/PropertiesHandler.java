package configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * 
 * The {@code PropertiesHandler} allows for the parsing of the properties files
 * as well as efficient retrieval of property values from the files
 * 
 *
 */
public class PropertiesHandler {

	/**
	 * The properties for the specific archive
	 */
	private final Properties configurationProperties;

	/**
	 * Constructor which takes in the fileName to be processed. This results in
	 * the properties from the properties file being successfully loaded into a
	 * java object to allow for manipulation and use.
	 * 
	 * @param fileName
	 *            {@link String} instance representing the properties file being
	 *            read in
	 */
	public PropertiesHandler(String fileName) {
		configurationProperties = new Properties();
		try {
			InputStream in = new FileInputStream(new File(fileName));
			configurationProperties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a specific property from the properties file based on the key
	 * specified
	 * 
	 * @param key
	 *            {@link String} instance of the name of the property being
	 *            retrieved
	 * @return {@link String} instance of the value mapped to the specified key
	 */
	public String getProperty(String key) {
		return configurationProperties.getProperty(key);
	}

	/**
	 * Gets all the property names contained within the properties file
	 * 
	 * @return {@link Set} instance of all the property names
	 */
	public Set<String> getAllPropertyNames() {
		return configurationProperties.stringPropertyNames();
	}

	/**
	 * Indicates whether the properties file contains a specific property/key
	 * 
	 * @param key
	 *            {@link String} instance representing the property name being
	 *            queried
	 * @return {@link boolean} indicating whether the specified property is
	 *         contained within the file
	 */
	public boolean containsKey(String key) {
		return configurationProperties.containsKey(key);
	}

	/**
	 * String representation of all the properties and their vlaues
	 * 
	 */

	public String toString() {
		String result = "";
		for (String prop : getAllPropertyNames()) {
			result += prop + " = " + getProperty(prop) + "\n";
		}
		return result;
	}
}
