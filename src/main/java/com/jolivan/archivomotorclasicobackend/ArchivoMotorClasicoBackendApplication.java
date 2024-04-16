package com.jolivan.archivomotorclasicobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@SpringBootApplication
//@EnableNeo4jAuditing
@EnableNeo4jRepositories
public class ArchivoMotorClasicoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchivoMotorClasicoBackendApplication.class, args);
	}

}

