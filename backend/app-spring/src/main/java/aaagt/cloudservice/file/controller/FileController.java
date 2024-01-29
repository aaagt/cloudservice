package aaagt.cloudservice.file.controller;

import aaagt.cloudservice.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/file")
    @ResponseStatus(HttpStatus.OK)
    public void postFile(@RequestParam String filename,
                         @RequestPart("hash") String hash,
                         @RequestPart("file") MultipartFile file) throws IOException {
        log.info(hash);
        fileService.createFile(filename, file);
    }

    @DeleteMapping("/file")
    public String deleteFile() {
        return "delete file";
    }

    @GetMapping("/file")
    public String getFile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return "get file to " + currentPrincipalName;
    }

    @PutMapping("/file")
    public String putFile() {
        return "put file";
    }

    @GetMapping("/list")
    public String getList() {
        return "list files";
    }

}
