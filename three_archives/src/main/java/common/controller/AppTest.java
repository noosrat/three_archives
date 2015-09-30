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
		System.out.println("sessionfactory");
		Session session = sessionFactory.openSession();
		System.out.println("session");
		session.beginTransaction();
		System.out.println("begin");
		User user = new User("firstuser");
		session.save(user);
		System.out.println("instanciated");
		session.getTransaction().commit();
		System.out.println("sent");
		session.close();
		System.out.println("closed");
	}
}