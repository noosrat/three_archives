package uploads;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import common.controller.Controller;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraClient;
import common.fedora.FedoraException;
import common.fedora.FedoraGetRequest;
import search.SearchController;



public class UploadController implements Controller{
	
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		if (request.getPathInfo().substring(1).contains("redirect_uploads")) 
		{
			
			return "WEB-INF/frontend/Uploads/UploadHome.jsp";
		}
		return uploads(request, response);
		
	}
	
	private String uploads(HttpServletRequest request,HttpServletResponse response) throws Exception{

		String result = "";		
		
		if (request.getParameter("upload_files") != null) 
		{
			String action = request.getParameter("actions");
			String file_path=request.getParameter("storage_path");
			String archive=request.getParameter("archive");
			UploadService uploadService=new UploadService();
			uploadService.upload(action,file_path,archive);
			//call method from upload services to add the files to fedora
			//fedora stuff to update index
			updateSolrIndexWithNewFedoraObjects();
			//we do the below to refresh the images in the application
			request.getSession().setAttribute("objects", SearchController.getSearch().findFedoraDigitalObjects("*"));
			//we need to refresh autocomplete
			HashMap<String,String> archives = new HashMap<String,String>();
			archives.put((String)request.getSession().getAttribute("ARCHIVE"), (String)request.getSession().getAttribute("MEDIA_PREFIX"));
			AutoCompleteUtility.refreshAutocompleFile(archives);
			result = "WEB-INF/frontend/Uploads/uploadItems.jsp";
			
			
			
		}
		
		if (request.getParameter("upload_more_files") != null) 
		{
			result = "WEB-INF/frontend/Uploads/UploadHome.jsp";
			
		}
		
		return result;
		
	}

	
	private void updateSolrIndexWithNewFedoraObjects() throws Exception {
		System.out.println("about to updat solr index");
		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest();
		StringBuilder query = new StringBuilder("http://localhost:8089/fedoragsearch/rest?operation=updateIndex&action=fromFoxmlFiles&value=");
		fedoraGetRequest.setRequest(query);
		try {
			FedoraClient.executeWithoutParsingResponse(fedoraGetRequest);
		} catch (FedoraException e) {
			System.out.println(e);
			throw new Exception("Unable to update the index after uploading new items.  Please report to IT",e);
		}
		System.out.println("Successfully updated index with new objects");
	}
	

}
