package common.controller;


import java.util.ArrayList;
import java.util.List;






import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.model.Exhibition;
import common.model.ManageUsers;
import common.model.User;
import exhibitions.ManageExhibition;
import history.HistoryController;
import search.SearchController;

public class GeneralController implements Controller {

	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = "";
		ArrayList<String> cart = new ArrayList<String>();
		//cart.add("ms:1");
		//ManageUsers userManager= new ManageUsers();
		//userManager.addUser(new User("admin","admin","ADMINISTRATOR"));
		//userManager.addUser(new User("student","student","privileged"));
		HttpSession session = request.getSession();
		request.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		HistoryController historyController = new HistoryController();
		String pathInfo =  request.getPathInfo().substring(1);
		System.out.println(pathInfo);
		
		
		if (pathInfo.equalsIgnoreCase("SequinsSelfAndStruggle")){
			session.setAttribute("ARCHIVE", "Sequins, Self and Struggle");
			session.setAttribute("MEDIA_CART", cart);
			historyController.execute(request, response);
			return "sequinsSelfAndStruggleHome.jsp";

		} else if (pathInfo.equalsIgnoreCase("MovieSnaps")) {
			session.setAttribute("ARCHIVE", "Movie Snaps");
			session.setAttribute("MEDIA_CART", cart);
			historyController.execute(request, response);
			return "movieSnapsHome.jsp";

		} else if (pathInfo.equalsIgnoreCase("HarfieldVillage")) {
			session.setAttribute("ARCHIVE", "Harfield Village");
			session.setAttribute("MEDIA_CART", cart);
			historyController.execute(request, response);
			return "harfieldVillageHome.jsp";
		}

		return "index.jsp";
	}

}