package com.microdiab.mpatient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * Entry point of the Microservice Patient application.
 *
 * The {@code MpatientApplication} class bootstraps and launches the Spring Boot–based
 * microservice responsible for patient management within the MicroDiab ecosystem.
 *
 * This application is registered as a discovery client thanks to the
 * {@link EnableDiscoveryClient} annotation, allowing it to interact with
 * service registries such as Eureka or other Spring Cloud compatible platforms.
 *
 * @see SpringApplication
 * @see EnableDiscoveryClient
 * @see SpringBootApplication
 * @author JBM
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MpatientApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpatientApplication.class, args);
    }

}
