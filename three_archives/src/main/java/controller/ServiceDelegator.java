package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;

import services.search.Search;
import model.Exhibition;

public class ServiceDelegator {
	// handle exceptions in this method
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		String url = "index.jsp";
		try {
			if (request.getPathInfo().substring(1).equals("search")) {

				url = searchFedoraObjects(request, response);
			} else if (request.getPathInfo().substring(1)
					.equals("viewExhibitions")) {
				url = exhibitions(request, response);

			}
		} catch (FedoraClientException exception) {
			request.setAttribute("message", exception.getMessage());
		}

		return url;
	}

	enum Query {
		ALL("all"), ID("pid"), TITLE("title"), DESCRIPTION("description");

		private final String mapping;

		private Query(String mapping) {
			this.mapping = mapping;
		}
	}

	private String searchFedoraObjects(HttpServletRequest request,
			HttpServletResponse response) throws FedoraClientException {
		Search search = new Search();

		String terms = request.getParameter("terms");
		String query = request.getParameter("query");

		List<DatastreamProfile> pids = new ArrayList<DatastreamProfile>();

		if (query != null && !query.equalsIgnoreCase(Query.ALL.mapping)) {
			pids = search.findObjectsWithQuery(query + "~*" + terms + "*");

		} else {
			pids = search.findObjects(terms);
		}

		if (pids != null && !pids.isEmpty()) {
			request.setAttribute("objects", pids);
		} else {
			request.setAttribute("message", "No results to return");
		}
		return "search.jsp";
	}

	private String exhibitions(HttpServletRequest request,
			HttpServletResponse response) throws FedoraClientException {
		String result = "";
		Exhibition[] allExhibitions = new Exhibition[5];
		
		if (request.getParameter("view_all_exhibitions") != null) {
			String action = request.getParameter("view_all_exhibitions");
			if (action.equals("View All Exhibitions")) {
				allExhibitions = new Exhibition[5];
				for (int i = 0; i < 5; i++) {
					allExhibitions[i] = new Exhibition();
					allExhibitions[i].AutoExhibitionGenerator();
				}
				request.setAttribute("all_exhibitions", allExhibitions);
				response.setContentType("Exhibition");
				result = "/exhibition.jsp";

			}
		}

		if (request.getParameter("selectedExhibit") != null) {
			allExhibitions = new Exhibition[5];
			for (int i = 0; i < 5; i++) {
				allExhibitions[i] = new Exhibition();
				allExhibitions[i].AutoExhibitionGenerator();
			}
			String selectedExhibit = request.getParameter("selectedExhibit");
			int exhibitionId = Integer.parseInt(request
					.getParameter("selectedExhibit"));
			String[] images = allExhibitions[exhibitionId].getMedia();
			request.setAttribute("images", images);
			String path = "D:\\images\\2.jpg";
			request.setAttribute("image", path);
			request.setAttribute("message", selectedExhibit);
			response.setContentType("text/html");
			result = "/ExhibitionViewer.jsp";

		} else if (1 == 2) {
			// File fi = new File("D:\\images-missGay2013\\2.jpg");
			// byte[] fileContent=Files.readAllBytes(fi.toPath());
			// byte[] imageBytes = fileContent;
			String path = "D:\\images\\2.jpg";
			response.setContentType("text/html");
			request.setAttribute("image", path);
			result = "/ExhibitionViewer.jsp";
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
