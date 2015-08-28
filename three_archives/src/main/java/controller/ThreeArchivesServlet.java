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
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		redirect(request, response);
		
		ServiceDelegator serviceDelegator = new ServiceDelegator();
		String result = serviceDelegator.execute(request, response);
		request.getServletContext().getRequestDispatcher("/" + result)
				.forward(request, response);

	}

	private void redirect(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getPathInfo().substring(1).equals("redirect_search")) {

			request.getServletContext().getRequestDispatcher("/search.jsp")
					.forward(request, response);
		} 
		if (request.getPathInfo().substring(1).equals("redirect_maps")) {

			request.getServletContext()
					.getRequestDispatcher("/mapoverview.jsp")
					.forward(request, response);
		}

		if (request.getPathInfo().substring(1).equals("redirect_exhibitions")) {

			request.getServletContext()
					.getRequestDispatcher("/exhibitionHome.jsp")
					.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
