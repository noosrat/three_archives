package configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;

public class IndexPage {

	private static HashMap<String, PropertiesHandler> ARCHIVES;
	private static String PATH;


	public static void generateIndexPage(String path) throws Exception{
		PATH = path;
		ARCHIVES = new HashMap<String, PropertiesHandler>();
		IndexPage lp = new IndexPage();
		lp.locateAndAssignProperties();
		lp.generateNewLandingPage();
		
	}
	private File getConfigurationDirectory() {
		ClassLoader classLoader = IndexPage.class.getClassLoader();
		File file = new File(classLoader.getResource("configuration").getFile());
		return file;
	}

	private void locateAndAssignProperties() {
		File directory = getConfigurationDirectory();
		if (directory != null) {
			for (File file : directory.listFiles()) {
				if (file.getName().contains(".properties") && !file.getName().contains("general")) {
					ARCHIVES.put(file.getName(), new PropertiesHandler("configuration/" + file.getName()));
				}
			}
		}

	}

	private void generateNewLandingPage() throws Exception {
		System.out.println("GENERATING NEW LD");
		PrintWriter writer = null;
		BufferedReader br = null;
		try {
			File directory = getConfigurationDirectory();
			String fileN = "";
			if (directory != null) {
				for (File file : directory.listFiles()) {
					if (file.getName().equals("skeletonLandingPage.jsp")) {
						fileN = "../webapps/"+PATH+"/tempIndex.jsp";
						System.out.println("New file to be written to " + fileN);
						FileReader reader = new FileReader(file);
						br = new BufferedReader(reader);
						writer = new PrintWriter(new BufferedWriter(new FileWriter(fileN)));
						System.out.println("WE FUND THE FILE " + (writer==null));

						String line;
						while ((line = br.readLine()) != null) {
							System.out.println("THIS IS THE LINE BEING READ " + line);
							//first part
							if (line.contains("carousel-indicators")) {
								System.out.println(
							//we now need to iterate through our properties files/archivess and replace the necessary values
								
										"***************************************************WE found our line to replace");
								line = line +  "<li data-target=\"#myCarousel\" data-slide-to=\"0\" class=\"active\"></li>"; //this is just for the first one..now for the other ones..0 to length minus on we add the same line
								for (int x=1; x<ARCHIVES.size();x++){
									line += "<li data-target=\"#myCarousel\" data-slide-to=\"" +x+ "\"></li>";
								}
							}
							
							//second part
							if (line.contains("carousel-inner")){
								System.out.println("WE FOUND THE SECOND SECTION TO GENERATE");//first one of carousel is different to rest
								boolean first = true;
								for (String archive: ARCHIVES.keySet()){
									if (first){
									
										line +=  "<div class=\"item active\"><div class=\"fill\" style=\"background-image:url(${pageContext.request.contextPath}"+ ARCHIVES.get(archive).getProperty("archive.landingpage.image") +");\"></div>";
										line += "<div class=\"carousel-caption\">";
										line += "<h2>" + ARCHIVES.get(archive).getProperty("archive.name") + "</h2>";
										String newName = ARCHIVES.get(archive).getProperty("archive.name").replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "");
										line+= "<p><a class=\"btn btn-large btn-primary\" href=\"${pageContext.request.contextPath}/archives/" + newName + "\">Explore Archive</a></p></div></div>";
										first = false;
									}
									else{
										//this is for all the other entries of archives
										line += "<div class=\"item\">";
										line +=  "<div class=\"fill\" style=\"background-image:url(${pageContext.request.contextPath}"+ ARCHIVES.get(archive).getProperty("archive.landingpage.image") +");\"></div>";
										line += "<div class=\"carousel-caption\">";
										line += "<h2>" + ARCHIVES.get(archive).getProperty("archive.name") + "</h2>";
										String newName = ARCHIVES.get(archive).getProperty("archive.name").replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "");
										line+= "<p><a class=\"btn btn-large btn-primary\" href=\"${pageContext.request.contextPath}/archives/" + newName + "\">Explore Archive</a></p></div></div>";
									}
								}
								
							}
							writer.write(line);
							writer.println();
						}

					}
				}
			}

		} catch (Exception ex) {
			System.out.println("Something went wrong while trying to generate the index.jsp file");
			System.out.println(ex);
			throw ex;
		} finally {
			writer.close();
			br.close();
		}
		
		File realName = new File("../webapps"+ PATH+"/index.jsp");
		realName.delete(); // remove the old file
		new File("../webapps"+ PATH+"/tempIndex.jsp").renameTo(realName); // Rename temp file
		
	}

}
