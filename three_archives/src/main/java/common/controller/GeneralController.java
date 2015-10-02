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
		String pathInfo =  request.getPathInfo().substring(1);
		System.out.println(pathInfo);
		if (pathInfo.equalsIgnoreCase("SequinsSelfAndStruggle")){
			session.setAttribute("ARCHIVE", "Sequins, Self and Struggle");
			historyController.execute(request, response);
			return "sequinsSelfAndStruggleHome.jsp";

		} else if (pathInfo.equalsIgnoreCase("MovieSnaps")) {
			session.setAttribute("ARCHIVE", "Movie Snaps");
			historyController.execute(request, response);
			return "movieSnapsHome.jsp";

		} else if (pathInfo.equalsIgnoreCase("HarfieldVillage")) {
			session.setAttribute("ARCHIVE", "Harfield Village");
			historyController.execute(request, response);
			return "harfieldVillageHome.jsp";

		}

		return "index.jsp";
	}

}