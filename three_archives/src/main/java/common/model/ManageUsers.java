package common.model;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session; 
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ManageUsers {
	   //method to add user to the db
	   public void addUser(User user){
			  SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		      Session session =sessionFactory.openSession();
		      session.beginTransaction();
		      session.save(user); 
		      session.getTransaction().commit();
		      session.close(); 
		   }
	   public static java.util.List listAllUsers(){
		   SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		   Session session = sessionFactory.openSession();
		   session.beginTransaction();
		   Criteria criteria = session.createCriteria(User.class);
		   List<User> users = (List<User>) criteria.list();
	        session.getTransaction().commit();
	        session.close();
	        return users;
	    }
	   
	   public String approveUser(String username, String password)
	   {
		   String approve="";
		   List users=listAllUsers();
		   String name;
		   String pass;
		   String role;
		   for (int i=0; i<users.size();i++)
		   {
			   name=((User)users.get(i)).getUsername();
			   pass=((User)users.get(i)).getPassword();
			   if(name.equals(username))
			   {
				   if(pass.equals(password))
				   {
					   role=((User)users.get(i)).getRole();
					   approve=role;
					   break;
				   }
				   else
				   {
					   approve="incorrect";
					   break;
				   }
			   }
		   }
		   
		   return approve;
		   
	   }
}
