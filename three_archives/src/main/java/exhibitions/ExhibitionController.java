package exhibitions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yourmediashelf.fedora.client.FedoraClientException;

import common.controller.Controller;
import common.model.Exhibition;

public class ExhibitionController implements Controller {

	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		if (request.getPathInfo().substring(1).contains("redirect_exhibitions")) 
		{
			return "WEB-INF/frontend/exhibitions/exhibitionHome.jsp";

		}
		return exhibitions(request, response);
	}
	

	private String exhibitions(HttpServletRequest request,HttpServletResponse response) throws FedoraClientException, ServletException, IOException 
	{
		String result = "";
		ExhibitionService service= new ExhibitionService();
		HttpSession session = request.getSession();
		
		Exhibition[] allExhibitions =new Exhibition[5];
		
		if (request.getParameter("view_all_exhibitions") != null) 
		{
			String action = request.getParameter("view_all_exhibitions");
			if (action.equals("View All Exhibitions")) 
			{
				allExhibitions = service.listAllExhibitions();
				request.setAttribute("all_exhibitions", allExhibitions);
				response.setContentType("Exhibition");
				result = "WEB-INF/frontend/exhibitions/exhibition.jsp";
			}
		}
		
		if (request.getParameter("selectedExhibit") != null) 
		{
			allExhibitions = service.listAllExhibitions();
			String selectedExhibit = request.getParameter("selectedExhibit");
			int exhibitionId = Integer.parseInt(request.getParameter("selectedExhibit"));
			String[] images = allExhibitions[exhibitionId].getMedia();
			request.setAttribute("images", images);
			String path = "D:\\images\\2.jpg";
			request.setAttribute("image", path);
			request.setAttribute("message", exhibitionId);
			response.setContentType("text/html");
			result = "WEB-INF/frontend/exhibitions/exhibitionViewer.jsp";
		} 
		
		if (request.getParameter("create_exhibition") != null) 
		{
			String action = request.getParameter("create_exhibition");
			if (action.equals("Create Exhibition")) 
			{
				result = "WEB-INF/frontend/exhibitions/createExhibition.jsp";
			}
		}
		
		if (request.getParameter("selectedTemplate")!=null )
		{
			String selectedTemplate=request.getParameter("selectedTemplate");
			request.setAttribute("returnedExhibitionTemplate",selectedTemplate);
			session.setAttribute("TEMPLATE_ID",selectedTemplate);
			result = "WEB-INF/frontend/exhibitions/createExhibition2.jsp";
		}
		
		if (request.getParameter("exhibition_det")!=null)
		{
			String action=request.getParameter("exhibition_det");
			if(action.equals("Next"))
			{
				
				String template=(String)session.getAttribute("TEMPLATE_ID");
				String userAction=request.getParameter("user_action");
				session.setAttribute("POPULATED_TEMPLATE",userAction);
				request.setAttribute("template",template);
				request.setAttribute("user_actions", userAction);
			
				result = "WEB-INF/frontend/exhibitions/createExhibition3.jsp";
			}
			else if(action.equals("Back"))
			{
				result = "WEB-INF/frontend/exhibitions/createExhibition.jsp";
			}
		}
		
		if (request.getParameter("submit_exhibition")!=null)
		{
			String action=request.getParameter("submit_exhibition");
			if (action.equals("Next"))
			{
				//get all the exhibition content
				//create exhibition object
				//view the newly created exhibition object
				//allow the user to save the exhibition object in the DB..
				String exhibitionTitle= request.getParameter("Title");
				String exhibitionDescription= request.getParameter("Description");
				String exhibitionCreator= request.getParameter("Creator");
				String exhibitionDate= request.getParameter("Date");
				String exhibitionViewable= request.getParameter("viewable");
				
				request.setAttribute("ExhibitionTitle",exhibitionTitle);
				request.setAttribute("ExhibitionDescription",exhibitionDescription);
				request.setAttribute("ExhibitionCreator",exhibitionCreator);
				request.setAttribute("ExhibitionDate",exhibitionDate);
				request.setAttribute("ExhibitionViewable",exhibitionViewable);
				
				result = "WEB-INF/frontend/exhibitions/createViewExhibition.jsp";
			}
			else //if (action.equals("Back"))
			{
				//go back to the create exhibition page
				//but pre-populate the template with what has already been done
				//also pre-populate the shopping cart
			}
		}
		
		else if (1 == 2) {
			// File fi = new File("D:\\images-missGay2013\\2.jpg");
			// byte[] fileContent=Files.readAllBytes(fi.toPath());
			// byte[] imageBytes = fileContent;
			String path = "D:\\images\\2.jpg";
			response.setContentType("text/html");
			request.setAttribute("image", path);
			result = "ExhibitionViewer.jsp";
			// String path=getServletContext().getRealPath(File.separator);
			// File f =new File("D:\\Exhibit2.jpg");
			// BufferedImage bi=ImageIO.read(f);
			// OutputStream out=response.getOutputStream();

			// ImageIO.write(bi, "jpg", out);
			// out.close();
		}
		return result;

	}

}
