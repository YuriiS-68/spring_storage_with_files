package dz_spring_3.service;

import dz_spring_3.dao.GeneralDAO;
import dz_spring_3.dao.StorageDAO;
import dz_spring_3.model.IdEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class ServiceGeneral<T extends IdEntity> {
    @Autowired
    GeneralDAO generalDAO;

    @Autowired
    StorageDAO storageDAO;

    @SuppressWarnings("unchecked")
    public T save(T t) {

        return (T) generalDAO.save(t);
    }

    @SuppressWarnings("unchecked")
    public void update(T t){

        generalDAO.update(t);
    }
}
