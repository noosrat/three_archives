package configuration;

import java.util.HashMap;
import java.util.Scanner;

/**
 * The configuration utility is responsible for the introduction of a new
 * archive to the Personal Histories archival collection. The utility is
 * responsible for the generation of the classes and properties to map to the
 * new archive. It involves reading in a properties file and processing the
 * properties accordingly. The utility regenerates the landing page for the
 * personal histories collection to include an image for the new archive and
 * generates the properties file as well as a blank home page for the new
 * collection. The application then renders the rest of the pages depenedent on
 * the properties contained within the properties file.
 *
 */
public class ConfigurationUtility {

	/**
	 * This reads in the properties for the new archive and generates the
	 * properties file to be used within the application along with the index page and the home page for the new archive.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		HashMap<String, String> details = new HashMap<String, String>();
		System.out.println("*******************************");
		System.out.println(
				"Welcome to the Archive Configuration Utility.\n  This will assist in the addition of your new archive.  \n Please may you enter the required data when prompted.");
		System.out.println("*******************************");
		System.out.println("Name of Archive");
		details.put("archive.name", input.nextLine());
		System.out.println("The full name of the image to appear on the landing page for personal histories.  This image should be placed in the /images folder of the application.");
		details.put("archive.landingpage.image", "/images/"+input.nextLine());
		System.out.println("Prefix for multimedia file IDs i.e. mn");
		details.put("archive.multimedia.prefix", input.nextLine());
		System.out.println(
				"The following involves the archival services.  Please indicate with a true or false as to whether the new archive will require these services");
		System.out.println("Search and Browse");
		details.put("service.searchandbrowse", input.nextLine());
		System.out.println("History and Statistics");
		details.put("service.historyandstatistics", input.nextLine());
		System.out.println("Exhibitions");
		details.put("service.exhibitions", input.nextLine());
		System.out.println("Uploads");
		details.put("service.uploads", input.nextLine());
		System.out.println("Maps");
		details.put("serivce.maps", input.nextLine());
		System.out.println("Downloads");
		details.put("service.downloads", input.nextLine());
		System.out.println("User Commenting");
		details.put("service.annotations", input.nextLine());
		System.out.println("Thank you.  The files for your new archive will now be generated and processed");
		input.close();

		try {
			String properties = PropertiesConfiguration.generatePropertiesFile(details);
			PageGeneration.locateAndAssignProperties();
			PageGeneration.generateIndexPage();
			PageGeneration.generateEmptyArchiveHomePage(properties);
		} catch (Exception e) {
			System.out.println("An issue seems to have occurred while attempting to generate the archive files");
			e.printStackTrace();
		}

	}

}
