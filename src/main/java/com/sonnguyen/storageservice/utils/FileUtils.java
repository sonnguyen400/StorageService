package com.sonnguyen.storageservice.utils;

import com.sonnguyen.storageservice.exception.FileStoreException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
@Component
public class FileUtils {
    @Value("${storage.upload_uri_pattern}")
    private String upload_uri_pattern;
    @Value("${storage.upload_path}")
    private String uploadPath;
    public static String extractExtensionFromName(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

    public String storeFile(MultipartFile file) {
        String relativePath=String.format("%s/%s",uploadPath,buildTimePath(Instant.now()));
        String directoryPath=String.format("%s/%s",System.getProperty("user.dir"),relativePath);
        File directoryDir=new File(directoryPath);
        if (!directoryDir.exists()) {
            directoryDir.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        String extension = extractExtensionFromName(originalFilename);
        String newFilename = UUID.randomUUID() + extension; // Unique filename with extension

        try {
            File des=new File(directoryPath+"/"+newFilename);
            file.transferTo(des);
            return String.format("%s/%s",relativePath,newFilename); // Relative path to file
        } catch (IOException e) {
            throw new FileStoreException(e.getMessage());
        }
    }

    public static File readFile(String relativePath){
        String path=System.getProperty("user.dir")+"\\"+relativePath;
        return new File(path);
    }
    public static void deleteFile(String relativePath){
        String path=System.getProperty("user.dir")+relativePath;
        File file=new File(path);
        file.delete();
    }
    public String buildTimePath(Instant date){
        DateFormat dateFormat = new SimpleDateFormat(upload_uri_pattern);
        return dateFormat.format(new Date(date.getEpochSecond()*1000));
    }
}
