package edu.utdallas.cs6303.finalproject.controllers;

import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.utdallas.cs6303.finalproject.model.database.UploadedFile;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UploadedFileRepository;
import edu.utdallas.cs6303.finalproject.services.storage.StorageFileNotFoundException;
import edu.utdallas.cs6303.finalproject.services.storage.StorageServiceInterface;
import edu.utdallas.cs6303.finalproject.services.storage.enums.StorageServiceSizeEnum;

@Controller
@RequestMapping(FileUploadController.REQUEST_MAPPING)
public class FileUploadController {

    @Autowired
    private StorageServiceInterface storageService;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    public static final String REQUEST_MAPPING = "/Files";

    @GetMapping(value = "/")
    @ResponseBody
    public List<UploadedFile> listUploadedFiles(Model model, @RequestParam(name = "start", required = false) String start, @RequestParam(name = "length", required = false) String length) {
        List<UploadedFile> uploadedFileList = uploadedFileRepository.findAll();
        if (start != null && length != null) {
            int startInt  = Integer.parseInt(start);
            int lengthInt = Integer.parseInt(length);
            if (startInt > 0 && lengthInt > 1) {
                return uploadedFileList.stream().skip(startInt).limit(lengthInt).collect(Collectors.toList());
            }
        }
        return uploadedFileList;
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename, RedirectAttributes redirectAttributes) {
        if (filename == null || filename.isEmpty()) {
            storageService.store(file, file.getOriginalFilename());
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        } else {
            storageService.store(file, filename);
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + filename + "!");
        }
        return HomeController.REDIRECT_TO + "/Images";
    }

    @GetMapping("/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        Resource file = storageService.loadAsResource(fileName, StorageServiceSizeEnum.ROOT);
        return processResponseEntity(file);
    }

    @GetMapping("/{size}/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveSizedFile(@PathVariable final String fileName, @PathVariable final String size) {
        try {
            StorageServiceSizeEnum value = StorageServiceSizeEnum.valueOf(size.toUpperCase());
            Resource               file  = storageService.loadAsResource(fileName, value);
            return processResponseEntity(file);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LoggerFactory.getLogger(FileUploadController.class).error("fileName = {}. Size = {}", fileName, size);
            return null;
        } catch (StorageFileNotFoundException e) {
            return serveFile(fileName);
        }
    }

    public Model addImgFilesToModel(Model model) {
        model.addAttribute("img_files", this.storageService.getFileStream().filter(file -> {
            String fileType = URLConnection.guessContentTypeFromName(file);
            return file.endsWith("webp") || (fileType != null && fileType.startsWith("image"));
        }).collect(Collectors.toList()));

        return model;
    }

    public Model addFilesToModel(Model model) {
        model.addAttribute("files", this.storageService.getFileStream().collect(Collectors.toList()));
        return model;
    }

    private ResponseEntity<Resource> processResponseEntity(Resource file) {
        String fileName = file.getFilename();
        if (fileName != null && (fileName.endsWith("jpg") || fileName.endsWith("jpeg") || fileName.endsWith("png") || fileName.endsWith("webp"))) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
        } else {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }
    }
}