package com.greencode.musicarchivebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicArchiveBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicArchiveBackendApplication.class, args);
		System.out.println("Iniciou!!!");
	}
	// https://www.youtube.com/watch?v=vY7c7k8xmKE&ab_channel=JavaTechie Para configurar bucket S3
}
