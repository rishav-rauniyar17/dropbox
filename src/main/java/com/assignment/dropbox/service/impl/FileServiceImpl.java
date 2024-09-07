package com.assignment.dropbox.service.impl;

import com.assignment.dropbox.constants.FileUploadConstants;
import com.assignment.dropbox.dto.FileUploadResponseDto;
import com.assignment.dropbox.entity.FileEntity;
import com.assignment.dropbox.repository.FileRepository;
import com.assignment.dropbox.service.FileService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public FileUploadResponseDto uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        // Create directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Get the target file path and save the file
        Path filePath = uploadPath.resolve(file.getOriginalFilename());
        try {
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new IOException("Failed to save file", e);
        }

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setCreatedAt(System.currentTimeMillis());
        fileEntity.setSize(file.getSize());
        fileEntity.setFileType(file.getContentType());

        try {
            fileEntity = fileRepository.save(fileEntity);
        } catch (Exception e) {
            throw new IOException("Failed to save file metadata", e);
        }

        return new FileUploadResponseDto(
                FileUploadConstants.STATUS_201,
                FileUploadConstants.MESSAGE_201,
                fileEntity.getId()
        );
    }

    @Override
    public byte[] getFileContent(Long fileId) throws IOException {
        Optional<FileEntity> fileEntityOpt = fileRepository.findById(fileId);

        if (fileEntityOpt.isEmpty()) {
            throw new FileNotFoundException("File not found with id: " + fileId);
        }

        FileEntity fileEntity = fileEntityOpt.get();
        Path filePath = Paths.get(uploadDir).resolve(fileEntity.getFileName());

        if (Files.exists(filePath)) {
            return Files.readAllBytes(filePath);
        } else {
            throw new FileNotFoundException("File not found on the server: " + fileEntity.getFileName());
        }
    }

    @Override
    public String getFileName(Long fileId) throws FileNotFoundException {
        Optional<FileEntity> fileEntityOpt = fileRepository.findById(fileId);

        if (fileEntityOpt.isEmpty()) {
            throw new FileNotFoundException("File not found with id: " + fileId);
        }

        return fileEntityOpt.get().getFileName();
    }

    @Override
    public FileUploadResponseDto updateFileMetadata(Long fileId, FileEntity updatedMetadata) throws IOException {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));

        if (updatedMetadata.getFileName() != null) {
            fileEntity.setFileName(updatedMetadata.getFileName());
        }
        if (updatedMetadata.getFileType() != null) {
            fileEntity.setFileType(updatedMetadata.getFileType());
        }
        if (updatedMetadata.getSize() != null) {
            fileEntity.setSize(updatedMetadata.getSize());
        }
        if (updatedMetadata.getCreatedAt() != null) {
            fileEntity.setCreatedAt(updatedMetadata.getCreatedAt());
        }

        fileRepository.save(fileEntity);

        return new FileUploadResponseDto("Success", "File metadata updated successfully", fileEntity.getId());
    }

    @Override
    public String deleteFile(Long fileId) throws IOException {
        // Fetch the file metadata from the database
        Optional<FileEntity> fileEntityOpt = fileRepository.findById(fileId);

        if (fileEntityOpt.isEmpty()) {
            throw new FileNotFoundException("File not found with id: " + fileId);
        }

        FileEntity fileEntity = fileEntityOpt.get();

        // Delete the file from the file system
        Path filePath = Paths.get(uploadDir).resolve(fileEntity.getFileName());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        } else {
            throw new FileNotFoundException("File not found on the server: " + fileEntity.getFileName());
        }

        // Delete the file metadata from the database
        fileRepository.delete(fileEntity);

        return "File deleted successfully";
    }

    @Override
    public List<FileEntity> listFiles() {
        return fileRepository.findAll();
    }
}
