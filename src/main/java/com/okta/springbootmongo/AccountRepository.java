package com.okta.springbootmongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

//@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Flux<Account> findAll();
}
