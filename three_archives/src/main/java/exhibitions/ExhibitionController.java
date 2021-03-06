/*Author: Nicole Petersen
Description: COntroller class for the Exhibition service
*/

package exhibitions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.Controller;
import common.fedora.FedoraException;
import common.model.Exhibition;
import common.model.ManageUsers;
import common.model.User;
import search.FedoraCommunicator;

public class ExhibitionController implements Controller {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception { // Mandatory method for a class that implements Controller
		if (request.getPathInfo().substring(1).contains("redirect_exhibitions")) { // IF the user wants to create or view exhibitions, go to the exhibition home page
			HttpSession session = request.getSession();
			
			return "WEB-INF/frontend/exhibitions/exhibitionHome.jsp";
		}
		return exhibitions(request, response);
	}

	private String exhibitions(HttpServletRequest request, HttpServletResponse response) throws FedoraException {

		String result = ""; // Specifies the path of the page that must be returned next
		ExhibitionService service = new ExhibitionService();
		HttpSession session = request.getSession();

		List<Exhibition> allExhibitions = new ArrayList<Exhibition>(); // ArrayList of all exhibitions retrieved from the database

		if (request.getParameter("authorise") != null) {
			ManageUsers userManager = new ManageUsers();
			System.out.println("AUTHORISE");// log
			String username = request.getParameter("new_username");
			System.out.println("User name " + username);
			String password = request.getParameter("new_pwd");
			System.out.println("password" );//log
			String role = userManager.approveUser(username, password);
			System.out.println("User role " + role);
			session.setAttribute("USER", role);
			return "index.jsp";
		}
		if (request.getParameter("view_all_exhibitions") != null) {// If the user wants to view an exhibition
			String action = request.getParameter("view_all_exhibitions");
			if (action.equals("View All Exhibitions")) {
				allExhibitions = service.listExhibitions();// Get all exhibitions from the database
				request.setAttribute("all_exhibitions", allExhibitions);
				response.setContentType("Exhibition");
				result = "WEB-INF/frontend/exhibitions/exhibition.jsp";
			}
		}

		if (request.getParameter("selectedExhibit") != null) {

			int exhibitionId = Integer.parseInt(request.getParameter("selectedExhibit"));// get the specific exhibition ID 
			Exhibition exhibition = service.getExhibition(exhibitionId);// get the specific exhibition from the database
			//Obtain all exhibition variables
			String title = exhibition.getTitle();
			String description = exhibition.getDescription();
			int templateid = exhibition.getTemplateid();
			String creator = exhibition.getCreator();
			String cover = exhibition.getCover();
			String border = exhibition.getBorder();
			String[] images = exhibition.getMedia().split("%");
			for (int j = 0; j < images.length; j++) {
				images[j] = images[j].trim();
				if (images[j].equals("")) {
					images[j] = null;
				} else {
					images[j] = "http://personalhistories.cs.uct.ac.za:8089/fedora/objects/" + images[j] + "/datastreams/IMG/content";
				}
			}

			String[] captions = exhibition.getCaptions().split("%");
			// Set session variables to send to the JSP
			request.setAttribute("images", images);
			request.setAttribute("ExhibitionTitle", title);
			request.setAttribute("ExhibitionDescription", description);
			request.setAttribute("ExhibitionCreator", creator);
			request.setAttribute("ExhibitionCover", cover);
			request.setAttribute("ExhibitionBorder", border);
			request.setAttribute("PopulatedTemplateArray", images);
			session.setAttribute("CAPTIONS", captions);
			response.setContentType("text/html");
			//Go to a specific viewer JSP, based on the template ID of the exhibition
			if (templateid == 1) {
				result = "WEB-INF/frontend/exhibitions/createViewExhibition.jsp";
			} else if (templateid == 2) {
				result = "WEB-INF/frontend/exhibitions/createViewExhibition2.jsp";
			} else if (templateid == 3) {
				result = "WEB-INF/frontend/exhibitions/createViewExhibition3.jsp";
			}

		}

		if (request.getParameter("create_exhibition") != null) { // If the user wants to create exhibitions
			String action = request.getParameter("create_exhibition");
			result = "WEB-INF/frontend/exhibitions/createExhibition.jsp";

		}

		if (request.getParameter("selectedTemplate") != null) {// After the user selects a template
			try {
				ArrayList<String> cart = new ArrayList<String>(); 
				String selectedTemplate = request.getParameter("selectedTemplate");// Get the template ID
				// Go to a specific creator JSP based on the template that was selected
				if (selectedTemplate.equals("1")) {
					session.setAttribute("TEMPLATE_ID", selectedTemplate);
					result = "WEB-INF/frontend/exhibitions/populateTemplate1.jsp";
				}

				else if (selectedTemplate.equals("2")) {
					session.setAttribute("TEMPLATE_ID", selectedTemplate);
					result = "WEB-INF/frontend/exhibitions/populateTemplate2.jsp";
				} else {
					session.setAttribute("TEMPLATE_ID", selectedTemplate);
					result = "WEB-INF/frontend/exhibitions/populateTemplate3.jsp";
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (request.getParameter("exhibition_det") != null) {// If the user completed the exhibition details

			String action = request.getParameter("exhibition_det");

			if (action.equals("Save")) {

				// Get the captions 
				String[] captions = new String[12];

				if (!(request.getParameter("input_cap0").equals(""))) {
					captions[0] = (String) request.getParameter("input_cap0");
				}
				if (!(request.getParameter("input_cap1").equals(""))) {
					captions[1] = (String) request.getParameter("input_cap1");
				}
				if (!(request.getParameter("input_cap2").equals(""))) {
					captions[2] = (String) request.getParameter("input_cap2");
				}
				if (!(request.getParameter("input_cap3").equals(""))) {
					captions[3] = (String) request.getParameter("input_cap3");
				}
				if (!(request.getParameter("input_cap4").equals(""))) {
					captions[4] = (String) request.getParameter("input_cap4");
				}
				if (!(request.getParameter("input_cap5").equals(""))) {
					captions[5] = (String) request.getParameter("input_cap5");
				}
				if (!(request.getParameter("input_cap6").equals(""))) {
					captions[6] = (String) request.getParameter("input_cap6");
				}
				if (!(request.getParameter("input_cap7").equals(""))) {
					captions[7] = (String) request.getParameter("input_cap7");
				}
				if (!(request.getParameter("input_cap8").equals(""))) {
					captions[8] = (String) request.getParameter("input_cap8");
				}
				if (!(request.getParameter("input_cap9").equals(""))) {
					captions[9] = (String) request.getParameter("input_cap9");
				}
				if (!(request.getParameter("input_cap10").equals(""))) {
					captions[10] = (String) request.getParameter("input_cap10");
				}
				if (!(request.getParameter("input_cap11").equals(""))) {
					captions[11] = (String) request.getParameter("input_cap11");
				}

				session.setAttribute("CAPTIONS", captions);
				String userAction = request.getParameter("user_action"); // Get the image movements (drags and drops)

				session.setAttribute("POPULATED_TEMPLATE", userAction);

				// get all the exhibition content
				// create exhibition object
				// view the newly created exhibition object
				// allow the user to save the exhibition object in the DB..
				String exhibitionTitle = request.getParameter("Title");
				String exhibitionDescription = request.getParameter("Description");
				String exhibitionCreator = request.getParameter("Creator");
				String exhibitioncover = request.getParameter("cover");
				String exhibitionborder = request.getParameter("border");
				int templateid = Integer.parseInt((String) session.getAttribute("TEMPLATE_ID"));

				String mediaString = (String) session.getAttribute("POPULATED_TEMPLATE");
				String[] media = new String[12];
				media = service.processTemplate(mediaString);

				String exmediaString = "";
				for (int i = 0; i < media.length; i++) {
					if (media[i] != null) {
						System.out.println(i);
						exmediaString = exmediaString + media[i] + " %";
					} else {
						System.out.println(i);
						exmediaString = exmediaString + " %";
					}
				}

				String excaptionsString = "";
				for (int i = 0; i < captions.length; i++) {

					if (captions[i] != null) {
						captions[i] = captions[i].replace("%", "percent");
						excaptionsString = excaptionsString + captions[i] + " %";
					} else {
						System.out.println(i);
						excaptionsString = excaptionsString + " %";
					}

				}

				// save the exhibition
				Integer id = service.saveExhibition(new Exhibition(exhibitionTitle.replace("%", "percent"),
						exhibitionDescription.replace("%", "percent"), templateid,
						exhibitionCreator.replace("%", "percent"), exmediaString, excaptionsString, exhibitioncover,
						exhibitionborder));
				//View the exhibition after it is created
				if (session.getAttribute("TEMPLATE_ID").equals("1")) { 

					Exhibition exhibition = service.getExhibition(id);
					String title = exhibition.getTitle();
					String description = exhibition.getDescription();
					String creator = exhibition.getCreator();
					String cover = exhibition.getCover();
					String border = exhibition.getBorder();
					String[] images = exhibition.getMedia().split("%");
					for (int j = 0; j < images.length; j++) {
						images[j] = images[j].trim();
						if (images[j].equals("")) {
							images[j] = null;
						} else {
							images[j] = "http://personalhistories.cs.uct.ac.za:8089/fedora/objects/" + images[j]
									+ "/datastreams/IMG/content";
						}
					}

					String[] captionss = exhibition.getCaptions().split("%");
					request.setAttribute("images", images);
					request.setAttribute("ExhibitionTitle", title);
					request.setAttribute("ExhibitionDescription", description);
					request.setAttribute("ExhibitionCreator", creator);
					request.setAttribute("ExhibitionCover", cover);
					request.setAttribute("ExhibitionBorder", border);
					request.setAttribute("PopulatedTemplateArray", images);
					session.setAttribute("CAPTIONS", captionss);
					result = "WEB-INF/frontend/exhibitions/createViewExhibition.jsp";// View
																						// before
																						// saving
				}

				else if (session.getAttribute("TEMPLATE_ID").equals("2")) {
					Exhibition exhibition = service.getExhibition(id);
					String title = exhibition.getTitle();
					String description = exhibition.getDescription();
					String creator = exhibition.getCreator();
					String cover = exhibition.getCover();
					String border = exhibition.getBorder();
					String[] images = exhibition.getMedia().split("%");
					for (int j = 0; j < images.length; j++) {
						images[j] = images[j].trim();
						if (images[j].equals("")) {
							images[j] = null;
						} else {
							images[j] = "http://personalhistories.cs.uct.ac.za:8089/fedora/objects/" + images[j]
									+ "/datastreams/IMG/content";
						}
					}

					String[] captionss = exhibition.getCaptions().split("%");
					request.setAttribute("images", images);
					request.setAttribute("ExhibitionTitle", title);
					request.setAttribute("ExhibitionDescription", description);
					request.setAttribute("ExhibitionCreator", creator);
					request.setAttribute("ExhibitionCover", cover);
					request.setAttribute("ExhibitionBorder", border);
					request.setAttribute("PopulatedTemplateArray", images);
					session.setAttribute("CAPTIONS", captionss);
					// View before saving
					result = "WEB-INF/frontend/exhibitions/createViewExhibition2.jsp";
				} else if (session.getAttribute("TEMPLATE_ID").equals("3")) {
					Exhibition exhibition = service.getExhibition(id);
					String title = exhibition.getTitle();
					String description = exhibition.getDescription();
					String creator = exhibition.getCreator();
					String cover = exhibition.getCover();
					String border = exhibition.getBorder();
					String[] images = exhibition.getMedia().split("%");
					for (int j = 0; j < images.length; j++) {
						images[j] = images[j].trim();
						if (images[j].equals("")) {
							images[j] = null;
						} else {
							images[j] = "http://personalhistories.cs.uct.ac.za:8089/fedora/objects/" + images[j]
									+ "/datastreams/IMG/content";
						}
					}

					String[] captionss = exhibition.getCaptions().split("%");
					request.setAttribute("images", images);
					request.setAttribute("ExhibitionTitle", title);
					request.setAttribute("ExhibitionDescription", description);
					request.setAttribute("ExhibitionCreator", creator);
					request.setAttribute("ExhibitionCover", cover);
					request.setAttribute("ExhibitionBorder", border);
					request.setAttribute("PopulatedTemplateArray", images);
					session.setAttribute("CAPTIONS", captionss);
					// View before saving
					result = "WEB-INF/frontend/exhibitions/createViewExhibition3.jsp";// View
																						// before
																						// saving
				}
			} else if (action.equals("Back")) {
				result = "WEB-INF/frontend/exhibitions/createExhibition.jsp";
			}
		}

		return result;
	}

}
