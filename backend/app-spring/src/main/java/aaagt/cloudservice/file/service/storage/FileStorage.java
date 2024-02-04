package aaagt.cloudservice.file.service.storage;

import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileStorage {

    Resource get(String filename) throws FileNotFoundException;

    void save(String filename, byte[] bytes) throws IOException;

    void delete(String filename);

    boolean exists(String storageFileId);
}
