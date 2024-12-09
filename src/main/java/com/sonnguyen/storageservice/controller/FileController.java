package com.sonnguyen.storageservice.controller;

import com.sonnguyen.storageservice.service.FileDataService;
import com.sonnguyen.storageservice.viewmodel.FileDataListVm;
import com.sonnguyen.storageservice.viewmodel.FileInfoPostVm;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadFileById(@PathVariable(name = "id") Long fileId, HttpServletResponse response){
        return fileDataService.downloadFileById(fileId,response);
    }

}
