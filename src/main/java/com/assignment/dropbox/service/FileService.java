package com.assignment.dropbox.service;


import com.assignment.dropbox.dto.FileUploadResponseDto;
import com.assignment.dropbox.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    FileUploadResponseDto uploadFile(MultipartFile file) throws IOException;

    byte[] getFileContent(Long fileId) throws IOException;

    String getFileName(Long fileId) throws IOException;

    FileUploadResponseDto updateFileMetadata(Long fileId, FileEntity updatedMetadata) throws IOException;

    String deleteFile(Long fileId) throws IOException;

    List<FileEntity> listFiles();
}