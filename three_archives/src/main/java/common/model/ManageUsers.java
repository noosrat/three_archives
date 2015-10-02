package common.model;
import java.util.Iterator;
import common.model.Exhibition;
import java.util.List;
import org.hibernate.HibernateException; 
import org.hibernate.Query;
import org.hibernate.Criteria;
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ManageUsers {
	private static SessionFactory factory; 
	
	/* Method to RETRIEVE a user from the db */
	   public User getExhibition(int id){
		   SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		   Session session =sessionFactory.openSession();
		   session.beginTransaction();
		   session.beginTransaction();
		   User dbUser = (User) session.get(User.class, id);
		   session.getTransaction().commit();
		   session.close();
		   return dbUser;
		}
}
