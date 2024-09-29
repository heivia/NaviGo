package com.csit321.NaviGo.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class VisitorImgService {

    @Value("${visitorimage.storage.path}")
    private String imageStoragePath;

    public String saveImage(MultipartFile file, String cardNo, String timeIn) throws IOException {
        Path storagePath = Paths.get(imageStoragePath);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }
        String fileName = generateFileName(cardNo, timeIn);
        Path filePath = storagePath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return filePath.toString();
    }

    public Resource loadImage(String cardNo, String timeIn) throws IOException {
        Path storagePath = Paths.get(imageStoragePath);
        String fileName = generateFileName(cardNo, timeIn);

        Path filePath = storagePath.resolve(fileName);
        if (Files.exists(filePath)) {
            return new UrlResource(filePath.toUri());
        } else {
            throw new IOException("File not found: " + fileName);
        }
    }

    private String generateFileName(String cardNo, String timeIn) {
    	  // Include date and time to avoid conflicts between AM/PM across days
    	  return "visitor_" + cardNo + "_" + timeIn + ".jpg";
    	}

}
