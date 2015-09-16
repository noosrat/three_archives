package search;

import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class SolrCommunicator {

	private static String url="http://localhost:8080/solr/collection1";
	private static final SolrServer solr;
//	private static final String[] queryFields = new String[]{"PID","fgs.label","fgs.createdDate","fgs.lastModifiedDate","dc.date","dc.description","dc.format","dc.identifier","dc.publisher","dc.relation","dc.rights","dc.subject","dc.title"};
	
	
	static {
		solr = new HttpSolrServer(url);
	}
	
	
	private static SolrQuery solrQuery(String query){
		/*
		 * When conducting the query we need to specifiy which fields we will essentially be looking in...and we need to append the query to our query strings
		 */
		System.out.println("in SOLR Query");
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.set("q",query);//,"fgs.label:\"Spring Queen\"","fgs.createdDate::\"Spring Queen\"","dc.description::\"Spring Queen\"");
		solrQuery.setStart(0);
		solrQuery.setRows(1000000000);
		solrQuery.setFields("PID");
		
		return solrQuery;
	}

//	
//	private static String[] buildQuery(String query){
//		System.out.println("Building SOLR Query");
//		String[] queryResult = new String[queryFields.length];
//		for (int index=0; index<queryFields.length; index++ ){
//			queryResult[index] = queryFields[index] + ":\"" + query + "\"";
//			System.out.println("building query for each field: " + queryResult[index]);
//		}
//		
//		return queryResult;
//	}
//	
	private static ArrayList<String> solrResponse(SolrQuery solrQuery) throws SolrServerException{
		System.out.println("in SOLR response");
		QueryResponse queryResponse = solr.query(solrQuery);
		SolrDocumentList list = queryResponse.getResults();
		System.out.println("SOLR response size: " + list.size());
		System.out.println("SOLR response size: " + list.getNumFound());
		ArrayList<String> pids = new ArrayList<String>();
		for (int x=0; x<list.getNumFound(); x++){
			pids.add((String)list.get(x).get("PID"));
		}
		System.out.println("Result found : " + pids.toString());
		return pids;
	}
	
	/*
	 * this is our entry point into the solr search and the class....this is where the query will be constructed and execution called
	 */
	public static ArrayList<String> solrSearch(String query) throws SolrServerException{
		return solrResponse(solrQuery(query));
	}
	

}