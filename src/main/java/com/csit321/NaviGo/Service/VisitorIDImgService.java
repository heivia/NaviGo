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
import java.nio.file.StandardCopyOption;

@Service
public class VisitorIDImgService {

    @Value("${visitorIDimage.storage.path}")
    private String imageStoragePath;

    public String saveImage(MultipartFile file, String cardNo, String date) throws IOException {
        Path storagePath = Paths.get(imageStoragePath);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        // Modify the fileName to exclude time (only cardNo and date should be used)
        String fileName = generateFileName(cardNo, date);
        Path filePath = storagePath.resolve(fileName);
        
        // Save the image
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING); // Overwrite if exists
        return filePath.toString();
    }

    public Resource loadImage(String cardNo, String date) throws IOException {
        Path storagePath = Paths.get(imageStoragePath);
        String fileName = generateFileName(cardNo, date);

        Path filePath = storagePath.resolve(fileName);
        if (Files.exists(filePath)) {
            return new UrlResource(filePath.toUri());
        } else {
            throw new IOException("File not found: " + fileName);
        }
    }

    // Modify the file name generation to exclude "am/pm"
    private String generateFileName(String cardNo, String date) {
        // Ensuring it's only cardNo and date
        return "visitor_" + cardNo + "_" + date + ".jpg";
    }
}