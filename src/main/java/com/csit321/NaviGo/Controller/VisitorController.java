package com.csit321.NaviGo.Controller;

import com.csit321.NaviGo.Entity.VisitorEntity;
import com.csit321.NaviGo.Service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/visitor")
@CrossOrigin(origins = "http://localhost:3000")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    // Fetch all visitors
    @GetMapping("/getAllVisitors")
    public List<VisitorEntity> getAllVisitors() {
        return visitorService.getAllVisitors();
    }

    // Add a visitor
    @PostMapping("/addVisitor")
    public ResponseEntity<VisitorEntity> addVisitor(@RequestBody VisitorEntity visitor) {
        // Log the visitor object to check field mapping
        System.out.println("Visitor received: " + visitor);
        System.out.println("Selected Gate: " + visitor.getSelectedGate());
        System.out.println("Office Visited: " + visitor.getOfficeVisited());  // Log office visited for debugging

        // Call the service to add visitor with generated card number and save it to the database
        VisitorEntity addedVisitor = visitorService.addVisitorWithCardNo(visitor);

        return ResponseEntity.ok(addedVisitor);
    }

    // Get the next card number
    @GetMapping("/nextCardNumber")
    public ResponseEntity<Map<String, Integer>> getNextCardNumber() {
        int nextCardNo = visitorService.getAllVisitors().size() + 1;
        Map<String, Integer> response = new HashMap<>();
        response.put("nextCardNo", nextCardNo);
        return ResponseEntity.ok(response);
    }

    // Fetch visitor by card number
    @GetMapping("/getVisitorByCardNo/{cardNo}")
    public ResponseEntity<VisitorEntity> getVisitorByCardNo(@PathVariable("cardNo") int cardNo) {
        VisitorEntity visitor = visitorService.findVisitorByCardNo(cardNo);
        if (visitor != null) {
            return ResponseEntity.ok(visitor);
        }
        return ResponseEntity.status(404).body(null);
    }

    // Update visitor's time-out based on card number
    @PutMapping("/updateVisitorTimeOut/{cardNo}")
    public ResponseEntity<String> updateVisitorTimeOut(@PathVariable("cardNo") int cardNo) {
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTimeOut = formatDateTime(currentTime);

        VisitorEntity visitor = visitorService.findVisitorByCardNo(cardNo);
        if (visitor != null && visitor.getStatus() == 1) {
            visitor.setTimeOut(formattedTimeOut);  // Set the formatted time-out
            visitor.setStatus(0);  // Set status to indicate the visitor has exited
            visitorService.updateVisitor(visitor);
            return ResponseEntity.ok("Time-out updated successfully");
        } else {
            return ResponseEntity.status(404).body("Visitor not found or already logged out");
        }
    }

    // Utility method to format date and time
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a dd/MM/yyyy");
        return dateTime.format(formatter).toUpperCase();  // Ensure AM/PM is in uppercase
    }
}
