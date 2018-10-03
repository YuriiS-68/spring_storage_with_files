package dz_spring_3.dao;

import dz_spring_3.model.Storage;

import java.util.List;

public class StorageDAO extends GeneralDAO<Storage> {

    private static final String SQL_GET_ALL_STORAGE = "SELECT * FROM STORAGE_";
    private static final String SQL_GET_BY_ID_STORAGE = "SELECT * FROM STORAGE_ WHERE STORAGE_ID = ?";

    public List<Storage> getAllStorage(){
        setType(Storage.class);

        return getAllObjects(SQL_GET_ALL_STORAGE);
    }

    public void delete(long id) {

        setType(Storage.class);

        delete(id, SQL_GET_BY_ID_STORAGE);
    }

    public Storage findById(long id){

        setType(Storage.class);

        return findById(id, SQL_GET_BY_ID_STORAGE);
    }
}
