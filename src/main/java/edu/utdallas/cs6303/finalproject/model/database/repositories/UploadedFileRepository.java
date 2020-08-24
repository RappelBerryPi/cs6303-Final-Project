package edu.utdallas.cs6303.finalproject.model.database.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utdallas.cs6303.finalproject.model.database.UploadedFile;

@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
    UploadedFile findByFileName(String fileName);

    UploadedFile findFirstByOrderByIdAsc();

    List<UploadedFile> findByFileNameEndingWith(String fileExtension);

    Page<UploadedFile> findByMimeTypeMimeTypeStartingWith(String mimeTypeMajor, Pageable pageable);

    List<UploadedFile> findAllByMimeTypeMimeTypeStartingWith(String mimeTypeMajor);

    List<UploadedFile> findByMimeTypeIsNull();
}