package com.vti.testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.vti.testing")
public class CrudKylApplication {
	public static void main(String[] args) {
		SpringApplication.run(CrudKylApplication.class, args);
}
}
