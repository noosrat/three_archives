package common.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
import common.fedora.UploadClient;
import downloads.DownloadController;
import exhibitions.ExhibitionController;
import history.HistoryController;
import maps.MapController;
import search.BrowseController;
import search.FedoraCommunicator;
import search.SearchController;
import uploads.UploadController;

public class ServiceDelegator {
	// handle exceptions in this method

	private static final HashMap<String, Controller> controllers = new HashMap<String, Controller>();

	static {
		controllers.put("search", new SearchController());
		controllers.put("exhibitions", new ExhibitionController());
		controllers.put("general", new GeneralController());
		controllers.put("maps", new MapController());
		controllers.put("downloads", new DownloadController());
		controllers.put("uploads", new UploadController());
		controllers.put("browse", new BrowseController());
		controllers.put("history", new HistoryController());
	}

	public HashMap<String, Controller> getControllers() {
		return controllers;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws FedoraException, Exception{
		String url = "index.jsp";
		/*System.out.println(request.getParameter("deletions"));
			if (request.getParameter("deletions")!=null && !request.getParameter("deletions").equals(""))
			{
				System.out.println("in deletions");
			String deletions=request.getParameter("deletions");
			String delete[] = deletions.split(",");
			
			for (int k=0; k<delete.length;k++){
				download.removeFromCart(cart, delete[k]);
			}*/
		String pathInfo = request.getPathInfo().substring(1);
		try {
			if (pathInfo.contains("search")) {
				url = controllers.get("search").execute(request, response);
			} else if (pathInfo.contains("browse")) {
				url = controllers.get("browse").execute(request, response);
			} else if (pathInfo.contains("exhibition")) {
				url = controllers.get("exhibitions").execute(request, response);
			}else if (pathInfo.contains("maps")) {
				url = controllers.get("maps").execute(request, response);
			}else if (pathInfo.contains("downloads")) {
				url = controllers.get("downloads").execute(request, response);
			} else if (pathInfo.contains("uploads")) {
				url = controllers.get("uploads").execute(request, response);
			} else if (pathInfo.contains("history")) {
				url = controllers.get("history").execute(request, response);
			} else {
				System.out.println("PROCESSING GENERAL");
				url = controllers.get("general").execute(request, response);
			}
		} catch (Exception exception) {
			request.setAttribute("message", exception.getMessage());
			System.out.println(exception);

			} 
		persistAllRequestInformation();
		return url;
	}

	/* this is everything to be persisted after each request is executed */
	private void persistAllRequestInformation() {
		HistoryController.persistNecessaryRequestInformation();

	}

}
