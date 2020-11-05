package edu.utdallas.cs6303.finalproject.model.validation.photo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.utdallas.cs6303.finalproject.model.database.UploadedFile;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UploadedFileRepository;


public class PhotoPickValidation implements ConstraintValidator<PhotoPickIsValid, String> {

    private UploadedFileRepository uploadedFileRepository;

    public PhotoPickValidation(UploadedFileRepository uploadedFileRepository) {
        this.uploadedFileRepository = uploadedFileRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        UploadedFile uploadedFile = uploadedFileRepository.findByFileName(value);
        boolean isValid = true;
        if (uploadedFile == null || !uploadedFile.getMimeType().getMimeTypeString().startsWith("image")) {
            isValid = false;
        }
        return isValid;
    }
}