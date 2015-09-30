package common.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ThreeArchivesServlet
 */
public class ThreeArchivesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ThreeArchivesServlet() {
		super();
	}

	private void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Entered Servlet");

		//just wnat to quickly clear out the cookie values we have first
		System.out.println("PathInfo : " + request.getPathInfo());
//		clearAllOtherCookies(request,response);
		ServiceDelegator serviceDelegator = new ServiceDelegator();
		String result = serviceDelegator.execute(request, response);
		request.getServletContext().getRequestDispatcher("/" + result).forward(request, response);
	}

	private void clearAllOtherCookies(HttpServletRequest request, HttpServletResponse response){
		Cookie[] cookies = request.getCookies();
		System.out.println("WE FOUND THIS MANY COOKIES " + cookies.length);
		for (Cookie c: cookies){
			if (!(c.getName().equalsIgnoreCase("dateLastVisited"))){
				//we want to remove all of teh cookies
				c.setValue(null);
				c.setMaxAge(0);
				c.setPath("/");
				response.addCookie(c);
			}
		}
		System.out.println("We now have " + request.getCookies().length + " cookies");
		request.getServletContext().getRequestDispatcher("/index.jsp");
		
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

}
