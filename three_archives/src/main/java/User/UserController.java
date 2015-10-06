package User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.Controller;
import common.model.ManageUsers;
import common.model.User;

public class UserController implements Controller {
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		if (request.getPathInfo().substring(1).contains("redirect_user")) 
		{
			return "WEB-INF/frontend/User/AddUser.jsp";
		}
		return uploads(request, response);
		
	}
	
	private String uploads(HttpServletRequest request,HttpServletResponse response){

		String result = "";		
		HttpSession session = request.getSession();
		if(request.getParameter("add_user")!=null)
		{
			ManageUsers userManager=new ManageUsers();
			String username=request.getParameter("new_username");
			System.out.print("u "+username);
			String password=request.getParameter("new_pwd");
			System.out.print("p "+password);
			userManager.addUser(new User(username,password,"privileged"));
			return "index.jsp";
		}
		if(request.getParameter("authorise")!=null)
		{
			ManageUsers userManager=new ManageUsers();
			System.out.println("AUTHOR");
			String username=request.getParameter("new_username");
			System.out.println("username "+username);
			String password=request.getParameter("new_pwd");
			System.out.println("password "+password);
			String role=userManager.approveUser(username, password);
			System.out.println("user role "+role);
			session.setAttribute("USER", role);
			return "index.jsp";
		}
		else if(request.getParameter("logout")!=null)
		{
			session.setAttribute("USER", "false");
			return "index.jsp";
		}
		
		
		
		return result;
		
	}
}
