package edu.utdallas.cs6303.finalproject.services.image;

import java.nio.file.Path;
import java.util.List;

import edu.utdallas.cs6303.finalproject.model.database.UploadedFile;
import edu.utdallas.cs6303.finalproject.services.storage.enums.StorageServiceSizeEnum;

import java.awt.image.BufferedImage;
import java.io.File;

public interface ImageResizerInterface {

    BufferedImage resizeImage(BufferedImage image, int width, int height);

    List<Thread> ConvertAndResizeImage(String fileName);

    boolean resize(Path inFile, Path outFile, int width, int height);

    boolean resizeAndSaveImage(final BufferedImage image, File outFile, final int width, final int height);

    boolean resizeMaintainingAspectRatio(Path inFile, Path outFile, StorageServiceSizeEnum size);

    UploadedFile addEntryIntoUploadedFileEntryTable(Path ActualFile);

}
