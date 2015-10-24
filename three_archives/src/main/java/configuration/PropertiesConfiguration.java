 package configuration;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class is responsible for the generation of the properties file from the
 * properties specified by the user. This properties file be used throughout the
 * application to turn the necessary services on and off dependent on what is
 * required within the archive.
 * 
 * @author mthnox003
 *
 */
public class PropertiesConfiguration {
	/**
	 * Generates the Properties file
	 * 
	 * @param archiveDetails
	 *            {@link HashMap} instance containing the properties required
	 *            within the properties file
	 * @return {@link String} instance of the archive name with all
	 *         non-alphanumeric characters and spaces removed
	 * @throws Exception
	 */
	public static String generatePropertiesFile(HashMap<String, String> archiveDetails) throws Exception {
		String name = archiveDetails.get("archive.name").replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "");
		FileOutputStream output = null;
		Properties specificProperties = new Properties();
		try {
			output = new FileOutputStream("src/main/resources/configuration/" + name + ".properties");
			for (String property : archiveDetails.keySet()) {
				specificProperties.put(property, archiveDetails.get(property));
			}
			specificProperties.store(output, null);
		} catch (Exception ex) {
			System.out.println("Something has gone wrong when generating the properties file");
			System.out.println(ex);
			throw ex;
		} finally {
			output.close();
		}
		return name;

	}

}
