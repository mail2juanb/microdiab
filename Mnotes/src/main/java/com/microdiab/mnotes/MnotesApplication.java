package com.microdiab.mnotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for the *MNotes* microservice, part of the *MicroDiab* project.
 * This class serves as the entry point for the Spring Boot application and enables
 * service discovery using Spring Cloud's {@link org.springframework.cloud.client.discovery.EnableDiscoveryClient}.
 *
 * <p>The *MNotes* microservice is responsible for managing patient notes, integrating with
 * MongoDB for data storage and Spring Cloud for service discovery and registration with Eureka.
 * This microservice is designed to work within the broader *MicroDiab* architecture,
 * which includes other microservices such as *mgateway*, *clientui*, *mrisk*, *eureka* and *mpatient*.</p>
 *
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.cloud.client.discovery.EnableDiscoveryClient
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MnotesApplication {

    /**
     * Main method to start the *MNotes* Spring Boot application.
     * Initializes the Spring context and starts the embedded server.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(MnotesApplication.class, args);
    }

}
