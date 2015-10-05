package maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.Controller;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.UploadClient;

public class MapController implements Controller {

	UploadClient client=new UploadClient("http://localhost:8080/fedora", "fedoraAdmin","12345",null);
	String title;
	String creator;
	String event;
	String subject;
	String description;
	String publisher;
	String contributor;
	String date;
	String resourcetype;
	String format;
	String source;
	String language;
	String relation;
	String coverage;
	String rights;
	String collection;
	String location;
	
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String archive = (String) request.getSession().getAttribute("ARCHIVE");
		Map search = new Map();
		request.setAttribute("digitalObject", search.place("ms:3"));
		request.setAttribute("placement", 1);
			
			if (archive.equalsIgnoreCase("Harfield Village")){
				
				if (request.getPathInfo().substring(1).contains("polygon")) {
	
					String points = (String) request.getParameter("polypoints");
					System.out.println(points);
					
					PrintWriter file = new PrintWriter(new FileWriter(
						    new File("points.txt"), 
						    true /* append = true */));
					//PrintWriter file = new PrintWriter("points.txt");
					//split points then loop
					file.append(points);
					file.close();
					
					//request.setAttribute("points", read());
					//string maniulation send to database
				}
			
			request.setAttribute("points", read());
			return "WEB-INF/frontend/maps/harfieldoverview.jsp";
			
			//setAttribute to points array for to be sent (points are from database)
			}
			else if (archive.equalsIgnoreCase("Movie Snaps")){
			if (request.getPathInfo().substring(1).contains("place")) {
				
				String pid = request.getParameter("image");
				FedoraDigitalObject image = search.place(pid);
				request.setAttribute("digitalObject", image);	
			}
			
			Set<FedoraDigitalObject> digitalObjects = new HashSet<FedoraDigitalObject>();
			digitalObjects = search.findFedoraDigitalObjects("&query=coverage~*");	
			request.setAttribute("objects", digitalObjects);
			
			return "WEB-INF/frontend/maps/mapoverview.jsp";}
			
			else{
				if(request.getPathInfo().substring(1).contains("place")){
					String pid = request.getParameter("image");
					FedoraDigitalObject image = search.place(pid);
					request.setAttribute("digitalObject", image);
					request.setAttribute("placement", 0);
					
					Set<FedoraDigitalObject> digitalObjects = new HashSet<FedoraDigitalObject>();
					digitalObjects = search.findFedoraDigitalObjects("&query=coverage~*");	
					request.setAttribute("objects", digitalObjects);
					
					return "WEB-INF/frontend/maps/mapoverview.jsp";
				}
				else if(request.getPathInfo().substring(1).contains("done")){
					
					String cover = request.getParameter("latlng");
					//putrequest
					Set<FedoraDigitalObject> digitalObjects = new HashSet<FedoraDigitalObject>();
					digitalObjects = search.findFedoraDigitalObjects("&query=coverage~*");	
					request.setAttribute("objects", digitalObjects);
					
					return "WEB-INF/frontend/maps/mapoverview.jsp";
				}
				else{request.setAttribute("view", search.place("ms:3"));
				if (request.getParameter("annotations")!=null){
					//put annotations 
					FedoraDigitalObject digi=(FedoraDigitalObject) request.getAttribute("view");
					title=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("TITLE");
					creator=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("CREATOR");
					event=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("EVENT");
					subject=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("SUBJECT");
					description=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("DESCRIPTION");
					publisher=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("PUBLISHER");
					contributor=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("CONTRIBUTOR");
					date=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("DATE");
					resourcetype=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("RESOURCETYPE");
					format=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("FORMAT");
					source=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("SOURCE");
					language=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("LANGUAGE");
					relation=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("RELATION");
					coverage=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("COVERAGE");
					rights=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("RIGHTS");
					collection=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("COLLECTION");
					location=((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("LOCATION");
					
					
				}
				return "WEB-INF/frontend/imageviewer.jsp";}
				
			}
	}
	
	

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
