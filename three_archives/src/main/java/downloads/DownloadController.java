package downloads;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import maps.Map;
import common.controller.Controller;
import common.fedora.FedoraDigitalObject;

public class DownloadController implements Controller{

	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Download download = new Download();
		ArrayList<String> cart = (ArrayList<String>) request.getSession().getAttribute("MEDIA_CART");
		Set<FedoraDigitalObject> cartObjects = download.getFedoraDigitalObjects(cart);
		
		request.setAttribute("cart", cartObjects);
		//FedoraDigitalObject ob = populateFedoraDigitalObject(${cart})
		if (request.getPathInfo().substring(1).contains("checkout")){
			download.downloadFedoraDigitalObjects(cartObjects);
			return "WEB-INF/frontend/downloads/cart.jsp";
		}
		else {
			return "WEB-INF/frontend/downloads/cart.jsp";}
	}

}
