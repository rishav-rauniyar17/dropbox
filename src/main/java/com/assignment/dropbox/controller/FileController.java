package com.assignment.dropbox.controller;//package com.assignment.dropbox.controller;


import com.assignment.dropbox.dto.FileUploadResponseDto;
import com.assignment.dropbox.entity.FileEntity;
import com.assignment.dropbox.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponseDto> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileUploadResponseDto response = fileService.uploadFile(file);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FileUploadResponseDto("Error", "Failed to upload file", null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FileUploadResponseDto("Error", "An unexpected error occurred", null));
        }
    }


    @PutMapping("/{fileId}")
    public ResponseEntity<FileUploadResponseDto> updateMetadata(
            @PathVariable Long fileId,
            @RequestBody FileEntity updatedMetadata) {
        try {
            FileUploadResponseDto response = fileService.updateFileMetadata(fileId, updatedMetadata);
            return ResponseEntity.ok(response);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FileUploadResponseDto("Error", "File metadata not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FileUploadResponseDto("Error", "Failed to update file metadata", null));
        }
    }


    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long fileId) {
        try {
            byte[] fileContent = fileService.getFileContent(fileId);
            String fileName = fileService.getFileName(fileId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .body(fileContent);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @GetMapping
    public ResponseEntity<List<FileEntity>> listFiles() {
        List<FileEntity> files = fileService.listFiles();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId) {
        try {
            String message = fileService.deleteFile(fileId);
            return ResponseEntity.ok(message);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error deleting the file");
        }
    }
}

