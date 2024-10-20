package com.csit321.NaviGo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class wakebackendController {

    @GetMapping("/wakebackend")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
