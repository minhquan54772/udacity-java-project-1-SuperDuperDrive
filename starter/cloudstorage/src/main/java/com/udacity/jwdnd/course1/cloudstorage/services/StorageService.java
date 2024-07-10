package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {
    private final Path uploadFileFolder = Paths.get("uploaded-files");

    public void storeFile(MultipartFile file) {
        try {
            Files.createDirectories(this.uploadFileFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        Path destinationFile = this.uploadFileFolder.resolve(file.getOriginalFilename()).normalize().toAbsolutePath();

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFileOnStorage(File file) throws IOException {
        Path absolutePath = this.uploadFileFolder.resolve(file.getFilename()).normalize().toAbsolutePath();
        try {
            Files.deleteIfExists(absolutePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource downloadFile(String fileName) {
        java.io.File file = this.uploadFileFolder.resolve(fileName).toFile();
        try {
            if (file.exists()) {
                return new UrlResource(file.toURI());
            } else {
                throw new RuntimeException("File does not exist");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
