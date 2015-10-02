package common.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import search.SearchController;

public class GeneralController implements Controller {

	
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		String result = "";
		ArrayList<String> cart = new ArrayList<String>();
		cart.add("ms:1");
		
		HttpSession session = request.getSession();
		request.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		if (request.getPathInfo().substring(1).contains("redirect_sequins")) {
			session.setAttribute("ARCHIVE", "Sequins, Self and Struggle");
			session.setAttribute("MEDIA_CART", cart);

			return "sequinsSelfStruggleHome.jsp";

		} else if (request.getPathInfo().substring(1).contains("redirect_snaps")) {
			session.setAttribute("ARCHIVE", "Movie Snaps");
			session.setAttribute("MEDIA_CART", cart);
			return "movieSnapsHome.jsp";

		} else if (request.getPathInfo().substring(1).contains("redirect_harfield")) {
			session.setAttribute("ARCHIVE", "Harfield Village");
			session.setAttribute("MEDIA_CART", cart);
			return "harfieldVillageHome.jsp";

		}

		return "index.jsp";
	}

}
