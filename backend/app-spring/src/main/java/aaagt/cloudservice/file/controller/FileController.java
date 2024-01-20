package aaagt.cloudservice.file.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    @PostMapping("/file")
    public String postFile() {
        return "post file";
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
