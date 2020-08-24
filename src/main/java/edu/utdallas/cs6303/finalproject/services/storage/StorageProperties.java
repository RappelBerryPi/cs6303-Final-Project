package edu.utdallas.cs6303.finalproject.services.storage;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;

import edu.utdallas.cs6303.finalproject.services.storage.enums.StorageServiceSizeEnum;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
    private String rootLocation      = "upload-dir";
    private String smallLocation     = "upload-dir/small";
    private String mediumLocation    = "upload-dir/medium";
    private String largeLocation     = "upload-dir/large";
    private String thumbNailLocation = "upload-dir/thumbnail";

    private Map<StorageServiceSizeEnum, String> hashDictionary = new EnumMap<>(StorageServiceSizeEnum.class);

    public StorageProperties() {
        hashDictionary.put(StorageServiceSizeEnum.ROOT, this.rootLocation);
        hashDictionary.put(StorageServiceSizeEnum.SMALL, this.smallLocation);
        hashDictionary.put(StorageServiceSizeEnum.MEDIUM, this.mediumLocation);
        hashDictionary.put(StorageServiceSizeEnum.LARGE, this.largeLocation);
        hashDictionary.put(StorageServiceSizeEnum.THUMBNAIL, this.thumbNailLocation);
    }

    public String getLocation(StorageServiceSizeEnum size) {
        return this.hashDictionary.get(size);
    }

    public Path getLocationAsPath(StorageServiceSizeEnum size) {
        Path p = Paths.get(getLocation(size));
        if (p.isAbsolute()) {
            return p;
        }
        try {
            URI    uri  = StorageProperties.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            String path = uri.getPath();
            File   f;
            if (path == null) {
                path = uri.toString();
                if (!(path.startsWith("jar:") && path.endsWith("!/"))) {
                    System.err.println("UNKNOWN URI: " + uri.toString() + " " + p.toString());
                    return p;
                }
                // jar:file:/C:/Users/leppa/workspace/Projects/ImperfectMommy/target/main-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!/
                path = path.substring(4);
                int filenameExtensionPosition = path.indexOf(".jar!");
                path = path.substring(0, filenameExtensionPosition + 4);
                uri  = new URI(path);
            }
            f = new File(uri);
            f = f.getParentFile().getParentFile(); // get grandparent becuse this resolves to
                                                   // projectroot/target/classes and we're looking for
                                                   // projectroot/upload-dir
            p = f.toPath().resolve(p);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return p;
        }
        this.hashDictionary.replace(size, p.toAbsolutePath().toString());
        return p;
    }

    public void setLocation(StorageServiceSizeEnum size, String location) {
        this.hashDictionary.replace(size, location);
    }
}