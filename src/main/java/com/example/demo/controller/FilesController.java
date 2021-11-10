package com.example.demo.controller;

import com.example.demo.config.Constant;
import com.example.demo.model.request.UploadFileGetLinkRequest;
import com.example.demo.model.request.UploadFileGetLinkResponse;
import com.example.demo.service.CombineFileService;
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
import java.util.Arrays;
import java.util.UUID;

@Controller
@RequestMapping("api")
public class FilesController {

    private final static String UPLOADED_FOLDER  = new File("src\\main\\java\\com\\example\\demo\\data").getAbsolutePath();
    private final FileUploadService fileUploadService;
    private final CombineFileService combineFileService;

    public FilesController(FileUploadService fileUploadService,
                           CombineFileService combineFileService) {
        this.fileUploadService = fileUploadService;
        this.combineFileService = combineFileService;
    }

    @PostMapping("/files/upload_links")
    public ResponseEntity<UploadFileGetLinkResponse> uploadLinks(@RequestBody UploadFileGetLinkRequest request) {
        String folderName = UUID.randomUUID().toString();
        String location = Constant.TEMP_FILE;
        File file = new File(location, folderName);
        if (!file.exists()) {
            file.mkdir();
        }
        combineFileService.combineFile(request.getPartNumber(), folderName,
                request.getContentType(), request.getFileSize(), request.getFileName());
        UploadFileGetLinkResponse response = fileUploadService.generatePart(request.getPartNumber(), folderName);
        return ResponseEntity.ok().body(response);
    }
    @PatchMapping("/files/upload/{folder}")
    public ResponseEntity<?> uploadFile (@RequestParam("file")MultipartFile  file, @RequestParam("part") String part, @PathVariable("folder") String folder) {
        if (file.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        String url = Constant.TEMP_FILE + "/" + folder + "/" + part + getFileExtension(file.getOriginalFilename());
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(url);
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/files/link")
    public ResponseEntity<?> linkFile (@RequestParam("fileIndex") String fileIndex) {

        return ResponseEntity.ok().build();
    }

    @PostMapping("/files/done")
    public ResponseEntity<?> done(@RequestParam("location") String location) throws IOException {
        String folderLocation = Constant.TEMP_FILE + "/" + location;
        File folder = new File(folderLocation);
        System.out.println(Arrays.asList(folder.listFiles()).size());
        String newLocation = Constant.FINAL_FILE + "/" + location;
        fileUploadService.combineFiles(folder, newLocation);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/files/chunk")
    public ResponseEntity<?> uploadFile (@RequestParam("folder") String folder) {
        /*String url = "D:\\DEV\\demo\\src\\main\\resources\\data\\temp\\6018dd82-9395-44f1-a5e7-22442aa77e6d\\1.mp4";
        fileUploadService.chunksFile("D:\\DEV\\demo\\src\\main\\resources\\data\\temp\\6018dd82-9395-44f1-a5e7-22442aa77e6d\\", url);*/
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
