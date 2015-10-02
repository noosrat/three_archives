package exhibitions;
import java.util.Iterator;

import common.model.Exhibition;


import java.util.List;
///import org.hibernate.mapping.List;
import org.hibernate.HibernateException; 
import org.hibernate.Query;
import org.hibernate.Criteria;
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class ManageExhibition {
	private static SessionFactory factory; 
	
	   /* Method to CREATE an exhibition in the database */
	   public Integer addExhibition(Exhibition exhibition){
		   Integer ID=0;
			  SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		      Session session =sessionFactory.openSession();
		      session.beginTransaction();
		      ID=(Integer)session.save(exhibition); 
		      session.getTransaction().commit();
		      session.close(); 
		      return ID;
		   }
	   /* Method to RETRIEVE an exhibition in the database */
	   public Exhibition getExhibition(int id){
		   SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		   Session session =sessionFactory.openSession();
		   session.beginTransaction();
		   session.beginTransaction();
		   Exhibition dbExhibition = (Exhibition) session.get(Exhibition.class, id);
		   
		   session.getTransaction().commit();
		   session.close();
		   
		   return dbExhibition;
		}
	   
	   /* Method to  READ all the exhibitions */
	   @SuppressWarnings("rawtypes")
	public java.util.List listAllExhibitions(){
		   SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		   Session session = sessionFactory.openSession();
		   session.beginTransaction();
		   Criteria criteria = session.createCriteria(Exhibition.class);
		   List<Exhibition> exhibits = (List<Exhibition>) criteria.list();
	        session.getTransaction().commit();
	        System.out.println("LISTED");
	        session.close();
	        return exhibits;
	    }
	 
	  
	  
}