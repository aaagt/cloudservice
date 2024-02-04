package aaagt.cloudservice.file.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class LocalFileStorage implements FileStorage {

    @Value("${aaagt.cloudservice.files.upload-path}")
    private String uploadPath;

    @Override
    public Resource get(String filename) throws FileNotFoundException {
        var filepath = uploadPath + filename;
        try {
            Path file = load(filepath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.warn("Could not read file: {}", filepath);
                throw new FileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            log.warn("Could not read file: {}", filepath, e);
            throw new FileNotFoundException("Could not read file: " + filename);
        }
    }

    private Path load(String filename) {
        Path rootLocation = Paths.get(uploadPath);
        return rootLocation.resolve(filename);
    }

    @Override
    public void save(String filename, byte[] bytes) throws IOException {
        var filepath = uploadPath + filename;
        new File(filepath).getParentFile().mkdirs();
        var path = Paths.get(filepath);
        Files.write(path, bytes);
    }

    @Override
    public void delete(String filename) {

    }

    @Override
    public boolean exists(String filename) {
        var filepath = uploadPath + filename;
        return new File(filepath).exists();
    }

}
