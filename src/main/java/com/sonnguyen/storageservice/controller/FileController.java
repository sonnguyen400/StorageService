package com.sonnguyen.storageservice.controller;

import com.sonnguyen.storageservice.service.FileDataService;
import com.sonnguyen.storageservice.viewmodel.FileDataListVm;
import com.sonnguyen.storageservice.viewmodel.FileDetailsGetVm;
import com.sonnguyen.storageservice.viewmodel.FileInfoPostVm;
import com.sonnguyen.storageservice.viewmodel.ThumbnailParamsVm;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FileController {
    FileDataService fileDataService;
    @GetMapping("/{id}")
    public FileDetailsGetVm findById(@PathVariable Long id){
        return fileDataService.findFileDetailById(id);
    }
    @PostMapping("/upload")
    public List<FileDataListVm> uploadAllFile(@RequestPart(name = "file") List<MultipartFile> files,@Valid FileInfoPostVm fileInfoPostVm){
        return fileDataService.uploadAll(files,fileInfoPostVm);
    }
    @GetMapping("/{id}/download")
    public void downloadFileById(@PathVariable(name = "id") Long fileId, HttpServletResponse response){
         fileDataService.downloadFileById(fileId,response);
    }
    @GetMapping("/{id}/download/thumbnail")
    public void downloadThumbnail(@PathVariable Long id,
                                  ThumbnailParamsVm thumbnailParamsVm,
                                  HttpServletResponse httpServletResponse){
       fileDataService.downloadThumbnailImage(id,thumbnailParamsVm,httpServletResponse);
    }
    @PostMapping("/{id}/delete")
    public void deleteById(@PathVariable Long id){
        fileDataService.deleteById(id);
    }

}
