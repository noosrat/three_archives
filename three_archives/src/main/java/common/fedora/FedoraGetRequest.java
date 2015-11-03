package common.fedora;

import java.util.TreeMap;

/*
 * The result format is ALWAYS to be in XML
 */
/**
 * The {@code FedoraGetRequest} is responsible for building up the HTTP request
 * to be propagated to the Fedora HTTP RESTFUL API using the
 * {@link FedoraClient}. The FedoraGetRequest contains various string mappings
 * to methods contained within the RestFUL API. Further information can be found
 * at {@link https://wiki.duraspace.org/display/FEDORA38/REST+API}
 * 
 * @author mthnox003
 * 
 *
 */
public class FedoraGetRequest {

	/**
	 * The {@link String} representation of the pid of the object being queried.
	 * This pid will be in every query made to the digital object repository
	 */
	private String pid;
	/**
	 * The {@link StringBuilder} instance representing the query to be made to
	 * the Fedora RESTFUL API. These queries are built up based on what is
	 * expected by the Fedora restful API
	 */
	private StringBuilder request;
	/**
	 * The {@link TreeMap} representing the queryParameters for the request
	 * being made to the digital object repository
	 * 
	 * The query parameters are structured with a key of {@link QueryParameter}
	 * and the value matching the key being the actual value required
	 */
	private TreeMap<QueryParameter, String> queryParameters;
	/**
	 * The {@link String} representation of the URL where the fedora repository
	 * service is being run. All of the requests are built up with the baseURL
	 * as the prefix
	 */
	private final static String BASE_URL = FedoraCredentials.getUrl();

	/**
	 * Constructor This parameterless constructor uses the baseURL to construct
	 * the {@link #request} attribute and concatenates it with "objects" in
	 * order to build up the appropriate request to be sent to fedora for the
	 * retrieval of the digital objects.
	 * 
	 * The {@link #queryParameters} is initialised to an empty {@link TreeMap}
	 * here
	 */
	public FedoraGetRequest() {
		this.request = new StringBuilder(BASE_URL.concat("/objects"));
		this.queryParameters = new TreeMap<QueryParameter, String>();
	}

	/**
	 * Constructor
	 * 
	 * @param pid
	 *            representing the pid of the digital object being queried
	 */
	public FedoraGetRequest(String pid) {
		this();
		this.pid = pid;
	}

	/**
	 * Gets the request string
	 * 
	 * @return {@link String} instance of the built up request
	 */
	public StringBuilder getRequest() {
		return request;
	}

	/**
	 * Sets the request string
	 * 
	 * @param request
	 *            {@link String} instance representing the request
	 */
	public void setRequest(StringBuilder request) {
		this.request = request;
	}

	/**
	 * Resetting the {@link #request} to only contain the base URL with the
	 * "objects" concatenated This is necessary after successful execution of
	 * each request to ensure that the subsequent request is not being built on
	 * top of an existing request
	 */
	public void resetRequest() {
		setRequest(new StringBuilder(BASE_URL.concat("/objects")));
	}

	/**
	 * Gets the pid of the object being dealt with
	 * 
	 * @return {@link #pid}
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * Sets the pid of the object being dealt with
	 * 
	 * @param persistentIdentifier
	 */

	public void setPersistentIdentifier(String persistentIdentifier) {
		this.pid = persistentIdentifier;
	}

	/**
	 * Gets the query parameters for the request
	 * 
	 * @return {@link TreeMap} instance of the query parameters for the fedora
	 *         get request
	 */
	public TreeMap<QueryParameter, String> getQueryParameters() {
		return queryParameters;
	}

	/**
	 * Sets the query parameters for the request. This is only decided and set
	 * within this class. The parameters are then processed and appended to the
	 * {@link #request}
	 * 
	 * @param queryParameters
	 *            representing the query parameters required for this request.
	 * 
	 */
	private void setQueryParameters(TreeMap<QueryParameter, String> queryParameters) {
		if (queryParameters != null) {
			this.queryParameters = queryParameters;
		}
		processParameters();
	}

	/**
	 * Dependent on what REST API method is being interacted with, the prefix
	 * for the request will differ. One prefix includes the persistent
	 * identifier which is used widely when making the requests, however, the
	 * {@link #pid} is not necessary when querying the repository for all
	 * digital objects as n {@link #findObjects(TreeMap, DublinCore...)}
	 * 
	 * @return {@link String} instance representing the request prefix
	 */
	private StringBuilder getPrefix() {
		StringBuilder prefix;
		if (getPid() != null) {
			prefix = getRequest().append("/").append(getPid());
		} else
			prefix = getRequest().append("/");
		return prefix;
	}

	/**
	 * This corresponds to the Fedora REST API method "findObjects" and returns
	 * a list of fedora digital objects that match the query parameters
	 * specified within the request. The structure of the request is
	 * "/objects ? [terms | query] [maxResults] [resultFormat] [pid] [label] [state] [ownerId] [cDate] [mDate] [dcmDate] [title] [creator] [subject] [description] [publisher] [contributor] [date] [type] [format] [identifier] [source] [language] [relation] [coverage] [rights]"
	 * where the possible query parameters are indicated by the items in [] an
	 * example request would be
	 * "/objects?terms=demo&pid=true&subject=true&label=true&resultFormat=xml"
	 * 
	 * 
	 * @param queryParameters
	 *            a {@link TreeMap} representation of the query parameters
	 *            required for the request.
	 * @param toReturn
	 *            a collection containing which {@link DublinCore} metadata
	 *            fields belonging to the objects to return.
	 * @return instance of {@link FedoraGetRequest} to be executed via the
	 *         {@link FedoraClient}
	 */
	public FedoraGetRequest findObjects(TreeMap<QueryParameter, String> queryParameters, DublinCore... toReturn) {
		getRequest().append("?");
		for (DublinCore dc : toReturn) {
			getRequest().append(dc.getDescription()).append("=true&");
		}
		setQueryParameters(queryParameters);

		return this;
	}

	/**
	 * This retrieves the content of the actual datastream. An example query
	 * would be "/objects/demo:29/datastreams/DC/content" The structure of the
	 * query may be
	 * "/objects/{pid}/datastreams/{dsID}/content ? [asOfDateTime] [download]"
	 * 
	 * @param dsid
	 * @param queryParameters
	 *            a collection representing the query parameters for the
	 *            request. The possible options for query parameters are
	 *            [asOfDateTime] and [download] whose values would be a date
	 *            time of format yyyy-MM-dd HH:mm:ss.SSS and a boolean respectively
	 * @return instance of {@link FedoraGetRequest} to be executed via the
	 *         {@link FedoraClient}
	 */
	public FedoraGetRequest getDatastreamDissemination(String dsid, TreeMap<QueryParameter, String> queryParameters) {
		getPrefix().append("/datastreams/").append(dsid).append("/content?");
		setQueryParameters(queryParameters);
		return this;
	}

	/**
	 * This constructs a request to get the object history of the digital
	 * object. The only query parameter (which is implicitly included) is
	 * [format] Example query "/objects/demo:29/versions?format=xml"
	 * 
	 * @return instance of {@link FedoraGetRequest} to be executed via the
	 *         {@link FedoraClient}
	 */
	public FedoraGetRequest getObjectHistory() {
		getPrefix().append("/versions?");
		setQueryParameters(new TreeMap<QueryParameter, String>());
		return this;
	}

	/**
	 * This methods retrieves the {@link FedoraDigitalObject}'s profile. This
	 * includes element such as the {@link FedoraDigitalObject.dateCreated},
	 * {@link FedoraDigitalObject.dateLastModified},
	 * {@link FedoraDigitalObject.state}. Example query:
	 * "/objects/demo:29?format=xml"
	 * 
	 * @param queryParameters
	 *            a collection representing the query parameters for the
	 *            request. The possible options for query parameters are
	 *            [format] and [asOfDateTime] where format is either xml or html
	 *            and asOfDateTime would be a specific date
	 * @return instance of {@link FedoraGetRequest} to be executed via the
	 *         {@link FedoraClient}
	 */
	public FedoraGetRequest getObjectProfile(TreeMap<QueryParameter, String> queryParameters) {
		getPrefix().append("?");
		setQueryParameters(queryParameters);
		return this;

	}

	/**
	 * This method lists all the datastreams that the fedora digital object has.
	 * Example query: "/objects/{pid}/datastreams ? [format] [asOfDateTime]"
	 * 
	 * @param queryParameters
	 *            a collection representing the query parameters for the
	 *            request. The possible options for query parameters are
	 *            [format] and [asOfDateTime] where format is either xml or html
	 *            and asOfDateTime would be a specific date.
	 * 
	 * @return instance of {@link FedoraGetRequest} to be executed via the
	 *         {@link FedoraClient}
	 */
	public FedoraGetRequest listDatastreams(TreeMap<QueryParameter, String> queryParameters) {
		getPrefix().append("/datastreams?");
		setQueryParameters(queryParameters);
		return this;
	}

	/**
	 * This methods retrieves a specific datastream of the fedora digital object
	 * identified by {@link #pid} Example query
	 * "/objects/demo:29/datastreams/DC?format=xml&validateChecksum=true"
	 * 
	 * @param dsid
	 *            {@link String} instance identifying the type of datastream.
	 *            This is the name value of {@link DatastreamID}
	 * @param queryParameters
	 *            a collection representing the query parameters for the
	 *            request. The possible options for query parameters are
	 *            [format],[asOfDateTime] and [validateChecksum] where format is
	 *            either xml or html,asOfDateTime would be a specific date, and
	 *            validateChecksum takes a boolean value
	 * 
	 * @return instance of {@link FedoraGetRequest} to be executed via the
	 *         {@link FedoraClient}
	 */

	public FedoraGetRequest getDatastream(String dsid, TreeMap<QueryParameter, String> queryParameters) {
		getPrefix().append("/datastreams/").append(dsid).append("?");
		setQueryParameters(queryParameters);
		return this;

	}

	/**
	 * This method gets the history for a specific datastream within a specific
	 * fedora object from the repository. Example query :
	 * "/objects/changeme:1/datastreams/DC/history?format=xml"
	 * 
	 * @param dsid
	 *            {@link String} instance identifying the type of datastream.
	 *            This is the name value of {@link DatastreamID}
	 * @return instance of {@link FedoraGetRequest} to be executed via the
	 *         {@link FedoraClient}
	 */
	public FedoraGetRequest getDatastreamHistory(String dsid) {
		getPrefix().append("/datastreams/").append(dsid).append("/history?");
		setQueryParameters(null);
		return this;
	}

	/**
	 * This method retrieves all the datastreams from of a specific fedora
	 * digital object. There is also an option to include the objects profile.
	 * Example query
	 * "/objects/demo:35/datastreams?profiles=true&asOfDateTime=2012-08-03T10:02:00.169Z"
	 * 
	 * @param queryParameters
	 *            a collection representing the query parameters for the
	 *            request. The possible options for query parameters are
	 *            [profiles],[asOfDateTime] where profiles expects a boolean
	 *            value and asOfDateTime would be a specific date
	 * 
	 * @return instance of {@link FedoraGetRequest} to be executed via the
	 *         {@link FedoraClient}
	 */

	public FedoraGetRequest getDatastreams(TreeMap<QueryParameter, String> queryParameters) {
		getPrefix().append("/datastreams?");
		setQueryParameters(queryParameters);
		return this;
	}

	/**
	 * This method retrieves the entire fedora digital object in XML format
	 * specific to fedora called FOXML. The objects entire profile and all of
	 * it's datastreams are represented within this XML. Example query
	 * "/objects/demo:29/objectXML"
	 * 
	 * @return instance of {@link FedoraGetRequest} to be executed via the
	 *         {@link FedoraClient}
	 */
	public FedoraGetRequest getObjectXML() {
		getPrefix().append("/objectXML");
		return this;
	}

	/**
	 * This method uses the query parameters built up from the above fedora
	 * methods and appends them to the end of the query. This method is called
	 * at the end of each of the methods in order to build the request in it's
	 * entirety. Given that the format is never specified throughout the
	 * requests using the query parameters, this method appends a default format
	 * of XML to each of the requests.
	 */
	private void processParameters() {
		for (QueryParameter key : queryParameters.keySet()) {
			getRequest().append(key.getDescription()).append("=").append(getQueryParameters().get(key)).append("&");
		}
		if (!queryParameters.containsKey(QueryParameter.RESULT_FORMAT)) {
			getRequest().append("format=xml");
		} else {
			getRequest().delete(getRequest().length() - 1, getRequest().length());
		}

	}

}
