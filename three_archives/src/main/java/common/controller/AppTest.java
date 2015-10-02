package common.controller;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import common.model.User;
 
/**
 * Unit test for simple App.
 */
public class AppTest{
	
	public AppTest(){}
 
	public void testApp() {
		SessionFactory sessionFactory = new Configuration().configure()
				.buildSessionFactory();
		
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		User user = new User();
		
		session.save(user);
		
		session.getTransaction().commit();
		
		session.close();
	}
}
