package controller;

import java.io.IOException;

import javax.servlet.ServletException;
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		response.getWriter().append("Served at Nox context path: ").append(request.getContextPath()).append("\n");
//		response.getWriter().append("Served at Nox servlet context path: ").append(request.getServletContext());
//		response.getWriter().append("Served at Nox path info: ").append(request.getPathInfo()).append("\n");
//		
//		Enumeration<String> e= request.getHeaderNames();
//		while (e.hasMoreElements()){
//			String header = e.nextElement();
//			response.getWriter().append("header: " ).append(header).append("value: ").append(request.getHeader(header)).append("\n") ;
//			
//		}
		
	
		ServiceDelegator serviceDelegator = new ServiceDelegator();
		String result = serviceDelegator.execute(request, response);
		
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/"+ "nox.jsp");
		
		request.getServletContext().getRequestDispatcher("/"+result).forward(request, response);

	
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
