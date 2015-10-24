package common.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.fedora.FedoraException;

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
	 * The process method is called regardless of whether a
	 * {@link #doGet(HttpServletRequest, HttpServletResponse)} or
	 * {@link #doPost(HttpServletRequest, HttpServletResponse)} method is called
	 * by the client. This method passes control to the {@link ServiceDelegator}
	 * class and dispatches the result retrieved from this class
	 * 
	 * @param request
	 * @param response
	 * @throws FedoraException
	 * @throws Exception
	 */
	private void process(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("Entered Servlet");

		System.out.println("PathInfo : " + request.getPathInfo());
		ServiceDelegator serviceDelegator = new ServiceDelegator();
		try {
			String result = serviceDelegator.execute(request, response);
			request.getServletContext().getRequestDispatcher("/" + result)
					.forward(request, response);
		} catch (Exception ex) {
			request.setAttribute("message",
					"Something seems to have gone wrong.  Please contact IT");
			System.out.println(ex);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		process(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

}
