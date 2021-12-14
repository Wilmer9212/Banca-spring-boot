package com.fenoreste.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.fenoreste.controller","com.fenoreste.service","com.fenoreste.servicesExternos","com.fenoreste.util","com.fenoreste.consumo"})
@EntityScan("com.fenoreste.entity")
@EnableJpaRepositories("com.fenoreste.dao")
public class SpringBancaWarApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBancaWarApplication.class, args);
	}

}
