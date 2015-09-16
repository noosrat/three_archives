package maps;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import search.Search;
import common.controller.Controller;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;

public class MapController implements Controller {

	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (request.getPathInfo().substring(1).contains("redirect_maps")) {

			List<FedoraDigitalObject> digitalObjects = new ArrayList<FedoraDigitalObject>();
			Search search = new Search();
			digitalObjects = search.findFedoraDigitalObjects("&query=coverage~*");	
			//digitalObjects = search.findFedoraDigitalObjects("kitten");	
			request.setAttribute("objects", digitalObjects);
			
			return "WEB-INF/frontend/maps/mapoverview.jsp";
		}
		else return null;

	}
	
	public String browse(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (request.getPathInfo().substring(1).contains("redirect_maps2")) {

			/*List<FedoraDigitalObject> digitalObjects = new ArrayList<FedoraDigitalObject>();
			Search search = new Search();
			digitalObjects = search.findFedoraDigitalObjects("coverage~*");
			
			request.setAttribute("objects", digitalObjects);*/
			
			return "WEB-INF/frontend/imageviewer.jsp";
		}
		else if (request.getPathInfo().substring(1).contains("redirect_har_maps")) {

			/*List<FedoraDigitalObject> digitalObjects = new ArrayList<FedoraDigitalObject>();
			Search search = new Search();
			digitalObjects = search.findFedoraDigitalObjects("coverage~*");
			
			request.setAttribute("objects", digitalObjects);*/
			
			return "WEB-INF/frontend/maps/harfieldoverview.jsp";
		}
		else return null;

	}
	
	/*public String place(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (request.getPathInfo().substring(1).contains("redirect_maps")) {

			return "WEB-INF/frontend/maps/mapoverview.jsp";
		}
		else return null;

	}*/

	
	
}
