package dz_spring_3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dz_spring_3.BadRequestException;
import dz_spring_3.dao.FileDAO;
import dz_spring_3.dao.StorageDAO;
import dz_spring_3.model.File;
import dz_spring_3.model.Storage;
import dz_spring_3.service.ServiceFile;
import dz_spring_3.service.ServiceGeneral;
import dz_spring_3.service.ServiceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@org.springframework.stereotype.Controller
public class ControllerStorage {

    @Autowired
    ServiceStorage serviceStorage;

    @Autowired
    ServiceFile serviceFile;

    @Autowired
    ServiceGeneral serviceGeneral;

    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.POST, value = "/saveStorage", produces = "text/plain")
    @ResponseBody
    public String saveStorage(HttpServletRequest req)throws IOException {

        Storage storage = mappingStorage(req);

        try {
            if (storage.getId() == null) {
                serviceGeneral.save(storage);
            }
            else
                throw new BadRequestException("Storage with id - " + storage.getId() + " can`t be saved in DB.");

        }catch (BadRequestException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
        return "Storage with id - " + storage.getId() + " saved success.";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/storage", produces = "text/plain")
    @ResponseBody
    public String getAllStorage(){

        return serviceStorage.getAllObjects().toString();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.PUT, value = "/updateStorage", produces = "text/plain")
    @ResponseBody
    public String updateStorage(HttpServletRequest req)throws IOException{
        Storage storage = mappingStorage(req);

        try {
            if (serviceStorage.findById(storage.getId()) != null){

                serviceGeneral.update(storage);

                return "Storage with id " + storage.getId() + " updated successfully.";
            }
            else
                throw new BadRequestException("Updating is not possible. Storage with id - " + storage.getId() + " is missing in the database.");

        }catch (BadRequestException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteStorage", produces = "text/plain")
    @ResponseBody
    public String deleteStorage(Long id){

        List<File> files = serviceFile.getFilesInStorage(id);

        try {
            if (files.size() > 0)
                throw new BadRequestException("The storage with id: " + id + " contains files.");

            if (serviceStorage.findById(id) != null){

                serviceStorage.delete(id);

                return "Storage with id " + id + " deleted successfully.";
            }
            else
                throw new BadRequestException("The ID - " + id + " does not exist in the DB.");

        }catch (BadRequestException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
    }

    private Storage mappingStorage(HttpServletRequest req)throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        try(BufferedReader reader = req.getReader()) {
            String line;

            while ((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();

        String input = objectMapper.writeValueAsString(stringBuilder.toString());

        return objectMapper.convertValue(input, Storage.class);
    }
}
