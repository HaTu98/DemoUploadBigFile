package com.example.demo.controller;

import com.example.demo.model.request.UploadFileGetLinkRequest;
import com.example.demo.service.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("api")
public class FilesController {

    private final static String UPLOADED_FOLDER  = new File("src\\main\\resources\\data").getAbsolutePath();
    private final FileUploadService fileUploadService;

    public FilesController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("files/upload_links")
    public ResponseEntity<?> uploadLinks(@RequestBody UploadFileGetLinkRequest request) {
        File file = new File(UPLOADED_FOLDER + "\\temp", UUID.randomUUID().toString());
        if (!file.exists()) {
            file.mkdir();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/files/upload")
    public ResponseEntity<?> uploadFile (@RequestParam("file")MultipartFile  file, @RequestParam("stt") Integer stt, @RequestParam("folder") String folder) {
        if (file.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + "\\temp\\" + folder + "\\" + stt + getFileExtension(file.getOriginalFilename()));
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("files/link")
    public ResponseEntity<?> linkFile (@RequestParam("fileIndex") String fileIndex) {

        return ResponseEntity.ok().build();
    }

    @PostMapping("files/done")
    public ResponseEntity<?> done(@RequestParam("location") String location) throws IOException {
        String folderLocation = UPLOADED_FOLDER + "\\" + "temp\\" + location;
        File folder = new File(folderLocation);
        String newLocation = UPLOADED_FOLDER + "\\" + "final\\" + location;
        fileUploadService.combineFile(folder, newLocation);
        return ResponseEntity.ok().build();
    }

    private String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
}
