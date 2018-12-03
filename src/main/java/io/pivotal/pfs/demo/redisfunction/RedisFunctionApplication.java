package io.pivotal.pfs.demo.redisfunction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class RedisFunctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisFunctionApplication.class, args);
	}

	@Bean
	public Function<Long,Fortune> getFortune() {
		return f ->  new Fortune("People are naturally attracted to you.");
	}

	@Bean
	public Function<Fortune, Fortune> saveFortune() {
		return f ->  f;
	}
}
