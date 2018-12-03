package io.pivotal.pfs.demo.redisfunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;
import java.util.function.Function;

@SpringBootApplication
public class RedisFunctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisFunctionApplication.class, args);
	}

	private static Logger LOG = LoggerFactory.getLogger(RedisFunctionApplication.class);
	@Autowired private FortuneRepository _repository;

	@Bean
	public Function<String,Fortune> getFortune() {
		return f ->  {
			if("random".equalsIgnoreCase(f)) {
				LOG.info("Retrieving Random fortune");
				return new Fortune("People are naturally attracted to you.");
			} else {
				LOG.info("Retrieving Fortune: " + f);

				Optional<Fortune> op =  _repository.findById(f);
				if (op.isPresent()) return op.get();
				return new Fortune("You are unlucky today");
			}
		};
	}

	@Bean
	public Function<String, Fortune> saveFortune() {
		return f ->  {
			LOG.info("Saving Fortune: " + f);
			Fortune fortune = _repository.save(new Fortune(f));
			return fortune;
		};
	}
}
