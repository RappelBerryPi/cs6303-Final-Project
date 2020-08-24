package edu.utdallas.cs6303.finalproject.services.storage.enums;

public enum StorageServiceSizeEnum {
    ROOT(0),
    THUMBNAIL(250),
    SMALL(500),
    MEDIUM(1024),
    LARGE(1920);

    private int size;
    StorageServiceSizeEnum(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }
}