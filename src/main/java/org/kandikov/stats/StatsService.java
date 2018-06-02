package org.kandikov.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class StatsService {

	public static void main(String[] args) {
		SpringApplication.run(StatsService.class, args);
	}

}
