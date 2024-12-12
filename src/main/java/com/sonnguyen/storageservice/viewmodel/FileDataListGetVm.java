package com.sonnguyen.storageservice.viewmodel;

import com.sonnguyen.storageservice.constant.FileAccessType;
import com.sonnguyen.storageservice.model.FileData;

import java.time.LocalDateTime;

public record FileDataListGetVm(
        Long id,
        String name,
        String path,
        String link,
        Integer version,
        Long size,
        String contentType,
        FileAccessType accessType,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
    public static FileDataListGetVm fromEntity(FileData fileData){
        return new FileDataListGetVm(
                fileData.getId(),
                fileData.getName(),
                fileData.getPath(),
                fileData.getOwner(),
                fileData.getVersion(),
                fileData.getSize(),
                fileData.getContentType(),
                fileData.getAccessType(),
                fileData.getCreatedDate(),
                fileData.getLastModifiedDate()
        );
    }
}
