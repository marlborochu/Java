package com.ma.utils;
/**
 * @author marlboro.chu@gmail.com
 **/
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {

	static final Logger logger = LogManager.getLogger(HibernateUtil.class.getName());
	private static HibernateUtil instance;
	private SessionFactory sf;

	private HibernateUtil() throws Exception {

	}

	public synchronized static HibernateUtil getInstance() throws Exception {

		if (instance == null) {
			instance = new HibernateUtil();
			instance.initSessionFacotry();
		}
		return instance;
	}

	private void initSessionFacotry() {

		try{
			Configuration configuration = new Configuration();
			configuration = configuration.configure("hibernate.cfg.xml");
	
			StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			sf = configuration.buildSessionFactory(serviceRegistry);
		}catch(Exception e){e.printStackTrace();}
	}

	public Session getSession() {
		Session session = sf.openSession();
		return session;
	}
	
	public int getSequence(Session session,String tableName){
		
		List seqs = session.createSQLQuery(
				"SELECT MAX(ES_SEQ) FROM "+tableName).list();
		if (seqs == null || seqs.isEmpty() || seqs.get(0) == null) {
			return 1;
		} else {
			int seq = (Integer) seqs.get(0);
			seq++;
			return seq;
		}
		
	}
	public int getSequence(Session session,String tableName,String colName){
		
		List seqs = session.createSQLQuery(
				"SELECT MAX("+colName+") FROM "+tableName).list();
		if (seqs == null || seqs.isEmpty() || seqs.get(0) == null) {
			return 1;
		} else {
			int seq = (Integer) seqs.get(0);
			seq++;
			return seq;
		}
		
	}
	
}
