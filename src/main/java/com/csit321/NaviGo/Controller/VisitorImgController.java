package com.csit321.NaviGo.Controller;

import java.io.IOException;

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

import com.csit321.NaviGo.Service.VisitorImgService;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "http://localhost:3000")
public class VisitorImgController {
    @Autowired
    private VisitorImgService visitorImgService;

    @PostMapping("/uploadVisitorImg")
    public ResponseEntity<String> uploadVisitorImg(@RequestParam("file") MultipartFile file, 
                                                   @RequestParam("cardNo") String cardNo, 
                                                   @RequestParam("timeIn") String timeIn) {
        if (file.isEmpty() || !isValidImage(file)) {
            return ResponseEntity.badRequest().body("Invalid file");
        }

        // Sanitize inputs
        String sanitizedTimeIn = timeIn.replaceAll("[:\\s]", "_").replaceAll("(AM|PM)", "");
        String sanitizedCardNo = cardNo.replaceAll("[^a-zA-Z0-9]", "_");

        try {
            String filePath = visitorImgService.saveImage(file, sanitizedCardNo, sanitizedTimeIn);
            return ResponseEntity.ok("Image saved at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save image");
        }
    }

    @GetMapping("/getVisitorImg/{cardNo}/{timeIn}")
    public ResponseEntity<Resource> getVisitorImg(@PathVariable String cardNo, 
                                                  @PathVariable String timeIn) {
        try {
            // Sanitize inputs
            String sanitizedTimeIn = timeIn.replaceAll("[:\\s]", "_").replaceAll("(AM|PM)", "");
            String sanitizedCardNo = cardNo.replaceAll("[^a-zA-Z0-9]", "_");

            Resource file = visitorImgService.loadImage(sanitizedCardNo, sanitizedTimeIn);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private boolean isValidImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"));
    }
}