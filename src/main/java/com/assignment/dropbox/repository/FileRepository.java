package com.assignment.dropbox.repository;

import com.assignment.dropbox.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

}
