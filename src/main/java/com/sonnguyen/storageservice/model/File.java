package com.sonnguyen.storageservice.model;

import com.sonnguyen.storageservice.constant.FileAccessType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File extends AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String path;
    private String owner;
    @Enumerated(EnumType.STRING)
    private FileAccessType accessType;
}
