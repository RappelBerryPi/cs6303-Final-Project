package edu.utdallas.cs6303.finalproject.controllers;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.utdallas.cs6303.finalproject.model.database.UploadedFile;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UploadedFileRepository;
import edu.utdallas.cs6303.finalproject.model.device.DeviceInfo;
import edu.utdallas.cs6303.finalproject.model.pages.ListPageInformation;
import edu.utdallas.cs6303.finalproject.model.tinymce.ImageTitleAndValue;
import edu.utdallas.cs6303.finalproject.model.tinymce.ImageUploadLocation;
import edu.utdallas.cs6303.finalproject.services.image.ImageFileURLBuilder;
import edu.utdallas.cs6303.finalproject.services.image.ImageFileURLBuilderInterface;
import edu.utdallas.cs6303.finalproject.services.storage.StorageServiceInterface;

@Controller
@RequestMapping(ImageController.REQUEST_MAPPING)
public class ImageController {

    public static final String REQUEST_MAPPING = "/Images";

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @Autowired
    private StorageServiceInterface storageService;

    @GetMapping("")
    public String images() {
        return "uploadForm";
    }

    @GetMapping(value = "/")
    public String listUploadedImageFiles(@RequestParam(name = "page") Optional<Integer> pageInt, @RequestParam(name = "length") Optional<Integer> sizeInt, Model model) throws IOException {
        int pageNum = pageInt.orElse(1) - 1;
        if (pageNum < 0) {
            throw new InvalidParameterException("The page must be greater than 1.");
        }
        int                               size                = sizeInt.orElse(10);
        Pageable                          page                = PageRequest.of(pageNum, size, Sort.by("id"));
        Page<UploadedFile>                files               = uploadedFileRepository.findByMimeTypeMimeTypeStringStartingWith("image", page);
        ListPageInformation<UploadedFile> listPageInformation = new ListPageInformation<>();
        listPageInformation.setPageObject(files);
        listPageInformation.setEditUrl("/");
        listPageInformation.setTitle("Images");
        model.addAttribute("listPageInformation", listPageInformation);
        return "fragments/imageAjax";
    }

    @PostMapping("/TinyMCEFileUpload")
    @ResponseBody
    public ImageUploadLocation handleFileUpload(@RequestParam("filename") MultipartFile file) {
        return handleFileUpload(file, file.getOriginalFilename());
    }

    private ImageUploadLocation handleFileUpload(MultipartFile file, String filename) {
        UploadedFile uploadedFile;
        if (filename == null || filename.isEmpty()) {
            uploadedFile = storageService.store(file, file.getOriginalFilename());
        } else {
            uploadedFile = storageService.store(file, filename);
        }
        DeviceInfo                   info     = new DeviceInfo("TinyMCE");
        ImageFileURLBuilderInterface builder  = new ImageFileURLBuilder();
        ImageUploadLocation          location = new ImageUploadLocation();
        location.setLocation(builder.forDeviceInfo(info).with(uploadedFile).build());
        return location;
    }

    @GetMapping(value = "/TinyMCEImageFiles")
    @ResponseBody
    public List<ImageTitleAndValue> loadImagesForList() {
        DeviceInfo                   info       = new DeviceInfo("TinyMCE");
        ImageFileURLBuilderInterface builder    = new ImageFileURLBuilder().forDeviceInfo(info);
        return uploadedFileRepository
                .findAllByMimeTypeMimeTypeStringStartingWith("image")
                .stream()
                .map(file -> new ImageTitleAndValue(file.getDescription(), builder.with(file).build()))
                .collect(Collectors.toList());
    }

}