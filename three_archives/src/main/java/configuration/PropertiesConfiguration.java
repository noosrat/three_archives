package configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Properties;

public class PropertiesConfiguration {

	public static void generatePropertiesFile(HashMap<String, String> archiveDetails) throws Exception {
		String name = archiveDetails.get("archive.name").replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "");
		FileOutputStream output = null;
		Properties specificProperties = new Properties();
		
		ClassLoader classLoader = PropertiesConfiguration.class.getClassLoader();
		File path = new File(classLoader.getResource("configuration").getFile());
		System.out.println("THIS IS WHAT WE FOUND AS THE PATH " + path.getAbsolutePath());
		try {
//			output = new FileOutputStream(path.getAbsolutePath()+ "/" + name + ".properties");
			output = new FileOutputStream(name + ".properties");
//			output = new FileOutputStream("/src/main/resources/configuration/" + name + ".properties");
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
		

	}

	public static void main(String[] args) {
		HashMap<String, String> details = new HashMap<String, String>();
		details.put("archive.name", "Test Achive");
		details.put("archive.landingpage.image", "/image/newArchive.jpeg");
		details.put("archive.multimedia.prefix", "nm");
		details.put("service.searchandbrowse", "true");
		details.put("service.historyandstatistics", "true");
		details.put("service.exhibitions", "false");
		details.put("service.uploads", "true");
		details.put("serivce.maps", "false");
		details.put("service.downloads", "true");
		details.put("service.annotations", "false");

		PropertiesConfiguration u = new PropertiesConfiguration();
		try{
		u.generatePropertiesFile(details);}catch(Exception ex){
			
		}
		// after we have generated this home.jsp we then need to create a new
		// section in our index.jsp for this archive and point
		// once we have a properties file let us just generate that home page
		// jsp...all we need to change there is the image and the actual archive
		// name
	}

}
