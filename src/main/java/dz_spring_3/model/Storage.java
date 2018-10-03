package dz_spring_3.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "STORAGE_")
public class Storage extends IdEntity {

    private Long id;
    private String formatsSupported;
    private String storageCountry;
    private Long storageSize;
    private List<File> files;

    public Storage() {
    }

    public Storage(Long id, String formatsSupported, String storageCountry, Long storageSize, List<File> files) {
        this.id = id;
        this.formatsSupported = formatsSupported;
        this.storageCountry = storageCountry;
        this.storageSize = storageSize;
        this.files = files;
    }

    @Id
    @SequenceGenerator(name = "ST_SQ", sequenceName = "STORAGE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ST_SQ")
    @Column(name = "STORAGE_ID")
    public Long getId() {
        return id;
    }

    @Column(name = "FORMAT_SUPPORTED")
    public String getFormatsSupported() {
        return formatsSupported;
    }

    @Column(name = "COUNTRY_STORAGE")
    public String getStorageCountry() {
        return storageCountry;
    }

    @Column(name = "SIZE_STORAGE")
    public Long getStorageSize() {
        return storageSize;
    }

    @OneToMany(mappedBy = "storage", fetch = FetchType.LAZY, targetEntity = File.class)
    public List<File> getFiles() {
        return files;
    }

    @JsonCreator
    public static Storage createFromJson(String jsonString){

        ObjectMapper objectMapper = new ObjectMapper();

        Storage storage = null;
        try {
            storage = objectMapper.readValue(jsonString, Storage.class);
        }catch (IOException e){
            e.printStackTrace();
        }

        return storage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFormatsSupported(String formatsSupported) {
        this.formatsSupported = formatsSupported;
    }

    public void setStorageCountry(String storageCountry) {
        this.storageCountry = storageCountry;
    }

    public void setStorageSize(Long storageSize) {
        this.storageSize = storageSize;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Storage storage = (Storage) o;
        return Objects.equals(files, storage.files) &&
                Objects.equals(formatsSupported, storage.formatsSupported) &&
                Objects.equals(storageCountry, storage.storageCountry) &&
                Objects.equals(storageSize, storage.storageSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(files, formatsSupported, storageCountry, storageSize);
    }

    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", formatsSupported='" + formatsSupported + '\'' +
                ", storageCountry='" + storageCountry + '\'' +
                ", storageSize=" + storageSize +
                '}';
    }
}
