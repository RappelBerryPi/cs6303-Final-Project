package edu.utdallas.cs6303.finalproject.model.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.util.StringUtils;

@Entity
@JsonIgnoreProperties(value = {"hasWebP", "hasThumbnail", "hasThumbnailWebP", "hasSmallPicture", "hasSmallWebP", "hasMediumPicture", "hasMediumWebP", "hasLargePicture", "hasLargeWebP", "mimeType"})
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TINYTEXT")
    private String description;
    private String fileName;

    private boolean hasWebP;
    private boolean hasThumbnail;
    private boolean hasThumbnailWebP;
    private boolean hasSmallPicture;
    private boolean hasSmallWebP;
    private boolean hasMediumPicture;
    private boolean hasMediumWebP;
    private boolean hasLargePicture;
    private boolean hasLargeWebP;

    @ManyToOne
    @JoinColumn(name = "mimeType_id")
    private MimeType mimeType;

    public UploadedFile() {
        super();
    }

    public UploadedFile(String fullFileName) {
        super();
        this.description = StringUtils.getFilename(fullFileName);
        this.fileName    = this.description;
    }

    public UploadedFile(String description, String fullFileName) {
        super();
        this.description = description;
        this.fileName = StringUtils.getFilename(fullFileName);
    }

    public Long getID() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean getHasThumbnail() {
        return hasThumbnail;
    }

    public void setHasThumbnail(boolean hasThumbnail) {
        this.hasThumbnail = hasThumbnail;
    }

    public boolean getHasThumbnailWebP() {
        return hasThumbnailWebP;
    }

    public void setHasThumbnailWebP(boolean hasThumbnailWebP) {
        this.hasThumbnailWebP = hasThumbnailWebP;
    }

    public boolean getHasSmallPicture() {
        return hasSmallPicture;
    }

    public void setHasSmallPicture(boolean hasSmallPicture) {
        this.hasSmallPicture = hasSmallPicture;
    }

    public boolean getHasMediumPicture() {
        return hasMediumPicture;
    }

    public void setHasMediumPicture(boolean hasMediumPicture) {
        this.hasMediumPicture = hasMediumPicture;
    }

    public boolean getHasLargePicture() {
        return hasLargePicture;
    }

    public void setHasLargePicture(boolean hasLargePicture) {
        this.hasLargePicture = hasLargePicture;
    }

    public boolean getHasWebP() {
        return hasWebP;
    }

    public void setHasWebP(boolean hasWebP) {
        this.hasWebP = hasWebP;
    }

    public boolean getHasSmallWebP() {
        return hasSmallWebP;
    }

    public void setHasSmallWebP(boolean hasSmallWebP) {
        this.hasSmallWebP = hasSmallWebP;
    }

    public boolean getHasMediumWebP() {
        return hasMediumWebP;
    }

    public void setHasMediumWebP(boolean hasMediumWebP) {
        this.hasMediumWebP = hasMediumWebP;
    }

    public boolean getHasLargeWebP() {
        return hasLargeWebP;
    }

    public void setHasLargeWebP(boolean hasLargeWebP) {
        this.hasLargeWebP = hasLargeWebP;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public void setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
    }
}
