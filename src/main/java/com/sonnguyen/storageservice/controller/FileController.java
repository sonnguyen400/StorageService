package com.sonnguyen.storageservice.controller;

import com.sonnguyen.storageservice.constant.FileAccessType;
import com.sonnguyen.storageservice.model.FileData;
import com.sonnguyen.storageservice.service.FileDataService;
import com.sonnguyen.storageservice.specification.DynamicSearch;
import com.sonnguyen.storageservice.specification.FileDataSpecification;
import com.sonnguyen.storageservice.utils.SearchUtils;
import com.sonnguyen.storageservice.viewmodel.FileDataListVm;
import com.sonnguyen.storageservice.viewmodel.FileDetailsGetVm;
import com.sonnguyen.storageservice.viewmodel.ThumbnailParamsVm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FileController {
    FileDataService fileDataService;

    @GetMapping("/file")
    public Page<FileData> findAll(
            @RequestParam(name = "page",defaultValue = "0",required = false) Integer page,
            @RequestParam(name = "size",defaultValue = "10",required = false) Integer size,
            HttpServletRequest request
    ){
        Sort sort = Sort.by(SearchUtils.parseSort(request.getParameterMap()));
        List<FileDataSpecification> fileDataSpecifications = parseRequestToSpecification(request);
        return fileDataService.findAll(fileDataSpecifications, PageRequest.of(page, size).withSort(sort));
    }
    @GetMapping("/private/file/{id}")
    public FileDetailsGetVm findById(@PathVariable Long id){
        return fileDataService.findFileDetailById(id);
    }
    @PostMapping("/private/file/upload")
    public List<FileDataListVm> uploadAllFile(@RequestPart(name = "file") List<MultipartFile> files,@RequestPart String owner){
        return fileDataService.uploadAll(files,owner, FileAccessType.PRIVATE);
    }
    @GetMapping("/file/{id}/download")
    public void downloadFileById(@PathVariable(name = "id") Long fileId, HttpServletResponse response){
         fileDataService.downloadFileById(fileId,response);
    }
    @GetMapping("/file/{id}/download/thumbnail")
    public void downloadThumbnail(@PathVariable Long id,
                                  ThumbnailParamsVm thumbnailParamsVm,
                                  HttpServletResponse httpServletResponse){
       fileDataService.downloadThumbnailImage(id,thumbnailParamsVm,httpServletResponse);
    }
    @PostMapping("/file/{id}/delete")
    public void deleteById(@PathVariable Long id){
        fileDataService.deleteById(id);
    }



    @GetMapping("/public/file/{id}")
    public FileDetailsGetVm findPublicById(@PathVariable Long id){
        return fileDataService.findFileDetailById(id);
    }
    @PostMapping("/public/file/upload")
    public List<FileDataListVm> uploadAllPublicFile(@RequestPart(name = "file") List<MultipartFile> files,String owner){
        return fileDataService.uploadAll(files,owner,FileAccessType.PUBLIC);
    }
    private List<FileDataSpecification> parseRequestToSpecification(HttpServletRequest request) {
        List<FileDataSpecification> fileDataSpecifications = new ArrayList<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        List<DynamicSearch> list = SearchUtils.parseOperator(parameterMap);
        list.forEach(dynamicSearch -> fileDataSpecifications.add(new FileDataSpecification(dynamicSearch)));
        return fileDataSpecifications;
    }
}
