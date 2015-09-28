package common.fedora;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class UploadClient {
	
	 	protected static final Set<String> SUPPORTED_PROTOCOLS = new HashSet<String>(Arrays.asList("http", "https"));
	    protected static final Set<String> CONTENT_METHODS = new HashSet<String>(Arrays.asList("PUT", "POST" ));
	    protected static final Set<String> SUPPORTED_METHODS = new HashSet<String>(Arrays.asList("GET", "PUT", "POST"));

	    //fedora components
	    protected String protocol;
	    protected String domain;
	    protected String port;
	    protected String context;
	    //authentication
	    protected String username;
	    protected String password;
	    protected String realm;
	   
	    protected String user_agent_header = null;
	    protected int max_redirects = 0;
	    
	   
	    public UploadClient(String repository_url, String username,String password, String realm)throws IllegalArgumentException {
	        setRepositoryURL(repository_url);
	        setAuthentication(username, password, realm);
	    }

	    
	    public String getAuthPassword() { return password; }
	    public String getAuthRealm() { return realm; }
	    public String getAuthUser() { return username; }
	    public String getContext() { return context; }
	    public String getDomain() { return domain; }
	    public int getMaxRedirects() { return max_redirects; }
	    public String getPort() { return port; }
	    public String getProtocol() { return protocol; }

	    
	    public String getRepositoryURL() {
	        return protocol + "://" + domain + ":" +port + "/" + context;
	    }


	    public boolean isContentMethod(String method_name) {
	        return CONTENT_METHODS.contains(method_name);
	    }

	  
	    public boolean isSupportedMethod(String method_name) {
	        return SUPPORTED_METHODS.contains(method_name);
	    }

	    
	    public boolean isSupportedProtocol(String protocol) {
	        return SUPPORTED_PROTOCOLS.contains(protocol);
	    }

	    
	    public void setAuthentication(String username, String password) {
	        setAuthUsername(username);
	        setAuthPassword(password);
	        setAuthRealm(null);
	    }

	    
	    public void setAuthentication(String username, String password,
	                                  String realm) {
	        setAuthUsername(username);
	        setAuthPassword(password);
	        setAuthRealm(realm);
	    }
	    
	    
	    public void setAuthPassword(String password) {
	        this.password = password;
	    }

	    
	    public void setAuthRealm(String realm) {
	        if ( realm == null ) {
	            this.realm = AuthScope.ANY_REALM;
	        } 
	        else {
	            this.realm = realm;
	        }
	    }

	        
	    public void setAuthUsername(String username) {
	        this.username = username;
	    }

	    
	    public void setContext(String tcontext) {
	        if ( tcontext.startsWith("/") ) { 
	            tcontext = tcontext.substring(1);
	        }
	        if ( tcontext.endsWith("/") ) {    
	            tcontext = tcontext.substring(0, tcontext.length()-1);
	        }
	        context = tcontext; 
	    }

	   
	    public void setDomain(String domain_name_or_url) {
	        int found = domain_name_or_url.indexOf("://");
	        if ( found > -1 ) {
	            int start = found+3;
	            String domain_ = domain_name_or_url.substring(start);
	            int end = domain_name_or_url.length();
	            found = domain_.indexOf(":");
	            if ( found > -1 ) {
	                end = start + found;
	            }
	            domain = domain_name_or_url.substring(start, end);
	        } else {
	            domain = domain_name_or_url;
	        }
	    }

	    public void setMaxRedirects(int redirects_to_follow) {
	         max_redirects = redirects_to_follow;
	    }

	    
	    public void setPort(int portnum) {
	        port = String.valueOf(portnum);
	    }

	          
	    public void setPort(String tport){
	        port = tport;
	    }

	    
	    public void setProtocol(String aprotocol) throws IllegalArgumentException {
	        protocol = aprotocol;
	     }

	    
	    public void setRepositoryURL(String url)throws IllegalArgumentException {

	        /* extract the protocol */
	        int found = url.indexOf("://");
	        if ( found > -1 ) {
	            setProtocol(url.substring(0, found));
	            url = url.substring(found+3);
	        } else {
	            throw new IllegalArgumentException("URL must be complete, beginning with valid protocol.");
	        }

	        /* extract the domain */
	        found = url.indexOf(":");
	        if ( found > -1 ) {
	            setDomain(url.substring(0, found));
	            url = url.substring(found+1);
	        } else {
	            throw new IllegalArgumentException("URL must be complete, including valid domain and port.");
	        }

	        /* extract the port number */
	        found = url.indexOf("/");
	        if ( found > -1 ) {
	            setPort(url.substring(0, found));
	            url = url.substring(found);
	        } else {
	            setPort(url);
	            url = "";
	        }

	        /* extract the repository context */
	        if ( ! url.equals("") ) {
	            found = url.indexOf("?");
	            if ( found > -1 ) {
	                throw new IllegalArgumentException("URL appears to contain a query, use the URL of the repository context instead.");
	            }
	            setContext(url);
	        } else {
	            throw new IllegalArgumentException("URL must be complete, including repository context.");
	        }
	    }

	   
	    /**
	     * Issues an HTTP GET request for the specified URI. 
	     * This method does not follow redirects.
	     *
	     * @param  query_uri
	     *         URI relative to the Fedora webapp (e.g. "/objects/demo:10").
	     *
	     * @return  HttpMethod
	     *          response returned by the HTTP GET request.
	     *
	     * @throws  IOException
	     *          whenever the maximum number of redirects is exceeded or
	     *          a redirect location is an invalid URL.
	     * @throws  Exception
	     *          uncaught exceptions may occasionally be thrown by the
	     *          org.apache.commons.httpclient package,
	     */
	    public HttpMethod GET(String query_uri)throws Exception {
	        HttpMethod http_method = getMethod("GET", query_uri);
	        HttpClient http_client = getClient();
	        http_client.executeMethod(http_method);
	        if (max_redirects > 0) {
	            Header location_header;
	            String follow_url;
	            int status_code = http_method.getStatusCode();
	            int num_redirects = 0;
	            while ( status_code >= 300 && status_code <= 399 &&
	                    num_redirects < max_redirects) {
	                location_header = http_method.getResponseHeader("location");
	                if ( location_header != null ) {
	                    follow_url = location_header.getValue();
	                    http_method = new GetMethod(follow_url);
	                    setMethodParameters(http_method);
	                    http_client.executeMethod(http_method);
	                    status_code = http_method.getStatusCode();
	                    num_redirects++;
	                } else {
	                    throw new IOException("HTTP Resquest status suggests redirect but no location header was supplied.");
	                }
	            }
	            if ( status_code >= 300 && status_code <= 399 ) {
	                throw new IOException("Maximum number of redirects exceeded.");
	            }
	        }
	        return http_method;
	    }

	    /**
	     * Issues an HTTP POST request with no content.
	     *
	     * @param query_uri
	     *        URI relative to the Fedora webapp (e.g. "/objects/demo:10").
	     *
	     * @return  HttpMethod
	     *          response returned by the HTTP POST request.
	     *
	     * @throws Exception
	     *         uncaught exceptions may occasionally be thrown by the
	     *         org.apache.commons.httpclient package,
	     */
	    public HttpMethod POST(String query_uri) throws Exception {
	        return submitRequest("POST", query_uri);
	    }

	    /**
	     * Issues an HTTP POST request with attached XML string content.
	     *
	     * @param query_uri
	     *        URI relative to the Fedora webapp (e.g. "/objects/demo:10").
	     * @param xml_string
	     *        XML string to be attached to the request.
	     *
	     * @return  HttpMethod
	     *          response returned by the HTTP POST request.
	     *
	     * @throws Exception
	     *         uncaught exceptions may occasionally be thrown by the
	     *         org.apache.commons.httpclient package,
	     */
	    public HttpMethod POST(String query_uri, String xml_string)throws Exception {
	        return submitRequest("POST", query_uri, xml_string);
	    }

	    /**
	     * Issues an HTTP POST request with attached content entity.
	     *
	     * @param query_uri
	     *        URI relative to the Fedora webapp (e.g. "/objects/demo:10").
	     * @param file_content
	     *        file to be attached to the request.
	     * @param chunked
	     *        indicates whether content in <code>request_entity</code> is
	     *        chunked or not.
	     *
	     * @return  HttpMethod
	     *          response returned by the HTTP POST request.
	     *
	     * @throws Exception
	     *         uncaught exceptions may occasionally be thrown by the
	     *         org.apache.commons.httpclient package,
	     */
	    public HttpMethod POST(String query_uri, File file_content, boolean chunked) throws Exception {
	        return submitRequest("POST", query_uri, file_content, chunked);
	    }



	    
	    public HttpMethod PUT(String query_uri, String xml_string)throws Exception {
	        return submitRequest("PUT", query_uri, xml_string);
	    }

	    

	    
	    protected HttpClient getClient() {
	        HttpClient http_client = new HttpClient();
	        handleAuthentication(http_client);
	        return http_client;
	    }

	    /**
	     * creates an HTTP method object.
	     *
	     * @param  method_name
	     *         name of the HTTP method to be created. Must be on of
	     *         "GET", "PUT", "POST" or "DELETE".
	     * @param  query_uri
	     *         URI containing the query string for the request.
	     *
	     * @return HttpMethod
	     *         an HttpMethod object of the appropriate type.
	     *
	     * @throws IllegalArgumentException
	     *         if <code>method_name</code> is not one of "GET", "PUT",
	     *         "POST" or "DELETE".
	     */
	    protected HttpMethod getMethod(String method_name, String query_uri)throws IllegalArgumentException {
	        String request_url = getRequestURL(query_uri);
	        HttpMethod http_method;
	        if (method_name.equals("GET")) {
	            http_method = new GetMethod(request_url);
	        } 
	        else if (method_name.equals("PUT")) {
	            http_method = new PutMethod(request_url);
	        }
	        else if (method_name.equals("POST")) {
	            http_method = new PostMethod(request_url);
	        } 
	        else if (method_name.equals("DELETE")) {
	            http_method = new DeleteMethod(request_url);
	        } 
	        else {
	            throw new IllegalArgumentException("Supported method types are GET, PUT and POST");
	        }
	        setMethodParameters(http_method);
	        return http_method;
	    }

	    
	    protected String getRequestURL(String query_uri) {
	        if ( query_uri.startsWith("/") ) {
	            return getRepositoryURL() + query_uri;
	        } 
	        else {
	            return getRepositoryURL() + "/" + query_uri;
	        }
	    }

	    
	    protected void handleAuthentication(HttpClient http_client) {
	        if ( username != null && password != null ) {
	            AuthScope auth_scope =
	                new AuthScope(domain, Integer.parseInt(port),
	                              realm);
	            UsernamePasswordCredentials credentials =
	                new UsernamePasswordCredentials(username, password);
	            http_client.getState().setCredentials(auth_scope, credentials);
	            http_client.getParams().setAuthenticationPreemptive(true);
	        }
	    }

	    
	    protected void setMethodParameters(HttpMethod http_method) {
	        http_method.getParams().setParameter("Connection", "Keep-Alive");
	        if (user_agent_header != null) {
	            http_method.setRequestHeader("User-Agent", user_agent_header);
	        }
	        if ( username != null && password != null ) {
	            http_method.setDoAuthentication(true);
	        }
	    }

	    
	    protected HttpMethod submitRequest(String method_name, String query_uri)
	              throws IllegalArgumentException, Exception {
	        HttpMethod http_method = getMethod(method_name, query_uri);
	        HttpClient http_client = getClient();
	        http_client.executeMethod(http_method);
	        return http_method;
	    }

	    
	    protected HttpMethod submitRequest(String method_name, String query_uri, String xml_string)
	              throws IllegalArgumentException, Exception {
	        if ( isContentMethod(method_name) ) {
	            EntityEnclosingMethod http_method =
	                (EntityEnclosingMethod) getMethod(method_name, query_uri);
	            http_method.setRequestEntity(new StringRequestEntity(xml_string,
	                                             "text/xml", "utf-8"));
	            http_method.setContentChunked(false);
	            HttpClient http_client = getClient();
	            http_client.executeMethod(http_method);
	            return http_method;
	        } else {
	            String msg = method_name +
	                         " does not support passing content entities.";
	            throw new IllegalArgumentException(msg);
	        }
	    }

	    
	    protected HttpMethod submitRequest(String method_name, String query_uri, File file_content, boolean chunked) throws IllegalArgumentException, Exception {
	        if ( isContentMethod(method_name) ) {
	            EntityEnclosingMethod http_method =
	                (EntityEnclosingMethod) getMethod(method_name, query_uri);
	            Part[] parts = { new StringPart("param_name", "value"),
	                             new FilePart(file_content.getName(), file_content) };
	            http_method.setRequestEntity(new MultipartRequestEntity(parts,
	                                                     http_method.getParams()));
	            http_method.setContentChunked(chunked);
	            HttpClient http_client = getClient();
	            http_client.executeMethod(http_method);
	            return http_method;
	        } else {
	            String msg = method_name +
	                         " does not support passing content entities.";
	            throw new IllegalArgumentException(msg);
	        }
	    }

}
