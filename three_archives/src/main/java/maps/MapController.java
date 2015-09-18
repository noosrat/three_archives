package maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.Controller;
import common.fedora.FedoraDigitalObject;

public class MapController implements Controller {

	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (request.getPathInfo().substring(1).contains("redirect_maps")) {

			Set<FedoraDigitalObject> digitalObjects = new HashSet<FedoraDigitalObject>();
			Map search = new Map();
			digitalObjects = search.findFedoraDigitalObjects("&query=coverage~*");	
			//digitalObjects = search.findFedoraDigitalObjects("kitten");	
			request.setAttribute("objects", digitalObjects);
			request.setAttribute("points", read());
//			
			//setAttribute to points array for to be sent (points are from database)
			
			return "WEB-INF/frontend/maps/harfieldoverview.jsp";
		}
		else if (request.getPathInfo().substring(1).contains("polygon")) {

			String points = (String) request.getParameter("polypoints");
			System.out.println(points);
			
			PrintWriter file = new PrintWriter("points.txt");
			//split points then loop
			file.println(points);
			file.close();
			
			request.setAttribute("points", read());
			//string maniulation send to database
			return "WEB-INF/frontend/maps/harfieldoverview.jsp";
		}
		else if (request.getPathInfo().substring(1).contains("place")) {

			List<FedoraDigitalObject> MapPic = (List<FedoraDigitalObject>) request.getAttribute("digitalObject");
			
			request.setAttribute("MapPic", MapPic);
		
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
	List<String> read()
	{	
		File file = new File("points.txt");
		Scanner sc;
		List<String> result = new ArrayList();
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine())
			{ result.add(sc.nextLine());
			System.out.println(result.get(0));}
			
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
}
