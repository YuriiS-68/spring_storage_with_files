package dz_spring_3.service;

import dz_spring_3.dao.FileDAO;
import dz_spring_3.model.File;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServiceFile {

    @Autowired
    FileDAO fileDAO;

    public File findById(long id){

        return fileDAO.findById(id);
    }

    public void delete(Long id) {

        fileDAO.delete(id);
    }

    public Long getStorageId(Long idFile){

        return fileDAO.getStorageId(idFile);
    }

    public Long sumSizeFilesInStorage(Long idStorage){

        return fileDAO.sumSizeFilesInStorage(idStorage);
    }

    @SuppressWarnings("unchecked")
    public List<File> getAllObjects(){

        return fileDAO.getAllFile();
    }

    public List<File> getFilesInStorage(Long storageId){

        return fileDAO.getFilesInStorage(storageId);
    }

    public void updateAll(List<File> files){

        fileDAO.updateAll(files);
    }
}
