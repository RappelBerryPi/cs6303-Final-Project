package edu.utdallas.cs6303.finalproject.services.image;

import javax.imageio.ImageIO;

import edu.utdallas.cs6303.finalproject.controllers.FileUploadController;
import edu.utdallas.cs6303.finalproject.model.database.MimeType;
import edu.utdallas.cs6303.finalproject.model.database.UploadedFile;
import edu.utdallas.cs6303.finalproject.model.database.repositories.MimeTypeRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UploadedFileRepository;
import edu.utdallas.cs6303.finalproject.services.storage.StorageServiceInterface;
import edu.utdallas.cs6303.finalproject.services.storage.enums.StorageServiceSizeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class ImageResizer implements ImageResizerInterface {

    @Autowired
    private StorageServiceInterface storageService;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @Autowired
    private MimeTypeRepository mimeTypeRepository;

    @Override
    public boolean resize(Path inFile, Path outFile, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(inFile.toFile());
            return resizeAndSaveImage(image, outFile.toFile(), width, height);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean resizeAndSaveImage(final BufferedImage image, File outFile, final int width, final int height) {
        BufferedImage outImage = resizeImage(image, width, height);
        String        fileType = "jpg";
        if (outFile.getName().contains(".")) {
            fileType = outFile.getName().substring(outFile.getName().lastIndexOf(".") + 1);
        }
        try {
            ImageIO.write(outImage, fileType, outFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // easier to just return false and say the conversion failed if the file is
                          // unwritable because of file type.
        }
    }

    @Override
    public BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage    image2     = new BufferedImage(width, height, image.getType());
        final Graphics2D graphics2D = image2.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return image2;
    }

    @Override
    public boolean resizeMaintainingAspectRatio(Path inFile, Path outFile, StorageServiceSizeEnum size) {
        try {
            BufferedImage image         = ImageIO.read(inFile.toFile());
            int           height        = -1;
            int           width         = -1;
            int           currentHeight = image.getHeight();
            int           currentWidth  = image.getWidth();
            float         aspectRatio;
            if (currentHeight > currentWidth) {
                height      = size.getSize();
                aspectRatio = (float) height / (float) currentHeight;
                width       = Math.round(aspectRatio * currentWidth);
            } else {
                width       = size.getSize();
                aspectRatio = (float) width / (float) currentWidth;
                height      = Math.round(aspectRatio * currentHeight);
            }
            if (aspectRatio > 1.0f) {
                return false; // there's no need to distort the image
            }
            return resizeAndSaveImage(image, outFile.toFile(), width, height);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Thread> convertAndResizeImage(String fileName) {
        ImageResizer resizer    = new ImageResizer();
        List<Thread> threads    = new LinkedList<>();
        var          ActualFile = this.storageService.load(fileName, StorageServiceSizeEnum.ROOT);
        try {
            threads.add(new Thread(() -> WebPConverter.convertFromImageToWebP(ActualFile.toAbsolutePath().toString(), false)));

            Arrays.stream(StorageServiceSizeEnum.values()).filter(x -> x != StorageServiceSizeEnum.ROOT).forEach(size -> {
                Path sizePath = this.storageService.load(fileName, size);
                if (!sizePath.toFile().exists()) {
                    Thread thread = new Thread(() -> {
                        if (resizer.resizeMaintainingAspectRatio(ActualFile, sizePath, size)) {
                            WebPConverter.convertFromImageToWebP(sizePath.toAbsolutePath().toString());
                        }
                    });
                    threads.add(thread);
                }

            });
            for (Thread T : threads) {
                T.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return threads;
    }

    @Override
    public UploadedFile addEntryIntoUploadedFileEntryTable(Path ActualFile) {
        String       fileName     = ActualFile.getFileName().toString();
        String       fileNameWebP = fileName + ".webp";
        UploadedFile file         = uploadedFileRepository.findByFileName(fileName);
        if (file == null) {
            file = new UploadedFile(FileUploadController.REQUESTMAPPING + "/" + fileName);
        }
        file.setHasWebP(storageService.exists(fileNameWebP, StorageServiceSizeEnum.ROOT));

        file.setHasThumbnail(storageService.exists(fileName, StorageServiceSizeEnum.THUMBNAIL));
        file.setHasThumbnailWebP(storageService.exists(fileNameWebP, StorageServiceSizeEnum.THUMBNAIL));

        file.setHasSmallPicture(storageService.exists(fileName, StorageServiceSizeEnum.SMALL));
        file.setHasSmallWebP(storageService.exists(fileNameWebP, StorageServiceSizeEnum.SMALL));

        file.setHasMediumPicture(storageService.exists(fileName, StorageServiceSizeEnum.MEDIUM));
        file.setHasMediumWebP(storageService.exists(fileNameWebP, StorageServiceSizeEnum.MEDIUM));

        file.setHasLargePicture(storageService.exists(fileName, StorageServiceSizeEnum.LARGE));
        file.setHasLargeWebP(storageService.exists(fileNameWebP, StorageServiceSizeEnum.LARGE));

        String fileExtension = StringUtils.getFilenameExtension(fileName);
        MimeType mimeType = mimeTypeRepository.findFirstByFileExtension(fileExtension);
        if (mimeType == null) {
            mimeType = new MimeType();
            mimeType.setFileExtension(fileExtension);
            mimeType.setMimeTypeString("application/octet");
            mimeTypeRepository.save(mimeType);
        }
        file.setMimeType(mimeType);
        uploadedFileRepository.save(file);

        return file;
    }

}