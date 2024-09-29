package com.csit321.NaviGo.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csit321.NaviGo.Service.VisitorIDImgService;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "http://localhost:3000")
public class VisitorIDImgController {

    @Autowired
    private VisitorIDImgService visitorIDImgService;

    // Upload Visitor ID image
    @PostMapping("/uploadIDImg")
    public ResponseEntity<String> uploadIDImg(@RequestParam("file") MultipartFile file, 
                                              @RequestParam("cardNo") String cardNo) {
        if (file.isEmpty() || !isValidImage(file)) {
            return ResponseEntity.badRequest().body("Invalid file");
        }

        // Get the current date without time
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Sanitize inputs
        String sanitizedCardNo = cardNo.replaceAll("[^a-zA-Z0-9]", "_");

        try {
            String filePath = visitorIDImgService.saveImage(file, sanitizedCardNo, date);
            return ResponseEntity.ok("Image saved at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save image");
        }
    }
    
    // Get Visitor ID image
    @GetMapping("/getIDImg/{cardNo}/{date}")
    public ResponseEntity<Resource> getIDImg(@PathVariable String cardNo, @PathVariable String date) {
        try {
            String sanitizedCardNo = cardNo.replaceAll("[^a-zA-Z0-9]", "_");
            Resource file = visitorIDImgService.loadImage(sanitizedCardNo, date);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Utility function to check if uploaded file is a valid image
    private boolean isValidImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"));
    }
}