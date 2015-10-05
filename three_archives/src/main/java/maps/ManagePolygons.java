package maps;
import java.util.Iterator;

import common.model.Polygon;


import java.util.List;
///import org.hibernate.mapping.List;
import org.hibernate.HibernateException; 
import org.hibernate.Query;
import org.hibernate.Criteria;
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class ManagePolygons {
	private static SessionFactory factory; 
	
	   /* Method to CREATE an Polygon in the database */
	   public Integer addPolygon(Polygon Polygon){
		   Integer ID=0;
			  SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		      Session session =sessionFactory.openSession();
		      session.beginTransaction();
		      ID=(Integer)session.save(Polygon); 
		      session.getTransaction().commit();
		      session.close(); 
		      return ID;
		   }
	   /* Method to RETRIEVE an Polygon in the database */
	   public Polygon getPolygon(int id){
		   SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		   Session session =sessionFactory.openSession();
		   session.beginTransaction();
		   session.beginTransaction();
		   Polygon dbPolygon = (Polygon) session.get(Polygon.class, id);
		   
		   session.getTransaction().commit();
		   session.close();
		   
		   return dbPolygon;
		}
	   
	   /* Method to  READ all the Polygons */
	public List<Polygon> listAllPolygons(){
		   SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		   Session session = sessionFactory.openSession();
		   session.beginTransaction();
		   Criteria criteria = session.createCriteria(Polygon.class);
		   List<Polygon> polys = (List<Polygon>) criteria.list();
	        session.getTransaction().commit();
	        System.out.println("LISTED");
	        session.close();
	        return polys;
	    }
	 
	  
	  
}