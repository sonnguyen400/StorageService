package com.sonnguyen.storageservice.utils;

import com.sonnguyen.storageservice.exception.FileStoreException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtils {
    public static String storeFile(MultipartFile file,String dir) {
        File fileDir=new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        String extension = ""; // File extension (e.g., .jpg, .png)
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String newFilename = UUID.randomUUID() + extension; // Unique filename with extension
        String filePath = dir + newFilename;

        File destinationFile = new File(filePath);
        try {
            file.transferTo(destinationFile);
            return filePath;
        } catch (IOException e) {
            throw new FileStoreException(e.getMessage());
        }
    }
}
