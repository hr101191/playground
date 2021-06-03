package com.hurui.atomix.cluster.discovery;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ClusterDao {
	
	public List<Cluster> getCluster() throws Exception {
		try(Session session = JdbcDiscoveryHibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Cluster> criteriaQuery = criteriaBuilder.createQuery(Cluster.class);
			Root<Cluster> root = criteriaQuery.from(Cluster.class);
			criteriaQuery.select(root);
			Query<Cluster> query = session.createQuery(criteriaQuery);
			return query.getResultList();
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	public void saveOrUpdateCluster(Cluster cluster) throws Exception {
		Transaction transaction = null;
		try(Session session = JdbcDiscoveryHibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(cluster);
			transaction.commit();
		} catch(Exception ex) {
			if(transaction != null) {
				transaction.rollback();
			}
			throw ex;
		}
	}
	
	public void removeFromCluster(Cluster cluster) throws Exception {
		Transaction transaction = null;
		try(Session session = JdbcDiscoveryHibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.delete(cluster);
			transaction.commit();
		} catch(Exception ex) {
			if(transaction != null) {
				transaction.rollback();
			}
			throw ex;
		}
	}
}
