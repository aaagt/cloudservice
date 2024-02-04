package aaagt.cloudservice.file.service;

import aaagt.cloudservice.file.dto.ListResponseFileItemDto;
import aaagt.cloudservice.file.entity.FileEntity;
import aaagt.cloudservice.file.repository.FileRepository;
import aaagt.cloudservice.file.service.dto.GetFileReturnDto;
import aaagt.cloudservice.file.service.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private final FileStorage fileStorage;

    @Value("${aaagt.cloudservice.files.upload-path}")
    private String uploadPath;

    public void createFile(String owner, String filename, MultipartFile file, String hash) throws IOException {
        log.info("Create file {} for user {}, multipart {}, hash {}", filename, owner, file, hash);
        if (hash == null) {
            hash = "fdfgd";
        }
        if (!file.isEmpty()) {
            var storageFileId = "";
            do {
                storageFileId = UUID.randomUUID() + "_" + filename;
            } while (fileStorage.exists(storageFileId));
            var entity = FileEntity.builder()
                    .owner(owner)
                    .fileHash(hash)
                    .size(file.getSize())
                    .storageFileId(storageFileId)
                    .filename(filename)
                    .build();
            fileRepository.save(entity);
            fileStorage.save(storageFileId, file.getBytes());
        }
    }

    public void deleteFile(String owner, String filename) throws FileNotFoundException {
        log.info("Delete file {} for user {}", filename, owner);
        var entity = fileRepository.findByOwnerAndFilename(owner, filename)
                .orElseThrow(() -> new FileNotFoundException("File %s of user %s is not found".formatted(filename, owner)));
        fileStorage.delete(entity.getStorageFileId());
        fileRepository.delete(entity);
    }

    public GetFileReturnDto get(String owner, String filename) throws FileNotFoundException {
        log.info("Get file {} for user {}", filename, owner);
        var entity = fileRepository.findByOwnerAndFilename(owner, filename)
                .orElseThrow(() -> new FileNotFoundException("File %s of user %s is not found".formatted(filename, owner)));
        return new GetFileReturnDto(
                fileStorage.get(entity.getStorageFileId()),
                entity.getFileHash()
        );
    }

    public void rename(String owner, String fromFilename, String toFilename) throws FileNotFoundException {
        log.info("Rename file {} to {} for user {}", fromFilename, toFilename, owner);
        var entity = fileRepository.findByOwnerAndFilename(owner, fromFilename)
                .orElseThrow(() -> new FileNotFoundException("File %s of user %s is not found".formatted(fromFilename, owner)));
        entity.setFilename(toFilename);
        fileRepository.save(entity);
    }

    public List<ListResponseFileItemDto> getList(String owner, Integer limit) {
        log.info("Get list for user {} with limit {}", owner, limit);
        var pageable = PageRequest.of(0, limit);
        return fileRepository.findAllByOwner(owner, pageable)
                .map(fileEntity -> new ListResponseFileItemDto(
                        fileEntity.getFilename(),
                        fileEntity.getSize())
                )
                .toList();
    }

}
