package aaagt.cloudservice.file.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "file", name = "files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "owner", nullable = false)
    String owner;

    @Column(name = "filename", nullable = false)
    String filename;

    @Column(name = "storage_file_id", unique = true, nullable = false)
    String storageFileId;

    @Column(name = "file_hash", nullable = false)
    String fileHash;

    @Column(name = "file_size", nullable = false)
    Long size;

}
