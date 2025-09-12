package org.example.sampleservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageController {

    @Value("${message:Hello from default}")
    private String message;

    @Value("${app.key:}")
    private String appKey;

    @GetMapping("/message")
    public Map<String, Object> getMessage() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", message);
        resp.put("app.key", appKey);
        return resp;
    }
}
