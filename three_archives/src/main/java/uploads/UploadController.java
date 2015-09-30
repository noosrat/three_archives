package uploads;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.Controller;



public class UploadController implements Controller{
	
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		if (request.getPathInfo().substring(1).contains("redirect_uploads")) 
		{
			
			return "WEB-INF/frontend/Uploads/UploadHome.jsp";
		}
		return uploads(request, response);
		
	}
	
	private String uploads(HttpServletRequest request,HttpServletResponse response){

		String result = "";		
		
		if (request.getParameter("upload_files") != null) 
		{
			String action = request.getParameter("actions");
			String file_path=request.getParameter("storage_path");
			String archive=request.getParameter("archive");
			UploadService uploadService=new UploadService();
			uploadService.upload(action,file_path,archive);
			//call method from upload services to add the files to fedora
			
			
			result = "WEB-INF/frontend/Uploads/uploadItems.jsp";
			
		}
		
		if (request.getParameter("upload_more_files") != null) 
		{
			result = "WEB-INF/frontend/Uploads/UploadHome.jsp";
			
		}
		
		return result;
		
	}
	
	

}
