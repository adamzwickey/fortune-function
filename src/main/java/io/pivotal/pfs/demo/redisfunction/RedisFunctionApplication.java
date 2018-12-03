package io.pivotal.pfs.demo.redisfunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
				List<Fortune> fortunes = new ArrayList<>();
				_repository.findAll().forEach(fortune -> fortunes.add(fortune));
				if(fortunes.isEmpty()) return null;

				Fortune fortune = fortunes.get(new Random().nextInt(fortunes.size()));
				LOG.debug("Fortune-[" + f + "]");
				return fortune;
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
