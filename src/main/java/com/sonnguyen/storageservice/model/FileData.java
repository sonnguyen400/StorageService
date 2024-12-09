package com.sonnguyen.storageservice.model;

import com.sonnguyen.storageservice.constant.FileAccessType;
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
    @Enumerated(EnumType.STRING)
    private FileAccessType accessType;
}
