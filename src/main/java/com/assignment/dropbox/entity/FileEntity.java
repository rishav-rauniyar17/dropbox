package com.assignment.dropbox.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "files")
@Getter
@Setter
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private Long createdAt;
    private Long size;
    private String fileType;
}
