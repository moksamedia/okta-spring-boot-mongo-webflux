package com.okta.springbootmongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface KayakRepository extends ReactiveMongoRepository<Kayak, Long> {
    Flux<Kayak> findAll();
}