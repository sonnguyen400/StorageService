package com.sonnguyen.storageservice.service;

import com.sonnguyen.storageservice.exception.ResourceNotFoundException;
import com.sonnguyen.storageservice.model.FileData;
import com.sonnguyen.storageservice.repository.FileDataRepository;
import com.sonnguyen.storageservice.utils.FileUtils;
import com.sonnguyen.storageservice.viewmodel.FileDataListVm;
import com.sonnguyen.storageservice.viewmodel.FileInfoPostVm;
import jakarta.servlet.http.HttpServletResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    FileDataRepository fileDataRepository;
    @Value("${storage.upload_path}")
    private String uploadPath;
    @Value("${storage.download-base-url}")
    private String downloadBaseUrl;
    public FileData findById(Long id){
        return fileDataRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("File not found"));
    }
    public List<FileDataListVm> uploadAll(List<MultipartFile> files, FileInfoPostVm fileInfoPortVm) {
        List<FileData> fileDataList =files.stream().map((file_)-> createFileAndSaveToDisk(file_,fileInfoPortVm)).toList();
        return fileDataRepository.saveAll(fileDataList)
                .stream()
                .map((file_)-> {
                    String downloadLink=downloadBaseUrl+"/"+file_.getId()+"/download";
                    return new FileDataListVm(file_.getName(),downloadLink);
                })
                .toList();
    }
    public FileData createFileAndSaveToDisk(MultipartFile file, FileInfoPostVm fileInfoPortVm) {
        String filePath = FileUtils.storeFile(file,uploadPath);
        return FileData.builder()
                .owner(fileInfoPortVm.owner())
                .accessType(fileInfoPortVm.accessType())
                .name(file.getOriginalFilename())
                .path(filePath)
                .build();
    }
    public void setDownloadHeader(String fileName,HttpServletResponse response){
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        String contentDisposition = String.format("attachment; filename=%s",fileName);
        response.setHeader(HttpHeaders.CONTENT_TYPE,mimeType);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,contentDisposition);
    }
    public void downloadFileById(Long fileId, HttpServletResponse response){
        FileData fileData=findById(fileId);
        File file=FileUtils.readFile(fileData.getPath());
        try(FileInputStream fileInputStream=new FileInputStream(file)) {
            setDownloadHeader(fileData.getName(),response);
            IOUtils.copy(fileInputStream,response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void downloadThumbnailImage(Long fileId,Integer width,Integer height,HttpServletResponse response){
        FileData fileData=findById(fileId);
        File file= FileUtils.readFile(fileData.getPath());
        try {
            setDownloadHeader(fileData.getName(),response);
            Thumbnails.of(file).size(width,height).toOutputStream(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
