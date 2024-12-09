package com.sonnguyen.storageservice.repository;

import com.sonnguyen.storageservice.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDataRepository extends JpaRepository<FileData,Long> {

}

