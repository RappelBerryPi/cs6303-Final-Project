package edu.utdallas.cs6303.finalproject.services.image;

import edu.utdallas.cs6303.finalproject.controllers.FileUploadController;
import edu.utdallas.cs6303.finalproject.model.database.UploadedFile;
import edu.utdallas.cs6303.finalproject.model.device.DeviceInfo;
import org.springframework.stereotype.Service;

@Service
public class ImageFileURLBuilder implements ImageFileURLBuilderInterface {

    private DeviceInfo   deviceInfo;
    private UploadedFile uploadedFile;

    public ImageFileURLBuilder() {
        // no necessary code to execute as this is implementing a builder interface and there is nothing to implement in the constructor.
    }

    @Override
    public ImageFileURLBuilderInterface forDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }

    @Override
    public ImageFileURLBuilderInterface with(UploadedFile file) {
        this.uploadedFile = file;
        return this;
    }

    @Override
    public String build() {
        String uploadedFileName = this.uploadedFile.getFileName();
        String webpExtension    = this.deviceInfo.getSupportsWebP() ? ".webp" : "";
        String deviceURLSize    = this.deviceInfo.getImgFileSizePath();
        return FileUploadController.REQUESTMAPPING + "/" + deviceURLSize + "/" + uploadedFileName + webpExtension;
    }
}