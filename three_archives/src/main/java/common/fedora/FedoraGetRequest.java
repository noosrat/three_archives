package common.fedora;

import java.util.HashMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/*
 * Notes on the class: our result format is ALWAYS to be in XML
 */
public class FedoraGetRequest {

	private final static Logger LOG = Logger.getLogger(FedoraGetRequest.class);

	private String persistentIdentifier;
	private StringBuilder request;
	private TreeMap<QueryParameters, String> queryParameters;
	private final static String baseURL = FedoraCredentials.getUrl();

	public FedoraGetRequest() {
		this.request = new StringBuilder(baseURL.concat("/objects"));
		this.queryParameters = new TreeMap<QueryParameters, String>();
	}

	public FedoraGetRequest(String persistentIdentifier) {
		this();
		this.persistentIdentifier = persistentIdentifier;
	}

	public StringBuilder getRequest() {
		return request;
	}

	public void setRequest(StringBuilder request) {
		this.request = request;
	}
	
	public void resetRequest(){
		setRequest(new StringBuilder(baseURL.concat("/objects")));
	}

	public String getPersistentIdentifier() {
		return persistentIdentifier;
	}

	public void setPersistentIdentifier(String persistentIdentifier) {
		this.persistentIdentifier = persistentIdentifier;
	}

	public TreeMap<QueryParameters, String> getQueryParameters() {
		return queryParameters;
	}

	private void setQueryParameters(
			TreeMap<QueryParameters, String> queryParameters) {
		if (queryParameters != null) {
			this.queryParameters = queryParameters;
		}
		processParameters();
	}

	private StringBuilder getPrefix() {
		StringBuilder prefix;
		if (getPersistentIdentifier() != null) {
			prefix = getRequest().append("/").append(getPersistentIdentifier());
		} else
			prefix = getRequest().append("/");
		return prefix;
	}
	
	// /objects ? [terms | query] [maxResults] [resultFormat] [pid] [label]
	// [state] [ownerId] [cDate] [mDate] [dcmDate] [title] [creator] [subject]
	// [description] [publisher] [contributor] [date] [type] [format]
	// [identifier] [source] [language] [relation] [coverage] [rights]
	// /objects?terms=demo&pid=true&subject=true&label=true&resultFormat=xml

	public FedoraGetRequest findObjects(
			TreeMap<QueryParameters, String> queryParameters,
			DublinCore... toReturn) {
		getRequest().append("?");
		// need to go through and add the DC ones so long
		for (DublinCore dc : toReturn) {
			getRequest().append(dc.getDescription()).append("=true&");
		}
		System.out.println("Request after dublin core fields specified "
				+ getRequest());
		setQueryParameters(queryParameters);

		return this;
	}

	// /objects/{pid}/datastreams/{dsID}/content ? [asOfDateTime] [download]
	// /objects/demo:29/datastreams/DC/content
	public FedoraGetRequest getDatastreamDissemination(String dsid, TreeMap<QueryParameters, String> queryParameters) {
		getPrefix().append("/datastreams/").append(dsid).append("/content?");
		setQueryParameters(queryParameters);
		return this;
	}

	// Probably will not use this?
	// // /objects/{pid}/methods/{sdefPid}/{method} ? [method parameters]
	// // /objects/demo:29/methods/demo:27/resizeImage?width=100
	// public void getDissemination(String pid, String sdefPid) {
	// getRequest().append("/").append(pid).
	// }

	// /objects/{pid}/versions ? [format]
	// /objects/demo:29/versions?format=xml
	public FedoraGetRequest getObjectHistory() {
		getPrefix().append("/versions?");
		setQueryParameters(new TreeMap<QueryParameters, String>());
		return this;
	}

	// /objects/{pid} ? [format] [asOfDateTime]
	// /objects/demo:29?format=xml
	public FedoraGetRequest getObjectProfile(
			TreeMap<QueryParameters, String> queryParameters) {
		getPrefix().append("?");
		setQueryParameters(queryParameters);
		return this;

	}

	// /objects/{pid}/datastreams ? [format] [asOfDateTime]
	// /objects/demo:35/datastreams?format=xml&asOfDateTime=2008-01-01T05:15:00Z
	public FedoraGetRequest listDatastreams(
			TreeMap<QueryParameters, String> queryParameters) {
		getPrefix().append("/datastreams?");
		setQueryParameters(queryParameters);
		return this;
	}

	// /objects/{pid}/datastreams/{dsID} ? [asOfDateTime] [format]
	// [validateChecksum]
	// /objects/demo:29/datastreams/DC?format=xml&validateChecksum=true

	public FedoraGetRequest getDatastream(String dsid,
			TreeMap<QueryParameters, String> queryParameters) {
		getPrefix().append("/datastreams/").append(dsid).append("?");
		setQueryParameters(queryParameters);
		return this;

	}

	// /objects/{pid}/datastreams/{dsid}/history ? [format]
	// GET: /objects/changeme:1/datastreams/DC/history?format=xml
	public FedoraGetRequest getDatastreamHistory(String dsid) {
		getPrefix().append("/datastreams/").append(dsid).append("/history?");
		setQueryParameters(null);
		return this;
	}

	// /objects/{pid}/datastreams ? [profiles] [asOfDateTime]
	// /objects/demo:35/datastreams?profiles=true&asOfDateTime=2012-08-03T10:02:00.169Z
	public FedoraGetRequest getDatastreams(
			TreeMap<QueryParameters, String> queryParameters) {
		getPrefix().append("/datastreams?");
		setQueryParameters(queryParameters);
		return this;
	}

	// /objects/{pid}/relationships ? [subject] [predicate] [format]
	// /objects/demo:29/relationships?subject=info%3afedora%2fdemo%3a29%2fDC
	public FedoraGetRequest getRelationships(
			TreeMap<QueryParameters, String> queryParameters) {
		getPrefix().append("/relationships?");
		setQueryParameters(queryParameters);
		return this;
	}

	// /objects/{pid}/objectXML
	// /objects/demo:29/objectXML
	public FedoraGetRequest getObjectXML() {
		getPrefix().append("/objectXML");
		return this;
	}

	private void processParameters() {
		/*
		 * we have a whole map of all the parameters now we must append it to
		 * our string
		 */
		System.out.println("Processing parameters" + queryParameters.values());

		for (QueryParameters key : queryParameters.keySet()) {
			getRequest().append(key.getDescription()).append("=")
					.append(getQueryParameters().get(key)).append("&");
		}
		if (!queryParameters.containsKey(QueryParameters.RESULT_FORMAT)) {
			getRequest().append("format=xml");
		} else {
			getRequest().delete(getRequest().length() - 1,
					getRequest().length());
		}

		System.out.println("Finished processing parameters, request: "
				+ getRequest().toString());

	}

	// public static void main(String[] args) throws FedoraException,
	// IOException{
	// FedoraGetRequest feGetRequest = new FedoraGetRequest();
	// feGetRequest.setPersistentIdentifier("sq:3");
	// feGetRequest.getObjectProfile(new TreeMap<QueryParameters,
	// String>()).execute();
	// System.out.println(feGetRequest.getFedoraResponse().getResponse());
	//
	// }

}