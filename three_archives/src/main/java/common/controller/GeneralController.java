package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import search.SearchController;

public class GeneralController implements Controller {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		request.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		if (request.getPathInfo().substring(1).contains("redirect_sequins")) {
			session.setAttribute("ARCHIVE", "Sequins, Self and Struggle");

			return "sequinsSelfStruggleHome.jsp";

		} else if (request.getPathInfo().substring(1).contains("redirect_snaps")) {
			session.setAttribute("ARCHIVE", "Movie Snaps");
			return "movieSnapsHome.jsp";

		} else if (request.getPathInfo().substring(1).contains("redirect_harfield")) {
			session.setAttribute("ARCHIVE", "Harfield Village");
			return "harfieldVillageHome.jsp";

		}

		return "index.jsp";
	}

}