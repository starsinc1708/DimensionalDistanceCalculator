package org.example.db_connection_test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DataConnectionTest {
    public static void main(String[] args) {
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            if (sessionFactory != null) {
                Session session = sessionFactory.openSession();
                System.out.println("Connected to the database!!!");
                session.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
