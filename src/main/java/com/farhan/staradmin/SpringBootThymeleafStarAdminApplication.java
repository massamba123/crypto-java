package com.farhan.staradmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.farhan.staradmin.entity") // Specify the package where your entities are located
@EnableJpaRepositories("com.farhan.staradmin.repository") // Specify the package where your repositories are located
public class SpringBootThymeleafStarAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootThymeleafStarAdminApplication.class, args);
	}
}
