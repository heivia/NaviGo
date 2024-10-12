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
 
    // Save image to the specified folder
    public String saveImage(MultipartFile file, String cardNo, String timeIn) throws IOException {
        Path storagePath = Paths.get(imageStoragePath);
        System.out.println("Saving file to directory: " + storagePath.toString()); // Log the storage path
 
        // Ensure the directory exists
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
            System.out.println("Directory created: " + storagePath.toString()); // Log directory creation
        }
 
        // Generate the file name with cardNo and timeIn
        String fileName = generateFileName(cardNo, timeIn);
        Path filePath = storagePath.resolve(fileName);
 
        System.out.println("Attempting to save file as: " + filePath.toString()); // Log the file path before saving
 
        // Save the image
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File successfully saved: " + filePath.toString()); // Log after saving
        return filePath.toString();
    }
 
 
    // Load image from the folder
    public Resource loadImage(String cardNo, String timeIn) throws IOException {
        Path storagePath = Paths.get(imageStoragePath);
        String fileName = generateFileName(cardNo, timeIn);
 
        Path filePath = storagePath.resolve(fileName);
        System.out.println("Looking for file at: " + filePath.toString()); // Log the file path
 
        if (Files.exists(filePath)) {
            return new UrlResource(filePath.toUri());
        } else {
            System.err.println("File not found: " + fileName); // Log the missing file
            throw new IOException("File not found: " + fileName);
        }
    }

 
// Generate file name based on card number and date only
    private String generateFileName(String cardNo, String date) {
        // Use only card number and date (without time) to name the file
        return "visitor_" + cardNo + "_" + date + ".jpg";
    }
 
 
}
