package maps;

import java.io.File;
import java.io.FileNotFoundException;
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

	private UploadClient client=new UploadClient("http://personalhistories.cs.uct.ac.za:8089/fedora", "fedoraAdmin","3Arch",null);
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
	String annotations;
	
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String archive = (String) request.getSession().getAttribute("ARCHIVE");
		Map search = new Map();
		request.setAttribute("placeObject", search.place("ms:3"));//dummy fedora object
		request.setAttribute("placement", 1);
			
			if (archive.equalsIgnoreCase("Harfield Village")){
				
				if (request.getPathInfo().substring(1).contains("polygon")) {
	
					String points = (String) request.getParameter("polypoints");
					System.out.println(points);
					
					PrintWriter file = new PrintWriter(new FileWriter(
						    new File("../webapps/data/points.txt"), 
						    true /* append = true */));
					//PrintWriter file = new PrintWriter("points.txt");
					//split points then loop
					file.append(points);
					file.close();
					
					//request.setAttribute("points", read());
					//string maniulation send to database
				}
			
			request.setAttribute("points", read("../webapps/data/points.txt"));
			request.setAttribute("harfieldcollections", read("../webapps/data/collections.txt"));
			return "WEB-INF/frontend/maps/harfieldoverview.jsp";
			
			//setAttribute to points array for to be sent (points are from database)
			}
			else{
				if(request.getPathInfo().substring(1).contains("place")){
					String pid = request.getParameter("image");
					FedoraDigitalObject image = search.place(pid);
					request.setAttribute("placeObject", image);
					request.setAttribute("placement", 0);
				}
				else if(request.getPathInfo().substring(1).contains("done")){
					System.out.println("in if");
					FedoraDigitalObject digi=(FedoraDigitalObject) request.getAttribute("placeObject");
					System.out.println(digi.getPid());
					String cover = request.getParameter("latlng");
					System.out.println(cover);
					title=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("TITLE"));
					creator=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("CREATOR"));
					event=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("EVENT"));
					subject=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("SUBJECT"));
					description=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("DESCRIPTION"));
					publisher=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("PUBLISHER"));
					contributor=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("CONTRIBUTOR"));
					date=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("DATE"));
					resourcetype=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("RESOURCETYPE"));
					format=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("FORMAT"));
					source=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("SOURCE"));
					language=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("LANGUAGE"));
					relation=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("RELATION"));
					coverage=""+cover;//makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("COVERAGE"));
					rights=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("RIGHTS"));
					collection=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("COLLECTION"));
					location=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("LOCATION"));
					annotations=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("ANNOTATIONS"));
					
					System.out.println("recovered all metadata");
					String xml = client.makeXML(title, creator, event, publisher, contributor, date, resourcetype, format, source, language, relation, location, rights, collection, coverage, subject, annotations, description);
					System.out.println("made xml");
					client.PUT("/objects/"+digi.getPid()+"/datastreams/DC?controlGroup=M&mimeType=text/xml", xml);
					System.out.println("put");
				}
				else if (request.getParameter("annotations")!=null){
					//put annotations 
					FedoraDigitalObject digi=(FedoraDigitalObject) request.getAttribute("view");
					title=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("TITLE"));
					creator=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("CREATOR"));
					event=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("EVENT"));
					subject=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("SUBJECT"));
					description=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("DESCRIPTION"));
					publisher=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("PUBLISHER"));
					contributor=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("CONTRIBUTOR"));
					date=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("DATE"));
					resourcetype=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("RESOURCETYPE"));
					format=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("FORMAT"));
					source=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("SOURCE"));
					language=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("LANGUAGE"));
					relation=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("RELATION"));
					coverage=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("COVERAGE"));
					rights=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("RIGHTS"));
					collection=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("COLLECTION"));
					location=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("LOCATION"));
				}
				Set<FedoraDigitalObject> digitalObjects = new HashSet<FedoraDigitalObject>();
				digitalObjects = search.findFedoraDigitalObjects("&query=coverage~*%25*");	
				request.setAttribute("mapObjects", digitalObjects);
				return "WEB-INF/frontend/maps/mapoverview.jsp";
				}
				
			}
	
	
	

	List<String> read(String filepath)
	{	
		File file = new File(filepath);
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
	
	String makesure(String string)
	{	
		if (string == null)
				return "";
		else
			return string;
	}
	
	
}
