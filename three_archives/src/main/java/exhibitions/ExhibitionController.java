package exhibitions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yourmediashelf.fedora.client.FedoraClientException;

import common.controller.Controller;
import common.model.Exhibition;

public class ExhibitionController implements Controller {

	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (request.getPathInfo().substring(1).contains("redirect_exhibitions")) {

			return "WEB-INF/frontend/exhibitions/exhibitionHome.jsp";

		}
		return exhibitions(request, response);

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
				result = "WEB-INF/frontend/exhibitions/exhibition.jsp";

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
			result = "WEB-INF/frontend/exhibitions/exhibitionViewer.jsp";

		} else if (1 == 2) {
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
