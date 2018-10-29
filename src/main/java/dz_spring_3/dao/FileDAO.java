package dz_spring_3.dao;

import dz_spring_3.model.File;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class FileDAO extends GeneralDAO<File> {

    private static final String SQL_GET_ALL_FILES = "SELECT * FROM FILE_";
    private static final String SQL_GET_FILES_IN_STORAGE ="SELECT * FROM FILE_ WHERE STORAGE_ID_F = :idParam";
    private static final String SQL_GET_BY_ID_FILE = "SELECT * FROM FILE_ WHERE FILE_ID = ?";
    private static final String SQL_SUM_FILES_SIZE_IN_STORAGE = "SELECT SUM(SIZE_FILE) FROM FILE_ " +
            "JOIN STORAGE_ ON FILE_.STORAGE_ID_F = STORAGE_.STORAGE_ID WHERE STORAGE_.STORAGE_ID = :idParam";
    private static final String SQL_GET_STORAGE_ID = "SELECT STORAGE_ID_F FROM FILE_ WHERE FILE_ID = :idParam";

    public List<File> getAllFile(){
        setType(File.class);

        return getAllObjects(SQL_GET_ALL_FILES);
    }

    @SuppressWarnings("unchecked")
    public List<File> getFilesInStorage(Long storageId){
        List<File> files;

        try(Session session = createSessionFactory().openSession()) {

            NativeQuery query = session.createNativeQuery(SQL_GET_FILES_IN_STORAGE);
            files = query.setParameter("idParam", storageId).addEntity(File.class).list();

        }catch (HibernateException e){
            System.err.println(e.getMessage());
            throw new HibernateException("Operation failed");
        }
        return files;
    }

    public void updateAll(List<File> files){

        Transaction transaction = null;
        try(Session session = createSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            for (File file : files){
                session.update(file);
            }

            transaction.commit();
        }catch (HibernateException e){

            if (transaction != null)
                transaction.rollback();

            System.err.println(e.getMessage());
            throw new HibernateException("Operation failed.");
        }
    }

    @SuppressWarnings("unchecked")
    public Long sumSizeFilesInStorage(Long idStorage){
        long amount;

        try (Session session = createSessionFactory().openSession()){

            NativeQuery query = session.createNativeQuery(SQL_SUM_FILES_SIZE_IN_STORAGE);

            query.setParameter("idParam", idStorage);

            if (query.uniqueResult() == null){
                amount = 0;
            }
            else
                amount = ((BigDecimal) query.uniqueResult()).longValue();
        }
        return amount;
    }

    public Long getStorageId(Long idFile){

        Long idStorage;
        try (Session session = createSessionFactory().openSession()){

            NativeQuery query = session.createNativeQuery(SQL_GET_STORAGE_ID);

            query.setParameter("idParam", idFile);

            if (query.uniqueResult() == null){
                idStorage = null;
            }
            else
                idStorage = ((BigDecimal) query.uniqueResult()).longValue();
        }
        return idStorage;
    }

    public void delete(long id) {

        setType(File.class);

        delete(id, SQL_GET_BY_ID_FILE);
    }

    public File findById(long id){

        setType(File.class);

        return findById(id, SQL_GET_BY_ID_FILE);
    }
}
