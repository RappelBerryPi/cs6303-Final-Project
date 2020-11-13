package edu.utdallas.cs6303.finalproject.services.image;

import edu.utdallas.cs6303.finalproject.model.device.DeviceInfo;
import edu.utdallas.cs6303.finalproject.services.storage.enums.StorageServiceSizeEnum;
import edu.utdallas.cs6303.finalproject.controllers.FileUploadController;
import edu.utdallas.cs6303.finalproject.model.database.UploadedFile;

public interface ImageFileURLBuilderInterface {

    public ImageFileURLBuilderInterface forDeviceInfo(DeviceInfo deviceInfo);

    public ImageFileURLBuilderInterface with(UploadedFile file);

    public String build();

    default String buildThumbnailURL(UploadedFile file) {
        return FileUploadController.REQUEST_MAPPING + "/" + DeviceInfo.capitalize(StorageServiceSizeEnum.THUMBNAIL) + "/" + file.getFileName();
    }

    default String buildLargeURL(UploadedFile file) {
        return FileUploadController.REQUEST_MAPPING + "/" + DeviceInfo.capitalize(StorageServiceSizeEnum.LARGE) + "/" + file.getFileName();
    }

    default String getBaseThumbnailURL() {
        return FileUploadController.REQUEST_MAPPING + "/" + DeviceInfo.capitalize(StorageServiceSizeEnum.THUMBNAIL) + "/";
    }
}
