package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }
    SessionFactory factory = Util.getSessionFactory();

    @Override
    public void createUsersTable() {
        Session session = factory.getCurrentSession();
        Transaction transaction=session.beginTransaction();
        Query query = session.createSQLQuery("CREATE TABLE IF NOT EXISTS User(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(64), lastName VARCHAR(64), age INT)").addEntity(User.class);
        query.executeUpdate();
        transaction.commit();
        session.close();
    }

    @Override
    public void dropUsersTable() {
        Session session = factory.getCurrentSession();
        Transaction transaction= session.beginTransaction();
        Query query = session.createSQLQuery("DROP TABLE IF EXISTS User");
        query.executeUpdate();
        transaction.commit();
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        User user = new User(name, lastName, age);
        try (Session session= factory.getCurrentSession()){
            transaction=session.beginTransaction();
            session.save(user);
            transaction.commit();
        }catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = factory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        User user=session.get(User.class, id);
        session.remove(user);
        transaction.commit();
        session.close();
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = factory.openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        Session session = factory.getCurrentSession();
        Transaction transaction= session.beginTransaction();
        session.createSQLQuery("DELETE  FROM User").executeUpdate();
        transaction.commit();
        session.close();

    }
}
