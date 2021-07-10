package com.hurui.apacheigniteembedded;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

import com.hurui.model.ignite.Person;

@ComponentScan("com.hurui")
@SpringBootApplication
public class ApacheIgniteEmbeddedApplication {

	@Autowired
	private SessionFactory sessionFactory;
	
	public static void main(String[] args) {
		SpringApplication.run(ApacheIgniteEmbeddedApplication.class, args);
	}

	@EventListener
	public void test(ApplicationStartedEvent event) {
		Transaction transaction = null;
		try(Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			Person person = new Person();
			person.setName("test");
			session.save(person);
			transaction.commit();
		} catch (Exception ex) {
			if(transaction != null) {
				transaction.rollback();
			}			
			ex.printStackTrace();
		}
		
		try(Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
			Root<Person> root = criteriaQuery.from(Person.class);
			criteriaQuery.where(criteriaBuilder.equal(root.get("name"), "test"));
			criteriaQuery.select(root);
			Query<Person> query = session.createQuery(criteriaQuery);
			query.setCacheable(Boolean.TRUE);
			
			System.out.println("Note: For the example below: Hibernate query will be logged it is the first time this criteria query is triggered!");
			System.out.println("Result below is fetched from DB:");
			Person person = query.getResultList().get(0);
			System.out.println("DB results fetched: " + person);
			System.out.println("Note: For the example below: Hibernate query will not be logged as result is retrieved from l2 cache successfully!");
			System.out.println("Result below is fetched from L1 cache:");
			System.out.println("DB results fetched: " + query.getResultList().get(0));
			transaction.commit();		
		} catch (Exception ex) {
			if(transaction != null) {
				transaction.rollback();
			}
			ex.printStackTrace();
		}
	}

}
