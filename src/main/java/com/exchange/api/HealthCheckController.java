package com.exchange.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class HealthCheckController {

    @GetMapping("/healthcheck")
    public String checkState() {
        return "Service-B: inst001 정상";
    }

}