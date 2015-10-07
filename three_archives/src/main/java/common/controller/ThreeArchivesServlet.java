package common.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.fedora.FedoraException;
import configuration.IndexPage;
import configuration.PropertiesConfiguration;

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
			throws FedoraException, Exception {
		System.out.println("Entered Servlet");

		// just wnat to quickly clear out the cookie values we have first
		System.out.println("PathInfo : " + request.getPathInfo());
		// clearAllOtherCookies(request,response);
		// testingConfig(request);
//		testPropertiesFileGeneration();
		ServiceDelegator serviceDelegator = new ServiceDelegator();
		String result = serviceDelegator.execute(request, response);
		request.getServletContext().getRequestDispatcher("/" + result).forward(request, response);
	}

	private void testingConfig(HttpServletRequest request) {
		System.out.println("WE ARE NOW TESTING THE CONFIG");
		try {
			IndexPage.generateIndexPage(request.getContextPath());
		} catch (Exception ex) {
			System.out.println("SOMETHING BROKE WHILE TRYING TO CONFIG");
			System.out.println(ex);
		}
	}

	private void testPropertiesFileGeneration() {
		System.out.println("TESTING PROPERTIES FILE CONFIG");
		HashMap<String, String> details = new HashMap<String, String>();
		details.put("archive.name", "My archive");
		details.put("archive.landingpage.image", "/image/newArchive.jpeg");
		details.put("archive.multimedia.prefix", "nm");
		details.put("service.searchandbrowse", "true");
		details.put("service.historyandstatistics", "true");
		details.put("service.exhibitions", "false");
		details.put("service.uploads", "true");
		details.put("serivce.maps", "false");
		details.put("service.downloads", "true");
		details.put("service.annotations", "false");

		PropertiesConfiguration u = new PropertiesConfiguration();
		try {
			u.generatePropertiesFile(details);
		} catch (Exception ex) {

		}

	}

	private void clearAllOtherCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		System.out.println("WE FOUND THIS MANY COOKIES " + cookies.length);
		for (Cookie c : cookies) {
			if (!(c.getName().equalsIgnoreCase("dateLastVisited"))) {
				// we want to remove all of teh cookies
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
		try {
			process(request, response);
		} catch (FedoraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			process(request, response);
		} catch (FedoraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
