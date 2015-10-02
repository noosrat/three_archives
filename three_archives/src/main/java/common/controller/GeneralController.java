package common.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.model.Exhibition;
import exhibitions.ManageExhibition;
import search.SearchController;

public class GeneralController implements Controller {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		request.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		if (request.getPathInfo().substring(1).contains("redirect_sequins")) {
			session.setAttribute("ARCHIVE", "Sequins, Self and Struggle");
			//AppTest at=new AppTest();
			//at.testApp();
		//	ManageExhibition man=new ManageExhibition();
			
		//	man.listExhibitions();
		
			//String media="sq:15 %sq:15 %sq:15 %sq:15 %sq:15 %sq:15 %sq:15 %sq:15 %sq:15 %sq:15 %sq:15 %sq:15 %";
			//String captions="1 %2 %3 %4 %5 %6 %7 %8 %9 %10 %11 %12";
			//man.addExhibition(new Exhibition("title","dscription",1,"creator",media,captions));
			//man.addExhibition(new Exhibition("title1","dscription1",1,"creator",media,captions));
			//man.addExhibition(new Exhibition("title2","dscription2",1,"creator",media,captions));
			//man.addExhibition(new Exhibition("title2","dscription2",1,"creator",media,captions));
			//man.getExhibition(1);
			//List ex=man.listAllExhibitions();
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