package com.vti.testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.vti")
public class CrudKylApplication {
	public static void main(String[] args) {
		SpringApplication.run(CrudKylApplication.class, args);
		
}
}
