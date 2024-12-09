package com.sonnguyen.storageservice.controller;

import com.sonnguyen.storageservice.service.FileDataService;
import com.sonnguyen.storageservice.viewmodel.FileDataListVm;
import com.sonnguyen.storageservice.viewmodel.FileInfoPostVm;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FileController {
    FileDataService fileDataService;
    @PostMapping("/upload")
    public List<FileDataListVm> uploadAllFile(@RequestPart(name = "file") List<MultipartFile> files,@Valid FileInfoPostVm fileInfoPostVm){
        return fileDataService.uploadAll(files,fileInfoPostVm);
    }
}
