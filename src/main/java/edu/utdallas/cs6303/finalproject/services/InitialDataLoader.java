package edu.utdallas.cs6303.finalproject.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.utdallas.cs6303.finalproject.model.database.MimeType;
import edu.utdallas.cs6303.finalproject.model.database.Privilege;
import edu.utdallas.cs6303.finalproject.model.database.Role;
import edu.utdallas.cs6303.finalproject.model.database.StoreItem;
import edu.utdallas.cs6303.finalproject.model.database.StoreItemSize;
import edu.utdallas.cs6303.finalproject.model.database.UploadedFile;
import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemCategoryEnum;
import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemSizeEnum;
import edu.utdallas.cs6303.finalproject.model.database.repositories.MimeTypeRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.RoleRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.StoreItemRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.StoreItemSizeRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UploadedFileRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UserRepository;
import edu.utdallas.cs6303.finalproject.services.image.ImageResizerInterface;
import edu.utdallas.cs6303.finalproject.services.storage.StorageServiceInterface;
import edu.utdallas.cs6303.finalproject.services.storage.enums.StorageServiceSizeEnum;
import edu.utdallas.cs6303.finalproject.services.user.UserServiceInterface;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StorageServiceInterface storageService;

    @Autowired
    private ImageResizerInterface imageResizer;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @Autowired
    MimeTypeRepository mimeTypeRepository;

    @Autowired
    UserServiceInterface userService;

    @Autowired
    StoreItemRepository storeItemRepository;

    @Autowired
    StoreItemSizeRepository storeItemSizeRepository;

    @Value("${initialDataLoader.adminUserName}")
    private String adminUserName;

    @Value("${initialDataLoader.adminUserPassword}")
    private String adminUserPassword;

    @Value("${initialDataLoader.adminUserEmail}")
    private String adminUserEmail;

    // we're assuming that this is being run in a local origin based sales tax state
    // (such as texas) and so the sales
    // tax will be the same no matter the destination. We are also assuming that
    // shipping is only happening in the same
    // or nearby zip codes and as such there is no need for several zip codes.

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        createUsersAndPrivileges();

        createMimeTypeRepository();

        // make sure that every file has a mapped mimeType
        List<UploadedFile> files = uploadedFileRepository.findByMimeTypeIsNull();
        if (files.isEmpty()) {
            for (UploadedFile file : files) {
                String   fileExtension = StringUtils.getFilenameExtension(file.getFileName());
                MimeType mimeType      = mimeTypeRepository.findFirstByFileExtension(fileExtension);
                file.setMimeType(mimeType);
            }
            uploadedFileRepository.saveAll(files);
        }

        storageService.init();

        // process images
        buildUploadedFileRepository();

        buildStore();

        alreadySetup = true;
    }

    private void buildStore() {
        buildStoreItem("Movie Theater Butter","The Epic classic revisited with layers of delicious butter drizzled on top. This salty wonder will have your tastebuds delighted for days.", "Buttery, Salty, nothing beats the original.", StoreItemCategoryEnum.CLASSIC);
        buildStoreItem("Cheddar","This Popcorn is covered in our special Cheddar seasoning, we even use Real Cheese!", "Salty, Cheesy, Perfect.", StoreItemCategoryEnum.CLASSIC);
        buildStoreItem("Kettle Corn","Kettle corn is a sweet mix of Sweet and Delicious, try a bag!", "Nothing like the classic taste of YUM!", StoreItemCategoryEnum.CLASSIC);

        buildStoreItem("Pumpkin Pie Drizzle","You've got Pumpkin Spice everything, here's one more for the books.", "Is it Fall Yet?", StoreItemCategoryEnum.SEASONAL);
        buildStoreItem("Caramel Apple","Is Your mouth watering yet? What's better than caramel covered apples? Popcorn!", "Sweeter than Honey, As tart as you want. Zangy, and Tasty", StoreItemCategoryEnum.SEASONAL);
        buildStoreItem("Hot Cocoa","Can someone say 'Polar Express?!?", "We got Hot Chocolate!", StoreItemCategoryEnum.SEASONAL);

        buildStoreItem("Rainbow","Its colorful and delicious", "See the Rainbow, Believe in the flavors, taste it and believe", StoreItemCategoryEnum.FAVORITES);
        buildStoreItem("Birthday Cake","Its colorful and Fun (not to mention delicious)!", "Happy Birthday!", StoreItemCategoryEnum.FAVORITES);
        buildStoreItem("Honey Butter","Just like Mammaw used to make, but you know, in popcorn form.", "Honey, Butter, what more is there to say? Try it, love it, eat it.", StoreItemCategoryEnum.FAVORITES);

        buildStoreItem("Paradise Blend","Ever wanted to have a Pina Colada, and a Margarita? Here's your chance!", "Wisk me away to good times.", StoreItemCategoryEnum.BLENDS);
        buildStoreItem("Sweet and Savory Blend","Sweet, Savory, Buttery, Deliciousness all wrapped in a little bow, What's stopping you from trying it?", "The best of both worlds!", StoreItemCategoryEnum.BLENDS);
    }

    private void buildStoreItem(String name, String longDescription, String shortDescription, StoreItemCategoryEnum category) {
        if (storeItemRepository.findFirstByName(name).isPresent()) {
            return;
        }
        StoreItem storeItem = new StoreItem();
        storeItem.setAmountInStock(1000);
        storeItem.setCategory(category);
        storeItem.setImage(uploadedFileRepository.findByFileNameEndingWith(".jpg").get(new Random().nextInt(10)));
        storeItem.setLongDescription(longDescription);
        storeItem.setName(name);
        storeItem.setShortDescription(shortDescription);
        storeItem.setVisible(true);
        List<StoreItemSize> items = new ArrayList<>();
        int                 i     = 0;
        for (StoreItemSizeEnum size : StoreItemSizeEnum.values()) {
            StoreItemSize item = new StoreItemSize();
            item.setCost(new BigDecimal(i++));
            item.setSize(size);
            item.setStoreItem(storeItem);
            items.add(item);
        }
        storeItem.setGroupStoreItems(items);
        storeItem = storeItemRepository.save(storeItem);
        for (StoreItemSize item : items) {
            item.setStoreItem(storeItem);
        }
        storeItemSizeRepository.saveAll(items);
    }

    private void createUsersAndPrivileges() {
        List<Privilege> userPrivileges     = userService.getBasicUserPrivileges();
        List<Privilege> employeePrivileges = userService.getBasicEmployeePrivileges();
        List<Privilege> adminPrivileges    = userService.getAdminPrivileges();

        Role adminRole    = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        Role userRole     = createRoleIfNotFound("ROLE_USER", userPrivileges);
        Role employeeRole = createRoleIfNotFound("ROLE_EMPLOYEE", employeePrivileges);
        createRoleIfNotFound("ROLE_ANONYMOUS", null);

        List<Role> roles = new ArrayList<>();
        roles.add(userRole);
        roles.add(employeeRole);
        roles.add(adminRole);

        createUserIfNotFound(this.adminUserName, "Admin", "Admin", this.adminUserPassword, roles, this.adminUserEmail);
    }

    private void createMimeTypeRepository() {
        if (mimeTypeRepository.count() != 0) {
            return;
        }
        final String IMAGE_VND_DWG                = "image/vnd.dwg";
        final String IMAGE_X_DWG                  = "image/x-dwg";
        final String IMAGE_JPEG                   = "image/jpeg";
        final String IMAGE_QUICKTIME              = "image/x-quicktime";
        final String VIDEO_MPEG                   = "video/mpeg";
        final String VIDEO_QUICKTIME              = "video/quicktime";
        final String AUDIO_AIFF                   = "audio/aiff";
        final String AUDIO_X_AIFF                 = "audio/x-aiff";
        final String AUDIO_MAKE                   = "audio/make";
        final String AUDIO_MIDI                   = "audio/midi";
        final String AUDIO_MPEG                   = "audio/mpeg";
        final String AUDIO_X_PN_REALAUDIO         = "audio/x-pn-realaudio";
        final String AUDIO_X_PN_REALAUDIO_PLUGIN  = "audio/x-pn-realaudio-plugin";
        final String APPLICATION_MSWORD           = "application/msword";
        final String APPLICATION_MSPOWERPOINT     = "application/mspowerpoint";
        final String APPLICATION_VND_MSPOWERPOINT = "application/vnd.ms-powerpoint";
        final String APPLICATION_MSEXCEL          = "application/excel";
        final String APPLICATION_MS_X_EXCEL       = "application/x-excel";
        final String APPLICATION_VND_MSEXCEL      = "application/vnd.ms-excel";

        List<MimeType> pairs = new ArrayList<>();
        pairs.add(new MimeType("art", "image/x-jg"));
        pairs.add(new MimeType("bm", "image/bmp"));
        pairs.add(new MimeType("bmp", "image/bmp"));
        pairs.add(new MimeType("bmp", "image/x-windows-bmp"));
        pairs.add(new MimeType("dwg", IMAGE_VND_DWG));
        pairs.add(new MimeType("dwg", IMAGE_X_DWG));
        pairs.add(new MimeType("dxf", IMAGE_VND_DWG));
        pairs.add(new MimeType("dxf", IMAGE_X_DWG));
        pairs.add(new MimeType("fif", "image/fif"));
        pairs.add(new MimeType("flo", "image/florian"));
        pairs.add(new MimeType("fpx", "image/vnd.fpx"));
        pairs.add(new MimeType("fpx", "image/vnd.net-fpx"));
        pairs.add(new MimeType("g3", "image/g3fax"));
        pairs.add(new MimeType("gif", "image/gif"));
        pairs.add(new MimeType("ico", "image/x-icon"));
        pairs.add(new MimeType("ief", "image/ief"));
        pairs.add(new MimeType("iefs", "image/ief"));
        pairs.add(new MimeType("jfif", IMAGE_JPEG));
        pairs.add(new MimeType("jfif-tbnl", IMAGE_JPEG));
        pairs.add(new MimeType("jpe", IMAGE_JPEG));
        pairs.add(new MimeType("jpeg", IMAGE_JPEG));
        pairs.add(new MimeType("jpg", IMAGE_JPEG));
        pairs.add(new MimeType("jps", "image/x-jps"));
        pairs.add(new MimeType("jut", "image/jutvision"));
        pairs.add(new MimeType("mcf", "image/vasa"));
        pairs.add(new MimeType("nap", "image/naplps"));
        pairs.add(new MimeType("naplps", "image/naplps"));
        pairs.add(new MimeType("nif", "image/x-niff"));
        pairs.add(new MimeType("niff", "image/x-niff"));
        pairs.add(new MimeType("pbm", "image/x-portable-bitmap"));
        pairs.add(new MimeType("pct", "image/x-pict"));
        pairs.add(new MimeType("pcx", "image/x-pcx"));
        pairs.add(new MimeType("pgm", "image/x-portable-graymap"));
        pairs.add(new MimeType("pgm", "image/x-portable-greymap"));
        pairs.add(new MimeType("pic", "image/pict"));
        pairs.add(new MimeType("pict", "image/pict"));
        pairs.add(new MimeType("pm", "image/x-xpixmap"));
        pairs.add(new MimeType("png", "image/png"));
        pairs.add(new MimeType("pnm", "image/x-portable-anymap"));
        pairs.add(new MimeType("ppm", "image/x-portable-pixmap"));
        pairs.add(new MimeType("qif", IMAGE_QUICKTIME));
        pairs.add(new MimeType("qti", IMAGE_QUICKTIME));
        pairs.add(new MimeType("qtif", IMAGE_QUICKTIME));
        pairs.add(new MimeType("ras", "image/cmu-raster"));
        pairs.add(new MimeType("ras", "image/x-cmu-raster"));
        pairs.add(new MimeType("rast", "image/cmu-raster"));
        pairs.add(new MimeType("rf", "image/vnd.rn-realflash"));
        pairs.add(new MimeType("rgb", "image/x-rgb"));
        pairs.add(new MimeType("rp", "image/vnd.rn-realpix"));
        pairs.add(new MimeType("svf", IMAGE_VND_DWG));
        pairs.add(new MimeType("svf", IMAGE_X_DWG));
        pairs.add(new MimeType("tif", "image/tiff"));
        pairs.add(new MimeType("tif", "image/x-tiff"));
        pairs.add(new MimeType("tiff", "image/tiff"));
        pairs.add(new MimeType("tiff", "image/x-tiff"));
        pairs.add(new MimeType("turbot", "image/florian"));
        pairs.add(new MimeType("wbmp", "image/vnd.wap.wbmp"));
        pairs.add(new MimeType("webp", IMAGE_JPEG));
        pairs.add(new MimeType("webp", "image/webp"));
        pairs.add(new MimeType("x-png", "image/png"));
        pairs.add(new MimeType("xbm", "image/x-xbitmap"));
        pairs.add(new MimeType("xbm", "image/x-xbm"));
        pairs.add(new MimeType("xbm", "image/xbm"));
        pairs.add(new MimeType("xif", "image/vnd.xiff"));
        pairs.add(new MimeType("xpm", "image/x-xpixmap"));
        pairs.add(new MimeType("xpm", "image/xpm"));
        pairs.add(new MimeType("xwd", "image/x-xwd"));
        pairs.add(new MimeType("xwd", "image/x-xwindowdump"));
        pairs.add(new MimeType("afl", "video/animaflex"));
        pairs.add(new MimeType("asf", "video/x-ms-asf"));
        pairs.add(new MimeType("asx", "video/x-ms-asf"));
        pairs.add(new MimeType("asx", "video/x-ms-asf-plugin"));
        pairs.add(new MimeType("avi", "video/avi"));
        pairs.add(new MimeType("avi", "video/msvideo"));
        pairs.add(new MimeType("avi", "video/x-msvideo"));
        pairs.add(new MimeType("avs", "video/avs-video"));
        pairs.add(new MimeType("dif", "video/x-dv"));
        pairs.add(new MimeType("dl", "video/dl"));
        pairs.add(new MimeType("dl", "video/x-dl"));
        pairs.add(new MimeType("dv", "video/x-dv"));
        pairs.add(new MimeType("fli", "video/fli"));
        pairs.add(new MimeType("fli", "video/x-fli"));
        pairs.add(new MimeType("fmf", "video/x-atomic3d-feature"));
        pairs.add(new MimeType("gl", "video/gl"));
        pairs.add(new MimeType("gl", "video/x-gl"));
        pairs.add(new MimeType("isu", "video/x-isvideo"));
        pairs.add(new MimeType("m2v", VIDEO_MPEG));
        pairs.add(new MimeType("mjpg", "video/x-motion-jpeg"));
        pairs.add(new MimeType("moov", VIDEO_QUICKTIME));
        pairs.add(new MimeType("mov", VIDEO_QUICKTIME));
        pairs.add(new MimeType("movie", "video/x-sgi-movie"));
        pairs.add(new MimeType("mp2", VIDEO_MPEG));
        pairs.add(new MimeType("mp2", "video/x-mpeg"));
        pairs.add(new MimeType("mp2", "video/x-mpeq2a"));
        pairs.add(new MimeType("mp3", VIDEO_MPEG));
        pairs.add(new MimeType("mp3", "video/x-mpeg"));
        pairs.add(new MimeType("mpa", VIDEO_MPEG));
        pairs.add(new MimeType("mpe", VIDEO_MPEG));
        pairs.add(new MimeType("mpeg", VIDEO_MPEG));
        pairs.add(new MimeType("mpg", VIDEO_MPEG));
        pairs.add(new MimeType("mv", "video/x-sgi-movie"));
        pairs.add(new MimeType("ogg", "video/ogg"));
        pairs.add(new MimeType("ogv", "video/ogg"));
        pairs.add(new MimeType("qt", VIDEO_QUICKTIME));
        pairs.add(new MimeType("qtc", "video/x-qtc"));
        pairs.add(new MimeType("rv", "video/vnd.rn-realvideo"));
        pairs.add(new MimeType("scm", "video/x-scm"));
        pairs.add(new MimeType("vdo", "video/vdo"));
        pairs.add(new MimeType("viv", "video/vivo"));
        pairs.add(new MimeType("viv", "video/vnd.vivo"));
        pairs.add(new MimeType("vivo", "video/vivo"));
        pairs.add(new MimeType("vivo", "video/vnd.vivo"));
        pairs.add(new MimeType("vos", "video/vosaic"));
        pairs.add(new MimeType("xdr", "video/x-amt-demorun"));
        pairs.add(new MimeType("xsr", "video/x-amt-showrun"));
        pairs.add(new MimeType("aif", AUDIO_AIFF));
        pairs.add(new MimeType("aif", AUDIO_X_AIFF));
        pairs.add(new MimeType("aifc", AUDIO_AIFF));
        pairs.add(new MimeType("aifc", AUDIO_X_AIFF));
        pairs.add(new MimeType("aiff", AUDIO_AIFF));
        pairs.add(new MimeType("aiff", AUDIO_X_AIFF));
        pairs.add(new MimeType("au", "audio/basic"));
        pairs.add(new MimeType("au", "audio/x-au"));
        pairs.add(new MimeType("funk", AUDIO_MAKE));
        pairs.add(new MimeType("gsd", "audio/x-gsm"));
        pairs.add(new MimeType("gsm", "audio/x-gsm"));
        pairs.add(new MimeType("it", "audio/it"));
        pairs.add(new MimeType("jam", "audio/x-jam"));
        pairs.add(new MimeType("kar", AUDIO_MIDI));
        pairs.add(new MimeType("la", "audio/nspaudio"));
        pairs.add(new MimeType("la", "audio/x-nspaudio"));
        pairs.add(new MimeType("lam", "audio/x-liveaudio"));
        pairs.add(new MimeType("lma", "audio/nspaudio"));
        pairs.add(new MimeType("lma", "audio/x-nspaudio"));
        pairs.add(new MimeType("m2a", AUDIO_MPEG));
        pairs.add(new MimeType("m3u", "audio/x-mpequrl"));
        pairs.add(new MimeType("mid", AUDIO_MIDI));
        pairs.add(new MimeType("mid", "audio/x-mid"));
        pairs.add(new MimeType("mid", "audio/x-midi"));
        pairs.add(new MimeType("midi", AUDIO_MIDI));
        pairs.add(new MimeType("midi", "audio/x-mid"));
        pairs.add(new MimeType("midi", "audio/x-midi"));
        pairs.add(new MimeType("mjf", "audio/x-vnd.audioexplosion.mjuicemediafile"));
        pairs.add(new MimeType("mod", "audio/mod"));
        pairs.add(new MimeType("mod", "audio/x-mod"));
        pairs.add(new MimeType("mp2", AUDIO_MPEG));
        pairs.add(new MimeType("mp2", "audio/x-mpeg"));
        pairs.add(new MimeType("mp3", "audio/mpeg3"));
        pairs.add(new MimeType("mp3", "audio/x-mpeg-3"));
        pairs.add(new MimeType("mpa", AUDIO_MPEG));
        pairs.add(new MimeType("mpg", AUDIO_MPEG));
        pairs.add(new MimeType("mpga", AUDIO_MPEG));
        pairs.add(new MimeType("my", AUDIO_MAKE));
        pairs.add(new MimeType("oga", "audio/ogg"));
        pairs.add(new MimeType("ogg", "audio/ogg"));
        pairs.add(new MimeType("pfunk", AUDIO_MAKE));
        pairs.add(new MimeType("pfunk", "audio/make.my.funk"));
        pairs.add(new MimeType("qcp", "audio/vnd.qcelp"));
        pairs.add(new MimeType("ra", AUDIO_X_PN_REALAUDIO));
        pairs.add(new MimeType("ra", AUDIO_X_PN_REALAUDIO_PLUGIN));
        pairs.add(new MimeType("ra", "audio/x-realaudio"));
        pairs.add(new MimeType("ram", AUDIO_X_PN_REALAUDIO));
        pairs.add(new MimeType("rm", AUDIO_X_PN_REALAUDIO));
        pairs.add(new MimeType("rmi", "audio/mid"));
        pairs.add(new MimeType("rmm", AUDIO_X_PN_REALAUDIO));
        pairs.add(new MimeType("rmp", AUDIO_X_PN_REALAUDIO));
        pairs.add(new MimeType("rmp", AUDIO_X_PN_REALAUDIO_PLUGIN));
        pairs.add(new MimeType("rpm", AUDIO_X_PN_REALAUDIO_PLUGIN));
        pairs.add(new MimeType("s3m", "audio/s3m"));
        pairs.add(new MimeType("sid", "audio/x-psid"));
        pairs.add(new MimeType("snd", "audio/basic"));
        pairs.add(new MimeType("snd", "audio/x-adpcm"));
        pairs.add(new MimeType("tsi", "audio/tsp-audio"));
        pairs.add(new MimeType("tsp", "audio/tsplayer"));
        pairs.add(new MimeType("voc", "audio/voc"));
        pairs.add(new MimeType("voc", "audio/x-voc"));
        pairs.add(new MimeType("vox", "audio/voxware"));
        pairs.add(new MimeType("vqe", "audio/x-twinvq-plugin"));
        pairs.add(new MimeType("vqf", "audio/x-twinvq"));
        pairs.add(new MimeType("vql", "audio/x-twinvq-plugin"));
        pairs.add(new MimeType("wav", "audio/wav"));
        pairs.add(new MimeType("wav", "audio/x-wav"));
        pairs.add(new MimeType("xm", "audio/xm"));
        pairs.add(new MimeType("doc", APPLICATION_MSWORD));
        pairs.add(new MimeType("docx", APPLICATION_MSWORD));
        pairs.add(new MimeType("dot", APPLICATION_MSWORD));
        pairs.add(new MimeType("mpp", "application/vnd.ms-project"));
        pairs.add(new MimeType("pko", "application/vnd.ms-pki.pko"));
        pairs.add(new MimeType("pot", APPLICATION_MSPOWERPOINT));
        pairs.add(new MimeType("pot", APPLICATION_VND_MSPOWERPOINT));
        pairs.add(new MimeType("ppa", APPLICATION_VND_MSPOWERPOINT));
        pairs.add(new MimeType("pps", APPLICATION_MSPOWERPOINT));
        pairs.add(new MimeType("pps", APPLICATION_VND_MSPOWERPOINT));
        pairs.add(new MimeType("ppt", APPLICATION_MSPOWERPOINT));
        pairs.add(new MimeType("ppt", "application/powerpoint"));
        pairs.add(new MimeType("ppt", APPLICATION_VND_MSPOWERPOINT));
        pairs.add(new MimeType("ppt", "application/x-mspowerpoint"));
        pairs.add(new MimeType("ppz", APPLICATION_MSPOWERPOINT));
        pairs.add(new MimeType("pwz", APPLICATION_VND_MSPOWERPOINT));
        pairs.add(new MimeType("sst", "application/vnd.ms-pki.certstore"));
        pairs.add(new MimeType("stl", "application/vnd.ms-pki.stl"));
        pairs.add(new MimeType("w6w", APPLICATION_MSWORD));
        pairs.add(new MimeType("wiz", APPLICATION_MSWORD));
        pairs.add(new MimeType("word", APPLICATION_MSWORD));
        pairs.add(new MimeType("xl", APPLICATION_MSEXCEL));
        pairs.add(new MimeType("xla", APPLICATION_MSEXCEL));
        pairs.add(new MimeType("xla", APPLICATION_MS_X_EXCEL));
        pairs.add(new MimeType("xlb", APPLICATION_MSEXCEL));
        pairs.add(new MimeType("xlb", APPLICATION_VND_MSEXCEL));
        pairs.add(new MimeType("xlb", APPLICATION_MS_X_EXCEL));
        pairs.add(new MimeType("xlc", APPLICATION_MSEXCEL));
        pairs.add(new MimeType("xlc", APPLICATION_VND_MSEXCEL));
        pairs.add(new MimeType("xlc", APPLICATION_MS_X_EXCEL));
        pairs.add(new MimeType("xld", APPLICATION_MSEXCEL));
        pairs.add(new MimeType("xld", APPLICATION_MS_X_EXCEL));
        pairs.add(new MimeType("xlk", APPLICATION_MSEXCEL));
        pairs.add(new MimeType("xlk", APPLICATION_MS_X_EXCEL));
        pairs.add(new MimeType("xll", APPLICATION_MSEXCEL));
        pairs.add(new MimeType("xll", APPLICATION_VND_MSEXCEL));
        pairs.add(new MimeType("xll", APPLICATION_MS_X_EXCEL));
        pairs.add(new MimeType("xlm", APPLICATION_MSEXCEL));
        pairs.add(new MimeType("xlm", APPLICATION_VND_MSEXCEL));
        pairs.add(new MimeType("xlm", APPLICATION_MS_X_EXCEL));
        pairs.add(new MimeType("xls", APPLICATION_MSEXCEL));
        pairs.add(new MimeType("xls", APPLICATION_VND_MSEXCEL));
        pairs.add(new MimeType("xls", APPLICATION_MS_X_EXCEL));
        pairs.add(new MimeType("xlsx", APPLICATION_VND_MSEXCEL));
        pairs.add(new MimeType("xlsx", APPLICATION_MS_X_EXCEL));
        pairs.add(new MimeType("xlt", APPLICATION_MSEXCEL));
        pairs.add(new MimeType("xlt", APPLICATION_MS_X_EXCEL));
        pairs.add(new MimeType("xlv", APPLICATION_MSEXCEL));
        pairs.add(new MimeType("xlv", APPLICATION_MS_X_EXCEL));
        pairs.add(new MimeType("xlw", APPLICATION_MSEXCEL));
        pairs.add(new MimeType("xlw", APPLICATION_VND_MSEXCEL));
        pairs.add(new MimeType("xlw", APPLICATION_MS_X_EXCEL));
        pairs.add(new MimeType("text", "text/plain"));
        pairs.add(new MimeType("txt", "text/plain"));
        pairs.add(new MimeType("arw", "image/x-sony-arw"));
        pairs.add(new MimeType("cr2", "image/x-canon-cr2"));
        pairs.add(new MimeType("crw", "image/x-canon-crw"));
        pairs.add(new MimeType("dcr", "image/x-kodak-dcr"));
        pairs.add(new MimeType("dng", "image/x-adobe-dng"));
        pairs.add(new MimeType("erf", "image/x-epson-erf"));
        pairs.add(new MimeType("k25", "image/x-kodak-k25"));
        pairs.add(new MimeType("kdc", "image/x-kodak-kdc"));
        pairs.add(new MimeType("mrw", "image/x-minolta-mrw"));
        pairs.add(new MimeType("nef", "image/x-nikon-nef"));
        pairs.add(new MimeType("orf", "image/x-olympus-orf"));
        pairs.add(new MimeType("pef", "image/x-pentax-pef"));
        pairs.add(new MimeType("raf", "image/x-fuji-raf"));
        pairs.add(new MimeType("raw", "image/x-panasonic-raw"));
        pairs.add(new MimeType("sr2", "image/x-sony-sr2"));
        pairs.add(new MimeType("srf", "image/x-sony-srf"));
        pairs.add(new MimeType("x3f", "image/x-sigma-x3f"));
        mimeTypeRepository.saveAll(pairs);
    }

    private void buildUploadedFileRepository() {
        this.getFileStream().filter(fileName -> fileName.endsWith("jpg") || fileName.endsWith("jpeg") || fileName.endsWith("png")).forEach(fileName -> {
            UploadedFile file = uploadedFileRepository.findByFileName(fileName);
            if (file == null) {
                var threads = imageResizer.convertAndResizeImage(fileName);
                threads.forEach(t -> {
                    try {
                        t.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            imageResizer.addEntryIntoUploadedFileEntryTable(storageService.load(fileName, StorageServiceSizeEnum.ROOT));
        });

        // process other files
        this.getFileStream().filter(fileName -> !(fileName.endsWith("jpg") || fileName.endsWith("jpeg") || fileName.endsWith("png") || fileName.endsWith("webp"))).forEach(fileName -> {
            UploadedFile file = uploadedFileRepository.findByFileName(fileName);
            if (file == null) {
                file = new UploadedFile();
                file.setFileName(fileName);
                file.setDescription(fileName);
                uploadedFileRepository.save(file);
            }
        });
    }

    private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    private void createUserIfNotFound(String userName, String firstName, String lastName, String password, Collection<Role> roles, String email) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setUsername(userName);
            user.setLastName(lastName);
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(password));
            user.setEmail(email);
            user.setRoles(roles);
            user.setActive();
            user.unlockAccount();
            userRepository.save(user);
        }
    }

    public Stream<String> getFileStream() {
        return storageService.loadAllFromFileSystem(StorageServiceSizeEnum.ROOT).map(path -> path.getFileName().toString());
    }

}