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
import search.SolrCommunicator;



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
		/*boolean isMultipart=ServletFileUpload.isMultipartContent(request);		
		if (isMultipart)
		{
			DiskFileItem factory= new DiskFileItemFactory();
			ServletContext servletContext = this.getServletConfig().getServletContext(); 
			File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir"); 
			factory.setRepository(repository);
			ServletFileUpload upload = new ServletFileUpload(factory); 
			// Parse the request List<FileItem> items = upload.parseRequest(request);


			// Process the uploaded items 
			Iterator<FileItem> iter = items.iterator(); 
			while (iter.hasNext()) { 
				FileItem item = iter.next(); 
				if (item.isFormField()) 
				{
					String name = item.getFieldName();
					String value = item.getString();
				} 
				else 
				{ 
					String fieldName = item.getFieldName(); 
					String fileName = item.getName(); 
					String contentType = item.getContentType(); 
					boolean isInMemory = item.isInMemory(); 
					long sizeInBytes = item.getSize();
				} 
			}
		}
		*/
		if (request.getParameter("upload_files") != null) 
		{
			String action = request.getParameter("actions");
			String file_path=request.getParameter("storage_path");
			String archive=request.getParameter("archive");
			UploadService uploadService=new UploadService();
			uploadService.upload(action,file_path,archive);
			System.out.println("* uploads completed");
			//call method from upload services to add the files to fedora
			//fedora stuff to update index
			SolrCommunicator.updateSolrIndex();
//			//we do the below to refresh the images in the application
			request.getSession().setAttribute("objects", SearchController.getSearch().findFedoraDigitalObjects("*"));
//			//we need to refresh autocomplete
			new AutoCompleteUtility().refreshAllAutocompleteFiles();
			result = "WEB-INF/frontend/Uploads/uploadItems.jsp";
			
		}
		
		if (request.getParameter("upload_more_files") != null) 
		{
			result = "WEB-INF/frontend/Uploads/UploadHome.jsp";
			
		}
		
		return result;
		
	}

}
