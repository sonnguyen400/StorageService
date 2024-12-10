package com.sonnguyen.storageservice.model;

import com.sonnguyen.storageservice.constant.FileAccessType;
import com.sonnguyen.storageservice.constant.FileType;
import com.sonnguyen.storageservice.utils.FileProcessor;
import com.sonnguyen.storageservice.utils.FileUtils;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileData extends AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String path;
    private String owner;
    private String contentType;
    private Long size;
    private Integer version;

    @Enumerated(EnumType.STRING)
    private FileAccessType accessType;

    public FileType getType() {
        return FileProcessor.guessFileTypeFromName(name);
    }
    public String getExtension(){
        return FileUtils.extractExtensionFromName(name);
    }
}
