package uploads;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import common.controller.Controller;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraClient;
import common.fedora.FedoraException;
import common.fedora.FedoraGetRequest;
import search.SearchController;
import search.SolrCommunicator;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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
		boolean isMultipart=ServletFileUpload.isMultipartContent(request);	
		String action = "";
		String archive="";
		UploadService uploadService=new UploadService();
		
		if (isMultipart)
		{
			try{
				List<FileItem> multiparts=new ServletFileUpload (new DiskFileItemFactory()).parseRequest(request);
				for (FileItem item:multiparts){
					if(item.isFormField()){
						String name=item.getFieldName();
						String value=item.getString();
						if (name.equals("actions"))
						{
							action=value;
						}
						else if(name.equals("archive"))
						{
								archive=value;
						}
						System.out.println(name+" "+value);
					}
					else{
						String name=new File(item.getName()).getName();
						item.write(new File(name));
					}
				}
				uploadService.upload(action,archive);
				System.out.println("* uploads completed");
				//call method from upload services to add the files to fedora
				//fedora stuff to update index
				SolrCommunicator.updateSolrIndex();
				//we do the below to refresh the images in the application
				request.getSession().setAttribute("objects", SearchController.getSearch().findFedoraDigitalObjects("*"));
				//we need to refresh autocomplete
					new AutoCompleteUtility().refreshAllAutocompleteFiles();
					result = "WEB-INF/frontend/Uploads/uploadItems.jsp";
					
			}
			catch(Exception e){
				System.out.println("ERROR: "+ e);
			}
			
			
		}
		
		
		if (request.getParameter("upload_more_files") != null) 
		{
			result = "WEB-INF/frontend/Uploads/UploadHome.jsp";
			
		}
		
		return result;
		
	}

}
