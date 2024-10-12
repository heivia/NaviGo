package com.csit321.NaviGo.Service;
 
import com.csit321.NaviGo.Entity.VisitorEntity;
import com.csit321.NaviGo.Repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
 
@Service
public class VisitorService {
 
    @Autowired
    private VisitorRepository visitorRepository;
 
    // Add a visitor and set card number based on the next available ID or card number
    public VisitorEntity addVisitorWithCardNo(VisitorEntity visitor) {
        // Fetch the current maximum card number or ID from the database
        Integer maxCardNo = visitorRepository.findMaxCardNo();
        if (maxCardNo == null) {
            maxCardNo = 0; // If no visitors are in the system, start from card number 1
        }
 
        // Set the card number as the next available number
        visitor.setCardNo(maxCardNo + 1);
 
        // Save the visitor with the auto-generated card number
        return visitorRepository.save(visitor);
    }
 
    // Get all visitors
    public List<VisitorEntity> getAllVisitors() {
        return visitorRepository.findAll();
    }
 
    // Find visitors by card number
    public VisitorEntity findVisitorByCardNo(int cardNo) {
        List<VisitorEntity> visitors = visitorRepository.findByCardNo(cardNo);
        for (VisitorEntity visitor : visitors) {
            if (visitor.getStatus() == 1) {
                return visitor;  // Return the visitor if currently checked in
            }
        }
        return null;
    }
 
    // Update visitor's timeOut and mark status as logged out
    public VisitorEntity updateVisitor(VisitorEntity visitor) {
        return visitorRepository.save(visitor); // Save the updated visitor entity to the database
    }
 
    // Generate the next card number
    public int generateCardNumber() {
        Integer maxCardNo = visitorRepository.findMaxCardNo();
        if (maxCardNo == null) {
            return 1; // Start from card number 1 if no previous visitors exist
        }
        return maxCardNo + 1; // Increment the max card number by 1
    }
 
    // Helper method to format timeOut with both time and date
    private String formatDateTime(String timeOut) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a dd/MM/yyyy");
        return currentTime.format(formatter);
    }
}
