package dz_spring_3.service;

import dz_spring_3.dao.GeneralDAO;
import dz_spring_3.model.IdEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceGeneral<T extends IdEntity> {

    private GeneralDAO generalDAO;

    @Autowired
    public ServiceGeneral(GeneralDAO generalDAO) {
        this.generalDAO = generalDAO;
    }

    @SuppressWarnings("unchecked")
    public T save(T t) {

        return (T) generalDAO.save(t);
    }

    @SuppressWarnings("unchecked")
    public void update(T t){

        generalDAO.update(t);
    }
}
