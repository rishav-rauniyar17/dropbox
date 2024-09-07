package com.assignment.dropbox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadResponseDto {
    private String status;
    private String message;
    private Long fileId;

}
