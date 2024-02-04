package aaagt.cloudservice.file.repository;

import aaagt.cloudservice.file.entity.FileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findByOwnerAndFilename(String owner, String filename);

    Page<FileEntity> findAllByOwner(String owner, Pageable pageable);
}
