package search;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;

import annotations.Annotations;
import common.controller.Controller;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
import history.History;
import history.HistoryController;

public class BrowseController implements Controller {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println(request.getParameter("annotations"));
		if(request.getParameter("annotations")!=null && !request.getParameter("annotations").isEmpty()){
			String pid=request.getParameter("pid");
			String annotations=request.getParameter("annotations");
			
			Set<FedoraDigitalObject> temp =(Set<FedoraDigitalObject>) request.getSession().getAttribute("objects");
			
			
			Annotations annotation=new Annotations();
			annotation.addAnnotation(pid, annotations, temp);
			
			request.setAttribute("objects", temp);
			}
		
		Browse.setFedoraDigitalObjects((Set<FedoraDigitalObject>)(request.getSession().getAttribute("objects")));
		Browse.initialise((String) request.getSession().getAttribute("MEDIA_PREFIX"));
		String result = browseFedoraObjects(request, response);
		browseFedoraObjects(request, response);
		request.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		(new HistoryController()).execute(request, response);
		// return result;
		request.getSession().setAttribute("categoriesAndObjects", Browse.getCategorisedFedoraDigitalObjects());
		return result;
	}

	private void browseAllFedoraObjects(HttpServletRequest request, HttpServletResponse response)
			throws FedoraException, SolrServerException {

		// this is to return everything in the archive collection...this is just
		// to illustrate browse temporarily
		/* we are intiially searching all the fedora objects here */
		Set<FedoraDigitalObject> fedoraDigitalObjectsForArchive = Browse.getFedoraDigitalObjectsForArchive(); 
		// before we actually set the attribute we need to filter per archive

		if (fedoraDigitalObjectsForArchive == null || fedoraDigitalObjectsForArchive.isEmpty()) {
			request.setAttribute("message", "No results to retuRrn");
		}
		request.getSession().setAttribute("objectsForArchive", fedoraDigitalObjectsForArchive);
	}

	private String browseFedoraObjects(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//adding items from browse to car then redirecting to browse
				System.out.println(request.getParameter("addedtocart"));
				if(request.getParameter("addedtocart")!=null && !request.getParameter("addedtocart").isEmpty()){
					System.out.println("in add to car");
					ArrayList<String> cart = (ArrayList<String>) request.getSession().getAttribute("MEDIA_CART");
					String selected=request.getParameter("addedtocart");
					System.out.println(selected);
					
					request.getSession().setAttribute("MEDIA_CART", cart);	
					
				}
		
		String category = request.getParameter("category");
		String value = request.getParameter(category);
		
		String result = "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";
		/*
		 * if the category is blank or null it means that it is just general
		 * browse
		 */
		if (category == null || category.isEmpty()) {
			browseAllFedoraObjects(request, response);
		} else {
			/*
			 * we want to add these values to our word cloud...both the category
			 * and the actual value
			 * 
			 */
			History.addTextToTagCloud(category,false);
			request.setAttribute("browseCategory", category);
			
			if (value==null){
				return "WEB-INF/frontend/searchandbrowse/browseCategory.jsp";
			}
			
			
			History.addTextToTagCloud(value,false);
			/*
			 * our category is not null...therefore we need to start filtering
			 * the searches by what has been selected by the user
			 */
			Browse.filterFedoraDigitalObjects(Browse.getFedoraDigitalObjectsForArchive(), category, value);
			request.setAttribute("categoryValue", value);
			
			
			request.getSession().setAttribute("objectsForArchive", Browse.getFilteredDigitalObjects());
		}
		request.setAttribute("browseCategories", Browse.getBrowsingCategories());
		return result;
	}

}
