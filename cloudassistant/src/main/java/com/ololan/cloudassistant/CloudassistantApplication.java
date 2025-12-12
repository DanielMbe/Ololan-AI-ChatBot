package com.ololan.cloudassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = { "com.ololan.cloudassistant" })
public class CloudassistantApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(CloudassistantApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CloudassistantApplication.class);
	}
}
