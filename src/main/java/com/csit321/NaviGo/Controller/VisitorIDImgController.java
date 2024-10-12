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
 
import com.csit321.NaviGo.Service.VisitorIDImgService;
 
@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "https://citsecure-frontend.onrender.com")
public class VisitorIDImgController {
 
    @Autowired
    private VisitorIDImgService visitorIDImgService;
 
    // Upload Visitor ID image
    @PostMapping("/uploadIDImg")
    public ResponseEntity<String> uploadIDImg(@RequestParam("file") MultipartFile file, 
                                              @RequestParam("cardNo") String cardNo,
                                              @RequestParam("date") String date) { // Only date now
        if (file.isEmpty() || !isValidImage(file)) {
            return ResponseEntity.badRequest().body("Invalid file");
        }
 
        // Sanitize the inputs
        String sanitizedCardNo = cardNo.replaceAll("[^a-zA-Z0-9]", "_");
        String sanitizedDate = date.replaceAll("[:\\s]", "_");  // Format date if necessary
 
        try {
            String filePath = visitorIDImgService.saveImage(file, sanitizedCardNo, sanitizedDate);
            System.out.println("Image successfully saved at: " + filePath);  // Logging the saved path
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
            String sanitizedDate = date.replaceAll("[:\\s]", "_");  // Ensure date format consistency
 
            Resource file = visitorIDImgService.loadImage(sanitizedCardNo, sanitizedDate);
            System.out.println("Image found: " + file.getFilename());  // Log the loaded file name
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
