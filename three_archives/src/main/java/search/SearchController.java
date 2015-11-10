package search;

import history.History;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

/**
 * The {@link SearchController} is responsible for the searching activity and
 * for delegating to the {@link Search} service
 * 
 * @author mthnox003
 *
 */
public class SearchController implements Controller {

	private static String archive;
	private static final Search SEARCH = new Search();

	public static Search getSearch() {
		return SEARCH;
	}

	/**
	 * The execute method is present in each controller and allows for the
	 * execution of the request from the client
	 * 
	 * @param request
	 *            {@link HttpServletRequest} instance representing the request
	 *            from the client
	 * @param response
	 *            {@link HttpServletResponse} instance representing the response
	 *            for the client
	 * @return {@link String} instance representing the next page to be
	 *         navigated to in the application. This returns the .jsp file path
	 *         to dispatch to
	 * @throws Exception
	 */

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";
		archive = (String) request.getSession().getAttribute("ARCHIVE_CONCAT");
		request.setAttribute("searchCategories", retrieveSearchCategories());
		request.setAttribute("browseCategories", new BrowseController().getBrowse().getBrowsingCategories());
		String requestPath = request.getPathInfo().substring(1);
		if (requestPath != null) {
			try {
				if (requestPath.contains("search_objects")) {
					searchFedoraDigitalObjects(request);
				}
			} catch (SolrServerException exception) {
				request.setAttribute("message", "An error occured.  Please contact IT");
				System.out.println("Something seems to have gone wrong with SOLR" + exception.getStackTrace());
			} catch (FedoraException exception) {
				System.out.println("Something seems to have gone wrong with Fedora" + exception.getStackTrace());

			} catch (Exception exception) {

				request.setAttribute("message", "An error occurred.  Please contact IT");
				System.out.println("Something seems to have gone wrong when trying to conduct a search");
				exception.printStackTrace();

			}
		}
		request.getSession().setAttribute("browseCategory", null);
		request.getSession().setAttribute("categoryValue", null);
		similarSearchTags(request);
		return result;

	}

	/**
	 * This method searches for digital objects dependent on what has been
	 * specified within the request. The search may be restricted to a search
	 * that has already been performed or it could be a completely new search to
	 * be conducted from scratch by querying the Solr search engine. The search
	 * could also be restricted to a particular category dependent on input from
	 * the request
	 * 
	 * @param request
	 *            {@link HttpServletRequest} instance representing the client
	 *            request
	 * @throws Exception
	 */
	private void searchFedoraDigitalObjects(HttpServletRequest request) throws Exception {
		String requestPath = request.getPathInfo().substring(1);
		System.out.println("SEARCHING FEDORA DIGITAL OBJECTS " + requestPath);

		StringBuilder terms = new StringBuilder("");
		String limit = (String) request.getParameter("limitSearch");
		System.out.println("LIMIT SEARCH VALUE " + limit);

		if (limit != null && !limit.isEmpty() && Boolean.parseBoolean(limit)) {
			System.out.println("WE ARE IN LIMIT SEARCH");
			Set<FedoraDigitalObject> results = new HashSet<FedoraDigitalObject>();
			Set<FedoraDigitalObject> exisitingObjects = (Set<FedoraDigitalObject>) request.getSession()
					.getAttribute("objectsForArchive");
			terms.append("(");
			for (FedoraDigitalObject digitalObject : exisitingObjects) {
				terms.append("PID:\"").append(digitalObject.getPid()).append("\" OR ");
			}

			terms.delete(terms.length() - 4, terms.length()).append(")");
			terms.append(" AND ");
		}

		String s = request.getParameter("terms");
		request.getSession().setAttribute("terms", s);
		String[] splitPath = requestPath.split("=");
		System.out.println("SIZE OF OUR SPLIT PATH " + splitPath.length);
		if (splitPath.length == 2) {
			SearchAndBrowseCategory queryCategory = SearchAndBrowseCategory.valueOf(splitPath[1]);
			// maybe here we just return to the page with re-organised/placed
			// search items..etc with our one selected and then they can still
			// put a value into the text box
			ArrayList<String> categories = SearchController.retrieveSearchCategories();
			if (categories.indexOf(queryCategory.name()) > -1) {
				categories.remove(categories.indexOf(queryCategory.name()));
				categories.add(0, queryCategory.name());
			}
			request.setAttribute("searchCategories", categories);
			if (s == null || s.isEmpty()) {
				return;
			}
			if (!(queryCategory.equals(SearchAndBrowseCategory.SEARCH_ALL))) {
				terms.append(queryCategory.getDublinCoreField()).append(":\"").append(s).append("\"");
				// for (int x = 0; x <
				// queryCategory.getDublinCoreField().size(); x++) {
				// if (x > 0 && x != queryCategory.getDublinCoreField().size() -
				// 1) {
				// terms.append(queryCategory.getDublinCoreField().get(x)).append(":\"").append(s).append("\"")
				// .append(" OR ");
				//
				// } else {
				//
				// terms.append(queryCategory.getDublinCoreField().get(x)).append(":\"").append(s).append("\"");
				// }
				// }
				// took this out since it is no longer an array list in that
				// type
			} else {
				terms.append(s);
			}
		}

		System.out.println("SEARCH STRING " + terms);
		Set<FedoraDigitalObject> digitalObjects = new HashSet<FedoraDigitalObject>();
		digitalObjects = getSearch().findFedoraDigitalObjects(new String(terms));
		filterFedoraObjectsForSpecificArchive((String) request.getSession().getAttribute("MEDIA_PREFIX"),
				digitalObjects);
		// need to restrict these for this archive....

		if (!(digitalObjects == null || digitalObjects.isEmpty())) {
			History.addTextToTagCloud(s, true);
		}
		request.getSession().setAttribute("objectsForArchive", digitalObjects);
		similarSearchTags(request);
	}

	/**
	 * This results in the filtering of the fedora digital objects matching the
	 * specific search terms to ensure that only objects for the specific
	 * archive are returned
	 * 
	 * @param multiMediaPrefix
	 *            {@link String} representation of the prefix for the PID of the
	 *            specific archive
	 * @param fedoraDigitalObjects
	 *            {@link Set} representing the fedora digital objects requiring
	 *            filtering
	 */
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

	/**
	 * Gets the search categories to be displayed on the interface. This is
	 * selected {@link SearchAndBrowseCategory} items
	 * 
	 * @return {@link ArrayList} representing the searching categories
	 */
	public static ArrayList<String> retrieveSearchCategories() {
		ArrayList<String> result = new ArrayList<String>();
		for (SearchAndBrowseCategory cat : SearchAndBrowseCategory.values()) {
			result.add(cat.name());
		}
		// remove unwanted categories
		result.remove(SearchAndBrowseCategory.FORMAT.name());
		result.remove(SearchAndBrowseCategory.TYPE.name());
		result.remove(SearchAndBrowseCategory.SOURCE.name());
		result.remove(SearchAndBrowseCategory.CONTRIBUTOR.name());
		result.remove(SearchAndBrowseCategory.SUBJECT.name());
		result.remove(SearchAndBrowseCategory.YEAR.name());
		result.remove(SearchAndBrowseCategory.LOCATION.name());
		return result;
	}

	/**
	 * This method retrieves a list of search tags matching the search terms
	 * entered. This is done by consulting the autcomplete file and selecting
	 * values from there that contain or the search term
	 * 
	 * @param request
	 *            {@link HttpServletRequest} instance representing the client
	 *            request
	 * @throws Exception
	 */
	private void similarSearchTags(HttpServletRequest request) throws Exception { // this
		TreeSet<String> results = new TreeSet<String>();
		String terms = (String) request.getParameter("terms");
		String[] splitSearchTerm = null;
		if (terms == null) {
			return;
		}
		splitSearchTerm = terms.split(" ");
		JSONParser parser = new JSONParser();
		StringBuilder file = new StringBuilder("../webapps/data/");

		file.append(archive).append(".json");

		Set<String> autocomplete;
		try {
			File dir = new File("../webapps/data/");
			if (!dir.exists()) {
				dir.mkdir();
			}
			File actualFile = new File(file.toString());
			if (!actualFile.exists()) {
				FileWriter newFile = new FileWriter(file.toString());
				newFile.write("[]");
				newFile.flush();
				newFile.close();
			}

			Object obj = parser.parse(new FileReader(new String(file)));
			JSONArray array = (JSONArray) obj;
			autocomplete = new TreeSet<String>(array);
		} catch (

		ParseException parseException)

		{
			System.out.println(parseException);
			throw new Exception(parseException);
		}
		for (

		String auto : autocomplete) {
			for (String term : splitSearchTerm) {
				if (auto.toLowerCase().contains(term.toLowerCase())) {
					results.add(auto.toUpperCase());
					break;
				}
			}
		}
		request.getSession().setAttribute("searchTags", results);
	}
}
