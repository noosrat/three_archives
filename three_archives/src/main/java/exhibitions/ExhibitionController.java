package exhibitions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.Controller;
import common.model.Exhibition;

public class ExhibitionController implements Controller {
	
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		if (request.getPathInfo().substring(1).contains("redirect_exhibitions")) 
		{
			HttpSession session = request.getSession();
			session.setAttribute("ARCHIVE", "seqiuns");
		
			return "WEB-INF/frontend/exhibitions/exhibitionHome.jsp";

		}
		return exhibitions(request, response);
	}
	
	private String exhibitions(HttpServletRequest request,
			HttpServletResponse response){

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
			String[] images= new String[10];
			 images = allExhibitions[exhibitionId].getMedia();
			for (int i=0;i<10;i++)//TODO Remove 
			{
				images[i]="http://localhost:8080/fedora/objects/sq:15/datastreams/IMG/content";
				
			}
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
				result = "WEB-INF/frontend/exhibitions/ExhibitionLogin.jsp";
			}
		}
		
		if (request.getParameter("login_to_create_exhibition") != null) 
		{
			String name= request.getParameter("name");
			String password= request.getParameter("password");
			String proceed="APPROVE"; //TODO Process user id and password
			if (proceed.equals("APPROVE")) 
			{
				result = "WEB-INF/frontend/exhibitions/createExhibition.jsp";
			}
			else
			{
				return "WEB-INF/frontend/exhibitions/exhibitionHome.jsp";
			}	
			
		}
		
		if (request.getParameter("selectedTemplate")!=null )
		{
			String selectedTemplate=request.getParameter("selectedTemplate");
			if(selectedTemplate.equals("1"))
			{
				session.setAttribute("TEMPLATE_ID",selectedTemplate);
				result = "WEB-INF/frontend/exhibitions/populateTemplate1.jsp";
			}
			
			else 
			{
				session.setAttribute("TEMPLATE_ID",selectedTemplate);
				result = "WEB-INF/frontend/exhibitions/populateTemplate2.jsp";
			}
			
			//session.setAttribute("TEMPLATE_ID",selectedTemplate);
			//result = "WEB-INF/frontend/exhibitions/createExhibition2.jsp";
		}
		
		if (request.getParameter("exhibition_det")!=null)
		{
			String action=request.getParameter("exhibition_det");
			if(action.equals("Next"))
			{
				
				//Get image captions
				//Get the captions as well
				String [] captions=new String[12];
				if(!(request.getParameter("input_cap0").equals(""))){captions[0]=request.getParameter("input_cap0");}
				if(!(request.getParameter("input_cap1").equals(""))){captions[1]=request.getParameter("input_cap1");}
				if(!(request.getParameter("input_cap2").equals(""))){captions[2]=request.getParameter("input_cap2");}
				if(!(request.getParameter("input_cap3").equals(""))){captions[3]=request.getParameter("input_cap3");}
				if(!(request.getParameter("input_cap4").equals(""))){captions[4]=request.getParameter("input_cap4");}
				if(!(request.getParameter("input_cap5").equals(""))){captions[5]=request.getParameter("input_cap5");}
				if(!(request.getParameter("input_cap6").equals(""))){captions[6]=request.getParameter("input_cap6");}
				if(!(request.getParameter("input_cap7").equals(""))){captions[7]=request.getParameter("input_cap7");}
				if(!(request.getParameter("input_cap8").equals(""))){captions[8]=request.getParameter("input_cap8");}
				if(!(request.getParameter("input_cap9").equals(""))){captions[9]=request.getParameter("input_cap9");}
				if(!(request.getParameter("input_cap10").equals(""))){captions[10]=request.getParameter("input_cap10");}
				if(!(request.getParameter("input_cap11").equals(""))){captions[11]=request.getParameter("input_cap11");}
				
				session.setAttribute("CAPTIONS", captions);
				String userAction=request.getParameter("user_action");
				session.setAttribute("POPULATED_TEMPLATE",userAction);
				
				//get all the exhibition content
				//create exhibition object
				//view the newly created exhibition object
				//allow the user to save the exhibition object in the DB..
				String exhibitionTitle= request.getParameter("Title");
				String exhibitionDescription= request.getParameter("Description");
				String exhibitionCreator= request.getParameter("Creator");
				String exhibitionViewable= request.getParameter("viewable");
				
				String mediaString=(String) session.getAttribute("POPULATED_TEMPLATE");
				String[] media=new String[12];
				//media=service.processTemplate(mediaString);
				
				for (int i=0;i<12;i++)//TODO Remove 
				{
					media[i]="http://localhost:8080/fedora/objects/sq:15/datastreams/IMG/content";
					
				}
				request.setAttribute("PopulatedTemplateArray",media);
				
				request.setAttribute("ExhibitionTitle",exhibitionTitle);
				request.setAttribute("ExhibitionDescription",exhibitionDescription);
				request.setAttribute("ExhibitionCreator",exhibitionCreator);
				request.setAttribute("ExhibitionViewable",exhibitionViewable);
				
				session.setAttribute("TITLE",exhibitionTitle);
				session.setAttribute("DESCRIPTION",exhibitionDescription);
				session.setAttribute("CREATOR",exhibitionCreator);
				session.setAttribute("VIEWABLE",exhibitionViewable);
				session.setAttribute("POPULATED_TEMPLATE_ARRAY",media);
				result = "WEB-INF/frontend/exhibitions/createViewExhibition.jsp";
				
				if(session.getAttribute("TEMPLATE_ID").equals("1")){
					result = "WEB-INF/frontend/exhibitions/createViewExhibition.jsp";//View before saving
				}
				
				else if(session.getAttribute("TEMPLATE_ID").equals("2")){
					result = "WEB-INF/frontend/exhibitions/createViewExhibition2.jsp";//View before saving	
				}
			}
			else if(action.equals("Back"))
			{
				result = "WEB-INF/frontend/exhibitions/createExhibition.jsp";
			}
		}
		
		
		if(request.getParameter("saved_exhibition")!=null)
		{
			String action = request.getParameter("save_exhibition");
			if(action.equals("save"))
			{
				//create exhibition object
				String title=(String) session.getAttribute("TITLE");
				int exID=1;//TODO deal with exhibition IDs!!
				String tempID=(String) session.getAttribute("TEMPLATE_ID");
				String[] media=(String [])session.getAttribute("POPULALTED_TEMPLATE_ID");
				String creator=(String)session.getAttribute("CREATOR");
				String description=(String)session.getAttribute("DESCRIPTION");
				String[] captions=(String[])session.getAttribute("CAPTIONS");
				Exhibition newExhibition= new Exhibition(title, exID, tempID, media, creator, description, captions);
				
				
				//send exhibition object to ExhibitionService
			}
			else{
				//do not save the exhibition and ho to exhibition home
			}
		}
		
		return result;
	}

}
