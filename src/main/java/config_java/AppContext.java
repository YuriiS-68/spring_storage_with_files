package config_java;

import dz_spring_3.controller.ControllerFile;
import dz_spring_3.controller.ControllerStorage;
import dz_spring_3.dao.FileDAO;
import dz_spring_3.dao.GeneralDAO;
import dz_spring_3.dao.StorageDAO;
import dz_spring_3.service.ServiceFile;
import dz_spring_3.service.ServiceGeneral;
import dz_spring_3.service.ServiceStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("dz_spring_3")
public class AppContext {

    @Bean
    public GeneralDAO generalDAO(){
        return new GeneralDAO();
    }

    @Bean
    public StorageDAO storageDAO(){
        return new StorageDAO();
    }

    @Bean
    public FileDAO fileDAO(){
        return new FileDAO();
    }

    @Bean
    public ServiceGeneral serviceGeneral(){
        return new ServiceGeneral(generalDAO());
    }

    @Bean
    public ServiceStorage serviceStorage(){
        return new ServiceStorage(storageDAO());
    }

    @Bean
    public ServiceFile serviceFile(){
        return new ServiceFile(fileDAO());
    }

    @Bean
    public ControllerStorage controllerStorage(){
        return new ControllerStorage(serviceStorage(), serviceFile(), serviceGeneral());
    }

    @Bean
    public ControllerFile controllerFile(){
        return new ControllerFile(serviceFile(), serviceStorage(), serviceGeneral());
    }
    /*@Bean
    @Scope("prototype")
    public FileDAO fileDAO(){
        FileDAO fileDAO = new FileDAO();

        return fileDAO;
    }

    @Bean
    @Scope("prototype")
    public StorageDAO storageDAO(){
        StorageDAO storageDAO = new StorageDAO();

        return storageDAO;
    }

    @Bean
    @Scope("prototype")
    public GeneralDAO generalDAO(){
        GeneralDAO generalDAO = new GeneralDAO();

        return generalDAO;
    }

    @Bean
    public ServiceStorage serviceStorage(){
        ServiceStorage serviceStorage = new ServiceStorage();
        serviceStorage.setStorageDAO(storageDAO());

        return serviceStorage;
    }

    @Bean
    public ServiceFile serviceFile(){
        ServiceFile serviceFile = new ServiceFile();
        serviceFile.setFileDAO(fileDAO());

        return serviceFile;
    }

    @Bean
    public ServiceGeneral serviceGeneral(){
        ServiceGeneral serviceGeneral = new ServiceGeneral();
        serviceGeneral.setGeneralDAO(generalDAO());

        return serviceGeneral;
    }

    @Bean
    public ControllerFile controllerFile(){
        ControllerFile controllerFile = new ControllerFile();
        controllerFile.setServiceFile(serviceFile());
        controllerFile.setServiceStorage(serviceStorage());
        controllerFile.setServiceGeneral(serviceGeneral());

        return controllerFile;
    }

    @Bean
    public ControllerStorage controllerStorage(){
        ControllerStorage controllerStorage = new ControllerStorage();
        controllerStorage.setServiceStorage(serviceStorage());
        controllerStorage.setServiceFile(serviceFile());
        controllerStorage.setServiceGeneral(serviceGeneral());

        return controllerStorage;
    }*/
}
