/*Author: Nicole Petersen
Description: Data manager class for the exhibition service. This class contains methods to interact with the database
*/
package exhibitions;

import common.model.Exhibition;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session; 
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class ManageExhibition {
	
	   /* Method to CREATE an exhibition in the database */
	   public Integer addExhibition(Exhibition exhibition){
		   Integer ID=0;
		   System.out.println("started saving the exhibition");//log
			  SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		      Session session =sessionFactory.openSession();
		      session.beginTransaction();
		      ID=(Integer)session.save(exhibition); //save the exhibition that was passed to this method
		      System.out.println("exhibition saved");// log
		      session.getTransaction().commit();
		      session.close(); 
		      System.out.println("ExhibitionID: "+ID);//log
		      return ID;
		   }
	   /* Method to RETRIEVE an exhibition in the database */
	   public Exhibition getExhibition(int id){
		   SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		   Session session =sessionFactory.openSession();
		   session.beginTransaction();
		   session.beginTransaction();
		   Exhibition dbExhibition = (Exhibition) session.get(Exhibition.class, id);// Get an exhibition from the database with the specific id
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
	        return exhibits; //Return a List of exhibition objects
	    }
	 
	  
	  
}
