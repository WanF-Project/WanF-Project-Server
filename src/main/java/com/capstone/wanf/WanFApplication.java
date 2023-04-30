package com.capstone.wanf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = @Server(url = "{https://wanf-general-server.duckdns.org", description = "WanF General Server"))
@SpringBootApplication
public class WanFApplication {

    public static void main(String[] args) {
        SpringApplication.run(WanFApplication.class, args);
    }

}
