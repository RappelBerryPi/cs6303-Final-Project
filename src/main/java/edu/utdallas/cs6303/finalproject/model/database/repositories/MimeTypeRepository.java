package edu.utdallas.cs6303.finalproject.model.database.repositories;

import java.util.List;

import edu.utdallas.cs6303.finalproject.model.database.MimeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MimeTypeRepository extends JpaRepository<MimeType, Long> {

     List<MimeType> findByMimeTypeString(String mimeTypeString);
     MimeType findFirstByFileExtension(String fileExtension);

}