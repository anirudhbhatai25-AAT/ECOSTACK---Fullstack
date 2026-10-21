package com.lnt.fullstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FullstackApplication {

    public static void main(String[] args) {
        SpringApplication.run(FullstackApplication.class, args);
        System.out.println("=================================================");
        System.out.println("🌱 L&T Eco-Analyzer Backend is Running on Port 8080!");
        System.out.println("=================================================");
    }
}