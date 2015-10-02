package exhibitions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;





import search.FedoraCommunicator;
import common.controller.Controller;
import common.fedora.DublinCore;
import common.fedora.FedoraClient;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
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
	
	private String exhibitions(HttpServletRequest request,HttpServletResponse response) throws FedoraException{

		String result = "";
		ExhibitionService service= new ExhibitionService();
		HttpSession session = request.getSession();
		
		List<Exhibition> allExhibitions = new ArrayList<Exhibition>();
		if (request.getParameter("view_all_exhibitions") != null) 
		{
			String action = request.getParameter("view_all_exhibitions");
			if (action.equals("View All Exhibitions")) 
			{
				allExhibitions=service.listExhibitions();
				
				request.setAttribute("all_exhibitions", allExhibitions);
				response.setContentType("Exhibition");
				result = "WEB-INF/frontend/exhibitions/exhibition.jsp";
			}
		}
		
		if (request.getParameter("selectedExhibit") != null) 
		{
			
			String selectedExhibit = request.getParameter("selectedExhibit");
			int exhibitionId = Integer.parseInt(request.getParameter("selectedExhibit"));
			
			Exhibition exhibition=service.getExhibition(exhibitionId);
			String title=exhibition.getTitle();
			String description=exhibition.getDescription();
			int templateid=exhibition.getTemplateid();
			String creator=exhibition.getCreator();
			String[] images= exhibition.getMedia().split("%");
			for (int j=0;j<images.length;j++)
			{
				images[j]=images[j].trim();
				if(images[j].equals("")){
					images[j]=null;
				}
				else{
					images[j]="http://localhost:8080/fedora/objects/"+images[j]+"/datastreams/IMG/content";
				}
			}
			
			// images = allExhibitions.get(exhibitionId).getMedia();
			//images[i]="http://localhost:8080/fedora/objects/sq:15/datastreams/IMG/content";
			String[] captions= exhibition.getCaptions().split("%");
			
			
			request.setAttribute("images", images);
			request.setAttribute("ExhibitionTitle",title);
			request.setAttribute("ExhibitionDescription",description);
			request.setAttribute("ExhibitionCreator",creator);
			
			request.setAttribute("PopulatedTemplateArray",images); 
			session.setAttribute("CAPTIONS",captions);
			response.setContentType("text/html");
			if(templateid==1)
			{
				result = "WEB-INF/frontend/exhibitions/createViewExhibition.jsp";
			}
			else if (templateid==2)
			{
				result = "WEB-INF/frontend/exhibitions/createViewExhibition2.jsp";
			}
			
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
		
		if (request.getParameter("selectedTemplate")!=null ){
			try {	
					String[] cart= new String[20];
					FedoraCommunicator fc= new FedoraCommunicator();

					/*FedoraDigitalObject ob=fc.populateFedoraDigitalObject("KEL:1");
					FedoraDigitalObject ob2=fc.populateFedoraDigitalObject("KEL:2");
					cart[0]=ob.getDatastreams().get("IMG").getContent(); 
					cart[1]=ob2.getDatastreams().get("IMG").getContent(); 
					cart[2]="http://localhost:8080/fedora/objects/KEL:3/datastreams/IMG/content";
					cart[3]="http://localhost:8080/fedora/objects/KEL:4/datastreams/IMG/content";
					cart[4]="http://localhost:8080/fedora/objects/KEL:5/datastreams/IMG/content";
					cart[5]="http://localhost:8080/fedora/objects/KEL:6/datastreams/IMG/content";
					cart[6]="http://localhost:8080/fedora/objects/KEL:7/datastreams/IMG/content";
					cart[7]="http://localhost:8080/fedora/objects/KEL:8/datastreams/IMG/content";
					cart[8]="http://localhost:8080/fedora/objects/KEL:9/datastreams/IMG/content";
					cart[9]="http://localhost:8080/fedora/objects/KEL:10/datastreams/IMG/content";
					cart[10]="http://localhost:8080/fedora/objects/KEL:11/datastreams/IMG/content";
					cart[11]="http://localhost:8080/fedora/objects/KEL:12/datastreams/IMG/content";
					cart[12]="http://localhost:8080/fedora/objects/KEL:13/datastreams/IMG/content";*/
					cart[0]="KEL:3";
					cart[1]="KEL:2";
					cart[2]="KEL:3";
					cart[3]="KEL:4";
					cart[4]="KEL:5";
					cart[5]="KEL:6";
					cart[6]="KEL:7";
					cart[7]="KEL:8";
					cart[8]="KEL:9";
					cart[9]="KEL:10";
					cart[10]="KEL:11";
					cart[11]="KEL:12";
					cart[12]="KEL:13";	
					session.setAttribute("MEDIA_CART", cart);
					String selectedTemplate=request.getParameter("selectedTemplate");
					if(selectedTemplate.equals("1")){
						session.setAttribute("TEMPLATE_ID",selectedTemplate);
						result = "WEB-INF/frontend/exhibitions/populateTemplate1.jsp";
					}
					
					else {
						session.setAttribute("TEMPLATE_ID",selectedTemplate);
						result = "WEB-INF/frontend/exhibitions/populateTemplate2.jsp";
					}
					
				
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (request.getParameter("exhibition_det")!=null)
		{
			String action=request.getParameter("exhibition_det");
			if(action.equals("Save"))
			{
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
				int templateid=Integer.parseInt((String)session.getAttribute("TEMPLATE_ID"));
				
				String mediaString=(String) session.getAttribute("POPULATED_TEMPLATE");
				String[] media=new String[12];
				media=service.processTemplate(mediaString);
				
				String exmediaString="";
				for(int i=0;i<media.length;i++)
				{
					if(media[i]!=null)
					{
						exmediaString=exmediaString+media[i]+" %";
					}
					else{
						exmediaString=exmediaString+" %";
					}
				}
				String excaptionsString="";
				for(int i=0;i<captions.length;i++)
				{
						excaptionsString=excaptionsString+captions[i]+" %";
				}
				
				/*request.setAttribute("PopulatedTemplateArray",media);
				request.setAttribute("ExhibitionTitle",exhibitionTitle);
				request.setAttribute("ExhibitionDescription",exhibitionDescription);
				request.setAttribute("ExhibitionCreator",exhibitionCreator);
				request.setAttribute("ExhibitionViewable",exhibitionViewable);
				
				session.setAttribute("TITLE",exhibitionTitle);
				session.setAttribute("DESCRIPTION",exhibitionDescription);
				session.setAttribute("CREATOR",exhibitionCreator);
				session.setAttribute("VIEWABLE",exhibitionViewable);
				
				
				session.setAttribute("POPULATED_TEMPLATE_ARRAY",media);
				result = "WEB-INF/frontend/exhibitions/createViewExhibition.jsp";*/
				
				//save the exhibition
				Integer id=service.saveExhibition(new Exhibition(exhibitionTitle,exhibitionDescription,templateid,exhibitionCreator,exmediaString,excaptionsString));
				
				if(session.getAttribute("TEMPLATE_ID").equals("1")){
				
					Exhibition exhibition=service.getExhibition(id);
					String title=exhibition.getTitle();
					String description=exhibition.getDescription();
					String creator=exhibition.getCreator();
					String[] images= exhibition.getMedia().split("%");
					for (int j=0;j<images.length;j++)
					{
						images[j]=images[j].trim();
						if(images[j].equals("")){
							images[j]=null;
						}
						else{
							images[j]="http://localhost:8080/fedora/objects/"+images[j]+"/datastreams/IMG/content";
						}
					}
					
					String[] captionss= exhibition.getCaptions().split("%");
					request.setAttribute("images", images);
					request.setAttribute("ExhibitionTitle",title);
					request.setAttribute("ExhibitionDescription",description);
					request.setAttribute("ExhibitionCreator",creator);
					
					request.setAttribute("PopulatedTemplateArray",images); 
					session.setAttribute("CAPTIONS",captionss);
					result = "WEB-INF/frontend/exhibitions/createViewExhibition.jsp";//View before saving
				}
				
				else if(session.getAttribute("TEMPLATE_ID").equals("2")){
					Exhibition exhibition=service.getExhibition(id);
					String title=exhibition.getTitle();
					String description=exhibition.getDescription();
					String creator=exhibition.getCreator();
					String[] images= exhibition.getMedia().split("%");
					for (int j=0;j<images.length;j++)
					{
						images[j]=images[j].trim();
						if(images[j].equals("")){
							images[j]=null;
						}
						else{
							images[j]="http://localhost:8080/fedora/objects/"+images[j]+"/datastreams/IMG/content";
						}
					}
					
					String[] captionss= exhibition.getCaptions().split("%");
					request.setAttribute("images", images);
					request.setAttribute("ExhibitionTitle",title);
					request.setAttribute("ExhibitionDescription",description);
					request.setAttribute("ExhibitionCreator",creator);
					
					request.setAttribute("PopulatedTemplateArray",images); 
					session.setAttribute("CAPTIONS",captionss);
					//View before saving
					result = "WEB-INF/frontend/exhibitions/createViewExhibition2.jsp";//View before saving	
				}
			}
			else if(action.equals("Back"))
			{
				result = "WEB-INF/frontend/exhibitions/createExhibition.jsp";
			}
		}
	
		
		return result;
	}

}
