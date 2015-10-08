package configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;

public class PageGeneration {

	private static HashMap<String, PropertiesHandler> ARCHIVES=new HashMap<String, PropertiesHandler>();

	public static void locateAndAssignProperties() {
		File directory = new File("src/main/resources/configuration/");
		if (directory != null) {
			for (File file : directory.listFiles()) {
				if (file.getName().contains(".properties") && !file.getName().contains("general")) {
					System.out.println("Processing properties of " + file.getName());
					ARCHIVES.put(file.getName(), new PropertiesHandler(file.getAbsolutePath()));
					System.out.println("Finished processing properties of " + file.getName());
				}
			}
			System.out.println("ARCHIVES SIZE " + ARCHIVES.size());
		}

	}

	public static void generateIndexPage() throws Exception {
		System.out.println("GENERATING NEW LANDING PAGE");
		PrintWriter writer = null;
		BufferedReader br = null;
		try {
			File directory = new File("src/main/resources/configuration");
			System.out.println("DIRECTORY IS " + directory);
			String fileN = "";
			if (directory != null) {
				for (File file : directory.listFiles()) {
					if (file.getName().equals("skeletonLandingPage.jsp")) {
						fileN = "src/main/webapp/tempIndex.jsp";
						System.out.println("New file to be written to " + fileN);
						FileReader reader = new FileReader(file);
						br = new BufferedReader(reader);
						writer = new PrintWriter(new BufferedWriter(new FileWriter(fileN)));
						System.out.println("WE FUND THE FILE " + (writer == null));

						String line;
						while ((line = br.readLine()) != null) {
							if (line.contains("carousel-indicators")) {
								line = line
										+ "<li data-target=\"#myCarousel\" data-slide-to=\"0\" class=\"active\"></li>"; 
								for (int x = 1; x < ARCHIVES.size(); x++) {
									line += "<li data-target=\"#myCarousel\" data-slide-to=\"" + x + "\"></li>";
								}
							}
							if (line.contains("carousel-inner")) {
								System.out.println("WE FOUND THE SECOND SECTION TO GENERATE");
								boolean first = true;
								for (String archive : ARCHIVES.keySet()) {
									if (first) {

										line += "<div class=\"item active\"><div class=\"fill\" style=\"background-image:url(${pageContext.request.contextPath}"
												+ ARCHIVES.get(archive).getProperty("archive.landingpage.image")
												+ ");\"></div>";
										line += "<div class=\"carousel-caption\">";
										line += "<h2>" + ARCHIVES.get(archive).getProperty("archive.name") + "</h2>";
										System.out.println("ARCHIVE: " + ARCHIVES.get(archive).getProperty("archive.name"));
										String newName = ARCHIVES.get(archive).getProperty("archive.name").replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "");
										line += "<p><a class=\"btn btn-large btn-primary\" href=\"${pageContext.request.contextPath}/archives/"
												+ newName + "\">Explore Archive</a></p></div></div>";
										first = false;
									} else {
										// this is for all the other entries of
										// archives
										line += "<div class=\"item\">";
										line += "<div class=\"fill\" style=\"background-image:url(${pageContext.request.contextPath}"
												+ ARCHIVES.get(archive).getProperty("archive.landingpage.image")
												+ ");\"></div>";
										line += "<div class=\"carousel-caption\">";
										line += "<h2>" + ARCHIVES.get(archive).getProperty("archive.name") + "</h2>";
										String newName = ARCHIVES.get(archive).getProperty("archive.name")
												.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "");
										line += "<p><a class=\"btn btn-large btn-primary\" href=\"${pageContext.request.contextPath}/archives/"
												+ newName + "\">Explore Archive</a></p></div></div>";
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

		File realName = new File("src/main/webapp/index.jsp");
		realName.delete(); // remove the old file
		new File("src/main/webapp/tempIndex.jsp").renameTo(realName); 
	}

	// this is the archive property file name
	public static void generateEmptyArchiveHomePage(String archive) throws Exception {
		// just need to copy the template as it is
		String file = "src/main/webapp/"+archive.substring(0,1).toLowerCase() + archive.substring(1) + "Home.jsp";
		System.out.println("GENERATING NEW ARCHIVE HOME PAGE");
		try {
			File directory = new File("src/main/resources/configuration");
			if (directory != null) {
				for (File source : directory.listFiles()) {
					if (source.getName().equals("skeletonHome.jsp")) {
						File destination = new File(file);
						System.out.println("New file to be written to " + destination.getAbsolutePath());
						Files.copy(source.toPath(), destination.toPath());
						System.out.println("File successfully copied");
						break;
					}

				}
			}

		} catch (Exception ex) {
			System.out.println("Something went wrong while trying to generate the index.jsp file");
			System.out.println(ex);
			throw ex;
		}
	}

}