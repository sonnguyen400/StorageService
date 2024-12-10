package com.sonnguyen.storageservice.repository;

import com.sonnguyen.storageservice.model.FileData;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDataRepository extends JpaRepository<FileData,Long> {
    @NotNull
    Page<FileData> findAll(Specification<FileData> specification,@NotNull Pageable pageable);
}

