/*Edited by Nicole Petersen
Description: Class to interact with Fedora*/package common.fedora;

import java.io.File;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
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
	    //constructor
	    public UploadClient(String repository_url, String username,String password, String realm)throws IllegalArgumentException {
	        setRepositoryURL(repository_url);
	        setAuthentication(username, password, realm);
	    }
	    //getters and setters

	   
	
	    
	    public String getRepositoryURL() {
	        return protocol + "://" + domain + ":" +port + "/" + context;
	    }


	    public void setAuthentication(String username, String password) {
	        setAuthUsername(username);
	        setAuthPassword(password);
	        setAuthRealm(null);
	    }

	    public void setAuthentication(String username, String password,String realm) {
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

	      
	    public HttpMethod POST(String query_uri) throws Exception {
	        return submitRequest("POST", query_uri);
	    }
  
	    public HttpMethod POST(String query_uri, String xml_string)throws Exception {
	        return submitRequest("POST", query_uri, xml_string);
	    }

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
	            AuthScope auth_scope =new AuthScope(domain, Integer.parseInt(port),realm);
	            UsernamePasswordCredentials credentials =new UsernamePasswordCredentials(username, password);
	            http_client.getState().setCredentials(auth_scope, credentials);
	            http_client.getParams().setAuthenticationPreemptive(true);  
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

	    
	    protected HttpMethod submitRequest(String method_name, String query_uri)throws IllegalArgumentException, Exception {
	        HttpMethod http_method = getMethod(method_name, query_uri);
	        HttpClient http_client = getClient();
	        http_client.executeMethod(http_method);
	        return http_method;
	    }

	    
	    protected HttpMethod submitRequest(String method_name, String query_uri, String xml_string)throws IllegalArgumentException, Exception {
	            EntityEnclosingMethod http_method =(EntityEnclosingMethod) getMethod(method_name, query_uri);
	            http_method.setRequestEntity(new StringRequestEntity(xml_string,"text/xml", "utf-8"));
	            http_method.setContentChunked(false);
	            HttpClient http_client = getClient();
	            http_client.executeMethod(http_method);
	            return http_method;
	    }

	    
	    protected HttpMethod submitRequest(String method_name, String query_uri, File file_content, boolean chunked) throws IllegalArgumentException, Exception {
	            EntityEnclosingMethod http_method =(EntityEnclosingMethod) getMethod(method_name, query_uri);
	            Part[] parts = {new StringPart("param_name", "value"),new FilePart(file_content.getName(), file_content) };
	            http_method.setRequestEntity(new MultipartRequestEntity(parts,http_method.getParams()));
	            http_method.setContentChunked(chunked);
	            HttpClient http_client = getClient();
	            http_client.executeMethod(http_method);
	            return http_method;
	        
	    }
		//class to make Fedora metadata XML
	    public String makeXML(String title,String creator, String event, String publisher, String contributor, String date,String resourcetype, String format, String source, String language, String relation, String location, String rights, String collection, String cords,String subject, String annotations, String description )
	    {

	    	title ="<dc:title>"+title.trim()+"</dc:title>";

			//creator ="<dc:creator>"+creator.trim()+"</dc:creator>";
	    	creator ="<dc:creator>"+""+"</dc:creator>";
			event =event.trim();

			publisher ="<dc:publisher>"+publisher.trim()+"</dc:publisher>";
			contributor ="<dc:contributor>"+contributor.trim()+"</dc:contributor>";
			date ="<dc:date>"+date.trim()+"</dc:date>";
			resourcetype ="<dc:type>"+resourcetype.trim()+"</dc:type>";
			format ="<dc:format>"+format.trim()+"</dc:format>";
			source ="<dc:source>"+source.trim()+"</dc:source>";
			language ="<dc:language>"+language.trim()+"</dc:language>";
			relation ="<dc:relation>"+relation.trim()+"</dc:relation>";
			location =location.trim();
			rights ="<dc:rights>"+rights.trim()+"</dc:rights>";
			collection =collection.trim();
			cords =cords.trim();
			description ="<dc:description>"+collection+ "%"+event+"%"+description+"%"+annotations+"</dc:description>";
			String coverage ="<dc:coverage>"+location+"%" +cords+"</dc:coverage>";
			subject="<dc:subject>"+subject+"</dc:subject>";

			String meta=title+description+creator+publisher+contributor+date+resourcetype+format+source+language+relation+coverage+rights+subject;


			String qot="\"";
			String startTag="<oai_dc:dc xmlns:oai_dc="+qot+"http://www.openarchives.org/OAI/2.0/oai_dc/"+qot+ " xmlns:dc="+qot+"http://purl.org/dc/elements/1.1/" +qot+" xmlns:xsi="+qot+"http://www.w3.org/2001/XMLSchema-instance"+qot+" xsi:schemaLocation="+qot+"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd"+qot+">";
			String endTag="</oai_dc:dc>";
			
			return (startTag+meta+endTag);
	    
	    }

}
