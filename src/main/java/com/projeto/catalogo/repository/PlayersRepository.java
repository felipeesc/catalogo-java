package com.projeto.catalogo.repository;

import com.projeto.catalogo.domain.Players;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PlayersRepository extends ReactiveCrudRepository<Players, Integer> {

}
