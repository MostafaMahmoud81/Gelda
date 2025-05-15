package com.example.gelda;

import com.example.gelda.security.SecurityConfiggg;
import com.example.gelda.config.WebConfig;
import com.example.gelda.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAspectJAutoProxy
@Import({WebConfig.class, SecurityConfiggg.class, SecurityConfig.class})
public class GeldaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeldaApplication.class, args);
	}

}
