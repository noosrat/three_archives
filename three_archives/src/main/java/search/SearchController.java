package search;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.noggit.JSONParser.ParseException;

import common.controller.Controller;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
import history.History;

public class SearchController implements Controller {

	private static String archive;
	private static Search search = new Search();

	public static Search getSearch() {
		return search;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";
		archive = (String) request.getSession().getAttribute("ARCHIVE_CONCAT");
		request.setAttribute("searchCategories", retrieveSearchCategories());
		request.setAttribute("browseCategories", Browse.getBrowsingCategories());
		String requestPath = request.getPathInfo().substring(1);
		if (requestPath != null) {
			try {
				if (requestPath.contains("search_objects")) {
					searchFedoraDigitalObjects(request, response);
				}
			} catch (SolrServerException exception) {
				request.setAttribute("message",
						"Something seems to have gone wrong with SOLR" + exception.getStackTrace());
			} catch (FedoraException exception) {
				request.setAttribute("message",
						"Something seems to have gone wrong with Fedora" + exception.getStackTrace());

			} catch (Exception exception) {
				request.setAttribute("message", exception.getStackTrace());
			}
		}
		similarSearchTags(request);
		return result;

	}

	private void searchFedoraDigitalObjects(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String requestPath = request.getPathInfo().substring(1);
		System.out.println("SEARCHING FEDORA DIGITAL OBJECTS " + requestPath);

		StringBuilder terms = new StringBuilder("");

		String limit = request.getParameter("limitSearch");
		if (limit != null && !limit.isEmpty() && limit.equalsIgnoreCase("limitSearch")) {
			Set<FedoraDigitalObject> results = new HashSet<FedoraDigitalObject>();
			// we essentially just need to filter the result we had with the new
			// search term?
			// what we need to do here is construct a dolr search with all the
			// existing items and the new condition..
			Set<FedoraDigitalObject> exisitingObjects = (Set<FedoraDigitalObject>) request.getSession()
					.getAttribute("objectsForArchive");
			// we need to build the query for fedora
			terms.append("(");
			for (FedoraDigitalObject digitalObject : exisitingObjects) {
				terms.append("PID:\"").append(digitalObject.getPid()).append("\" OR ");
			}
			terms.delete(terms.length() - 4, terms.length()).append(")");
			// now we can add the rest of the actual query
			terms.append(" AND ");
		}

		String s = request.getParameter("terms");
		System.out.println("VALUE OF TERMS " + s);
		// we want to add this to our word cloud..this is what has been typed
		// into the search box
		History.addTextToTagCloud(s, true);
		// if (requestPath.contains("search_objects/category")) {
		// then do all the specific searching
		String[] splitPath = requestPath.split("=");
		if (splitPath.length == 2) {
			SearchAndBrowseCategory queryCategory = SearchAndBrowseCategory.valueOf(splitPath[1]);
			// maybe here we just return to the page with re-organised/placed
			// search items..etc with our one selected and then they can still
			// put a value into the text box
			ArrayList<String> categories = SearchController.retrieveSearchCategories();
			categories.remove(categories.indexOf(queryCategory.name()));
			categories.add(0, queryCategory.name());
			request.setAttribute("searchCategories", categories);
			if (s == null || s.isEmpty()) {
				return;
			}
			if (!(queryCategory.equals(SearchAndBrowseCategory.SEARCH_ALL))) {
				for (int x = 0; x < queryCategory.getDublinCoreField().size(); x++) {
					if (x > 0 && x != queryCategory.getDublinCoreField().size() - 1) {
						terms.append(queryCategory.getDublinCoreField().get(x)).append(":\"").append(s).append("\"")
								.append(" OR ");

					} else {

						terms.append(queryCategory.getDublinCoreField().get(x)).append(":\"").append(s).append("\"");
					}
				}
			} else {
				terms.append(s);
			}
		}

		Set<FedoraDigitalObject> digitalObjects = new HashSet<FedoraDigitalObject>();
		digitalObjects = getSearch().findFedoraDigitalObjects(new String(terms));
		filterFedoraObjectsForSpecificArchive((String) request.getSession().getAttribute("MEDIA_PREFIX"),
				digitalObjects);
		// need to restrict these for this archive....

		if ((digitalObjects == null || digitalObjects.isEmpty())) {
			request.setAttribute("message", "No results to return");
		}
		request.getSession().setAttribute("objectsForArchive", digitalObjects);
		similarSearchTags(request);
	}

	private void filterFedoraObjectsForSpecificArchive(String multiMediaPrefix,
			Set<FedoraDigitalObject> fedoraDigitalObjects) {
		for (Iterator<FedoraDigitalObject> iterator = fedoraDigitalObjects.iterator(); iterator.hasNext();) {
			FedoraDigitalObject element = iterator.next();
			if (!(element.getPid().contains(multiMediaPrefix))) {
				iterator.remove(); // remove any object who does not have the
									// prefix for this archive
				System.out.println("removing object with pid " + element.getPid());
			}
		}

	}

	public static ArrayList<String> retrieveSearchCategories() {
		ArrayList<String> result = new ArrayList<String>();
		for (SearchAndBrowseCategory cat : SearchAndBrowseCategory.values()) {
			result.add(cat.name());
		}
		// remove unwanted categories
		result.remove(SearchAndBrowseCategory.FORMAT.name());
		result.remove(SearchAndBrowseCategory.SUBJECT.name());
		result.remove(SearchAndBrowseCategory.YEAR.name());
		return result;
	}

	private void similarSearchTags(HttpServletRequest request) throws Exception { // this
		TreeSet<String> results = new TreeSet<String>();
		String terms = (String) request.getParameter("terms");
		String[] splitSearchTerm = null;
		if (terms == null) {
			return;
		}
		splitSearchTerm = terms.split(" ");
		// now we read in the autocomplete json file
		JSONParser parser = new JSONParser();
		StringBuilder file = new StringBuilder("../webapps/data/");

		file.append(archive).append(".json");

		// read in the archive attribute and check which json file we are
		// reading in
		Set<String> autocomplete;
		try {
			File dir = new File("../webapps/data/");
			if (!dir.exists()) {
				System.out.println("OH NO THE DIRECTORY DOES NOT EXIST....WE MUST CREATE IT");
				dir.mkdir();
			}

			Object obj = parser.parse(new FileReader(new String(file)));
			JSONArray array = (JSONArray) obj;
			autocomplete = new TreeSet<String>(array);
		} catch (ParseException parseException) {
			System.out.println(parseException);
			throw new Exception(parseException);
		}

		// now we go trough out array and we select items to have as tags

		for (String auto : autocomplete) {
			for (String term : splitSearchTerm) {
				if (auto.toLowerCase().contains(term.toLowerCase())) {
					results.add(auto);
					break;
				}
			}
		}
		request.setAttribute("searchTags", results);
	}
}
