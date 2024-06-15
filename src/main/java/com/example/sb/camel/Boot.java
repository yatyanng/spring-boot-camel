package com.example.sb.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Boot {

	private static final Logger logger = LoggerFactory.getLogger(Boot.class);

	public static void main(String[] args) throws Exception {
		String configDirectory = "conf";
		if (args.length > 0) {
			configDirectory = args[0];
		}
		logger.info("config directory: {}", configDirectory);

		if (new java.io.File(configDirectory).exists() && new java.io.File(configDirectory).isDirectory()) {
			System.setProperty("spring.config.location", configDirectory + "/springboot.yml");
			System.setProperty("logging.config", configDirectory + "/logback.xml");
		}
		SpringApplication.run(Boot.class, args);
	}
}