package com.sonnguyen.storageservice.service;
import com.sonnguyen.storageservice.model.FileData;
import com.sonnguyen.storageservice.repository.FileDataRepository;
import com.sonnguyen.storageservice.utils.FileUtils;
import com.sonnguyen.storageservice.viewmodel.FileDataListVm;
import com.sonnguyen.storageservice.viewmodel.FileInfoPostVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileDataService {
    @Autowired
    FileDataRepository fileDataRepository;
    @Value("${storage.upload_path}")
    private String uploadPath;
    @Value("${storage.download-base-url}")
    private String downloadBaseUrl;
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
}
