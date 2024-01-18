package com.revature.equicloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication
@EnableTransactionManagement
public class EquiCloudApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		ApplicationContext context = SpringApplication.run(EquiCloudApplication.class, args);
	}

}
