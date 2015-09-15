package maps;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.Controller;

public class MapController implements Controller {

	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (request.getPathInfo().substring(1).contains("redirect_maps")) {

			return "WEB-INF/frontend/maps/mapoverview.jsp";
		}
		else return null;

	}

	
	
}
