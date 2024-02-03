package aaagt.cloudservice.file.controller;

import aaagt.cloudservice.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
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
        log.info("Creating file: {}, with hash: {}", filename, hash);
        fileService.createFile(filename, file);
    }

    @DeleteMapping("/file")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFile(@RequestParam String filename) throws FileNotFoundException {
        log.info("Deleting file: {}", filename);
        fileService.deleteFile(filename);
    }

    @GetMapping(value = "/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultiValueMap<String, Object>> getFile(@RequestParam String filename) throws FileNotFoundException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();

        Resource file = fileService.get(filename);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
        formData.add("hash", "hash123");
//        formData.add("file-data_1", new FileSystemResource("C:\Users\ganesh\img\logo.png"));
        formData.add("file", file);
//        formData.add("file-data_5", new FileSystemResource("D:\testxls.xlsx"));
        return new ResponseEntity<MultiValueMap<String, Object>>(formData, HttpStatus.OK);



        /*return ResponseEntity
                .ok()
                .body(new GetResponseDto("hash123", file));*/

//        return "get file to " + currentPrincipalName;
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
