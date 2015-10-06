package configuration;

import java.util.HashMap;
import java.util.Scanner;

public class ConfigurationUtility {
	/*
	 * This utility only generates for new classes..it does not worry about
	 * regeneration of the old ones... Therefore all we need to do is feed in a
	 * properties file and then process this properties file accordingly...the
	 * properties file must just be placed into the directory All we do is
	 * regenerate the main landing page and a generic home page for the new
	 * archive
	 */

	public static void main(String[] args) {
		// This utility will read in properties from the user from the command
		// line and will generate the file accordingly
		Scanner input = new Scanner(System.in);
		HashMap<String, String> details = new HashMap<String, String>();
		System.out.print(
				"Welcome to the Archive Configuration Utility.\n  This will assist in the addition of your new archive.  \n Please may you enter the required data when prompted.");
		System.out.println("Name of Archive");
		details.put("archive.name", input.nextLine());
		System.out.println("File path for the image to appear on the home page of Personal Histories");
		details.put("archive.landingpage.image", input.nextLine());
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
