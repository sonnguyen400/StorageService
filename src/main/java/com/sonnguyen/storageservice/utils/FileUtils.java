package com.sonnguyen.storageservice.utils;

import com.sonnguyen.storageservice.exception.FileStoreException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtils {
    public static String extractExtensionFromName(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
    public static String storeFile(MultipartFile file,String dir) {
        String directoryPath=System.getProperty("user.dir")+dir;
        File directoryDir=new File(directoryPath);
        if (!directoryDir.exists()) {
            directoryDir.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        String extension = extractExtensionFromName(originalFilename);
        String newFilename = UUID.randomUUID() + extension; // Unique filename with extension

        try {
            File des=new File(directoryPath+newFilename);
            file.transferTo(des);
            return dir+newFilename;
        } catch (IOException e) {
            throw new FileStoreException(e.getMessage());
        }
    }

    public static File readFile(String relativePath){
        String directoryPath=System.getProperty("user.dir")+relativePath;
        return new File(directoryPath);
    }
}
