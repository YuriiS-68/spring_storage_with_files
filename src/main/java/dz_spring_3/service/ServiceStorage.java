package dz_spring_3.service;

import dz_spring_3.dao.StorageDAO;
import dz_spring_3.model.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceStorage {

    private StorageDAO storageDAO;

    @Autowired
    public ServiceStorage(StorageDAO storageDAO) {
        this.storageDAO = storageDAO;
    }

    public Storage findById(long id){

        return storageDAO.findById(id);
    }

    public void delete(Long id){

        storageDAO.delete(id);
    }

    @SuppressWarnings("unchecked")
    public List<Storage> getAllObjects(){

        return storageDAO.getAllStorage();
    }
}
