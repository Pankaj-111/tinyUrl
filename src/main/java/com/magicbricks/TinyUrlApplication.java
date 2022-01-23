package com.magicbricks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@EnableAspectJAutoProxy
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "MagicBricks Short URL App"
		, version = "1.0"
		, description = "The Short URL Generator application")
)
public class TinyUrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinyUrlApplication.class, args);
	}
}
