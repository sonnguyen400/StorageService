package com.sonnguyen.storageservice.service;

import com.sonnguyen.storageservice.constant.FileAccessType;
import com.sonnguyen.storageservice.constant.FileType;
import com.sonnguyen.storageservice.exception.InvalidArgumentException;
import com.sonnguyen.storageservice.exception.ResourceNotFoundException;
import com.sonnguyen.storageservice.model.FileData;
import com.sonnguyen.storageservice.repository.FileDataRepository;
import com.sonnguyen.storageservice.specification.DynamicSearch;
import com.sonnguyen.storageservice.specification.FileDataSpecification;
import com.sonnguyen.storageservice.utils.FileUtils;
import com.sonnguyen.storageservice.utils.ImageUtils;
import com.sonnguyen.storageservice.viewmodel.FileDataListGetVm;
import com.sonnguyen.storageservice.viewmodel.FileDataListVm;
import com.sonnguyen.storageservice.viewmodel.FileDetailsGetVm;
import com.sonnguyen.storageservice.viewmodel.ThumbnailParamsVm;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

@Service
public class FileDataService {
    private static List<String> validFileExtension= List.of(".png",".jpg",".jpeg","doc","docx",".pdf");
    @Autowired
    FileDataRepository fileDataRepository;
    @Autowired
    FileUtils fileUtils;
    @Value("${storage.download-base-url}")
    private String downloadBaseUrl;
    public FileData findByIdAndAccessType(Long id,FileAccessType accessType) {
        return fileDataRepository.findByIdAndAccessType(id,accessType).orElseThrow(()->new ResourceNotFoundException("File not found"));
    }
    public Page<FileDataListGetVm> findAllPublicFile( List<FileDataSpecification> specifications,Pageable pageable){
        Specification<FileData> specification = new FileDataSpecification(new DynamicSearch("accessType",FileAccessType.PUBLIC, DynamicSearch.Operator.EQUAL));
        for(FileDataSpecification fileDataSpecification:specifications){
            specification=specification.and(fileDataSpecification);
        }
        return fileDataRepository.findAll(specification, pageable).map(FileDataListGetVm::fromEntity);
    }
    public Page<FileDataListGetVm> findAll(List<FileDataSpecification> specifications, Pageable pageable){
        if (!specifications.isEmpty()) {
            Specification<FileData> specification = specifications.getFirst();
            for(int i=1;i<specifications.size();i++){
                specification=specification.and(specifications.get(i));
            }
            return fileDataRepository.findAll(specification, pageable).map(FileDataListGetVm::fromEntity);
        }
        return fileDataRepository.findAll(pageable).map(FileDataListGetVm::fromEntity);
    }
    public void deleteById(Long id,FileAccessType accessType) {
        FileData fileData=findByIdAndAccessType(id,accessType);
        FileUtils.deleteFile(fileData.getPath());
        fileDataRepository.deleteById(id);
    }
    public FileDetailsGetVm findFileDetailById(Long id,FileAccessType accessType){
        FileData fileData=findByIdAndAccessType(id,accessType);
        String downloadLink=createDownloadUrl(fileData);
        return FileDetailsGetVm.builder()
                .link(downloadLink)
                .accessType(fileData.getAccessType())
                .createdDate(fileData.getCreatedDate())
                .lastModifiedDate(fileData.getLastModifiedDate())
                .name(fileData.getName())
                .owner(fileData.getOwner())
                .version(fileData.getVersion())
                .size(fileData.getSize())
                .contentType(fileData.getContentType())
                .accessType(fileData.getAccessType())
                .build();
    }
    public List<FileDataListVm> uploadAll(List<MultipartFile> files, String owner, FileAccessType accessType) {
        checkValidUploadedFile(files);
        List<FileData> fileDataList =files.stream().map((file_)-> createFileAndSaveToDisk(file_,owner,accessType)).toList();
        return fileDataRepository.saveAll(fileDataList)
                .stream()
                .map((file_)-> {
                    String downloadLink=createDownloadUrl(file_);
                    return new FileDataListVm(file_.getName(),downloadLink);
                })
                .toList();
    }
    public String createDownloadUrl(FileData fileData){
        StringBuilder sb=new StringBuilder();
        sb.append(downloadBaseUrl)
                .append(fileData.getAccessType()==FileAccessType.PUBLIC?"/public":"/private")
                .append("/file/")
                .append(fileData.getId())
                .append("/download");
        if(fileData.getType()==FileType.IMAGE){
            sb.append("/thumbnail");
        }
        return sb.toString();
    }
    public FileData createFileAndSaveToDisk(MultipartFile file, String owner, FileAccessType accessType) {
        String filePath = fileUtils.storeFile(file);
        return FileData.builder()
                .owner(owner)
                .accessType(accessType)
                .name(file.getOriginalFilename())
                .path(filePath)
                .contentType(file.getContentType())
                .size(file.getSize())
                .build();
    }

    public void checkValidUploadedFile(List<MultipartFile> files){
        List<String> fileExtensions=files.stream().map((file_)->FileUtils.extractExtensionFromName(file_.getOriginalFilename())).toList();
        for(String extension_:fileExtensions){
            if(!validFileExtension.contains(extension_)){
                throw new InvalidArgumentException("Invalid file type");
            }
        }
    }
    public void downloadFileById(Long fileId,FileAccessType accessType, HttpServletResponse response){
        FileData fileData=findByIdAndAccessType(fileId,accessType);
        File file=FileUtils.readFile(fileData.getPath());
        try(FileInputStream fileInputStream=new FileInputStream(file)) {
            setDownloadHeader(fileData.getName(),response);
            IOUtils.copy(fileInputStream,response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void downloadThumbnailImage(Long fileId,FileAccessType accessType, ThumbnailParamsVm thumbnailParamsVm, HttpServletResponse response){
        FileData fileData=findByIdAndAccessType(fileId,accessType);
        File file= FileUtils.readFile(fileData.getPath());
        try {
            setDownloadHeader(fileData.getName(),response);
            ImageUtils.resize(file,thumbnailParamsVm).toOutputStream(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setDownloadHeader(String fileName,HttpServletResponse response){
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        String contentDisposition = String.format("attachment; filename=%s",fileName);
        response.setHeader(HttpHeaders.CONTENT_TYPE,mimeType);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,contentDisposition);
    }
}
