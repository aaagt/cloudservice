package aaagt.cloudservice.file.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileService {

    @Value("${aaagt.cloudservice.files.upload-path}")
    private String uploadPath;

    public void createFile(String filename, MultipartFile file) throws IOException {
        saveUploadedFile(uploadPath + filename, file);
    }

    private void saveUploadedFile(String filepath, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            new File(filepath).getParentFile().mkdirs();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filepath);
            Files.write(path, bytes);
        }
    }

    public void deleteFile(String filename) throws FileNotFoundException {
        log.info("deleted");
    }

    public Resource get(String filename) throws FileNotFoundException {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename);
        }
    }

    public Path load(String filename) {
        Path rootLocation = Paths.get(uploadPath);
        return rootLocation.resolve(filename);
    }
}
