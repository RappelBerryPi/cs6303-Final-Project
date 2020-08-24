package edu.utdallas.cs6303.finalproject.services.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

import edu.utdallas.cs6303.finalproject.model.database.UploadedFile;
import edu.utdallas.cs6303.finalproject.services.storage.enums.StorageServiceSizeEnum;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;;

public interface StorageServiceInterface {

    boolean exists(String fileName, StorageServiceSizeEnum size);

    Path load(String filename, StorageServiceSizeEnum size);

    Resource loadAsResource(String filename, StorageServiceSizeEnum size);

    Stream<Path> loadAll(StorageServiceSizeEnum size);
    Stream<Path> loadAllFromFileSystem(StorageServiceSizeEnum size);
    Stream<String> getFileStream();

    void deleteAll();
    void init();
    UploadedFile store(MultipartFile file, String filename);
    UploadedFile store(InputStream inputStream, String fileName) throws IOException;
}