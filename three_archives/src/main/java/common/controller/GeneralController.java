package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



public class GeneralController implements Controller {
	
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		HttpSession session= request.getSession();
		if (request.getPathInfo().substring(1).contains("redirect_sequins")) 
		{
			session.setAttribute("ARCHIVE","Sequins, Self and Struggle");
			
			return "home.jsp";

		}
		else if (request.getPathInfo().substring(1).contains("redirect_snaps")) 
		{
			session.setAttribute("ARCHIVE","Movie Snaps");
			return "home.jsp";

		}
		else if (request.getPathInfo().substring(1).contains("redirect_harfield")) 
		{
			session.setAttribute("ARCHIVE","Harfield Village");
			return "home.jsp";

		}
		
		return general(request, response);
	}
	
	private String general(HttpServletRequest request,
			HttpServletResponse response){
		
		String result = "";
		HttpSession session = request.getSession();
		
		
		
		return result;

	}

}