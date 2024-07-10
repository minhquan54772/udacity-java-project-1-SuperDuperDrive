package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;


    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFilesByUserId(int userId) {
        return this.fileMapper.getFilesListByUserId(userId);
    }

    public File getFilesByFileId(int fileId) {
        return this.fileMapper.getFileById(fileId);
    }

    public int createFile(File file) {
        return this.fileMapper.uploadFile(file);
    }

    public void deleteFileById(int fileId) {
        this.fileMapper.deleteFile(fileId);
    }
}
