package com.cedalanavi.project_ijva500_soa_authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ProjectIjva500SoaAuthenticationApplication {

	@Bean
    // Spring Securiy secure password by encrypting them
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

	public static void main(String[] args) {
		SpringApplication.run(ProjectIjva500SoaAuthenticationApplication.class, args);
	}

}
