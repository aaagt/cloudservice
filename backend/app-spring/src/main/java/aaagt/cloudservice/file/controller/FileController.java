package aaagt.cloudservice.file.controller;

import aaagt.cloudservice.file.dto.ListResponseFileItemDto;
import aaagt.cloudservice.file.dto.PutFileRequestDto;
import aaagt.cloudservice.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/file")
    @ResponseStatus(HttpStatus.OK)
    public void postFile(@RequestParam String filename,
                         @RequestPart(value = "hash", required = false) String hash,
                         @RequestPart("file") MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        log.info("Creating file: {}, with hash: {}", filename, hash);
        fileService.createFile(currentPrincipalName, filename, file, hash);
    }

    @DeleteMapping("/file")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFile(@RequestParam String filename) throws FileNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        log.info("Deleting file: {}, of user {}", filename, currentPrincipalName);
        fileService.deleteFile(currentPrincipalName, filename);
    }

    @GetMapping(value = "/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultiValueMap<String, Object>> getFile(@RequestParam String filename) throws FileNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        var data = fileService.get(currentPrincipalName, filename);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
        formData.add("hash", data.hash());
        formData.add("file", data.file());

        return new ResponseEntity<>(formData, HttpStatus.OK);
    }

    @PutMapping("/file")
    @ResponseStatus(HttpStatus.OK)
    public void putFile(@RequestParam String filename,
                        @RequestBody PutFileRequestDto requestDto) throws FileNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        fileService.rename(currentPrincipalName, filename, requestDto.filename());
    }

    @GetMapping("/list")
    public List<ListResponseFileItemDto> getList(@RequestParam Integer limit) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return fileService.getList(currentPrincipalName, limit);
    }

}
