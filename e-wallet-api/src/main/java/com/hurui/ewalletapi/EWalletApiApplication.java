package com.hurui.ewalletapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.hurui")
@SpringBootApplication
public class EWalletApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EWalletApiApplication.class, args);
	}

}
