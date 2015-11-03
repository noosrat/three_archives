package configuration;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

public class PageGenerationTest {

	@Test
	public void testLocateAndAssignProperties() {
		// we need to check the value of ARCHIVES after calling the method
		PageGeneration.locateAndAssignProperties();
		HashMap<String, PropertiesHandler> testArchives = new HashMap<String, PropertiesHandler>();
		File directory = new File("src/main/resources/configuration/");
		if (directory != null) {
			for (File file : directory.listFiles()) {
				if (file.getName().contains(".properties")
						&& !file.getName().contains("general")) {
					System.out.println("Processing properties of "
							+ file.getName());
					testArchives.put(file.getName(),
							new PropertiesHandler(file.getAbsolutePath()));
				}
			}
		}

		assertEquals(PageGeneration.getArchives().size(), testArchives.size());
		assertEquals(PageGeneration.getArchives().toString(),
				testArchives.toString());
	}

	@Test
	public void testGenerateIndexPageCarouselIndicators() {
		// here we need to check whether our archive values have been filled in
		// let's use the archives that are already there
		HashMap<String, PropertiesHandler> archives = PageGeneration
				.getArchives();
		// find the index page that was generated and read it in and find the
		// values...
		try {
			PageGeneration.locateAndAssignProperties();
			PageGeneration.generateIndexPage();
		} catch (Exception ex) {
			System.out.println(ex);
			throw new AssertionError("Error while generating index page");
		}

		File result = new File("src/main/webapp/index.jsp");
		// check contents of result
		String expectedLine = "<ol class=\"carousel-indicators\">";
		expectedLine += "<li data-target=\"#myCarousel\" data-slide-to=\"0\" class=\"active\"></li>";

		for (int x = 1; x < archives.size(); x++) {
			expectedLine += "<li data-target=\"#myCarousel\" data-slide-to=\""
					+ x + "\"></li>";
		}

		BufferedReader br = null;
		String actualLine;
		try {
			FileReader reader = new FileReader(result);

			br = new BufferedReader(reader);
			while ((actualLine = br.readLine()) != null) {
				if (actualLine.contains("carousel-indicators")) {
					break;
				}

			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw new AssertionError(
					"an exception occurred while reading the file");
		} finally {

			try {
				br.close();
			} catch (Exception ex) {
				throw new AssertionError(
						"An exception occurred while trying to close the reader");
			}
		}

		assertEquals(expectedLine.trim(), actualLine.trim());

	}

	@Test
	public void testGenerateIndexPageCarouselInner() {
		// here we need to check whether our archive values have been filled in
		// let's use the archives that are already there
		HashMap<String, PropertiesHandler> archives = PageGeneration
				.getArchives();
		// find the index page that was generated and read it in and find the
		// values...
		try {
			PageGeneration.locateAndAssignProperties();
			PageGeneration.generateIndexPage();
		} catch (Exception ex) {
			System.out.println(ex);
			throw new AssertionError("Error while generating index page");
		}

		File result = new File("src/main/webapp/index.jsp");
		// check contents of result
		String expectedLine = "<div class=\"carousel-inner\">";

		BufferedReader br = null;
		String actualLine;
		try {
			FileReader reader = new FileReader(result);

			br = new BufferedReader(reader);
			while ((actualLine = br.readLine()) != null) {
				if (actualLine.contains("carousel-inner")) {
					break;
				}

			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw new AssertionError(
					"an exception occurred while reading the file");
		} finally {

			try {
				br.close();
			} catch (Exception ex) {
				throw new AssertionError(
						"An exception occurred while trying to close the reader");
			}
		}

		boolean first = true;
		for (String archive : archives.keySet()) {
			if (first) {

				expectedLine += "<div class=\"item active\"><div class=\"fill\" style=\"background-image:url(${pageContext.request.contextPath}"
						+ archives.get(archive).getProperty(
								"archive.landingpage.image") + ");\"></div>";
				expectedLine += "<div class=\"carousel-caption\">";
				expectedLine += "<h2>"
						+ archives.get(archive).getProperty("archive.name")
						+ "</h2>";
				System.out.println("ARCHIVE: "
						+ archives.get(archive).getProperty("archive.name"));
				String newName = archives.get(archive)
						.getProperty("archive.name")
						.replaceAll("[^a-zA-Z0-9\\s]", "")
						.replaceAll("\\s+", "");
				expectedLine += "<p><a class=\"btn btn-large btn-primary\" href=\"${pageContext.request.contextPath}/archives/"
						+ newName + "\">Explore Archive</a></p></div></div>";
				first = false;
			} else {
				// this is for all the other entries of
				// archives
				expectedLine += "<div class=\"item\">";
				expectedLine += "<div class=\"fill\" style=\"background-image:url(${pageContext.request.contextPath}"
						+ archives.get(archive).getProperty(
								"archive.landingpage.image") + ");\"></div>";
				expectedLine += "<div class=\"carousel-caption\">";
				expectedLine += "<h2>"
						+ archives.get(archive).getProperty("archive.name")
						+ "</h2>";
				String newName = archives.get(archive)
						.getProperty("archive.name")
						.replaceAll("[^a-zA-Z0-9\\s]", "")
						.replaceAll("\\s+", "");
				expectedLine += "<p><a class=\"btn btn-large btn-primary\" href=\"${pageContext.request.contextPath}/archives/"
						+ newName + "\">Explore Archive</a></p></div></div>";
			}
		}

		assertEquals(expectedLine.trim(), actualLine.trim());

	}

	@Test
	public void testGenerateEmptyArchiveHomePage() {
		try {
			PageGeneration.generateEmptyArchiveHomePage("myTestArchive");
		} catch (Exception ex) {
			throw new AssertionError(
					"An exception occurred while generating the page");

		}
		//just check that the file exists
		File f = new File("src/main/webapp/myTestArchiveHome.jsp");
		assertTrue(f.exists());
		f.delete();
	}
}
