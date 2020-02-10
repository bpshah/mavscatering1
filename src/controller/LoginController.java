package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.UserErrorMsgs;
import data.UserDAO;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String username;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				//doGet(request, response);
				HttpSession session = request.getSession();	
				String action = request.getParameter("action");
				String username = request.getParameter("username");
				String password = request.getParameter("password");	
				
				String url = "/Login.jsp";

				
				if (request.getParameter("loginBtn")!=null) 
				{
					User user = new User();
					user.setUsername(username);
					user.setPassword(password);		
					UserErrorMsgs UerrorMsgs = new UserErrorMsgs();		
					user.verifyUser(user, UerrorMsgs);
					session.setAttribute("User",user);
					session.setAttribute("errorMsgs",UerrorMsgs);

					if (UerrorMsgs.getErrorMsgs().equals("")) { //username & pw matches
						session.removeAttribute("User");
						session.removeAttribute("errorMsgs");
						HttpSession currentSession = request.getSession();							
						
						user = UserDAO.getUser(username);				
						
						currentSession.setAttribute("currentUser",user);
						
						//redirect to appropriate home page based on role
						String role = user.getRole();
						System.out.println("Login:" + role);
						if(role.equals("Admin"))
						  url = "/adminHomePage.jsp"; 
						else if(role.equals("Repairer"))
						  url = "/repairerHome.jsp";
						else if(role.equals("User"))
						  url = "/UserHome.jsp";
						else
						  url = "/facilityManagerHome.jsp";

					}
					else {
						//register button pressed
						session.removeAttribute("User");
						session.removeAttribute("errorMsgs");
						url = "/register.jsp";
					}
					getServletContext().getRequestDispatcher(url).forward(request, response);

		}
	}

}
