package dz_spring_3.service;

import dz_spring_3.dao.FileDAO;
import dz_spring_3.model.File;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServiceFile {

    @Autowired
    FileDAO fileDAO;

    public void delete(Long id) {

        fileDAO.delete(id);
    }

    @SuppressWarnings("unchecked")
    public List<File> getAllObjects(){

        return fileDAO.getAllFile();
    }
}
