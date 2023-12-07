package com.wecare;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WeCareApplication implements WebMvcConfigurer{
	@Bean
	ModelMapper modelMapper() {
        return new ModelMapper();
    }
	public static void main(String[] args) {
		SpringApplication.run(WeCareApplication.class, args);
	}
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// TODO Auto-generated method stub
		registry.addMapping("/**").allowedMethods("GET","POST","PUT","DELETE");
	}
}
