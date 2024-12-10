package com.sonnguyen.storageservice.viewmodel;

import com.sonnguyen.storageservice.constant.FileAccessType;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record FileDetailsGetVm(
    String name,
    String path,
    String link,
    String owner,
    Integer version,
    Long size,
    String contentType,
    FileAccessType accessType,
    LocalDateTime createdDate,
    LocalDateTime lastModifiedDate
){

}
