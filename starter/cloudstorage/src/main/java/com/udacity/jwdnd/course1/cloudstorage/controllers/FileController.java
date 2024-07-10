package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.StorageService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;
    private final StorageService storageService;

    public FileController(FileService fileService, UserService userService, StorageService storageService) {
        this.fileService = fileService;
        this.userService = userService;
        this.storageService = storageService;
    }

    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestPart("fileUpload") MultipartFile fileUpload, Authentication authentication, Model model) throws IOException {
        String originalFilename = fileUpload.getOriginalFilename();
        int userid = this.userService.getUserByUsername(authentication.getName()).getUserid();
        List<File> filesByUserId = this.fileService.getFilesByUserId(userid);
        Optional<File> duplicatedFiles = filesByUserId.stream().filter(file -> originalFilename.equals(file.getFilename())).findAny();
        if (duplicatedFiles.isPresent()) {
            model.addAttribute("success", false);
            model.addAttribute("errors", List.of("File already exists"));
            return "result";
        }

        try {
            this.storageService.storeFile(fileUpload);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("errors", List.of(e.getMessage()));
            return "result";
        }

        File newFile = new File(fileUpload.getOriginalFilename(), fileUpload.getContentType(), fileUpload.getSize(), userid, fileUpload.getBytes());
        this.fileService.createFile(newFile);

        model.addAttribute("success", true);
        model.addAttribute("errors", List.of());
        return "result";
    }

    @GetMapping("/{id}/delete")
    public String deleteFile(@PathVariable("id") int fileId, Model model) {
        try {
            File filesByFileId = this.fileService.getFilesByFileId(fileId);
            this.storageService.deleteFileOnStorage(filesByFileId);
        } catch (IOException e) {
            model.addAttribute("success", false);
            model.addAttribute("errors", List.of(e.getMessage()));
        }
        this.fileService.deleteFileById(fileId);
        model.addAttribute("success", true);
        model.addAttribute("errors", List.of());
        return "result";
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") int fileId, Model model) {
        try {
            File filesByFileId = this.fileService.getFilesByFileId(fileId);
            Resource resource = this.storageService.downloadFile(filesByFileId.getFilename());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
