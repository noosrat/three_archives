package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import history.HistoryController;
import search.SearchController;

public class GeneralController implements Controller {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		request.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		HistoryController historyController = new HistoryController();
		historyController.execute(request, response);
		String pathInfo =  request.getPathInfo().substring(1);
		if (pathInfo.equalsIgnoreCase("SequinsSelfStruggle")){
			session.setAttribute("ARCHIVE", "Sequins, Self and Struggle");
			return "sequinsSelfStruggleHome.jsp";

		} else if (pathInfo.equalsIgnoreCase("MovieSnaps")) {
			session.setAttribute("ARCHIVE", "Movie Snaps");
			return "movieSnapsHome.jsp";

		} else if (pathInfo.equalsIgnoreCase("HarfieldVillage")) {
			session.setAttribute("ARCHIVE", "Harfield Village");
			return "harfieldVillageHome.jsp";

		}

		return "index.jsp";
	}

}