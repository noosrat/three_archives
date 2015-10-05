package maps;

import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServerException;

import common.Service;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
import common.model.Polygon;

public class Map extends Service {
	
	ManagePolygons manager=new ManagePolygons();
	
	public Map() {
		super();
	}
	
	public Set<FedoraDigitalObject> findFedoraDigitalObjects(String terms) throws FedoraException, SolrServerException{
//		return getFedoraCommunicator().findFedoraDigitalObjectsUsingSearchTerms(terms);
		return getFedoraCommunicator().findFedoraDigitalObjects(terms, "maps");
	}
	
	public String test() throws FedoraException
	{
		return getFedoraCommunicator().populateFedoraDigitalObject("ms:1").getDatastreams().get("IMG").getContent() ;
		}
	
	public FedoraDigitalObject place(String pid) throws FedoraException
	{
		return getFedoraCommunicator().populateFedoraDigitalObject(pid);
		}
	
	public List<Polygon> listPolygons()
 	{
		//retrieve Polygon with id-PolygonId
		List<Polygon> listPolygons= manager.listAllPolygons();
		return listPolygons;
 	}
	
	public Integer savePolygon(Polygon Polygon)
 	{
		//Create new Polygon and save it to the db
		return(manager.addPolygon(Polygon));//add Polygon to the db
 	}

	
	public Polygon getPolygon(int PolygonID)
 	{
		//find titles of all Polygons with archive type= type
		return (manager.getPolygon(PolygonID));//get Polygon with the specific ID
 		
 	}
	
}