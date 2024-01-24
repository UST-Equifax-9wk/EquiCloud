package com.revature.equicloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication
@EnableTransactionManagement
public class EquiCloudApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(EquiCloudApplication.class, args);
	}

}
