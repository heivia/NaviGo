package com.csit321.NaviGo.Service;

import com.csit321.NaviGo.Entity.VisitorEntity;
import com.csit321.NaviGo.Repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;

    // Add a visitor and set card number based on the next available ID or card number
    public VisitorEntity addVisitorWithCardNo(VisitorEntity visitor) {
        Integer maxCardNo = visitorRepository.findMaxCardNo();
        if (maxCardNo == null) {
            maxCardNo = 0; // Start from card number 1 if no previous visitors exist
        }
        visitor.setCardNo(maxCardNo + 1);

        // Save visitor with all fields including officeVisited
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
        // Set the formatted time-out value to ensure "PM" format matches time-in
        String formattedTimeOut = formatDateTime(LocalDateTime.now());
        visitor.setTimeOut(formattedTimeOut);

        visitor.setStatus(0); // Set status to indicate the visitor has exited
        return visitorRepository.save(visitor); // Save the updated visitor entity to the database
    }

    // Utility method to format date and time for timeOut
    private String formatDateTime(LocalDateTime dateTime) {
        // Convert to GMT+8 timezone (Asia/Manila)
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.of("Asia/Manila"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a dd/MM/yyyy");
        return zonedDateTime.format(formatter).toUpperCase();  // Ensure AM/PM is in uppercase
    }
}
