package edu.utdallas.cs6303.finalproject.services.image;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import com.luciad.imageio.webp.WebPWriteParam;

public class WebPConverter {
    public static boolean convertFromImageToWebP(String inFile) {
        return convertFromImageToWebP(inFile, true);
    }

    public static boolean convertFromImageToWebP(String inFile, boolean force) {
        if (force || !(new File(inFile + ".webp").exists())) {
            return convertFromImageToWebP(inFile, inFile + ".webp");
        }
        return false;
    }

    public static boolean convertFromImageToWebP(String inFile, String outFile) {
        try {
            BufferedImage image = ImageIO.read(new File(inFile));
            if (!outFile.endsWith(".webp")) {
                outFile = outFile + ".webp";
            }
            ImageWriter    imageWriter = ImageIO.getImageWritersByMIMEType("image/webp").next();
            WebPWriteParam writeParam  = new WebPWriteParam(imageWriter.getLocale());
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
            imageWriter.setOutput(new FileImageOutputStream(new File(outFile)));
            imageWriter.write(null, new IIOImage(image, null, null), writeParam);
        } catch (Exception e) {
            return false;
            // nop just pass 0's to not resize if image doesn't exist to not have to
            // duplicate file handling code.
        }
        return true;
    }
}