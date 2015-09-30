package common.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



public class GeneralController implements Controller {
	
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		HttpSession session= request.getSession();
		ArrayList<String> cart = new ArrayList<String>();
		cart.add("ms:1");
		if (request.getPathInfo().substring(1).contains("redirect_sequins")) 
		{
			session.setAttribute("ARCHIVE","Sequins, Self and Struggle");
			session.setAttribute("MEDIA_CART", cart);
			AppTest at = new AppTest();
			at.testApp();
			return "home.jsp";

		}
		else if (request.getPathInfo().substring(1).contains("redirect_snaps")) 
		{
			session.setAttribute("ARCHIVE","Movie Snaps");
			session.setAttribute("MEDIA_CART", cart);
			return "home.jsp";

		}
		else if (request.getPathInfo().substring(1).contains("redirect_harfield")) 
		{
			session.setAttribute("ARCHIVE","Harfield Village");
			session.setAttribute("MEDIA_CART", cart);
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