package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The {@code Controller} interface is the parent of all controllers to be implemented and linked to services throughout the application
 *
 *@author mthnox003
 */
public interface Controller {
/**
 * The execute method is present in each controller and allows for the execution of the request from the client
 * @param request {@link HttpServletRequest} instance representing the request from the client
 * @param response {@link HttpServletResponse} instance representing the response for the client
 * @return {@link String} instance representing the next page to be navigated to in the application.  This returns the .jsp file path to dispatch to 
 * @throws Exception
 */
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
