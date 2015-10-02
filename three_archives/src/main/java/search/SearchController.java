package search;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.noggit.JSONParser.ParseException;

import common.controller.Controller;
import common.fedora.DatastreamID;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
import history.History;

public class SearchController implements Controller {

	private static String archive;
	private static Search search = new Search();

	public static Search getSearch() {
		return search;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String result = "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";
		archive = ((String) request.getSession().getAttribute("ARCHIVE")).replaceAll("[^a-zA-Z0-9\\s]", "")
				.replaceAll("\\s+", "");
		request.setAttribute("searchCategories", retrieveSearchCategories());
		request.setAttribute("browseCategories", Browse.getBrowsingCategories());
		request.setAttribute("autocompletion", autocompleteValues(Browse.getFedoraDigitalObjects()));

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
					.getAttribute("objects");
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
		//we want to add this to our word cloud..this is what has been typed into the search box
		History.addTextToTagCloud(s);
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
		if ((digitalObjects == null || digitalObjects.isEmpty())) {
			request.setAttribute("message", "No results to return");
		}
		request.getSession().setAttribute("objects", digitalObjects);
		similarSearchTags(request);
	}

	public static ArrayList<String> retrieveSearchCategories() {
		ArrayList<String> result = new ArrayList<String>();
		for (SearchAndBrowseCategory cat : SearchAndBrowseCategory.values()) {
			result.add(cat.name());
		}
		return result;
	}

	private static TreeSet<String> autocompleteValues(Set<FedoraDigitalObject> fedoraDigitalObjects) {
		TreeSet<String> values = new TreeSet<String>();

		for (FedoraDigitalObject fedoraDigitalObject : fedoraDigitalObjects) {
			DublinCoreDatastream dublinCoreDatastream = (DublinCoreDatastream) fedoraDigitalObject.getDatastreams()
					.get(DatastreamID.DC.name());

			for (String dublinCoreFieldValue : dublinCoreDatastream.getDublinCoreMetadata().values()) {
				String[] splitPercentage = dublinCoreFieldValue.split("%");
				values.addAll(Arrays.asList(splitPercentage));
				for (String string : splitPercentage) {
					// now we split by spaces to get the individual tokents
					String[] spaceSplit = string.split(" ");
					values.addAll(Arrays.asList(spaceSplit));
				}
			}
		}

		return values;

	}

	/*
	 * the below needs to moved to occur whenever there is an upload to the
	 * database...the data will then be retrieved from the db from the dofields
	 * table..
	 */
	public static void buildAutocompleteJSONFile(Set<FedoraDigitalObject> fedoraDigitalObjects) {
		JSONArray list = new JSONArray();
		Set<String> values = autocompleteValues(fedoraDigitalObjects);
		for (String s : values) {
			list.add(s);
		}
		try {

			String fileName = "../webapps/data/" + archive + ".json";

			FileWriter file = new FileWriter(fileName);
			file.write(list.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private Set<String> similarSearchTags(HttpServletRequest request) throws Exception { // this
		// be
		// read from the
		// relevant JSON
		// file and
		// taken into a
		// set and then
		// the matching
		// tags are
		// displayed
		// only pic top results that actually contain the search term
		TreeSet<String> results = new TreeSet<String>();
		String terms = (String) request.getParameter("terms");
		String[] splitSearchTerm = null;
		if (terms != null) {
			splitSearchTerm = terms.split(" ");
		}
		// now we read in the autocomplete json file
		JSONParser parser = new JSONParser();
		StringBuilder file = new StringBuilder("../webapps/data/");

		file.append(archive).append(".json");

		// read in the archive attribute and check which json file we are
		// reading in
		Set<String> autocomplete;
		try {
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
				if (auto.contains(term)) {
					results.add(auto);
					break; // breaking out since the whole auto line will be
							// addedd so we don't need to check the other terms
							// existience
				}
			}
		}
		request.setAttribute("searchTags", results);
		return results;
	}

}
