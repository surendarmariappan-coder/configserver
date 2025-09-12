package org.example;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    @Autowired
    private AppConfig appConfig;
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
    @PostConstruct
    public void getNumb() {
        System.out.println("numb: " + appConfig.getNumb());
    }
}


