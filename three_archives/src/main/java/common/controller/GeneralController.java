package common.controller;


import java.util.ArrayList;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.model.Exhibition;
import exhibitions.ManageExhibition;
import history.HistoryController;
import search.SearchController;

public class GeneralController implements Controller {

/*
 * this is where we should actually set all the session variables that are specific to the archives.........
 */
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		String result = "";
		ArrayList<String> cart = new ArrayList<String>();
		cart.add("ms:1");
		
		HttpSession session = request.getSession();
		clearArchiveSessionInformation(session);
		session.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		HistoryController historyController = new HistoryController();
		String pathInfo =  request.getPathInfo().substring(1);
		System.out.println(pathInfo);
		if (pathInfo.equalsIgnoreCase("SequinsSelfAndStruggle")){
			session.setAttribute("ARCHIVE", "Sequins, Self and Struggle");
			session.setAttribute("mediaPrefix", "sq");
			session.setAttribute("MEDIA_CART", cart);
			historyController.execute(request, response);
			return "sequinsSelfAndStruggleHome.jsp";

		} else if (pathInfo.equalsIgnoreCase("MovieSnaps")) {
			session.setAttribute("ARCHIVE", "Movie Snaps");
			session.setAttribute("mediaPrefix", "ms");
			session.setAttribute("MEDIA_CART", cart);
			historyController.execute(request, response);
			return "movieSnapsHome.jsp";

		} else if (pathInfo.equalsIgnoreCase("HarfieldVillage")) {
			session.setAttribute("ARCHIVE", "Harfield Village");
			session.setAttribute("mediaPrefix", "hv");
			session.setAttribute("MEDIA_CART", cart);
			historyController.execute(request, response);
			return "harfieldVillageHome.jsp";

		}

		return "index.jsp";
	}

	//the following all differ per archive
	private void clearArchiveSessionInformation(HttpSession session){
		System.out.println("Clearing session information");
		session.setAttribute("browseCategoryCookie", null);
		session.setAttribute("objectsModifiedSinceLastVisit", null);
		session.setAttribute("categoriesWithRecentUpdates", null);
		session.setAttribute("userFavouriteCategoriesWithRecentUpdates", null);
		session.setAttribute("tagCloud", null);
		session.setAttribute("objectsForArchive",null);
		session.setAttribute("mediaPrefix", null);
		System.out.println("Session information after clearing out");
		
		System.out.println("Category cookie: " + session.getAttribute("browseCategoryCookie") + " TagCloud " + session.getAttribute("tagCloud"));
		
		
		
		

	}

}