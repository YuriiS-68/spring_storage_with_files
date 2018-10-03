package dz_spring_3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dz_spring_3.BadRequestException;
import dz_spring_3.service.ServiceFile;
import dz_spring_3.dao.FileDAO;
import dz_spring_3.dao.StorageDAO;
import dz_spring_3.model.File;
import dz_spring_3.model.Storage;
import dz_spring_3.service.ServiceGeneral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Controller
public class ControllerFile {

    @Autowired
    ServiceFile serviceFile;

    @Autowired
    FileDAO fileDAO;

    @Autowired
    StorageDAO storageDAO;

    @Autowired
    ServiceGeneral serviceGeneral;


    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.POST, value = "/saveFile", produces = "text/plain")
    @ResponseBody
    public String saveFile(HttpServletRequest req)throws IOException {

        File file = mappingFile(req);

        try {
            if (file.getId() == null){

                checkNameFile(file.getName());
                serviceGeneral.save(file);
            }
            else
                throw new BadRequestException("File with id - " + file.getId() + " can`t be saved in DB.");

        }catch (BadRequestException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
        return "File with id - " + file.getId() + " saved success.";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/file", produces = "text/plain")
    @ResponseBody
    public String getAllFile(){

        return serviceFile.getAllObjects().toString();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.PUT, value = "/updateFile", produces = "text/plain")
    @ResponseBody
    public String updateFile(HttpServletRequest req)throws IOException{
        File file = mappingFile(req);
        long storageId;

        try {
            checkNameFile(file.getName());

            if (fileDAO.getStorageId(file.getId()) == null){
                storageId = 0;
            }
            else
                storageId = fileDAO.getStorageId(file.getId());

            file.setStorage(storageDAO.findById(storageId));

            if (fileDAO.findById(file.getId()) != null){

                serviceGeneral.update(file);

                return "File with id " + file.getId() + " updated successfully.";
            }
            else
                throw new BadRequestException ("Updating is not possible. File with id - " + file.getId() + " is missing in the database.");

        } catch (BadRequestException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteFile", produces = "text/plain")
    @ResponseBody
    public String deleteFile(HttpServletRequest req){
        File file;
        Storage storage;

        try {
            if (fileDAO.findById(Long.parseLong(req.getParameter("fileId"))) == null ||
                    storageDAO.findById(Long.parseLong(req.getParameter("storage"))) == null){

                throw new BadRequestException("The storage or file is not in the database.");
            }
            else {
                file = fileDAO.findById(Long.parseLong(req.getParameter("fileId")));
                storage = storageDAO.findById(Long.parseLong(req.getParameter("storage")));
            }

            if (file.getStorage().getId().equals(storage.getId())){

                serviceFile.delete(file.getId());

                return "File with id " + file.getId() + " deleted successfully.";
            }
            else
                throw new BadRequestException("The file with id - " + file.getId() + " does not exist in the DB.");

        } catch (BadRequestException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/putFile", produces = "text/plain")
    @ResponseBody
    String put(HttpServletRequest req)throws IOException{
        //проверить есть ли в базе хранилище и если есть, просетить его файлу
        //проверить есть ли в хранилище файл с таким id( не могут быть в одном хранилище файлы с одинаковым id)
        //проверить свободное место в хранилище
        //проверить совместимость форматов файла и хранилища
        File file = mappingFile(req);
        Storage storage;

        try {
            checkNameFile(file.getName());

            if (storageDAO.findById(Long.parseLong(req.getParameter("storageId"))) == null) {
                throw new BadRequestException("The storage is not in the database.");
            }
            else
                storage = storageDAO.findById(Long.parseLong(req.getParameter("storageId")));

            if (checkFileInStorage(file, storage)){
                file.setStorage(storage);
            }
            else
                throw new BadRequestException("File with id: " + file.getId() + " already exist in the storage with id: " + storage.getId());

            validate(storage, file);

            update(file);

        } catch (BadRequestException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
        return "The file with id: " + file.getId() + " was successfully placed in the storage with id: " + storage.getId();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/transferFile", produces = "text/plain")
    @ResponseBody
    String transferFile(HttpServletRequest req){
        //проверить существуют ли оба хранилища в базе данных
        //проверить находится ли файл в хранилище из которого делаем перенос файла
        //проверить есть ли уже в новом хранилище файл с таким id
        //проверить поддерживает ли хранилище формат переносимого файла
        //проверить хватит ли свободного места в новом хранилище для файла
        File file;
        Storage storageFrom;
        Storage storageTo;

        try {
            if (fileDAO.findById(Long.parseLong(req.getParameter("fileId"))) == null ||
                    storageDAO.findById(Long.parseLong(req.getParameter("storageTo"))) == null ||
                    storageDAO.findById(Long.parseLong(req.getParameter("storageFrom"))) == null){

                throw new BadRequestException("The storage or file is not in the database.");
            }
            else {
                file = fileDAO.findById(Long.parseLong(req.getParameter("fileId")));
                storageTo = storageDAO.findById(Long.parseLong(req.getParameter("storageTo")));
                storageFrom = storageDAO.findById(Long.parseLong(req.getParameter("storageFrom")));
            }

            if (checkFileInStorages(file, storageFrom, storageTo)) {
                file.setStorage(storageTo);
            }

            validate(storageTo, file);

            update(file);

        }catch (BadRequestException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }
        return "The file with id: " + file.getId() + " was successfully placed in the storage with id: " + storageTo.getId();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/transferAll", produces = "text/plain")
    @ResponseBody
    String transferAll(HttpServletRequest req) {
        //проверить существуют ли оба хранилища в базе данных
        //проверить есть ли все файлы в storageFrom и нет ли переносимых фалов в storageTo
        //проверить хватит ли места для файлов в storageTo
        //проверить файлы и storageTo на совместимость форматов
        List<File> files;
        Storage storageTo;

        try {
            if (storageDAO.findById(Long.parseLong(req.getParameter("storageTo"))) == null ||
                    storageDAO.findById(Long.parseLong(req.getParameter("storageFrom"))) == null){
                throw new BadRequestException("The storageTo or storageFrom is not in the database.");
            }
            else {
                storageTo = storageDAO.findById(Long.parseLong(req.getParameter("storageTo")));
                files = fileDAO.getFilesInStorage(Long.parseLong(req.getParameter("storageFrom")));
            }

            validateTransferAll(storageTo, files);

        }catch (BadRequestException e) {
            e.printStackTrace();
            return String.valueOf(e);
        }

        for (File file : files){
            if (file != null){
                file.setStorage(storageTo);
            }
        }

        fileDAO.updateAll(files);

        return "All files were successfully transferred to the storage with id: " + storageTo.getId();
    }

    @SuppressWarnings("unchecked")
    private void update(File file)throws BadRequestException{

        if (file.getId() != null){

            serviceGeneral.update(file);
        }
        else
            throw new BadRequestException("The file with id: " + file.getId() + " is not exist.");
    }

    private void validate(Storage storage, File file)throws BadRequestException{

        if (!checkFreeSpace(storage, file)) {
            throw new BadRequestException("For storage with id: " + storage.getId() +
                    " the file with id: " + file.getId() + " - is too large");
        }

        if (!checkFormat(storage, file)) {
            throw new BadRequestException("The storage with id: " + storage.getId() +
                    " does not support the file format with id: " + file.getId());
        }
    }

    private void validateTransferAll(Storage storageTo, List<File> files)throws BadRequestException{
        if (!checkFreeSpaceForTransferFiles(files, storageTo))
            throw new BadRequestException("Storage with id: " + storageTo.getId() + " cannot hold all files");

        if (!checkFormatAll(files, storageTo))
            throw new BadRequestException("The storage with id: " + storageTo.getId() + " does not support all file formats");

        if (!checkFilesInStorages(files, storageTo))
            throw new BadRequestException("One of the files already exists in the storage with id: " + storageTo.getId());
    }

    private boolean checkFormat(Storage storage, File file){

        String[] formatsStorage = storage.getFormatsSupported().split(",");

        for (String formattedCell : formatsStorage){
            if (formattedCell != null && file.getFormat() != null && file.getFormat().equals(formattedCell.trim())){
                return true;
            }
        }
        return false;
    }

    private boolean checkFormatAll(List<File> files, Storage storageTo){

        String[] formatsStorage = storageTo.getFormatsSupported().split(",");

        List<String> storageFormats = new ArrayList<>();

        for (String element : formatsStorage){
            if (element != null){
                storageFormats.add(element.trim());
            }
        }

        Set<String> fileFormats = new HashSet<>();

        for (File file : files){
            if (file != null){
                fileFormats.add(file.getFormat());
            }
        }

        return storageFormats.containsAll(fileFormats);
    }

    private boolean checkFilesInStorages(List<File> files, Storage storageTo)throws BadRequestException{
        for (File file : files){
            if (file != null && file.getStorage().getId().equals(storageTo.getId())){
                throw new BadRequestException("File with id: " + file.getId() + " already exist in the storage with id: " + storageTo.getId());
            }
        }
        return true;
    }

    private boolean checkFreeSpaceForTransferFiles(List<File> files, Storage storageTo){
        long sumInStorageTo = fileDAO.sumSizeFilesInStorage(storageTo.getId());
        long sum = 0;

        for (File file : files){
            if (file != null){
                sum += file.getSize();
            }
        }
        return (storageTo.getStorageSize() - sumInStorageTo) >= sum || sumInStorageTo == 0;
    }

    private boolean checkFileInStorages(File file, Storage storageFrom, Storage storageTo)throws BadRequestException{

        if (file.getStorage().getId() == null || file.getStorage().getId() != null && !file.getStorage().getId().equals(storageFrom.getId())) {
            throw new BadRequestException("File with id " + file.getId() + " is not in storage with id " + storageFrom.getId());
        }

        if (file.getStorage().getId() != null && file.getStorage().getId().equals(storageTo.getId())){
            throw new BadRequestException("File with id: " + file.getId() + " already exist in the storage with id: " + storageTo.getId());
        }
        return true;
    }

    private boolean checkFileInStorage(File file, Storage storage)throws BadRequestException{

        File fileFound = fileDAO.findById(file.getId());

        if (fileFound == null || fileFound.getStorage() == null)
            throw new BadRequestException("File with id " + file.getId() + " was not found in the database.");

        return  !fileFound.getStorage().getId().equals(storage.getId());

    }

    private boolean checkFreeSpace(Storage storage, File file){
        //сделать запрос в базу данных и просуммировать размеры файлов в одном хранилище
        //от размера хранилища отнять сумму размеров файлов
        long sum = fileDAO.sumSizeFilesInStorage(storage.getId());

        return storage.getStorageSize() - sum >= file.getSize() || sum == 0;
    }

    private static void checkNameFile(String name)throws BadRequestException{
        if (name.isEmpty() || name.length() > 10)
            throw new BadRequestException ("File name can't be more 10 chars. File with this name can't be created");
    }

    private File mappingFile(HttpServletRequest req)throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        try(BufferedReader reader = req.getReader()) {
            String line;

            while ((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();

        String input = objectMapper.writeValueAsString(stringBuilder.toString());

        return objectMapper.convertValue(input, File.class);
    }
}
