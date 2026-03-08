package io.pjj.ziphyeonjeon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import io.pjj.ziphyeonjeon.global.API.common.ExternalApiProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExternalApiProperties.class)
public class ZiphyeonjeonApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZiphyeonjeonApplication.class, args);
	}
}