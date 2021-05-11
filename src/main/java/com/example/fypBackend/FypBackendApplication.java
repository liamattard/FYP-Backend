package com.example.fypBackend;

import java.io.IOException;

import com.example.fypBackend.tools.Tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("application.properties")
public class FypBackendApplication {

    public static void main(String[] args) throws IOException {
        Tools.setPlacesApiCategories();
        SpringApplication.run(FypBackendApplication.class, args);
    }

}
