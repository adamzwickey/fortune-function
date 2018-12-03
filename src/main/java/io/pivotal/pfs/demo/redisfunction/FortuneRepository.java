package io.pivotal.pfs.demo.redisfunction;

import org.springframework.data.repository.CrudRepository;

public interface FortuneRepository extends CrudRepository<Fortune, Long> {
}
