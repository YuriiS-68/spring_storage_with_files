package dz_spring_3.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import dz_spring_3.BadRequestException;

import javax.persistence.*;
import java.io.IOException;
import java.util.Objects;

@Entity
@Table(name = "FILE_")
public class File extends IdEntity {

    private Long id;
    private String name;
    private String format;
    private Long size;
    private Storage storage;



    public File() {
    }

    public File(Long id, String name, String format, Long size, Storage storage){
        this.id = id;
        this.name = name;
        this.format = format;
        this.size = size;
        this.storage = storage;
    }

    @Id
    @SequenceGenerator(name = "F_SQ", sequenceName = "FILE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "F_SQ")
    @Column(name = "FILE_ID")
    public Long getId() {
        return id;
    }

    @Column(name = "NAME_FILE")
    public String getName() {
        return name;
    }

    @Column(name = "FORMAT_FILE")
    public String getFormat() {
        return format;
    }

    @Column(name = "SIZE_FILE")
    public Long getSize() {
        return size;
    }

    @JsonIgnore
    @ManyToOne(targetEntity = Storage.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "STORAGE_ID_F")
    public Storage getStorage() {
        return storage;
    }

    @JsonCreator
    public static File createFromJson(String jsonString){

        ObjectMapper objectMapper = new ObjectMapper();

        File file = null;
        try {
            file = objectMapper.readValue(jsonString, File.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        return file;
    }

    private static boolean checkNameFile(String name){

        return !name.isEmpty() && name.length() <= 10;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name)throws BadRequestException{
        if (!checkNameFile(name))
            try {
                throw new BadRequestException ("File name can't be more 10 chars. File with this name can't be created");
            } catch (BadRequestException e) {
                System.err.println(e.getMessage());
                throw e;
            }
        this.name = name;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return Objects.equals(name, file.name) &&
                Objects.equals(format, file.format) &&
                Objects.equals(size, file.size) &&
                Objects.equals(storage, file.storage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, format, size, storage);
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                " name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", size=" + size +
                ", storage=" + (storage != null ? storage.getId() : "null") +
                '}';
    }
}
