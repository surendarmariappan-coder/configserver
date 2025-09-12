package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    // Provide a safe default so the server doesn't fail if app.numb isn't defined
    @Value("${app.numb:0}")
    private int numb;

    public int getNumb() {
        System.out.println(numb);
        return numb;
    }
}
