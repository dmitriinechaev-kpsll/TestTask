package com.example.archtst;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class ArchtstApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArchtstApplication.class, args);
        System.out.println("\n" +
                "╔════════════════════════════════════════════════════════════════╗\n" +
                "║                                                                ║\n" +
                "║     🚀  ARCTST API STARTED SUCCESSFULLY!                       ║\n" +
                "║     📍  http://localhost:8080                                   ║\n" +
                "║     📚  API: http://localhost:8080/api/users                   ║\n" +
                "║     🐘  Database: PostgreSQL                                   ║\n" +
                "║     🏗️  Architecture: 3-Layer (Controller-Service-Repository)  ║\n" +
                "║     📦  DTO + Entity + Lombok                                  ║\n" +
                "║                                                                ║\n" +
                "╚════════════════════════════════════════════════════════════════╝\n");
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}