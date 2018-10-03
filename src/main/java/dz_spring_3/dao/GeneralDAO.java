package dz_spring_3.dao;

import dz_spring_3.model.IdEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

import javax.persistence.TypedQuery;
import java.util.List;

public class GeneralDAO<T extends IdEntity> {
    
    private static SessionFactory sessionFactory;
    private Class<T> type;
    
    public T save(T t){
        Transaction tr = null;
        try(Session session = createSessionFactory().openSession()){
            tr = session.getTransaction();
            tr.begin();

            session.save(t);

            tr.commit();
        }catch (HibernateException e){
            if (tr != null)
                tr.rollback();

            System.err.println(e.getMessage());
            throw new HibernateException("Operation failed.");
        }
        return t;
    }

    public void update(T t){

        Transaction tr = null;
        try (Session session = createSessionFactory().openSession()){
            tr = session.getTransaction();
            tr.begin();

            session.update(t);

            tr.commit();
        }catch (HibernateException e){
            if (tr != null)
                tr.rollback();

            System.err.println(e.getMessage());
            throw new HibernateException("Operation failed.");
        }
    }

    void delete(long id, String sql) {

        Transaction tr = null;
        try (Session session = createSessionFactory().openSession()){
            tr = session.getTransaction();
            tr.begin();

            session.delete(findById(id, sql));

            tr.commit();
        }catch (HibernateException e){
            if (tr != null)
                tr.rollback();

            System.err.println(e.getMessage());
            throw new HibernateException("Operation failed.");
        }
    }

    @SuppressWarnings("unchecked")
    List<T> getAllObjects(String sql){
        List<T> objects;

        try(Session session = createSessionFactory().openSession()) {

            NativeQuery query = session.createNativeQuery(sql);
            objects = query.addEntity(type).list();

        }catch (HibernateException e){
            System.err.println(e.getMessage());
            throw new HibernateException("Operation failed");
        }
        return objects;
    }

    @SuppressWarnings("unchecked")
    T findById(Long id, String sql){

        T t;

        try(Session session = createSessionFactory().openSession()) {

            NativeQuery query = session.createNativeQuery(sql);
            if (getSingleResult(query, id) == null){
                t = null;
            }
            else
                t = (T) query.addEntity(type).setParameter(1, id).getSingleResult();

        }catch (HibernateException e){
            System.err.println(e.getMessage());
            throw new HibernateException("Operation failed.");
        }
        return t;
    }

    private <T> T getSingleResult(TypedQuery<T> query, Long id){
        query.setMaxResults(1);

        List<T> list = query.setParameter(1, id).getResultList();

        if (list == null || list.isEmpty()){

            return null;
        }
        return list.get(0);
    }

    static SessionFactory createSessionFactory(){
        if (sessionFactory == null){
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }

    void setType(Class<T> type) {
        this.type = type;
    }
}
