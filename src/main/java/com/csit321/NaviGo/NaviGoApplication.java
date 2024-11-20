package com.csit321.NaviGo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
public class NaviGoApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaviGoApplication.class, args);
		System.out.println("NaviGo Application Started Successfully!");
	}

	@PostConstruct
	public void init() {
		// Set the default timezone to Asia/Manila
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Manila"));
		System.out.println("Server Timezone: " + ZoneId.systemDefault());
		System.out.println("Current Time: " + LocalDateTime.now());
	}
}
