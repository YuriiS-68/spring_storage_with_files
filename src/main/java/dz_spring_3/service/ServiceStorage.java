package dz_spring_3.service;

import dz_spring_3.dao.StorageDAO;
import dz_spring_3.model.Storage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServiceStorage {

    @Autowired
    StorageDAO storageDAO;

    public void delete(Long id){

        storageDAO.delete(id);
    }

    @SuppressWarnings("unchecked")
    public List<Storage> getAllObjects(){

        return storageDAO.getAllStorage();
    }
}
