package edu.utdallas.cs6303.finalproject.services.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.stream.Stream;

import edu.utdallas.cs6303.finalproject.model.database.UploadedFile;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UploadedFileRepository;
import edu.utdallas.cs6303.finalproject.services.image.ImageResizerInterface;
import edu.utdallas.cs6303.finalproject.services.storage.enums.StorageServiceSizeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService implements StorageServiceInterface {

    @Autowired
    private StorageProperties storageProperties;

    @Autowired
    UploadedFileRepository uploadedFileRepository;

    @Autowired
    ImageResizerInterface imageResizer;

    @Override
    public UploadedFile store(MultipartFile file, String fileName) {
        if (fileName.isEmpty()) {
            fileName = StringUtils.cleanPath(file.getOriginalFilename());
        }
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + fileName);
            }
            if (fileName.contains("..")) {
                // This is a security check
                throw new StorageException("Cannot store file with relative path outside current directory " + fileName);
            }
            try (InputStream inputStream = file.getInputStream()) {
                return store(inputStream, fileName);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + fileName, e);
        }
    }

    @Override
    public UploadedFile store(InputStream inputStream, String fileName) throws IOException {
        Path actualFile = this.storageProperties.getLocationAsPath(StorageServiceSizeEnum.ROOT).resolve(fileName);
        Files.copy(inputStream, actualFile, StandardCopyOption.REPLACE_EXISTING);

        var threads = imageResizer.convertAndResizeImage(fileName);
        for (Thread t : threads) {
            try {
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imageResizer.addEntryIntoUploadedFileEntryTable(actualFile);
    }

    @Override
    public Resource loadAsResource(String fileName, StorageServiceSizeEnum size) {
        try {
            Path     file     = load(fileName, size);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + fileName, e);
        }
    }

    @Override
    public void deleteAll() {
        Arrays.stream(StorageServiceSizeEnum.values()).forEach(x -> {
            FileSystemUtils.deleteRecursively(this.storageProperties.getLocationAsPath(x).toFile());
        });
    }

    @Override
    public void init() {
        try {
            for (StorageServiceSizeEnum value : StorageServiceSizeEnum.values()) {
                Files.createDirectories(this.storageProperties.getLocationAsPath(value));
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public Path load(String fileName, StorageServiceSizeEnum size) {
        return this.storageProperties.getLocationAsPath(size).resolve(fileName);
    }

    @Override
    public Stream<String> getFileStream() {
        StorageServiceSizeEnum size       = StorageServiceSizeEnum.ROOT;
        return this.loadAll(size).map(path -> path.getFileName().relativize(this.storageProperties.getLocationAsPath(size)).toString());
    }

    @Override
    public Stream<Path> loadAll(StorageServiceSizeEnum size) {
        Path startPath = this.storageProperties.getLocationAsPath(size);
        return uploadedFileRepository.findAll().stream().map(m -> startPath.resolve(m.getFileName()));
    }

    @Override
    public boolean exists(String fileName, StorageServiceSizeEnum size) {
        return this.load(fileName, size).toFile().exists();
    }

    @Override
    public Stream<Path> loadAllFromFileSystem(StorageServiceSizeEnum size) {
        Path startPath = this.storageProperties.getLocationAsPath(size);
        try {
            return Files.list(startPath);
        } catch (IOException e) {
            throw new StorageException("Could not List Items in Folder" + startPath.toAbsolutePath().toString(), e);
        }
    }
}
