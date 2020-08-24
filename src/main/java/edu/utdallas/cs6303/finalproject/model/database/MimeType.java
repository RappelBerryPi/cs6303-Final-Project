package edu.utdallas.cs6303.finalproject.model.database;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class MimeType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileExtension;

    private String mimeTypeString;

    @OneToMany(mappedBy = "mimeType")
    private List<UploadedFile> uploadedFile;

    public MimeType() {

    }

    public MimeType(String fileExtension, String mimeTypeString) {
        this.fileExtension = fileExtension;
        this.setMimeTypeString(mimeTypeString);
    }

    public String getMimeTypeString() {
        return mimeTypeString;
    }

    public void setMimeTypeString(String mimeTypeString) {
        this.mimeTypeString = mimeTypeString;
    }

    public Long getId() {
        return id;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public List<UploadedFile> getUploadedFile() {
        return uploadedFile;
    }

}