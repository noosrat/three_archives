
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.Exhibition;

import java.awt.image.BufferedImage;


/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet(description="Simple Servlet", urlPatterns={"/SimpleServlet"})
public class SimpleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Exhibition[] allExhibitions ;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SimpleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		allExhibitions = new Exhibition[5];
		if(request.getParameter("view_all_exhibitions") != null)
		{
			String action= request.getParameter("view_all_exhibitions");
			if(action.equals("View All Exhibitions"))
			{
				allExhibitions= new Exhibition[5];
				for(int i=0;i<5;i++)
				{
					allExhibitions[i]=new Exhibition();
					allExhibitions[i].AutoExhibitionGenerator();
				}
				request.setAttribute("all_exhibitions", allExhibitions);
				response.setContentType("Exhibition");
				RequestDispatcher dispatcher=getServletContext().getRequestDispatcher("/exhibition.jsp");
				dispatcher.forward(request,response);	
				
			
			}
		}
		
		if (request.getParameter("selectedExhibit") != null)
		{
			allExhibitions= new Exhibition[5];
			for(int i=0;i<5;i++)
			{
				allExhibitions[i]=new Exhibition();
				allExhibitions[i].AutoExhibitionGenerator();
			}
			String selectedExhibit=request.getParameter("selectedExhibit");
			int exhibitionId=Integer.parseInt( request.getParameter("selectedExhibit"));
			String [] images= allExhibitions[exhibitionId].getMedia();
			request.setAttribute("images",images);
			String path="D:\\images\\2.jpg";
			request.setAttribute("image",path);
			request.setAttribute("message",selectedExhibit);
			response.setContentType("text/html");	
			RequestDispatcher dispatcher=getServletContext().getRequestDispatcher("/ExhibitionViewer.jsp");
			dispatcher.forward(request,response);	
			
		}	
			else if(1==2)
			{
				//File fi = new File("D:\\images-missGay2013\\2.jpg");
				//byte[] fileContent=Files.readAllBytes(fi.toPath());
				//byte[] imageBytes = fileContent;
				String path="D:\\images\\2.jpg";
				response.setContentType("text/html");
				request.setAttribute("image",path);
				RequestDispatcher dispatcher=getServletContext().getRequestDispatcher("/ExhibitionViewer.jsp");
				dispatcher.forward(request,response);
				//String path=getServletContext().getRealPath(File.separator);
				//File f =new File("D:\\Exhibit2.jpg");
				//BufferedImage bi=ImageIO.read(f);
				//OutputStream out=response.getOutputStream();
				
				
				
				//ImageIO.write(bi, "jpg", out);
				//out.close();
			}
			
		
		
		
		
	}

}
