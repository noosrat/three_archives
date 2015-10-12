package annotations;

import java.util.Set;

import search.FedoraCommunicator;
import common.Service;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
import common.fedora.UploadClient;

public class Annotations extends Service{

	public Annotations() {
		super();
	}
	
	public Set<FedoraDigitalObject> addAnnotation(String pid, String annotations, Set<FedoraDigitalObject> current) throws FedoraException, Exception{
		
		System.out.println("madeit");
		UploadClient client=new UploadClient("http://localhost:8089/fedora", "fedoraAdmin","3Arch",null);
		
		System.out.println("in check annotations");
		//System.out.println(request.getParameter("pid"));
		//String annotations=request.getParameter("annotations");
		System.out.println(annotations);
		
		FedoraCommunicator fc=new FedoraCommunicator();
		FedoraDigitalObject temp=fc.populateFedoraDigitalObject(pid);
		
		current.remove(temp);
		
		String title=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("TITLE"));
		String creator=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("CREATOR"));
		String event=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("EVENT"));
		String subject=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("SUBJECT"));
		String description=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("DESCRIPTION"));
		String publisher=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("PUBLISHER"));
		String contributor=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("CONTRIBUTOR"));
		String date=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("DATE"));
		String resourcetype=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("RESOURCETYPE"));
		String format=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("FORMAT"));
		String source=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("SOURCE"));
		String language=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("LANGUAGE"));
		String relation=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("RELATION"));
		String coverage=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("COVERAGE"));
		String rights=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("RIGHTS"));
		String collection=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("COLLECTION"));
		String location=makesure(((DublinCoreDatastream) temp.getDatastreams().get("DC")).getDublinCoreMetadata().get("LOCATION"));
		//String annotations=makesure(((DublinCoreDatastream) digi.getDatastreams().get("DC")).getDublinCoreMetadata().get("ANNOTATIONS"));
		
		System.out.println("recovered all metadata");
		String xml = client.makeXML(title, creator, event, publisher, contributor, date, resourcetype, format, source, language, relation, location, rights, collection, coverage, subject, annotations, description);
		System.out.println("made xml");
		client.PUT("/objects/"+temp.getPid()+"/datastreams/DC?controlGroup=M&mimeType=text/xml", xml);
		System.out.println("put");
		
		temp=fc.populateFedoraDigitalObject(pid);
		current.add(temp);
		
		return current;
	}
	
	String makesure(String string)
	{	
		if (string == null)
				return "";
		else
			return string;
	}
}