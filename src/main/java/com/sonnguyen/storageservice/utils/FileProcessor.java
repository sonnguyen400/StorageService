package com.sonnguyen.storageservice.utils;

import com.sonnguyen.storageservice.constant.FileType;

public abstract class FileProcessor {
    public static FileType guessFileTypeFromName(String fileName){
        String extension=FileUtils.extractExtensionFromName(fileName).toLowerCase();
        return switch (extension) {
            case ".jpg", ".png", ".jpeg", ".webp" -> FileType.IMAGE;
            case ".mp4" -> FileType.VIDEO;
            case ".mp3" -> FileType.AUDIO;
            default -> FileType.DOCUMENT;
        };
    }
}
