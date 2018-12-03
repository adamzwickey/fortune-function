package io.pivotal.pfs.demo.redisfunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class RedisFunctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisFunctionApplication.class, args);
	}

	private static Logger LOG = LoggerFactory.getLogger(RedisFunctionApplication.class);
	@Autowired private FortuneRepository _repository;

	@Bean
	public Function<Long,Fortune> getFortune() {
		return f ->  {
			if(f == null) {
				LOG.info("Retrieving Random fortune");
				return new Fortune("People are naturally attracted to you.");
			} else {
				LOG.info("Retrieving Fortune: " + f);
				return new Fortune("People are naturally attracted to you.");
			}
		};
	}

	@Bean
	public Function<Fortune, Fortune> saveFortune() {
		return f ->  f;
	}
}
